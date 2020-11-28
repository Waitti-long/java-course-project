package cn.waitti.jcp.Tools;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ClearTool implements EnabledTool {
    Pane pane;
    ClearTool( Pane pane) {
        this.pane = pane;
    }
    @Override
    public void activate() {
        pane.getChildren().clear();
    }

    @Override
    public void deactivate() {
    }
}
