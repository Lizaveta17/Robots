package gui;

import gui.language.AppLanguage;
import gui.language.LanguageManager;
import gui.language.LocaleChangeable;
import gui.system_windows.InternalWindow;
import gui.system_windows.RecoveryConfirmDialog;
import gui.system_windows.closing.Closeable;
import gui.system_windows.closing.ClosingConfirmDialog;
import gui.system_windows.serialization.StateRecoverable;
import log.Logger;
import serializer.IntrenalFrameSerializer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public class MainApplicationFrame extends JFrame implements Closeable, LocaleChangeable, StateRecoverable, Serializable {
    LanguageManager languageManager = new LanguageManager(Locale.getDefault().getLanguage());
    ClosingConfirmDialog closeConfirmWindow = new ClosingConfirmDialog(languageManager);
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        super();
        setDefaultModeAndBounds();
        setContentPane(desktopPane);

        addLogWindow();
        addGameWindow();

        generateAndSetMenuBar();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        WindowAdapter windowAdapter = new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                exit();
            }

            public void windowOpened(WindowEvent windowEvent) {
                recovery();
            }
        };
        addWindowListener(windowAdapter);
    }

    private void setDefaultModeAndBounds() {
        try {
            UIManager.setLookAndFeel(DisplayMode.NIMBUS.className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);
    }

    private void addLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), languageManager.getLocaleValue("LogWindow.title"));
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        Logger.debug(languageManager.getLocaleValue("tests.startLog"));
        addWindow(logWindow);
    }

    private void addGameWindow() {
        int startWidth = 400;
        int startHeight = 400;
        GameWindow gameWindow = new GameWindow(languageManager.getLocaleValue("GameWindow.title"), startWidth, startHeight);
        gameWindow.setSize(startWidth, startHeight);
        gameWindow.setLocation(350, 10);
        addWindow(gameWindow);
    }

    protected void addWindow(InternalWindow frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuItem createMenuItem(String description_key, int eventKey, ActionListener handler) {
        String description = languageManager.getLocaleValue(description_key);
        JMenuItem menuItem = new JMenuItem(description, eventKey);
        menuItem.addActionListener(handler);
        return menuItem;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu menu = new JMenu(languageManager.getLocaleValue("displayMode"));
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                languageManager.getLocaleValue("displayMode.description")
        );

        for (DisplayMode mode : DisplayMode.values()) {
            String key = String.format("displayMode.%s", mode.toString());
            ActionListener handler = (event) -> {
                setLookAndFeel(mode.className);
                this.invalidate();
            };
            menu.add(createMenuItem(key, KeyEvent.VK_S, handler));
        }
        return menu;
    }

    private JMenu createTestMenu() {
        JMenu menu = new JMenu(languageManager.getLocaleValue("tests"));
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription(languageManager.getLocaleValue("tests.description"));

        ActionListener handler = (event) -> {
            Logger.debug(languageManager.getLocaleValue("tests.newLog"));
        };
        menu.add(createMenuItem("tests.logCommand", KeyEvent.VK_S, handler));
        return menu;
    }

    private JMenu createLanguageMenu() {
        JMenu menu = new JMenu(languageManager.getLocaleValue("language"));
        menu.setMnemonic(KeyEvent.VK_L);

        for (AppLanguage lang : AppLanguage.values()) {
            String key = String.format("language.%s", lang.locale);
            ActionListener handler = (event) -> {
                updateLocale(lang);
                this.revalidate();
            };
            menu.add(createMenuItem(key, KeyEvent.VK_K, handler));
        }
        return menu;
    }

    private JMenu createExitMenu() {
        JMenu menu = new JMenu(languageManager.getLocaleValue("close"));
        JButton button = new JButton(languageManager.getLocaleValue("close.button"));
        button.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        button.setContentAreaFilled(false);

        button.addActionListener(event -> exit());
        menu.add(button);
        return menu;
    }

    private void generateAndSetMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createLanguageMenu());
        menuBar.add(createExitMenu());
        setJMenuBar(menuBar);
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    @Override
    public void updateLocale(AppLanguage language) {
        languageManager.changeLanguage(language);
        generateAndSetMenuBar();
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            frame.setTitle(languageManager.getLocaleValue(String.format("%s.title", frame.getName())));
            InternalWindow window = (InternalWindow)frame;
            window.updateLocale(language);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void serialize() {
        Preferences preferences = Preferences.userNodeForPackage(MainApplicationFrame.class);
        preferences.put("language", languageManager.getCurrentLanguage().toString());
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            IntrenalFrameSerializer.serialize(frame);
        }
    }

    @Override
    public void exit() {
        if (closeConfirmWindow.confirmDialogAnswerIsPositive()) {
            serialize();
            dispose();
        }
    }

    @Override
    public void recovery() {
        Preferences prefs = Preferences.userNodeForPackage(MainApplicationFrame.class);
        try {
            prefs.sync();
            AppLanguage lang;
            try {
                lang = AppLanguage.valueOf(
                        prefs.get("language", Locale.getDefault().getLanguage()).toUpperCase()
                );
            } catch (IllegalArgumentException e) {
                Logger.warning(languageManager.getLocaleValue("logMessage.backingStoreError"));
                lang = AppLanguage.valueOf(Locale.getDefault().getLanguage());
            }

            LanguageManager langManager = new LanguageManager(lang.locale);
            RecoveryConfirmDialog recoveryConfirmWindow = new RecoveryConfirmDialog(langManager);
            updateLocale(lang);
            if (recoveryConfirmWindow.confirmDialogAnswerIsPositive()) {
                for (JInternalFrame frame : desktopPane.getAllFrames()) {
                    IntrenalFrameSerializer.deserialize(frame);
                }
            }
        } catch (BackingStoreException e) {
            Logger.debug(languageManager.getLocaleValue("logMessage.backingStoreError"));
        }
    }
}
