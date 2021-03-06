package cn.waitti.jcp.Serialize;

import cn.waitti.jcp.Controller;
import cn.waitti.jcp.Tools.*;
import com.google.gson.Gson;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
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
        node2Tool.put(Path.class, PenTool.class);
        node2Tool.put(Text.class, TextTool.class);
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


    private static Map<Object, Object> serializeNode(Node child) {
        var clazz = child.getClass();
        var list = serializeMap.get(clazz);
        var ret = new HashMap<>();
        try {
            if (clazz.equals(Path.class)) {
                Path path = (Path) child;
                var arr = new ArrayList<ArrayList<Double>>();
                for (PathElement element : path.getElements()) {
                    if (element instanceof MoveTo) {
                        var v = (MoveTo) element;
                        var a = new ArrayList<Double>();
                        a.add(v.getX());
                        a.add(v.getY());
                        arr.add(a);
                    } else if (element instanceof LineTo) {
                        var v = (LineTo) element;
                        var a = new ArrayList<Double>();
                        a.add(v.getX());
                        a.add(v.getY());
                        arr.add(a);
                    }
                }
                ret.put("Elements", arr);
            } else if (clazz.equals(Text.class)) {
                Text text = (Text) child;
                ret.putAll(Map.of(
                        "FontName", text.getFont().getName(),
                        "FontSize", text.getFont().getSize(),
                        "Text", text.getText(),
                        "X", text.getX(),
                        "Y", text.getY()
                ));
            } else {
                for (Pair<Class<?>, String> s : list) {
                    ret.put(s.getValue(), clazz.getMethod("get" + s.getValue()).invoke(child));
                }
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
            if (clazz.equals(Path.class)) {
                Path path = new Path();
                var arr = (ArrayList<ArrayList<Double>>) map.get("Elements");
                for (int i = 0; i < arr.size(); i++) {
                    var pair = arr.get(i);
                    if (i == 0) {
                        path.getElements().add(new MoveTo(pair.get(0), pair.get(1)));
                    } else {
                        path.getElements().add(new LineTo(pair.get(0), pair.get(1)));
                    }
                }
                ret = path;
            } else if (clazz.equals(Text.class)) {
              Text text = new Text();
              text.setFont(Font.font((String)map.get("FontName"), (double)map.get("FontSize")));
              text.setText((String)map.get("Text"));
              text.setX((double)map.get("X"));
              text.setY((double)map.get("Y"));
              ret = text;
            } else {
                var list = serializeMap.get(clazz);
                ret = clazz.getConstructor().newInstance();
                for (Pair<Class<?>, String> s : list) {
                    clazz.getMethod("set" + s.getValue(), s.getKey()).invoke(ret, map.get(s.getValue()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void serialize() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt")
        );
        File file = fileChooser.showSaveDialog(controller.cPane.getScene().getWindow());

        if (file != null) {
            serialize(file);
        }
    }

    public static void serialize(File file) {
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println(serialize(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String serialize(boolean nil) {
        var res = new ArrayList<>();
        for (Node child : controller.cPane.getChildren()) {
            res.add(serializeNode(child));
        }
        return gson.toJson(res);
    }

    public static void deserialize() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));
        File file = fileChooser.showOpenDialog(controller.cPane.getScene().getWindow());
        if (file != null) {
            deserialize(file, null);
        }
    }

    public static void deserialize(File file, String str) {
        var children = controller.cPane.getChildren();
        children.clear();
        String r = "";
        if (file != null && str == null) {
            try {
                var sc = new BufferedReader(new FileReader(file));
                r = sc.lines().collect(Collectors.joining());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (str != null)
            r = str;
        try {
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
