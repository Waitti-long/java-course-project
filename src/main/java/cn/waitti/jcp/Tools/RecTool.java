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
    public void activate() {
        pane.setOnMousePressed(this::start);
        pane.setOnMouseReleased(this::end);
    }

    @Override
    public void deactivate() {
        pane.setOnMousePressed(null);
        pane.setOnMouseReleased(null);
    }

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
}
