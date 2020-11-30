package cn.waitti.jcp.Tools;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class RecTool implements EnabledTool {
    public Pane pane;
    public double x1, x2, y1, y2, width, height;
    public ColorPicker colorPicker;
    public ComboBox fillBox;
    RecTool(Pane cPane,ColorPicker colorPicker,ComboBox fillBox) {
        this.pane = cPane;
        this.colorPicker=colorPicker;
        this.fillBox=fillBox;
    }

    Rectangle rectangle = new Rectangle();
    List<Rectangle> rectangleList = new ArrayList<>();


    @Override
    public void start(MouseEvent mouseEvent) {
        x1 = mouseEvent.getX();
        y1 = mouseEvent.getY();
        if(fillBox.getValue()!=null && fillBox.getValue().toString().equals("Fill")){
            rectangle.setFill(colorPicker.getValue());
            rectangle.setStroke(null);
        }
        else if(fillBox.getValue()==null||fillBox.getValue().toString().equals("Stroke")) {
            rectangle.setStroke(colorPicker.getValue());
            rectangle.setFill(null);
        }
        rectangle.setX(x1);
        rectangle.setY(y1);
        rectangle.setOnMouseDragged(
                event -> {
                    Rectangle p = (Rectangle) event.getSource();
                    if(pane.contains(event.getX(),event.getY())) {
                        p.setX(event.getX());
                        p.setY(event.getY());
                    }
                });
        rectangle.setOnMouseReleased(e -> Revocation.push());
    }

    @Override
    public void end(MouseEvent mouseEvent) {
        x2 = mouseEvent.getX();
        y2 = mouseEvent.getY();
        width = x2 - x1;
        height = y2 - y1;
        if (width < 0) {
            width = -width;
            x1 = x1 - width;
        }
        if (height < 0) {
            height = -height;
            y1 = y1 - height;
        }
        rectangle.setHeight(height);
        rectangle.setWidth(width);
        pane.getChildren().add(rectangle);

        rectangleList.add(rectangle);
        rectangle = new Rectangle();
        Revocation.push();
    }
    /*@Override
    public void activate() {
        gc=canvas.getGraphicsContext2D();
        canvas.setOnMousePressed(this::startDrawRec);
        canvas.setOnMouseReleased(this::endDrawRec);
    }

    public void startDrawRec(MouseEvent mouseEvent){
        x1= mouseEvent.getX();
        y1= mouseEvent.getY();
        path=new Path();
        path.getElements().add(new MoveTo(x1, y1));
    }

    public void pathRec(MouseEvent mouseEvent){
        path.getElements().add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
    }

    public void endDrawRec(MouseEvent mouseEvent){
        x2= mouseEvent.getX();
        y2= mouseEvent.getY();
        width=x2-x1;
        height=y2-y1;
        DrawRec(x1,y1,width,height);
        path=null;
    }

    public void DrawRec(double x1, double y1, double width, double height){
        if(width<0){
            width=-width;
            x1=x1-width;
        }
        if (height < 0) {
            height = -height;
            y1 = y1 - height;
        }
        gc.strokeRect(x1,y1,width,height);
    }
    @Override
    public void deactivate() {
        gc=null;
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
    }*/
}
