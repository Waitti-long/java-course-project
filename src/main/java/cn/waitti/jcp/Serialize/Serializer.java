package cn.waitti.jcp.Serialize;

import com.google.gson.Gson;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import org.hildan.fxgson.FxGson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Serializer {
    public static SerializeItem serializeItem(Node node){
        if(node instanceof Circle){
            return new SerializeItem(Circle.class, FxGson.createWithExtras().toJson(node));
        }
        return null;
    }

    public static void serialize(Pane pane){
        ArrayList<SerializeItem> arrayList = new ArrayList<>();
        for (Node child : pane.getChildren()) {
            var res = serializeItem(child);
            if(res != null)arrayList.add(res);
        }
        new Gson().toJson(arrayList);
    }
}
