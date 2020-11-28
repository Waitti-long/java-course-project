package cn.waitti.jcp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Arrays;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/main.fxml")),640, 480);
        scene.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("java-course-project");
        primaryStage.show();
    }
}
