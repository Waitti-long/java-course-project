package cn.waitti.jcp;

import cn.waitti.jcp.Tools.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


public class Controller {
    public BorderPane pane;
    public Pane cPane;
    public ColorPicker colorPicker;
    public ComboBox fillBox;
    public ComboBox fontBox;
    public ComboBox sizeBox;
    public CheckBox boldCheck;
    public CheckBox italicCheck;
    public MenuItem FileSaver;
    public MenuItem FileClose;
    public MenuItem Undo;

    public void initialize() throws Exception {
        ToolPicker.activate(MouseTool.class);
    }

    public void choosePen(MouseEvent mouseEvent) throws Exception {
        ToolPicker.activateWithArgs(PenTool.class, new Class[]{Pane.class, ColorPicker.class,ComboBox.class}, cPane, colorPicker,sizeBox);
    }

    public void chooseMouse(MouseEvent mouseEvent) throws Exception {
        ToolPicker.activate(MouseTool.class);
    }

    public void chooseRec(MouseEvent mouseEvent) throws Exception {
        ToolPicker.activateWithArgs(RecTool.class, new Class[]{Pane.class, ColorPicker.class, ComboBox.class,ComboBox.class}, cPane, colorPicker, fillBox,sizeBox);
    }

    public void chooseEllipse(MouseEvent mouseEvent) throws Exception {
        ToolPicker.activateWithArgs(EllipseTool.class, new Class[]{Pane.class, ColorPicker.class, ComboBox.class,ComboBox.class}, cPane, colorPicker, fillBox,sizeBox);
    }

    public void chooseClear(ActionEvent mouseEvent) throws Exception {
        ToolPicker.activateWithArgs(ClearTool.class, new Class[]{Pane.class}, cPane);
    }

    public void chooseCircle(MouseEvent mouseEvent) throws Exception {
        ToolPicker.activateWithArgs(CircleTool.class, new Class[]{Pane.class, ColorPicker.class, ComboBox.class,ComboBox.class}, cPane, colorPicker, fillBox,sizeBox);
    }

    public void chooseLine(MouseEvent mouseEvent) throws Exception {
        ToolPicker.activateWithArgs(LineTool.class, new Class[]{Pane.class, ColorPicker.class, ComboBox.class,ComboBox.class}, cPane, colorPicker, fillBox,sizeBox);
    }

    public void chooseText(MouseEvent mouseEvent) throws Exception {
        ToolPicker.activateWithArgs(TextTool.class, new Class[]{Pane.class, ColorPicker.class, ComboBox.class, ComboBox.class,CheckBox.class,CheckBox.class}, cPane, colorPicker, fontBox, sizeBox,boldCheck,italicCheck);
    }

    public void chooseSave(ActionEvent mouseEvent)throws Exception{
        ToolPicker.activateWithArgs(SaveTool.class, new Class[]{BorderPane.class,Pane.class}, pane,cPane);
    }

    public void chooseClose(ActionEvent actionEvent)throws Exception{
        Platform.exit();
    }

    public void chooseUndo(ActionEvent actionEvent)throws Exception{
        Revocation.pop();
    }

    public void chooseModify(MouseEvent mouseEvent)throws Exception{
        ToolPicker.activate(ModifyTool.class);
    }
}
