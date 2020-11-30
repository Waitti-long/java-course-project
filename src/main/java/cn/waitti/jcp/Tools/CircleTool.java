package cn.waitti.jcp.Tools;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class CircleTool implements EnabledTool {
    Circle circle = new Circle();
    Pane pane;
    ColorPicker colorPicker;
    ComboBox fillBox;
    double x1, y1, x2, y2;
    List<Circle> circleList = new ArrayList<>();

    CircleTool(Pane pane, ColorPicker colorPicker, ComboBox fillBox) {
        this.pane = pane;
        this.colorPicker = colorPicker;
        this.fillBox = fillBox;
    }

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
        if (fillBox.getValue() != null && fillBox.getValue().toString().equals("Fill")) {
            circle.setFill(colorPicker.getValue());
            circle.setStroke(null);
        } else if (fillBox.getValue() == null || fillBox.getValue().toString().equals("Stroke")) {
            circle.setStroke(colorPicker.getValue());
            circle.setFill(null);
        }
        circle.setOnMouseDragged(
                event -> {
                    Circle p = (Circle) event.getSource();
                    if (pane.contains(event.getX(), event.getY())) {
                        p.setCenterX(event.getX());
                        p.setCenterY(event.getY());
                    }
                });
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
    }
}
