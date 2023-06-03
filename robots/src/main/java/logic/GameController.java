package logic;

import entity.ComputerRobot;
import entity.RobotDirection;
import entity.Target;
import entity.UserRobot;
import logic.entity.GameState;
import logic.entity.Winner;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

public class GameController {
    private int fieldWidth;
    private int fieldHeight;
    private final ComputerRobot computerRobot;
    private final UserRobot userRobot;
    private final HashMap<RobotDirection, Boolean> userMovementState = new HashMap<>();
    private final Target target;
    private boolean isGameOn = false;
    private volatile int score = 0;
    private static final int VICTORY_SCORE = 2;

    private final PropertyChangeSupport scoreChangeDispatcher = new PropertyChangeSupport(this);

    public GameController(int width, int height) {
        fieldWidth = width;
        fieldHeight = height;
        computerRobot = new ComputerRobot(
                -100, -100, 0, 0.1, Color.MAGENTA, 30, 20, 5
        );
        userRobot = new UserRobot(-100, -100, 1, 3, Color.BLUE, 30, 30);
        target = new Target(-100, -100, Color.RED);
        //        target = new Target(MathLogic.round(width / 2.0), MathLogic.round(height / 2.0), Color.RED);
    }

    private void initUserMovementState(){
        for (RobotDirection direction : RobotDirection.values()) {
            userMovementState.put(direction, false);
        }
    }

    public UserRobot getUserRobot() {
        return userRobot;
    }

    public ComputerRobot getComputerRobot() {
        return computerRobot;
    }

    public Target getTarget() {
        return target;
    }

    public void updateDirection(RobotDirection direction, boolean state) {
        userMovementState.put(direction, state);
    }

    public void moveUserRobot() {
        if (userMovementState.get(RobotDirection.UP)) {
            userRobot.goUp();
        }
        if (userMovementState.get(RobotDirection.DOWN)) {
            userRobot.goDown();
        }
        if (userMovementState.get(RobotDirection.LEFT)) {
            userRobot.goLeft();
        }
        if (userMovementState.get(RobotDirection.RIGHT)) {
            userRobot.goRight();
        }
        correctUserRobotCoordinates();
    }

    private void correctUserRobotCoordinates() {
        if (fieldWidth != 0) {
            double newX = MathLogic.applyLimits(userRobot.x, userRobot.widthRadious, fieldWidth - userRobot.widthRadious);
            userRobot.x = newX;
        }

        if (fieldHeight != 0) {
            double newY = MathLogic.applyLimits(userRobot.y, userRobot.heightRadious, fieldHeight - userRobot.heightRadious);
            userRobot.y = newY;
        }
    }

    private boolean isUserRobotReachedTarget() {
        double distance = Math.sqrt(Math.pow(userRobot.x - target.x, 2) + Math.pow(userRobot.y - target.y, 2));
        return distance < userRobot.widthRadious || distance < userRobot.heightRadious;
    }

    private void setStartPositions(){
        target.x = 300;
        target.y = 300;

        userRobot.x = 200;
        userRobot.y = 200;

        computerRobot.x = 100;
        computerRobot.y = 100;
    }

    private void generateNewTarget() {
        int newX = MathLogic.generateRandomLimitInt(fieldWidth);
        int newY = MathLogic.generateRandomLimitInt(fieldHeight);
        target.x = newX;
        target.y = newY;
    }

    private void updateScore(int diff){
        int newScore = score + diff;
        if (newScore < 0){
            stopGame(Winner.COMPUTER);
        }
        score = newScore;
        scoreChangeDispatcher.firePropertyChange(GameState.SCORE_CHANGED.state, null, newScore);
    }

    public void startGame(){
        isGameOn = true;
        score = 0;
        initUserMovementState();
        setStartPositions();
    }
    
    private void stopGame(Winner winner){
        isGameOn = false;
        scoreChangeDispatcher.firePropertyChange(GameState.GAME_END.state, null, winner);
    }

    public void onModelUpdateEvent() {
        if (!isGameOn){
            return;
        }

        moveUserRobot();

//        if (!isGameOn){
//            return;
//        }
        if (isUserRobotReachedTarget()) {
            updateScore(1);
             if (score == VICTORY_SCORE){
                 stopGame(Winner.USER);
             } else {
                 generateNewTarget();
             }
        }
    }

    public void addStateGameListener(PropertyChangeListener listener){
        scoreChangeDispatcher.addPropertyChangeListener(GameState.SCORE_CHANGED.state, listener);
        scoreChangeDispatcher.addPropertyChangeListener(GameState.GAME_END.state, listener);
    }
}
