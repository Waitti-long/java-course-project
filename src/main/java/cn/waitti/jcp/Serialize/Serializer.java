package cn.waitti.jcp.Serialize;

import cn.waitti.jcp.Controller;
import cn.waitti.jcp.Shapes.VCircle;
import com.google.gson.Gson;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Serializer {
    private static Controller controller;
    private static Gson gson = new Gson();

    public static void init(Controller controller) {
        Serializer.controller = controller;
    }

    private static Map<Object, Object> serializeFilled(double R, double G, double B) {
        return Map.of(
                "Filled", true,
                "R", R,
                "G", G,
                "B", B
        );
    }

    private static Map<Object, Object> serializeUnFilled() {
        return Map.of("Filled", false);
    }

    private static Map<Object, Object> serializeCircle(Node child) {
        Circle node = (Circle) child;
        Map<Object, Object> ret = new HashMap<>(Map.of(
                "CenterX", node.getCenterX(),
                "CenterY", node.getCenterY(),
                "Radius", node.getRadius()
        ));
        if (node.getFill() != null) {
            ret.putAll(serializeFilled(((Color) node.getFill()).getRed(), ((Color) node.getFill()).getGreen(), ((Color) node.getFill()).getBlue()));
        } else {
            ret.putAll(serializeUnFilled());
        }
        return Map.of("Circle", ret);
    }

    private static Map<Object, Object> serializeEllipse(Node child) {
        Ellipse node = (Ellipse) child;
        Map<Object, Object> ret = new HashMap<>(Map.of(
                "CenterX", node.getCenterX(),
                "CenterY", node.getCenterY(),
                "RadiusX", node.getRadiusX(),
                "RadiusY", node.getRadiusY()
        ));
        if (node.getFill() != null) {
            ret.putAll(serializeFilled(((Color) node.getFill()).getRed(), ((Color) node.getFill()).getGreen(), ((Color) node.getFill()).getBlue()));
        } else {
            ret.putAll(serializeUnFilled());
        }
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
        if (node.getFill() != null) {
            ret.putAll(serializeFilled(((Color) node.getFill()).getRed(), ((Color) node.getFill()).getGreen(), ((Color) node.getFill()).getBlue()));
        } else {
            ret.putAll(serializeUnFilled());
        }
        return Map.of("Rectangle", ret);
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
                }
            }
            pw.println(gson.toJson(res));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deserialize() {
    }
}
