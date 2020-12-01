package cn.waitti.jcp.Tools;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;

import java.util.ArrayList;
import java.util.List;

public class EllipseTool implements EnabledTool {
    public Pane pane;
    public double x1, x2, y1, y2, width, height;
    public ColorPicker colorPicker;
    public ComboBox fillBox;
    public ComboBox sizeBox;

    EllipseTool(Pane cPane, ColorPicker colorPicker, ComboBox fillBox,ComboBox sizeBox) {
        this.pane = cPane;
        this.colorPicker = colorPicker;
        this.fillBox = fillBox;
        this.sizeBox=sizeBox;
    }

    Ellipse ellipse = new Ellipse();
    List<Ellipse> ellipseList = new ArrayList<>();

    @Override
    public void start(MouseEvent mouseEvent) {
        x1 = mouseEvent.getX();
        y1 = mouseEvent.getY();
        if (fillBox.getValue() != null && fillBox.getValue().toString().equals("Fill")) {
            ellipse.setFill(colorPicker.getValue());
            ellipse.setStroke(null);
        } else if (fillBox.getValue() == null || fillBox.getValue().toString().equals("Stroke")) {
            ellipse.setStroke(colorPicker.getValue());
            if(sizeBox.getValue()==null)
                ellipse.setStrokeWidth(1);
            else
                ellipse.setStrokeWidth(Double.parseDouble(sizeBox.getValue().toString()));
            ellipse.setFill(null);
        }
        ellipse.setCenterX(x1);
        ellipse.setCenterY(y1);
        ellipse.setOnMouseDragged(e -> {
            Ellipse el = (Ellipse) e.getSource();
            if (pane.contains(e.getX(), e.getY())&& ToolPicker.getCurrentTool() instanceof MouseTool) {
                el.setCenterX(e.getX());
                el.setCenterY(e.getY());
            }
        });
        ellipse.setOnMousePressed(event -> {
            Ellipse el = (Ellipse) event.getSource();
            if(ToolPicker.getCurrentTool() instanceof ModifyTool) {
                if (fillBox.getValue() != null && fillBox.getValue().toString().equals("Fill")) {
                    el.setFill(colorPicker.getValue());
                    el.setStroke(null);
                } else if (fillBox.getValue() == null || fillBox.getValue().toString().equals("Stroke")) {
                    el.setStroke(colorPicker.getValue());
                    if (sizeBox.getValue() == null)
                        el.setStrokeWidth(1);
                    else
                        el.setStrokeWidth(Double.parseDouble(sizeBox.getValue().toString()));
                    el.setFill(null);
                }
            }
        });
        ellipse.setOnMouseReleased(e -> Revocation.push());
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
        ellipse.setRadiusX(width);
        ellipse.setRadiusY(height);
        pane.getChildren().add(ellipse);
        ellipseList.add(ellipse);
        ellipse = new Ellipse();
        Revocation.push();
    }
}
