package gui;

import gui.system_windows.InternalWindow;
import gui.system_windows.GameRestartDialog;
import logic.GameController;
import logic.entity.GameState;
import logic.entity.Winner;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class GameWindow extends InternalWindow {
    private final GameVisualizer gameVisualizer;
    private final GameController gameController;
    private final JLabel scoreLabel;
    private JButton startButton;

    public GameWindow(String title, int startWidth, int startHeight) {
        super(title);
        gameController = new GameController(startWidth, startHeight);
        gameController.addGameStateListener(this);
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
        startButton = new JButton(languageManager.getLocaleValue("start"));
        startButton.addActionListener(event -> {
            gameController.startGame();
            startButton.setEnabled(false);
            }
        );

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(startButton, BorderLayout.CENTER);
        return panel;
    }

    private void handleGameEnd(Object winner){
        String messageKey = "";
        if (winner.equals(Winner.USER)) {
            messageKey = "winMessage";
        } else if (winner.equals(Winner.COMPUTER)) {
            messageKey = "lostMessage";
        }
        if (new GameRestartDialog(languageManager, messageKey).confirmDialogAnswerIsPositive()){
            startButton.setEnabled(true);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GameState.SCORE_CHANGED.state)) {
            scoreLabel.setText(languageManager.getLocaleValue("score") + evt.getNewValue().toString());
        }
        if (evt.getPropertyName().equals(GameState.GAME_END.state)) {
            handleGameEnd(evt.getNewValue());
        }
    }
}
