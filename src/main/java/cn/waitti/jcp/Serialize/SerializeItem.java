package cn.waitti.jcp.Serialize;

import java.io.Serializable;

public class SerializeItem {
    public Class<?> clazz;
    public String serializeString;
    SerializeItem(Class<?> clazz, String serializeString){
        this.clazz = clazz;
        this.serializeString = serializeString;
    }
}
