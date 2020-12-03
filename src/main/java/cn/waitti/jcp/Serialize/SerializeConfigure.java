package cn.waitti.jcp.Serialize;

import javafx.scene.Node;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeConfigure {
    Class<? extends Node> node();
    Class<?>[] serializeClasses();
    String[] serializeStrings();
}
