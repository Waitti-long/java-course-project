package cn.waitti.jcp.Tools;

import javafx.collections.ObservableList;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

public class PenTool implements EnabledTool {
    Pane pane;
    Path path = null;
    ColorPicker colorPicker;
    PenTool(Pane pane,ColorPicker colorPicker) {
        this.pane = pane;
        this.colorPicker= colorPicker;
    }

    @Override
    public void activate() {
        pane.setOnMousePressed(this::start);
        pane.setOnMouseDragged(this::among);
        pane.setOnMouseReleased(this::end);

    }

    @Override
    public void deactivate() {
        pane.setOnMousePressed(null);
        pane.setOnMouseDragged(null);
        pane.setOnMouseReleased(null);
    }

    @Override
    public void start(MouseEvent mouseEvent) {
        if (!pane.contains(mouseEvent.getX(), mouseEvent.getY()))
            return;
        path = new Path();
        path.setOnMouseDragged(
                event -> {
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
                }
        );
        path.setOnMouseReleased(e -> Revocation.push());
        path.setStroke(colorPicker.getValue());
        path.setStrokeWidth(1);
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
}
