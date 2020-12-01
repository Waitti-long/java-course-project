package cn.waitti.jcp.Serialize;

import cn.waitti.jcp.Controller;
import com.google.gson.Gson;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

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

    private static Map<Object, Object> serializeFilled(double R, double G, double B){
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
        if(node.getFill() != null){
            ret.putAll(serializeFilled(((Color)node.getFill()).getRed(),((Color)node.getFill()).getGreen(),((Color)node.getFill()).getBlue()));
        }else{
            ret.putAll(serializeUnFilled());
        }
        return Map.of("Circle", ret);
    }

    private static Map<Object, Object> serializerEllipse(Node child){
        Ellipse node = (Ellipse)child;
        return Map.of("Ellipse",null );
    }

    public static void serialize() {
        try (PrintWriter pw = new PrintWriter(new File("C:/Users/Waitti/Desktop/1.txt"))) {
            var res = new ArrayList<Object>();
            for (Node child : controller.cPane.getChildren()) {
                if (child instanceof Circle) {
                    res.add(serializeCircle(child));
                } else if (child instanceof Ellipse) {
//                    res.add();
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
