package serializer;

import gui.system_windows.InternalWindow;
import log.Logger;

import javax.swing.*;
import java.util.prefs.Preferences;

public class Serializer {
    public static void serialize(Preferences preferences, JInternalFrame frame) {
        Preferences frameNode = preferences.node(frame.getClass().getName());
        frameNode.put("title", frame.getTitle());
        frameNode.putInt("width", frame.getWidth());
        frameNode.putInt("height", frame.getHeight());
        frameNode.putInt("x", frame.getX());
        frameNode.putInt("y", frame.getY());
        frameNode.putBoolean("icon", frame.isIcon());
    }

    public static InternalFrameModel deserialize(Preferences preferences, String frameName){
        Preferences frameNode = preferences.node(frameName);
        return new InternalFrameModel(
            frameNode.get("title", ""),
            frameNode.getInt("width", 0),
            frameNode.getInt("width", 0),
            frameNode.getInt("x", 0),
            frameNode.getInt("y", 0),
            frameNode.getBoolean("icon", false)
        );
    }
}
