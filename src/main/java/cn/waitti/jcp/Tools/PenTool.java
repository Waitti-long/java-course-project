package cn.waitti.jcp.Tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class PenTool implements EnabledTool{
    Pane pane;
    Path path = null;
    static PenTool instance = null;

    PenTool(Pane pane){
        this.pane = pane;
    }

    @Override
    public void activate() {
        pane.setOnMousePressed(this::startDrawLine);
        pane.setOnMouseDragged(this::drawLine);
        pane.setOnMouseReleased(this::endDrawLine);
    }

    @Override
    public void deactivate() {
        pane.setOnMousePressed(null);
        pane.setOnMouseDragged(null);
        pane.setOnMouseReleased(null);
    }

    public void startDrawLine(MouseEvent mouseEvent) {
        System.out.println("start draw line");
        path = new Path();
        path.setStrokeWidth(2);
        path.getElements().add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
        pane.getChildren().add(path);
    }

    public void drawLine(MouseEvent mouseEvent) {
        path.getElements().add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
    }

    public void endDrawLine(MouseEvent mouseEvent) {
        System.out.println("end draw line");
        path = null;
    }
}
