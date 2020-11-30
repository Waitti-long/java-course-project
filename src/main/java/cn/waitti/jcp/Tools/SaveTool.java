package cn.waitti.jcp.Tools;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class SaveTool implements EnabledTool{
    public FileChooser fileChooser=new FileChooser();
    public BorderPane pane;
    public Pane cPane;
    SaveTool(BorderPane pane,Pane cPane){
        this.pane=pane;
        this.cPane=cPane;
    }

    public void FileSave(){
        Stage stage=(Stage) pane.getScene().getWindow();
        fileChooser.setTitle("保存图片");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG","*.png"),
                new FileChooser.ExtensionFilter("JPG","*.jpg")
        );
        File file=fileChooser.showSaveDialog(stage);
        WritableImage writableImage= cPane.snapshot(new SnapshotParameters(),null);
        String type=fileChooser.getSelectedExtensionFilter().getDescription();
        System.out.println(type);
        if(file!=null){
            try{
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage,null),type,file);
                System.out.println("Save");
                file=null;
                fileChooser=new FileChooser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void activate() {
        FileSave();
    }

    @Override
    public void deactivate() {

    }
}
