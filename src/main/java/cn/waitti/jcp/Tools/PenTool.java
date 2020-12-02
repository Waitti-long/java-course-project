package cn.waitti.jcp.Tools;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

public class PenTool implements EnabledTool {
    Pane pane;
    Path path = null;
    ColorPicker colorPicker;
    ComboBox sizeBox;
    PenTool(Pane pane,ColorPicker colorPicker,ComboBox sizeBox) {
        this.pane = pane;
        this.colorPicker= colorPicker;
        this.sizeBox=sizeBox;
    }

    @Override
    public void start(MouseEvent mouseEvent) {
        if (!pane.contains(mouseEvent.getX(), mouseEvent.getY()))
            return;
        path = new Path();
        path.setOnMouseDragged(mouseDragged());
        path.setOnMousePressed(mousePressed());
        path.setOnMouseReleased(mouseReleased());
        path.setStroke(colorPicker.getValue());
        if(sizeBox.getValue()==null)
            path.setStrokeWidth(1);
        else
            path.setStrokeWidth(Double.parseDouble(sizeBox.getValue().toString()));
        path.getElements().add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
        pane.getChildren().add(path);
    }

    @Override
    public void among(MouseEvent mouseEvent) {
        if (path == null || !pane.contains(mouseEvent.getX(), mouseEvent.getY()))
            return;
        path.getElements().add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
    }

    @Override
    public void end(MouseEvent event) {
        path = null;
        Revocation.push();
    }

    @Override
    public EventHandler<? super MouseEvent> mouseDragged() {
        return event -> {
            if (ToolPicker.getCurrentTool() instanceof MouseTool) {
                Path p = (Path) event.getSource();
                ObservableList<PathElement> elements = p.getElements();
                MoveTo origin = (MoveTo) elements.get(0);
                MoveTo dist = new MoveTo(event.getX(), event.getY());
                double offsetX = dist.getX() - origin.getX();
                double offsetY = dist.getY() - origin.getY();
                for (int i = 1; i < elements.size(); i++) {
                    LineTo lineTo = (LineTo) elements.get(i);
                    elements.set(i, new LineTo(lineTo.getX() + offsetX, lineTo.getY() + offsetY));
                }
                p.getElements().set(0, dist);
            }
        };
    }

    @Override
    public EventHandler<? super MouseEvent> mousePressed() {
        return event -> {
            Path p = (Path) event.getSource();
            if(ToolPicker.getCurrentTool() instanceof ModifyTool){
                p.setStroke(colorPicker.getValue());
                if(sizeBox.getValue()==null)
                    p.setStrokeWidth(1);
                else
                    p.setStrokeWidth(Double.parseDouble(sizeBox.getValue().toString()));
            }
        };
    }

    @Override
    public EventHandler<? super MouseEvent> mouseReleased() {
        return e -> Revocation.push();
    }
}
