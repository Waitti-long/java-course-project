package cn.waitti.jcp.Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class PenTool implements EnabledTool{
    AnchorPane cPane;
    Path path = null;

    PenTool(AnchorPane cPane){
        this.cPane = cPane;
    }

    @Override
    public void activate() {
        cPane.setOnMousePressed(this::startDrawLine);
        cPane.setOnMouseDragged(this::drawLine);
        cPane.setOnMouseReleased(this::endDrawLine);

    }

    @Override
    public void deactivate() {
        cPane.setOnMousePressed(null);
        cPane.setOnMouseDragged(null);
        cPane.setOnMouseReleased(null);
    }

    public void startDrawLine(MouseEvent mouseEvent) {
//        System.out.println("start draw line");
        path = new Path();
        path.setStrokeWidth(2);
        path.getElements().add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
        cPane.getChildren().add(path);
    }

    public void drawLine(MouseEvent mouseEvent) {
        path.getElements().add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
    }

    public void endDrawLine(MouseEvent mouseEvent) {
//        System.out.println("end draw line");
        path = null;
    }
}
