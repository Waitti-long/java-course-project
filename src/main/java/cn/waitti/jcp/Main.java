package cn.waitti.jcp;

import cn.waitti.jcp.Serialize.Serializer;
import cn.waitti.jcp.Tools.*;
import com.sun.javafx.fxml.FXMLLoaderHelper;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        var loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Scene scene = new Scene(loader.load(), 640, 480);
        scene.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN), Revocation::pop);
        primaryStage.setScene(scene);
        primaryStage.setTitle("java-course-project");
        primaryStage.show();
        Controller controller = loader.getController();
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), Serializer::serialize);
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN), Serializer::deserialize);
        Revocation.init(controller);
        Serializer.init(controller);
        ToolPicker.activateWithArgs(PenTool.class, new Class[]{Pane.class, ColorPicker.class, ComboBox.class}, controller.cPane, controller.colorPicker, controller.sizeBox);
        ToolPicker.activateWithArgs(RecTool.class, new Class[]{Pane.class, ColorPicker.class, ComboBox.class, ComboBox.class}, controller.cPane, controller.colorPicker, controller.fillBox, controller.sizeBox);
        ToolPicker.activateWithArgs(EllipseTool.class, new Class[]{Pane.class, ColorPicker.class, ComboBox.class, ComboBox.class}, controller.cPane, controller.colorPicker, controller.fillBox, controller.sizeBox);
        ToolPicker.activateWithArgs(ClearTool.class, new Class[]{Pane.class}, controller.cPane);
        ToolPicker.activateWithArgs(CircleTool.class, new Class[]{Pane.class, ColorPicker.class, ComboBox.class, ComboBox.class}, controller.cPane, controller.colorPicker, controller.fillBox, controller.sizeBox);
        ToolPicker.activateWithArgs(LineTool.class, new Class[]{Pane.class, ColorPicker.class, ComboBox.class, ComboBox.class}, controller.cPane, controller.colorPicker, controller.fillBox, controller.sizeBox);
        ToolPicker.activateWithArgs(TextTool.class, new Class[]{Pane.class, ColorPicker.class, ComboBox.class, ComboBox.class, CheckBox.class, CheckBox.class}, controller.cPane, controller.colorPicker, controller.fontBox, controller.sizeBox, controller.boldCheck, controller.italicCheck);
        ToolPicker.activate(MouseTool.class);
    }
}
