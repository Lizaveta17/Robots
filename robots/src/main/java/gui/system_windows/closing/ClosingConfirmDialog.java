package gui.system_windows.closing;

import gui.language.LanguageManager;
import gui.system_windows.confirmation.BaseConfirmDialog;
import gui.system_windows.confirmation.ConfirmDialog;

public class ClosingConfirmDialog extends BaseConfirmDialog {
    public ClosingConfirmDialog(LanguageManager languageManager) {
        super(languageManager);
    }

    @Override
    public String getMessage() {
        return languageManager.getLocaleValue("close.confirmMessage");
    }
}
