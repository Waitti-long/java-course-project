package cn.waitti.jcp.Tools;

import cn.waitti.jcp.Serialize.Serializer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.Optional;
import java.util.Stack;

public class NewTool implements EnabledTool{
    public BorderPane pane;
    public Pane cPane;
    static Stack<Integer> saveStack=new Stack<>();
    NewTool(BorderPane pane,Pane cPane){
        this.pane=pane;
        this.cPane=cPane;
    }

    public void FileOpen(){
        //saveStack不为空时提示保存
        if(!saveStack.empty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Tips");
            alert.setHeaderText("You haven't saved it before,do you want to save it?");
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            ButtonType buttonTypeCancel=new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeYes,buttonTypeNo,buttonTypeCancel);
            Optional<ButtonType> result=alert.showAndWait();
            if(result.get()==buttonTypeYes){
                Serializer.serialize();
                pop();
            }
            else if(result.get()==buttonTypeNo){
                cPane.getChildren().clear();
                Revocation.popOut();
                Revocation.push();
            }
            else{}
        }
        //为空时不提示保存
        else{
            cPane.getChildren().clear();
            Revocation.popOut();
            Revocation.push();
        }
    }

    public static void push(){
        saveStack.push(1);
    }

    public static void pop(){
        while(!saveStack.empty())
            saveStack.pop();
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
