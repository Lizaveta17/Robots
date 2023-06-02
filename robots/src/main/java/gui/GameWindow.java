package gui;

import gui.system_windows.InternalWindow;
import logic.GameController;
import logic.GameState;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class GameWindow extends InternalWindow {
    private final GameVisualizer gameVisualizer;
    private final GameController gameController;

    private final JLabel scoreLabel;

    public GameWindow(String title, int startWidth, int startHeight) {
        super(title);
        gameController = new GameController(startWidth, startHeight);
        gameController.addStateGameListener(this);
        gameVisualizer = new GameVisualizer(gameController);

        scoreLabel = getScoreLabel();
        getContentPane().add(getContentPanel());
        pack();
    }

    private JPanel getContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(getScorePanel(), BorderLayout.NORTH);
        contentPanel.add(gameVisualizer, BorderLayout.CENTER);
        contentPanel.add(getStartButtonPanel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel getScorePanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createRaisedBevelBorder());
        panel.add(scoreLabel, BorderLayout.CENTER);
        return panel;
    }

    private JLabel getScoreLabel(){
        JLabel label = new JLabel(languageManager.getLocaleValue("score") + "0", SwingConstants.CENTER);
        label.setFont(new Font("Verdana",2,15));
        return label;
    }

    private JPanel getStartButtonPanel(){
        JButton button = new JButton(languageManager.getLocaleValue("start"));
        button.addActionListener(event -> {
            gameController.startGame();
            button.setEnabled(false);
            }
        );

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(button, BorderLayout.CENTER);
        return panel;
    }

    private void changeScore(Object newValue){
        scoreLabel.setText(languageManager.getLocaleValue("score") + newValue.toString());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GameState.SCORE_CHANGED.state)) {
            changeScore(evt.getNewValue());
        }
        if (evt.getPropertyName().equals(GameState.GAME_OVER.state)) {
//            updateBundleResources((ResourceBundle) evt.getNewValue());
        }
        if (evt.getPropertyName().equals(GameState.GAME_WIN.state)) {
//            updateBundleResources((ResourceBundle) evt.getNewValue());
        }
    }
}
