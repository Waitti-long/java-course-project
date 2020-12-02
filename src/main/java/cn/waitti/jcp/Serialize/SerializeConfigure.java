package cn.waitti.jcp.Serialize;

import javafx.scene.Node;

public @interface SerializeConfigure {
    Class<? extends Node> node();
    Class<?>[] serializeClasses();
    String[] serializeStrings();
}
