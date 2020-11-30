package cn.waitti.jcp.Tools;

import cn.waitti.jcp.Controller;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.Stack;

public class Revocation {
    static Controller controller;
    static Stack<ArrayList<Node>> stack = new Stack<>();

    public static void init(Controller controller) {
        Revocation.controller = controller;
        Revocation.push();
    }

    public static void push() {
        // TODO：deep copy
        stack.push(new ArrayList<>(controller.cPane.getChildren()));
    }


    public static void pop() {
        controller.cPane.getChildren().clear();
        if(stack.size() > 1)stack.pop();
        if(!stack.isEmpty()){
            controller.cPane.getChildren().addAll(stack.peek());
        }
    }
}
