package gui;

import gui.closing.JInternalFrameWithClosingConfirmation;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JInternalFrameWithClosingConfirmation {
    private final GameVisualizer m_visualizer;

    public GameWindow(String title, int startWidth, int startHeight) {
        super(title);
        m_visualizer = new GameVisualizer(startWidth, startHeight);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
