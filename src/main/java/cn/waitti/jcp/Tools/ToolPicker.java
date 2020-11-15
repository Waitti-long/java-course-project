package cn.waitti.jcp.Tools;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class ToolPicker {
    private static HashMap<Class<? extends EnabledTool>, EnabledTool> store = new HashMap<>();
    private static EnabledTool currentTool = null;

    public static void activateWithArgs(Class<? extends EnabledTool> clazz, Class[] classes, Object... args) throws Exception {
        if (!store.containsKey(clazz)) {
            store.put(clazz, clazz.getDeclaredConstructor(classes).newInstance(args));
        }
        if(currentTool != null){
            currentTool.deactivate();
        }
        currentTool = store.get(clazz);
        currentTool.activate();
    }

    public static void activate(Class<? extends EnabledTool> clazz) throws Exception {
        activateWithArgs(clazz, new Class[]{});
    }
}
