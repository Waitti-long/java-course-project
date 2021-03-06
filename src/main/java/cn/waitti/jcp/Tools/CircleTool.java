package cn.waitti.jcp.Tools;

import cn.waitti.jcp.Serialize.SerializeConfigure;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

@SerializeConfigure(node = Circle.class, serializeClasses = {double.class, double.class, double.class}, serializeStrings = {"CenterX", "CenterY", "Radius"})
public class CircleTool implements EnabledTool {
    Circle circle = new Circle();
    Pane pane;
    ColorPicker colorPicker;
    ComboBox fillBox;
    ComboBox sizeBox;
    double x1, y1, x2, y2;
    List<Circle> circleList = new ArrayList<>();

    CircleTool(Pane pane, ColorPicker colorPicker, ComboBox fillBox, ComboBox sizeBox) {
        this.pane = pane;
        this.colorPicker = colorPicker;
        this.fillBox = fillBox;
        this.sizeBox = sizeBox;
    }


    @Override
    public void start(MouseEvent mouseEvent) {
        x1 = mouseEvent.getX();
        y1 = mouseEvent.getY();
        if (fillBox.getValue() != null && fillBox.getValue().toString().equals("Fill")) {
            circle.setFill(colorPicker.getValue());
            circle.setStroke(null);
        } else if (fillBox.getValue() == null || fillBox.getValue().toString().equals("Stroke")) {
            circle.setStroke(colorPicker.getValue());
            if (sizeBox.getValue() == null)
                circle.setStrokeWidth(1);
            else
                circle.setStrokeWidth(Double.parseDouble(sizeBox.getValue().toString()));
            circle.setFill(null);
        }
        circle.setOnMouseDragged(mouseDragged());
        circle.setOnMousePressed(mousePressed());
        circle.setOnMouseDragReleased(mouseDragReleased());
    }

    @Override
    public void end(MouseEvent mouseEvent) {
        x2 = mouseEvent.getX();
        y2 = mouseEvent.getY();
        circle.setCenterX((x2 + x1) / 2.0);
        circle.setCenterY((y1 + y2) / 2.0);
        circle.setRadius(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) / 2.0);
        pane.getChildren().add(circle);
        circleList.add(circle);
        circle = new Circle();
        Revocation.push();
        NewTool.push();
    }

    @Override
    public EventHandler<? super MouseEvent> mouseDragged() {
        return event -> {
            Circle p = (Circle) event.getSource();
            if (pane.contains(event.getX(), event.getY()) && ToolPicker.getCurrentTool() instanceof MouseTool) {
                p.setCenterX(event.getX());
                p.setCenterY(event.getY());
            }
        };
    }

    @Override
    public EventHandler<? super MouseEvent> mousePressed() {
        return event -> {
            Circle p = (Circle) event.getSource();
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
