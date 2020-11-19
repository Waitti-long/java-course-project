package cn.waitti.jcp.Tools;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class PenTool implements EnabledTool{
    //Group group;
    //Path path = null;
    Canvas canvas;
    GraphicsContext gc=null;
    PenTool(Canvas canvas){
        //this.group = group;
        this.canvas=canvas;
    }

    @Override
    public void activate() {
        canvas.setOnMousePressed(this::startDrawLine);
        canvas.setOnMouseDragged(this::drawLine);
        canvas.setOnMouseReleased(this::endDrawLine);

    }

    @Override
    public void deactivate() {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
    }

    public void startDrawLine(MouseEvent mouseEvent) {
//        System.out.println("start draw line");
        /*path = new Path();
        path.setStrokeWidth(2);
        path.getElements().add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));*/
        gc=canvas.getGraphicsContext2D();
        gc.moveTo(mouseEvent.getX(), mouseEvent.getY());
        //group.getChildren().add(path);
    }

    public void drawLine(MouseEvent mouseEvent) {
        //path.getElements().add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
        gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
        gc.stroke();
    }

    public void endDrawLine(MouseEvent mouseEvent) {
//        System.out.println("end draw line");
        //path = null;
        gc=null;
    }
}
