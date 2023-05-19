package serializer;

import gui.language.AppLanguage;
import gui.language.LanguageManager;
import gui.system_windows.InternalWindow;
import log.Logger;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.util.Locale;
import java.util.prefs.Preferences;

public class Serializer {
    public static void serialize(InternalWindow window) {
        String name = window.getName();
        Preferences prefs = Preferences.userNodeForPackage(window.getClass());
        prefs.putInt(name + ".width", window.getWidth());
        prefs.putInt(name + ".height", window.getHeight());
        prefs.putInt(name + ".x", window.getX());
        prefs.putInt(name + ".y", window.getY());
//        prefs.put(name + ".locale", window.getCurrentLanguage());
    }

    public static void deserialize(InternalWindow window) {
        String name = window.getName();
        Preferences prefs = Preferences.userNodeForPackage(window.getClass());
        window.setSize(prefs.getInt(name + ".width", 0), prefs.getInt(name + ".height", 0));
        window.setLocation(prefs.getInt(name + ".x", 0), prefs.getInt(name + ".y", 0));

        String lang = prefs.get(name + ".locale", Locale.getDefault().getLanguage()).toUpperCase();
//        try {
//            window.updateLocale(AppLanguage.valueOf(lang));
//        } catch (IllegalArgumentException e) {
//            Logger.warning(languageManager.getLocaleValue("logMessage.loadLocaleError") + " " + name);
//        }
    }
}
