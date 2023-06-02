package gui.system_windows;

import gui.language.LanguageManager;
import gui.system_windows.confirmation.BaseConfirmSettings;
import gui.system_windows.confirmation.ConfirmDialog;

public class RecoveryConfirmDialog implements ConfirmDialog {
    LanguageManager languageManager;

    public RecoveryConfirmDialog(LanguageManager languageManager){
        this.languageManager = languageManager;
    }
    @Override
    public Object[] getOptions() {
        return BaseConfirmSettings.getOptions(languageManager);
    }

    @Override
    public String getMessage() {
        return languageManager.getLocaleValue("recovery.confirmMessage");
    }

    @Override
    public String getTitle() {
        return BaseConfirmSettings.getTitle(languageManager);
    }

    public boolean needRecovery() {
        return confirmDialogAnswerIsPositive();
    }
}
