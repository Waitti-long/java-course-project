package cn.waitti.jcp.Tools;

import javafx.scene.input.MouseEvent;

public interface Tool {
    void start(MouseEvent mouseEvent);

    default void among(MouseEvent mouseEvent){

    }

    void end(MouseEvent mouseEvent);
}
