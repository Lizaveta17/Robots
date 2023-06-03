package gui.system_windows.confirmation;

import gui.language.LanguageManager;

public class BaseConfirmDialog implements ConfirmDialog{
    protected LanguageManager languageManager;

    public BaseConfirmDialog(LanguageManager languageManager){
        this.languageManager = languageManager;
    }

    @Override
    public Object[] getOptions() {
        return new Object[]{
                languageManager.getLocaleValue("confirmWindowOptions.yes"),
                languageManager.getLocaleValue("confirmWindowOptions.no")
        };
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public String getTitle() {
        return languageManager.getLocaleValue("confirmTitle");
    }
}
