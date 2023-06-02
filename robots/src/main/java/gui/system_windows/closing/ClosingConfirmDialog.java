package gui.system_windows.closing;

import gui.language.LanguageManager;
import gui.system_windows.confirmation.BaseConfirmSettings;
import gui.system_windows.confirmation.ConfirmDialog;

public class ClosingConfirmDialog implements ConfirmDialog {
    LanguageManager languageManager;

    public ClosingConfirmDialog(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    public boolean needClose() {
        return confirmDialogAnswerIsPositive();
    }

    @Override
    public Object[] getOptions() {
        return BaseConfirmSettings.getOptions(languageManager);
    }

    @Override
    public String getMessage() {
        return languageManager.getLocaleValue("close.confirmMessage");
    }

    @Override
    public String getTitle() {
        return BaseConfirmSettings.getTitle(languageManager);
    }
}
