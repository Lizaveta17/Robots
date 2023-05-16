package gui.closing;

import gui.language.AppLanguage;
import gui.language.LanguageManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

public class JFrameWithClosingConfirmation extends JFrame {
    protected final LanguageManager languageManager = new LanguageManager(Locale.getDefault().getLanguage());
    public JFrameWithClosingConfirmation(){
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        WindowAdapter windowAdapter = new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {exit();}
        };
        addWindowListener(windowAdapter);
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

    protected void updateLocale(AppLanguage language) {
        languageManager.changeLanguage(language);
    }
}
