package gui;

import gui.system_windows.InternalWindow;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends InternalWindow {
    private final GameVisualizer m_visualizer;

    public GameWindow(String title) {
        super(title);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
