package gui.system_windows;

import gui.language.LanguageManager;
import gui.system_windows.confirmation.BaseConfirmDialog;
import gui.system_windows.confirmation.ConfirmDialog;

public class RecoveryConfirmDialog extends BaseConfirmDialog {
    public RecoveryConfirmDialog(LanguageManager languageManager) {
        super(languageManager);
    }
    @Override
    public String getMessage() {
        return languageManager.getLocaleValue("recovery.confirmMessage");
    }
}
