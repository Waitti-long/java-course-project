package cn.waitti.jcp.Tools;

import cn.waitti.jcp.Controller;

public interface EnabledTool extends Tool{
    default void activate() {
        Revocation.getController().cPane.setOnMousePressed(this::start);
        Revocation.getController().cPane.setOnMouseDragged(this::among);
        Revocation.getController().cPane.setOnMouseReleased(this::end);
    }


    default void deactivate() {
        Revocation.getController().cPane.setOnMousePressed(null);
        Revocation.getController().cPane.setOnMouseDragged(null);
        Revocation.getController().cPane.setOnMouseReleased(null);
    }


}

