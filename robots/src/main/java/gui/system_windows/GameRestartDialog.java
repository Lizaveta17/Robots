package gui.system_windows;

import gui.language.LanguageManager;
import gui.system_windows.confirmation.BaseConfirmDialog;

public class GameRestartDialog extends BaseConfirmDialog {
    String gameResultMessageKey;
    public GameRestartDialog(LanguageManager languageManager, String gameResultMessageKey) {
        super(languageManager);
        this.gameResultMessageKey = gameResultMessageKey;
    }
    @Override
    public String getMessage() {
        String resultMessage = languageManager.getLocaleValue(gameResultMessageKey);
        String confirmMessage = languageManager.getLocaleValue("restart.confirmMessage");
        return resultMessage + "\n" + confirmMessage;
    }
}
