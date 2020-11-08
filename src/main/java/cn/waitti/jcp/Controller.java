package cn.waitti.jcp;

import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;

public class Controller {
    public Circle circle;
    public BorderPane pane;

    public void initialize(){
        circle.setOnMouseClicked(action -> {
            circle.setRadius(circle.getRadius() * 1.1);
        });
    }
}
