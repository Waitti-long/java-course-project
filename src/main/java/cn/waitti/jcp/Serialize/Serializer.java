package cn.waitti.jcp.Serialize;

import cn.waitti.jcp.Controller;
import cn.waitti.jcp.Tools.*;
import com.google.gson.Gson;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ConfigurationBuilder;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Serializer {
    private static Controller controller;
    private static Gson gson = new Gson();
    private static Map<Object, List<Pair<Class<?>, String>>> serializeMap = new HashMap<>();

    public static void scan() {
        Reflections reflections = new Reflections("cn.waitti.jcp", new TypeAnnotationsScanner(),
                new SubTypesScanner(), new MethodAnnotationsScanner());
        Set<Class<?>> mappers = reflections.getTypesAnnotatedWith(SerializeConfigure.class);
        for (Class<?> mapper : mappers) {
            SerializeConfigure conf = mapper.getAnnotation(SerializeConfigure.class);
            var node = conf.node();
            var classes = conf.serializeClasses();
            var strs = conf.serializeStrings();
            List<Pair<Class<?>, String>> list = new ArrayList<>();
            for (int i = 0; i < classes.length; i++) {
                list.add(new Pair<>(classes[i], strs[i]));
                System.out.println(strs[i]);
            }
            serializeMap.put(node, list);
        }
    }

    static {
        serializeMap.put(Circle.class, Arrays.asList(new Pair<>(double.class, "CenterX"), new Pair<>(double.class, "CenterY"), new Pair<>(double.class, "Radius")));
        serializeMap.put(Ellipse.class, Arrays.asList(new Pair<>(double.class, "CenterX"), new Pair<>(double.class, "CenterY"), new Pair<>(double.class, "RadiusX"), new Pair<>(double.class, "RadiusY")));
        serializeMap.put(Rectangle.class, Arrays.asList(new Pair<>(double.class, "X"), new Pair<>(double.class, "Y"), new Pair<>(double.class, "Height"), new Pair<>(double.class, "Width")));
        serializeMap.put(Line.class, Arrays.asList(new Pair<>(double.class, "StartX"), new Pair<>(double.class, "StartY"), new Pair<>(double.class, "EndX"), new Pair<>(double.class, "EndY")));
        serializeMap.put(Text.class, Arrays.asList(new Pair<>(String.class, "Text"), new Pair<>(String.class, "Font"), new Pair<>(double.class, "Size")));
    }

    public static void init(Controller controller) {
        Serializer.controller = controller;
    }

    private static Map<Object, Object> serializeFilled(Paint fill, Paint stroke, Double strokeWidth) {
        if (fill != null) {
            Color color = (Color) fill;
            return Map.of(
                    "Filled", true,
                    "R", color.getRed(),
                    "G", color.getGreen(),
                    "B", color.getBlue()
            );
        } else {
            Color color = (Color) stroke;
            return Map.of(
                    "Filled", false,
                    "R", color.getRed(),
                    "G", color.getGreen(),
                    "B", color.getBlue(),
                    "StrokeWidth", strokeWidth
            );
        }
    }

    private static <T extends Node> void deserializeFill(T child, Map<?, ?> map) {
        Class<? extends Node> clazz = child.getClass();
        try {
            var filled = (boolean) map.get("Filled");
            var color = Color.color((double) map.get("R"), (double) map.get("G"), (double) map.get("B"));
            Object nil = null;
            if (filled) {
                clazz.getMethod("setFill", Paint.class).invoke(child, color);
                clazz.getMethod("setStroke", Paint.class).invoke(child, nil);
            } else {
                clazz.getMethod("setStroke", Paint.class).invoke(child, color);
                clazz.getMethod("setFill", Paint.class).invoke(child, nil);
                clazz.getMethod("setStrokeWidth", double.class).invoke(child, (double) map.get("StrokeWidth"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<Object, Object> serializeNode(Node child) {
        var clazz = child.getClass();
        var list = serializeMap.get(clazz);
        var ret = new HashMap<>();
        try {
            for (Pair<Class<?>, String> s : list) {
                ret.put(s.getValue(), clazz.getMethod("get" + s.getValue()).invoke(child));
            }
            ret.putAll(serializeFilled((Paint) clazz.getMethod("getFill").invoke(child), (Paint) clazz.getMethod("getStroke").invoke(child), (double) clazz.getMethod("getStrokeWidth").invoke(child)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Map.of(clazz.getName(), ret);
    }

    private static Node deserializeNode(String clazzStr, Map<?, ?> map) {
        Node ret = null;
        try {
            Class<? extends Node> clazz = (Class<? extends Node>) Class.forName(clazzStr);
            var list = serializeMap.get(clazz);
            ret = clazz.getConstructor().newInstance();
            for (Pair<Class<?>, String> s : list) {
                clazz.getMethod("set" + s.getValue(), s.getKey()).invoke(ret, map.get(s.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void serialize() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("save");
        File file = fileChooser.showOpenDialog(controller.cPane.getScene().getWindow());
        if (file != null)
            serialize(file);
    }

    public static void serialize(File file) {
        try (PrintWriter pw = new PrintWriter(file)) {
            var res = new ArrayList<>();
            for (Node child : controller.cPane.getChildren()) {
                res.add(serializeNode(child));
            }
            pw.println(gson.toJson(res));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deserialize() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("read");
        File file = fileChooser.showOpenDialog(controller.cPane.getScene().getWindow());
        if (file != null)
            deserialize(file);
    }

    public static void deserialize(File file) {
        System.out.println("deserialize");
        var children = controller.cPane.getChildren();
        children.clear();
        try (var sc = new BufferedReader(new FileReader(file))) {
            String r = sc.lines().collect(Collectors.joining());
            var arr = gson.fromJson(r, ArrayList.class);
            for (Object o : arr) {
                var map = (Map<?, ?>) o;
                for (Object key : map.keySet()) {
                    Node node = deserializeNode((String) key, (Map<?, ?>) map.get(key));
                    EnabledTool tool = ToolPicker.getTool(null);
                    if (node != null && tool != null) {
                        deserializeFill(node, (Map<?, ?>) map.get(key));
                        node.setOnMouseReleased(tool.mouseReleased());
                        node.setOnMouseDragged(tool.mouseDragged());
                        node.setOnMousePressed(tool.mousePressed());
                        children.add(node);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
