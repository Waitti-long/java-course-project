package cn.waitti.jcp;

import cn.waitti.jcp.Tools.EnabledTool;
import cn.waitti.jcp.Tools.MouseTool;
import cn.waitti.jcp.Tools.PenTool;
import cn.waitti.jcp.Tools.ToolPicker;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;

public class Controller {
    public BorderPane pane;
    public AnchorPane cPane;


    public void initialize()throws Exception {
        ToolPicker.activate(MouseTool.class);
    }

    public void choosePen(MouseEvent mouseEvent) throws Exception{
        ToolPicker.activateWithArgs(PenTool.class, new Class[]{AnchorPane.class}, cPane);
    }

    public void chooseMouse(MouseEvent mouseEvent) throws Exception{
        ToolPicker.activate(MouseTool.class);
    }
}
