package cn.waitti.jcp.Tools;

import cn.waitti.jcp.Serialize.SerializeConfigure;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

@SerializeConfigure(node = Rectangle.class, serializeStrings = {"X", "Y", "Height", "Width"}, serializeClasses = {double.class, double.class, double.class, double.class})
public class RecTool implements EnabledTool {
    public Pane pane;
    public double x1, x2, y1, y2, width, height;
    public ColorPicker colorPicker;
    public ComboBox fillBox;
    public ComboBox sizeBox;

    RecTool(Pane cPane, ColorPicker colorPicker, ComboBox fillBox, ComboBox sizeBox) {
        this.pane = cPane;
        this.colorPicker = colorPicker;
        this.fillBox = fillBox;
        this.sizeBox = sizeBox;
    }

    Rectangle rectangle = new Rectangle();
    List<Rectangle> rectangleList = new ArrayList<>();


    @Override
    public void start(MouseEvent mouseEvent) {
        x1 = mouseEvent.getX();
        y1 = mouseEvent.getY();
        if (fillBox.getValue() != null && fillBox.getValue().toString().equals("Fill")) {
            rectangle.setFill(colorPicker.getValue());
            rectangle.setStroke(null);
        } else if (fillBox.getValue() == null || fillBox.getValue().toString().equals("Stroke")) {
            rectangle.setStroke(colorPicker.getValue());
            if (sizeBox.getValue() == null)
                rectangle.setStrokeWidth(1);
            else
                rectangle.setStrokeWidth(Double.parseDouble(sizeBox.getValue().toString()));
            rectangle.setFill(null);
        }
        rectangle.setX(x1);
        rectangle.setY(y1);
        rectangle.setOnMouseDragged(mouseDragged());
        rectangle.setOnMousePressed(mousePressed());
        rectangle.setOnMouseDragReleased(mouseDragReleased());
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
        NewTool.push();
    }

    @Override
    public EventHandler<? super MouseEvent> mouseDragged() {
        return event -> {
            Rectangle p = (Rectangle) event.getSource();
            if (pane.contains(event.getX(), event.getY()) && ToolPicker.getCurrentTool() instanceof MouseTool) {
                p.setX(event.getX());
                p.setY(event.getY());
            }
        };
    }

    @Override
    public EventHandler<? super MouseEvent> mousePressed() {
        return event -> {
            Rectangle p = (Rectangle) event.getSource();
            if (ToolPicker.getCurrentTool() instanceof ModifyTool) {
                if (fillBox.getValue() != null && fillBox.getValue().toString().equals("Fill")) {
                    p.setFill(colorPicker.getValue());
                    p.setStroke(null);
                } else if (fillBox.getValue() == null || fillBox.getValue().toString().equals("Stroke")) {
                    p.setStroke(colorPicker.getValue());
                    if (sizeBox.getValue() == null)
                        p.setStrokeWidth(1);
                    else
                        p.setStrokeWidth(Double.parseDouble(sizeBox.getValue().toString()));
                    p.setFill(null);
                }
            }
            Revocation.push();
            NewTool.push();
        };
    }

    @Override
    public EventHandler<? super MouseEvent> mouseDragReleased() {
        return event -> {
            Revocation.push();
            NewTool.push();
        };
    }
}
