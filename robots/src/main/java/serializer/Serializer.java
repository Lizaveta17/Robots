package serializer;

import gui.system_windows.InternalWindow;
import java.util.prefs.Preferences;

public class Serializer {
    public static void serialize(InternalWindow window) {
        String name = window.getName();
        Preferences prefs = Preferences.userNodeForPackage(window.getClass());
        prefs.putInt(name + ".width", window.getWidth());
        prefs.putInt(name + ".height", window.getHeight());
        prefs.putInt(name + ".x", window.getX());
        prefs.putInt(name + ".y", window.getY());
    }

    public static void deserialize(InternalWindow window) {
        String name = window.getName();
        Preferences prefs = Preferences.userNodeForPackage(window.getClass());
        window.setSize(prefs.getInt(name + ".width", 0), prefs.getInt(name + ".height", 0));
        window.setLocation(prefs.getInt(name + ".x", 0), prefs.getInt(name + ".y", 0));
    }
}
