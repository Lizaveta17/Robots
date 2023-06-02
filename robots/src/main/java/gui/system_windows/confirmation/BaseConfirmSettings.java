package gui.system_windows.confirmation;

import gui.language.LanguageManager;

public class BaseConfirmSettings {
    public static Object[] getOptions(LanguageManager languageManager) {
        return new Object[]{
                languageManager.getLocaleValue("confirmWindowOptions.yes"),
                languageManager.getLocaleValue("confirmWindowOptions.no")
        };
    }

    public static String getTitle(LanguageManager languageManager) {
        return languageManager.getLocaleValue("confirmTitle");
    }
}
