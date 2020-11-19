package cn.waitti.jcp;

import cn.waitti.jcp.Tools.*;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;

public class Controller {
    public BorderPane pane;
    public Pane cPane;
    public Group group;

    public void initialize() throws Exception {
        ToolPicker.activate(MouseTool.class);
    }

    public void choosePen(MouseEvent mouseEvent) throws Exception {
        ToolPicker.activateWithArgs(PenTool.class, new Class[]{Pane.class}, cPane);
    }

    public void chooseMouse(MouseEvent mouseEvent) throws Exception {
        ToolPicker.activate(MouseTool.class);
    }

    public void chooseRec(MouseEvent mouseEvent) throws Exception {
        ToolPicker.activateWithArgs(RecTool.class, new Class[]{});
    }
}
