package cn.waitti.jcp.Tools;

import cn.waitti.jcp.Controller;
import cn.waitti.jcp.Serialize.Serializer;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.shape.Path;

import java.util.ArrayList;
import java.util.Stack;

public class Revocation {
    static Controller controller;
    static Stack<String> stack = new Stack<>();
    static int count = 0;

    public static void init(Controller controller) {
        Revocation.controller = controller;
//        Revocation.push();
    }

    public static void push() {
        try {
            stack.push(Serializer.serialize(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pop() {
        controller.cPane.getChildren().clear();
        if (!stack.isEmpty()) stack.pop();
        if (!stack.isEmpty()) {
            Serializer.deserialize(null, stack.peek());
        }
    }

    public static void popOut() {
        while (!stack.isEmpty())
            stack.pop();
    }

    public static Controller getController() {
        return Revocation.controller;
    }
}
