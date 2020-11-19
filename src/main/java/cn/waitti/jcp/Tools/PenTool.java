package cn.waitti.jcp.Tools;

import com.sun.javafx.geom.Vec2d;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
import javafx.scene.shape.PathElement;

public class PenTool implements EnabledTool {
    AnchorPane cPane;
    Path path = null;

    PenTool(AnchorPane cPane) {
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
        path.setOnMouseDragged(
                event -> {
                    Path p = (Path)event.getSource();
                    ObservableList<PathElement> elements = p.getElements();
                    MoveTo origin = (MoveTo) elements.get(0);
                    MoveTo dist = new MoveTo(event.getX(), event.getY());
                    double offsetX = dist.getX() - origin.getX();
                    double offsetY = dist.getY() - origin.getY();
                    for (int i = 1; i < elements.size(); i++) {
                        LineTo lineTo = (LineTo)elements.get(i);
                        elements.set(i, new LineTo(lineTo.getX() + offsetX, lineTo.getY() + offsetY));
                    }
                    p.getElements().set(0, dist);
                }
        );
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
