package gui;

import gui.closing.JFrameWithClosingConfirmation;
import gui.closing.JInternalFrameWithClosingConfirmation;
import gui.language.AppLanguage;
import log.Logger;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;


public class MainApplicationFrame extends JFrameWithClosingConfirmation {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final ArrayList<JInternalFrameWithClosingConfirmation> internalFrames = new ArrayList<>();

    public MainApplicationFrame() {
        setDefaultMode();

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);
        logWindow.setName("logWindow");
        internalFrames.add(logWindow);

        GameWindow gameWindow = new GameWindow(languageManager.getLocaleValue("gameWindow.title"));
        gameWindow.setSize(400, 400);
        gameWindow.setName("gameWindow");
        addWindow(gameWindow);
        internalFrames.add(gameWindow);

        generateAndSetMenuBar();
    }

    private void setDefaultMode() {
        try {
            UIManager.setLookAndFeel(DisplayMode.NIMBUS.className);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), languageManager.getLocaleValue("logWindow.title"));
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        Logger.debug(languageManager.getLocaleValue("tests.startLog"));
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
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
    protected void updateLocale(AppLanguage language) {
        super.updateLocale(language);
        generateAndSetMenuBar();
        for (JInternalFrameWithClosingConfirmation frame : internalFrames) {
            frame.setTitle(languageManager.getLocaleValue(String.format("%s.title", frame.getName())));
            frame.updateLocale(language);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
}
