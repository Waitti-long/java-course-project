package cn.waitti.jcp.Tools;

import cn.waitti.jcp.Serialize.SerializeConfigure;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

@SerializeConfigure(node = Line.class, serializeStrings = {"StartX", "StartY", "EndX", "EndY"}, serializeClasses = {double.class, double.class, double.class, double.class})
public class LineTool implements EnabledTool {
    Line line = new Line();
    Pane pane;
    ColorPicker colorPicker;
    ComboBox fillBox;
    ComboBox sizeBox;
    double x1, x2, y1, y2;
    List<Line> lineList = new ArrayList<>();

    LineTool(Pane pane, ColorPicker colorPicker, ComboBox fillBox, ComboBox sizeBox) {
        this.colorPicker = colorPicker;
        this.fillBox = fillBox;
        this.pane = pane;
        this.sizeBox = sizeBox;
    }

    @Override
    public void start(MouseEvent mouseEvent) {
        line.setStartX(mouseEvent.getX());
        line.setStartY(mouseEvent.getY());
        line.setStroke(colorPicker.getValue());
        if (sizeBox.getValue() == null)
            line.setStrokeWidth(1);
        else
            line.setStrokeWidth(Double.parseDouble(sizeBox.getValue().toString()));
        line.setOnMouseDragged(mouseDragged());
        line.setOnMousePressed(mousePressed());
        line.setOnMouseDragReleased(mouseDragReleased());
    }

    @Override
    public EventHandler<? super MouseEvent> mouseDragged() {
        return event -> {
            Line p = (Line) event.getSource();
            if (pane.contains(event.getX(), event.getY()) && ToolPicker.getCurrentTool() instanceof MouseTool) {
                x1 = p.getStartX();
                y1 = p.getStartY();
                x2 = p.getEndX();
                y2 = p.getEndY();
                p.setStartX(event.getX());
                p.setStartY(event.getY());
                p.setEndX(event.getX() + (x2 - x1));
                p.setEndY(event.getY() + y2 - y1);
            }
        };
    }

    @Override
    public EventHandler<? super MouseEvent> mousePressed() {
        return event -> {
            Line p = (Line) event.getSource();
            if (ToolPicker.getCurrentTool() instanceof ModifyTool) {
                p.setStroke(colorPicker.getValue());
                if (sizeBox.getValue() == null)
                    p.setStrokeWidth(1);
                else
                    p.setStrokeWidth(Double.parseDouble(sizeBox.getValue().toString()));
                Revocation.push();
                NewTool.push();
            }

        };
    }

    @Override
    public EventHandler<? super MouseEvent> mouseDragReleased() {
        return event -> {
            Revocation.push();
            NewTool.push();
        };
    }

    @Override
    public void end(MouseEvent mouseEvent) {
        line.setEndX(mouseEvent.getX());
        line.setEndY(mouseEvent.getY());
        pane.getChildren().add(line);
        lineList.add(line);
        line = new Line();
        Revocation.push();
        NewTool.push();
    }
}
