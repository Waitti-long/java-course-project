package cn.waitti.jcp.Tools;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public interface Tool {
    void start(MouseEvent mouseEvent);

    default void among(MouseEvent mouseEvent) {

    }

    void end(MouseEvent mouseEvent);

    default EventHandler<? super MouseEvent> mouseDragged() {
        return event -> {
        };
    }

    default EventHandler<? super MouseEvent> mousePressed() {
        return event -> {
        };
    }

    default EventHandler<? super MouseEvent> mouseReleased() {
        return event -> {
        };
    }
}
