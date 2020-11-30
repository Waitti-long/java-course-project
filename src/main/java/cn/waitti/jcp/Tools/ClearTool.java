package cn.waitti.jcp.Tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ClearTool implements EnabledTool {
    Pane pane;

    ClearTool(Pane pane) {
        this.pane = pane;
    }

    @Override
    public void activate() {
        pane.getChildren().clear();
    }

    @Override
    public void deactivate() {
    }

    @Override
    public void start(MouseEvent event) {

    }

    @Override
    public void end(MouseEvent event) {

    }
}
