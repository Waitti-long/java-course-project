package cn.waitti.jcp.Tools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class RecTool implements EnabledTool{
    public Canvas canvas;
    public GraphicsContext gc=null;
    public double x1,x2,y1,y2,width,height;
    Path path;
    RecTool(Canvas canvas){this.canvas=canvas;}
    @Override
    public void activate() {
        gc=canvas.getGraphicsContext2D();
        canvas.setOnMousePressed(this::startDrawRec);
        canvas.setOnMouseReleased(this::endDrawRec);
    }

    public void startDrawRec(MouseEvent mouseEvent){
        x1= mouseEvent.getX();
        y1= mouseEvent.getY();
        path=new Path();
        path.getElements().add(new MoveTo(x1, y1));
    }

    public void pathRec(MouseEvent mouseEvent){
        path.getElements().add(new LineTo(mouseEvent.getX(), mouseEvent.getY()));
    }

    public void endDrawRec(MouseEvent mouseEvent){
        x2= mouseEvent.getX();
        y2= mouseEvent.getY();
        width=x2-x1;
        height=y2-y1;
        DrawRec(x1,y1,width,height);
        path=null;
    }

    public void DrawRec(double x1, double y1, double width, double height){
        if(width<0){
            width=-width;
            x1=x1-width;
        }
        if (height < 0) {
            height = -height;
            y1 = y1 - height;
        }
        gc.strokeRect(x1,y1,width,height);
    }
    @Override
    public void deactivate() {
        gc=null;
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
    }
}
