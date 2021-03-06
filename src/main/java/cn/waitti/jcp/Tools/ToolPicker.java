package cn.waitti.jcp.Tools;

import java.util.HashMap;

public class ToolPicker {
    private static final HashMap<Class<? extends EnabledTool>, EnabledTool> store = new HashMap<>();
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

    public static EnabledTool getCurrentTool(){
        return currentTool;
    }

    public static void activate(Class<? extends EnabledTool> clazz) throws Exception {
        activateWithArgs(clazz, new Class[]{});
    }

    public static EnabledTool getTool(Class<? extends EnabledTool> clazz){
        return store.get(clazz);
    }
}
