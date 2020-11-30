package cn.waitti.jcp.Tools;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class LineTool implements EnabledTool {
    Line line = new Line();
    Pane pane;
    ColorPicker colorPicker;
    ComboBox fillBox;
    double x1, x2, y1, y2;
    List<Line> lineList = new ArrayList<>();

    LineTool(Pane pane, ColorPicker colorPicker, ComboBox fillBox) {
        this.colorPicker = colorPicker;
        this.fillBox = fillBox;
        this.pane = pane;
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
        line.setStartX(mouseEvent.getX());
        line.setStartY(mouseEvent.getY());
        line.setStroke(colorPicker.getValue());
        line.setOnMouseDragged(
                event -> {
                    Line p = (Line) event.getSource();
                    if (pane.contains(event.getX(), event.getY())) {
                        x1 = p.getStartX();
                        y1 = p.getStartY();
                        x2 = p.getEndX();
                        y2 = p.getEndY();
                        p.setStartX(event.getX());
                        p.setStartY(event.getY());
                        p.setEndX(event.getX() + (x2 - x1));
                        p.setEndY(event.getY() + y2 - y1);
                    }
                });
    }

    @Override
    public void end(MouseEvent mouseEvent) {
        line.setEndX(mouseEvent.getX());
        line.setEndY(mouseEvent.getY());
        pane.getChildren().add(line);
        lineList.add(line);
        line = new Line();
        Revocation.push();
    }
}
