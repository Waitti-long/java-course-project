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

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Serializer {
    private static Controller controller;
    private static Gson gson = new Gson();
    private static Map<Object, List<Pair<Class<?>, String>>> serializeMap = new HashMap<>();
    private static Map<Class<? extends Node>, Class<? extends EnabledTool>> node2Tool = new HashMap<>();

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
            }
            Class<? extends EnabledTool> cl = (Class<? extends EnabledTool>) mapper;
            node2Tool.put(node, cl);
            serializeMap.put(node, list);
        }
    }

    public static void init(Controller controller) {
        Serializer.controller = controller;
        scan();
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

    // TODO Pane.class
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
        fileChooser.setTitle("保存");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT","*.txt")
        );
        File file = fileChooser.showSaveDialog(controller.cPane.getScene().getWindow());

        if (file != null) {
            serialize(file);
            file=null;
            fileChooser=new FileChooser();
        }
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
        fileChooser.setTitle("打开");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        File file = fileChooser.showOpenDialog(controller.cPane.getScene().getWindow());
        if (file != null) {
            deserialize(file);
            file=null;
            fileChooser=new FileChooser();
        }
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
                    EnabledTool tool = ToolPicker.getTool(node2Tool.get(Class.forName((String) key)));
                    if (node != null && tool != null) {
                        deserializeFill(node, (Map<?, ?>) map.get(key));
                        node.setOnMouseDragReleased(tool.mouseDragReleased());
                        node.setOnMouseDragged(tool.mouseDragged());
                        node.setOnMousePressed(tool.mousePressed());
                        children.add(node);
                    }
                }
            }
                NewTool.push();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
