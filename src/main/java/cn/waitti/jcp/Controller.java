package cn.waitti.jcp;

import cn.waitti.jcp.Tools.EnabledTool;
import cn.waitti.jcp.Tools.MouseTool;
import cn.waitti.jcp.Tools.PenTool;
import cn.waitti.jcp.Tools.ToolPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

public class Controller {
    public AnchorPane pane;
    public AnchorPane canvas;

    public void initialize()throws Exception {
        ToolPicker.activate(MouseTool.class);
    }

    public void choosePen(MouseEvent mouseEvent) throws Exception{
        ToolPicker.activateWithArgs(PenTool.class, new Class[]{Pane.class}, pane);
    }

    public void chooseMouse(MouseEvent mouseEvent) throws Exception{
        ToolPicker.activate(MouseTool.class);
    }
}
