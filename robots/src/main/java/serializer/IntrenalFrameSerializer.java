package serializer;

import log.Logger;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.util.prefs.Preferences;

public class IntrenalFrameSerializer {
    public static void serialize(JInternalFrame window) {
        String name = window.getName();
        Preferences prefs = Preferences.userNodeForPackage(window.getClass());
        prefs.putInt(name + ".width", window.getWidth());
        prefs.putInt(name + ".height", window.getHeight());
        prefs.putInt(name + ".x", window.getX());
        prefs.putInt(name + ".y", window.getY());
        prefs.putBoolean(name + ".isIcon", window.isIcon());
        prefs.putBoolean(name + ".isMaximum", window.isMaximum());
    }

    public static void deserialize(JInternalFrame window) {
        String name = window.getName();
        Preferences prefs = Preferences.userNodeForPackage(window.getClass());
        window.setSize(prefs.getInt(name + ".width", 0), prefs.getInt(name + ".height", 0));
        window.setLocation(prefs.getInt(name + ".x", 0), prefs.getInt(name + ".y", 0));

        try{
            if (prefs.getBoolean(name + ".isIcon", false)){
                window.setIcon(true);
            }
            if (prefs.getBoolean(name + ".isMaximum", false)){
                window.setMaximum(true);
            }
        } catch (PropertyVetoException e){
            e.printStackTrace();
        }
    }
}
