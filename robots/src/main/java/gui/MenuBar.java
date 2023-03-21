package main.java.gui;

import main.java.log.Logger;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MenuBar extends JMenuBar {
    public MenuBar() {
        this.add(createLookAndFeelMenu());
        this.add(createTestMenu());
    }

    private JMenuItem createMenuItem(String name) {
        JMenuItem menuItem = new JMenuItem(name, KeyEvent.VK_S);
        menuItem.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        return menuItem;
    }

    private JMenu createLookAndFeelMenu(){
        JMenu menu = new JMenu("Режим отображения");
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");

        JMenuItem systemLookAndFeel = createMenuItem("Системная схема");
        menu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = createMenuItem("Универсальная схема");
        menu.add(crossplatformLookAndFeel);

        return menu;
    }
    private JMenu createTestMenu(){
        JMenu menu = new JMenu("Тесты");
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        {
            JMenuItem addLogMessageItem = createMenuItem("Сообщение в лог");
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            menu.add(addLogMessageItem);
        }

        return menu;
    }

    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
