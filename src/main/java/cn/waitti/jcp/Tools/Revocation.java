package cn.waitti.jcp.Tools;

import cn.waitti.jcp.Controller;
import javafx.scene.Node;
import javafx.scene.control.Button;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.Stack;

public class Revocation {
    static Controller controller;
    static Stack<ArrayList<Node>> stack = new Stack<>();
    static int count = 0;

    public static void init(Controller controller) {
        Revocation.controller = controller;
        Revocation.push();
    }

    public static void push() {
        try{
            ArrayList<Node> arr = new ArrayList<>();
            for (Node child : controller.cPane.getChildren()) {
                Node node = child.getClass().getConstructor().newInstance();
                BeanUtils.copyProperties(node, child);
                arr.add(node);
                System.out.println("push "+count++);
            }
            stack.push(arr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void pop() {
        controller.cPane.getChildren().clear();
        if(stack.size() > 1)stack.pop();
        if(!stack.isEmpty()){
            controller.cPane.getChildren().addAll(stack.peek());
        }
    }

    public static Controller getController(){
        return Revocation.controller;
    }
}
