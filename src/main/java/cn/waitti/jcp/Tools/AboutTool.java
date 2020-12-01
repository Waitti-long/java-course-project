package cn.waitti.jcp.Tools;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;

public class AboutTool{
    public static void showDialog(){
        Dialog dialog=new Dialog();
        dialog.setTitle("About");
        dialog.setContentText("WrittenBy: @TangSeng\n                  @Waitti ");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.showAndWait();
    }
}
