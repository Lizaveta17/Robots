package gui.closing;

import gui.language.AppLanguage;
import gui.language.LanguageManager;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.util.Locale;

public class JInternalFrameWithClosingConfirmation extends JInternalFrame {
    protected final LanguageManager languageManager = new LanguageManager(Locale.getDefault().getLanguage());
    public JInternalFrameWithClosingConfirmation(String title){
        super(title, true, true, true, true);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        InternalFrameAdapter frameAdapter = new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {exit();}
        };
        addInternalFrameListener(frameAdapter);
    }

    protected void exit() {
        if (JOptionPane.showConfirmDialog(
                this,
                languageManager.getLocaleValue("close.confirmMessage"),
                languageManager.getLocaleValue("close.confirmTitle"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    public void updateLocale(AppLanguage language) {
        languageManager.changeLanguage(language);
    }
}
