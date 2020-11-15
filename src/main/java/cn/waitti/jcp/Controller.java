package cn.waitti.jcp;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

public class Controller {
    public Canvas canvas;
    public AnchorPane pane;
    Path path = null;

    public void initialize() {

    }


    public void startDrawLine(MouseEvent mouseEvent) {
        System.out.println("start draw line");
        path = new Path();
        path.setStrokeWidth(2);
        path.getElements().add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
        pane.getChildren().add(path);
    }

    public void drawLine(MouseEvent mouseEvent) {
        path.getElements().add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
    }

    public void endDrawLine(MouseEvent mouseEvent) {
        System.out.println("end draw line");
        path = null;
    }


}
