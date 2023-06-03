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
import serializer.InternalFrameModel;
import serializer.Serializer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;



public class MainApplicationFrame extends JFrame implements Closeable, LocaleChangeable, StateRecoverable, Serializable {
    class InternalWindowFactory{
        private void setGeometry(InternalFrameModel model, InternalWindow frame){
            frame.setSize(model.width, model.height);
            frame.setLocation(model.x, model.y);
        }
        private LogWindow createLogWindow(InternalFrameModel model) {
            LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
            logWindow.setTitle(languageManager.getLocaleValue("logWindow.title"));
            logWindow.pack();
            Logger.debug(languageManager.getLocaleValue("tests.startLog"));
            return logWindow;
        }

        private GameWindow createGameWindow(InternalFrameModel model) {
            GameWindow gameWindow = new GameWindow();
            gameWindow.setTitle(languageManager.getLocaleValue("gameWindow.title"));
            return gameWindow;
        }

        public InternalWindow createInternalFrame(Class<?> cls, InternalFrameModel model){
            InternalWindow frame;
            if (cls == GameWindow.class){
                frame = createGameWindow(model);
            }
            else if (cls == LogWindow.class){
                frame = createLogWindow(model);
            } else {
                frame = new InternalWindow();
                frame.setTitle(model.title);
            }
            setGeometry(model, frame);
            desktopPane.add(frame);
            try{
                frame.setIcon(model.icon);
            } catch (PropertyVetoException e) {
                // ignore
            }
            frame.setVisible(true);

            return frame;
        }
    }
    static LanguageManager languageManager;
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final InternalWindowFactory windowFactory = new InternalWindowFactory();

    public MainApplicationFrame() {
        super();
        setDefaultModeAndBounds();
        setContentPane(desktopPane);

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
            InternalWindow window = (InternalWindow) frame;
            window.setTitle(languageManager.getLocaleValue(String.format("%s.title", frame.getName())));
            window.updateLocale(language);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void serialize() {
        Preferences preferences = Preferences.userNodeForPackage(MainApplicationFrame.class);
        preferences.put("language", languageManager.getCurrentLanguage().toString());
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            Serializer.serialize(preferences, frame);
        }
    }

    @Override
    public void exit() {
        if (new ClosingConfirmDialog(languageManager).needClose()) {
            serialize();
            dispose();
        }
    }

    private AppLanguage getSavedLanguage(Preferences prefs){
        try {
            return AppLanguage.valueOf(
                    prefs.get("language", Locale.getDefault().getLanguage()).toUpperCase()
            );
        } catch (IllegalArgumentException e) {
            Logger.warning(languageManager.getLocaleValue("logMessage.backingStoreError"));
            return AppLanguage.valueOf(Locale.getDefault().getLanguage());
        }
    }

    private Class<?> getFrameClassFromName(String frameName){
        try {
            return Class.forName(frameName);
        } catch (ClassNotFoundException e) {
            Logger.error(e.toString());
        }
        return null;
    }

    private void deserialize(Preferences prefs) throws BackingStoreException {
        generateAndSetMenuBar();
        for (String frameName : prefs.childrenNames()) {
            Class<?> frameClass = getFrameClassFromName(frameName);
            InternalFrameModel model = Serializer.deserialize(prefs, frameName);
            windowFactory.createInternalFrame(frameClass, model);
            SwingUtilities.updateComponentTreeUI(this);
        }
    }

    public void defaultStart(){
        languageManager = new LanguageManager(Locale.getDefault().getLanguage());
        generateAndSetMenuBar();
        windowFactory.createInternalFrame(GameWindow.class, new InternalFrameModel("", 400, 400, 350, 10, false));
        windowFactory.createInternalFrame(LogWindow.class, new InternalFrameModel("", 300, 600, 10, 10, false));
    }

    @Override
    public void recovery() {
        Preferences prefs = Preferences.userNodeForPackage(MainApplicationFrame.class);
        try {
            prefs.sync();
            AppLanguage lang = getSavedLanguage(prefs);
            languageManager = new LanguageManager(lang.locale);

            if (new RecoveryConfirmDialog(languageManager).needRecovery()) {
                deserialize(prefs);
            } else {
                defaultStart();
            }
        } catch (BackingStoreException e) {
            Logger.debug(languageManager.getLocaleValue("logMessage.backingStoreError"));
            defaultStart();
        }
    }
}
