package gui.system_windows.confirmation;

import javax.swing.*;

public interface ConfirmDialog {
    Object[] getOptions();

    String getMessage();
    String getTitle();

    default boolean confirmDialogAnswerIsPositive() {
        Object[] options = getOptions();
        return JOptionPane.showOptionDialog(
                null,
                getMessage(),
                getTitle(),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        ) == JOptionPane.YES_OPTION;
    }



}
