package gui.system_windows;

import gui.language.AppLanguage;
import gui.language.LanguageManager;
import gui.language.LocaleChangeable;
import gui.system_windows.closing.Closeable;
import gui.system_windows.closing.ClosingConfirmDialog;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

public class InternalWindow extends JInternalFrame implements Closeable, LocaleChangeable, PropertyChangeListener {
    protected LanguageManager languageManager;
    ClosingConfirmDialog closeDialog;

    public InternalWindow(String title){
        super(title, true, true, true, true);
        languageManager = new LanguageManager(Locale.getDefault().getLanguage());
        closeDialog = new ClosingConfirmDialog(languageManager);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        InternalFrameAdapter frameAdapter = new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {exit();}
        };
        addInternalFrameListener(frameAdapter);
    }

    public void exit(){
        if (closeDialog.confirmDialogAnswerIsPositive()){
            dispose();
        }
    }

    @Override
    public void updateLocale(AppLanguage language) {
        languageManager.changeLanguage(language);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
}
