package cn.waitti.jcp.Serialize;

import cn.waitti.jcp.Controller;
import com.google.gson.Gson;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Serializer {
    private static Controller controller;
    private static Gson gson = new Gson();

    public static void init(Controller controller) {
        Serializer.controller = controller;
    }

    private static Map<Object, Object> serializeFilled(Paint fill, Paint stroke, Double strokeWidth) {
        if(fill != null){
            Color color = (Color)fill;
            return Map.of(
                    "Filled", true,
                    "R", color.getRed(),
                    "G", color.getGreen(),
                    "B", color.getBlue()
            );
        }else {
            Color color = (Color)stroke;
            return Map.of(
                    "Filled", false,
                    "R", color.getRed(),
                    "G", color.getGreen(),
                    "B", color.getBlue(),
                    "StrokeWidth", strokeWidth
            );
        }
    }

    private static void deserializeFilled(Node node, Map<?, ?> map) {
        
    }

    private static Map<Object, Object> serializeCircle(Node child) {
        Circle node = (Circle) child;
        Map<Object, Object> ret = new HashMap<>(Map.of(
                "CenterX", node.getCenterX(),
                "CenterY", node.getCenterY(),
                "Radius", node.getRadius()
        ));
        ret.putAll(serializeFilled(node.getFill(), node.getStroke(), node.getStrokeWidth()));
        return Map.of("Circle", ret);
    }

    private static Circle deserializeCircle(Map<?, ?> map) {
        Circle node = new Circle();
        for (Object o : map.keySet()) {
            if (o.equals("CenterX")) node.setCenterX((double) map.get(o));
            else if (o.equals("CenterY")) node.setCenterY((double) map.get(o));
            else if (o.equals("Radius")) node.setRadius((double) map.get(o));
        }
        if ((boolean) map.get("Filled")) {
            node.setFill(Color.color((double) map.get("R"), (double) map.get("G"), (double) map.get("B")));
            node.setStroke(null);
        } else {
            node.setStroke(Color.color((double) map.get("R"), (double) map.get("G"), (double) map.get("B")));
            node.setStrokeWidth((double) map.get("StrokeWidth"));
            node.setFill(null);
        }
        return node;
    }

    private static Map<Object, Object> serializeEllipse(Node child) {
        Ellipse node = (Ellipse) child;
        Map<Object, Object> ret = new HashMap<>(Map.of(
                "CenterX", node.getCenterX(),
                "CenterY", node.getCenterY(),
                "RadiusX", node.getRadiusX(),
                "RadiusY", node.getRadiusY()
        ));
        ret.putAll(serializeFilled(node.getFill(), node.getStroke(), node.getStrokeWidth()));
        return Map.of("Ellipse", ret);
    }

    private static Map<Object, Object> serializeRectangle(Node child) {
        Rectangle node = (Rectangle) child;
        Map<Object, Object> ret = new HashMap<>(Map.of(
                "X", node.getX(),
                "Y", node.getY(),
                "Height", node.getHeight(),
                "Width", node.getWidth()
        ));
        ret.putAll(serializeFilled(node.getFill(), node.getStroke(), node.getStrokeWidth()));
        return Map.of("Rectangle", ret);
    }

    private static Map<Object, Object> serializeLine(Node child) {
        Line node = (Line) child;
        Map<Object, Object> ret = new HashMap<>(Map.of(
                "StartX", node.getStartX(),
                "StartY", node.getStartY(),
                "EndX", node.getEndX(),
                "EndY", node.getEndY()
        ));
        ret.putAll(serializeFilled(node.getFill(), node.getStroke(), node.getStrokeWidth()));
        return Map.of("Line", ret);
    }

    private static Map<Object, Object> serializePath(Node child) {
        Path node = (Path) child;
        Map<Object, Object> ret = new HashMap<>();
        var list = new ArrayList<>();
        for (PathElement element : node.getElements()) {
            if (element instanceof MoveTo) {
                var el = (MoveTo) element;
                list.add(Map.of("X", el.getX(), "Y", el.getY()));
            } else if (element instanceof LineTo) {
                var el = (LineTo) element;
                list.add(Map.of("X", el.getX(), "Y", el.getY()));
            }
        }
        ret.put("Elements", list);
        ret.putAll(serializeFilled(node.getFill(), node.getStroke(), node.getStrokeWidth()));
        return Map.of("Path", ret);
    }

    private static Map<Object, Object> serializeText(Node child) {
        Text node = (Text) child;
        Map<Object, Object> ret = new HashMap<>(Map.of(
                "Text", node.getText(),
                "Font", node.getFont().getName(),
                "Size", node.getFont().getSize()
        ));
        ret.putAll(serializeFilled(node.getFill(), node.getStroke(), node.getStrokeWidth()));
        return Map.of("Text", ret);
    }

    public static void serialize() {
        try (PrintWriter pw = new PrintWriter(new File("C:/Users/Waitti/Desktop/1.txt"))) {
            var res = new ArrayList<>();
            for (Node child : controller.cPane.getChildren()) {
                if (child instanceof Circle) {
                    res.add(serializeCircle(child));
                } else if (child instanceof Ellipse) {
                    res.add(serializeEllipse(child));
                } else if (child instanceof Rectangle) {
                    res.add(serializeRectangle(child));
                } else if (child instanceof Line) {
                    res.add(serializeLine(child));
                } else if (child instanceof Path) {
                    res.add(serializePath(child));
                } else if (child instanceof Text) {
                    res.add(serializeText(child));
                } else {
                    System.out.println("there are something can't be serialize");
                }
            }
            pw.println(gson.toJson(res));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deserialize() {
        var children = controller.cPane.getChildren();
        children.clear();
        try (var sc = new BufferedReader(new FileReader(new File("C:/Users/Waitti/Desktop/1.txt")))) {
            String r = sc.lines().collect(Collectors.joining());
            var arr = gson.fromJson(r, ArrayList.class);
            for (Object o : arr) {
                var map = (Map<?, ?>) o;
                for (Object key : map.keySet()) {
                    if (key.equals("Circle")) {
                        children.add(deserializeCircle((Map<?, ?>) map.get(key)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
