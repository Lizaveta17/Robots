package gui.adapters;

import logic.GameController;
import entity.RobotDirection;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyPressAdapter extends KeyAdapter {
    private final GameController gameController;
    public KeyPressAdapter(GameController gameController){
        this.gameController = gameController;
    }

    public void keyPressed(KeyEvent e) {
        updateMovementState(e, true);
    }
    public void keyReleased(KeyEvent e) {
        updateMovementState(e, false);
    }

    private void updateMovementState(KeyEvent e, boolean state){
        switch (e.getKeyCode()){
            case KeyEvent.VK_A -> gameController.updateDirection(RobotDirection.LEFT, state);
            case KeyEvent.VK_W -> gameController.updateDirection(RobotDirection.UP, state);
            case KeyEvent.VK_D -> gameController.updateDirection(RobotDirection.RIGHT, state);
            case KeyEvent.VK_S -> gameController.updateDirection(RobotDirection.DOWN, state);
        }
    }

}
