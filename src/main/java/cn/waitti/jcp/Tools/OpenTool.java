package cn.waitti.jcp.Tools;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class OpenTool implements EnabledTool{
    public FileChooser fileChooser=new FileChooser();
    public BorderPane pane;
    public Pane cPane;
    OpenTool(BorderPane pane,Pane cPane){
        this.pane=pane;
        this.cPane=cPane;
    }

    public void FileOpen(){
        Stage stage=(Stage) pane.getScene().getWindow();
        fileChooser.setTitle("打开图片");
        fileChooser.getExtensionFilters().addAll(
          new FileChooser.ExtensionFilter("PNG","*.png"),
          new FileChooser.ExtensionFilter("JPG","*.jpg")
        );
        File file=fileChooser.showOpenDialog(stage);
        if(file!=null){
            Image image=new Image(file.toURI().toString());
            ImageView imageView=new ImageView(image);
            pane.setCenter(imageView);
            file=null;
            fileChooser=new FileChooser();
        }
    }

    @Override
    public void activate() {
        FileOpen();
    }

    @Override
    public void start(MouseEvent mouseEvent) {

    }

    @Override
    public void end(MouseEvent mouseEvent) {

    }
}
