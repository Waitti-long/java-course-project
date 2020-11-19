package cn.waitti.jcp.Tools;

import com.sun.javafx.geom.Vec2d;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

public class PenTool implements EnabledTool {
    Pane pane;
    Path path = null;

    PenTool(Pane pane) {
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
//        System.out.println("start draw line");
        if (!pane.contains(mouseEvent.getX(), mouseEvent.getY()))
            return;
        path = new Path();
        path.setOnMouseDragged(
                event -> {
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
        );
        path.setStrokeWidth(2);
        path.getElements().add(new MoveTo(mouseEvent.getX(), mouseEvent.getY()));
        pane.getChildren().add(path);
//        gc=pane.getGraphicsContext2D();
//        gc.moveTo(mouseEvent.getX(), mouseEvent.getY());
        //group.getChildren().add(path);
    }

    public void drawLine(MouseEvent mouseEvent) {
        if (path == null || !pane.contains(mouseEvent.getX(), mouseEvent.getY()))
            return;
        path.getElements().add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
//        gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
//        gc.stroke();
    }

    public void endDrawLine(MouseEvent mouseEvent) {
//        System.out.println("end draw line");
        path = null;
//        gc=null;
    }
}
