package logic;

import entity.*;
import logic.entity.GameState;
import logic.entity.Winner;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    private int fieldWidth;
    private int fieldHeight;
    private final ComputerRobot computerRobot;

    private final Target computerRobotTarget;
    private final UserRobot userRobot;
    private final HashMap<RobotDirection, Boolean> userMovementState = new HashMap<>();
    private final ColorTarget food;
    private final ColorTarget accelerator;
    private boolean isGameOn = false;
    private boolean isComputerRobotOn = true;
    private volatile int score;
    private static final int VICTORY_SCORE = 5;

    private final Timer timer = new Timer("events generator", true);
    private final PropertyChangeSupport gameStateChangeDispatcher = new PropertyChangeSupport(this);

    public GameController(int width, int height) {
        fieldWidth = width;
        fieldHeight = height;
        computerRobot = new ComputerRobot(
                -100, -100, 0, 1.2, Color.MAGENTA, 30, 20, 5
        );
        userRobot = new UserRobot(-100, -100, 1, 3, Color.BLUE, 30, 30, 0.5);
        food = new ColorTarget(-100, -100, 12, Color.RED);
        accelerator = new ColorTarget(-100, -100, 12, Color.YELLOW);
        computerRobotTarget = new Target(-100, -100, 0);
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

    public ColorTarget getFood() {
        return food;
    }

    public ColorTarget getAccelerator(){
        return accelerator;
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

    private void updateComputerRobotTarget(int x, int y, int diam){
        computerRobotTarget.x = x;
        computerRobotTarget.y = y;
        computerRobotTarget.diam = diam;
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

    private boolean isUserRobotReachedTarget(Target target) {
        double distance = Math.sqrt(Math.pow(userRobot.x - target.x, 2) + Math.pow(userRobot.y - target.y, 2));
        return distance <= userRobot.widthRadious + target.radius || distance <= userRobot.heightRadious + target.radius;
    }

    private boolean isComputerRobotReachedUserRobot() {
        Rectangle computerRobotTargetModel = new Rectangle(computerRobotTarget.x, computerRobotTarget.y,
                computerRobotTarget.diam, computerRobotTarget.diam);
        Rectangle computerRobotModel = new Rectangle(computerRobot.getRoundedX(), computerRobot.getRoundedY(),
                computerRobot.widthDiam, computerRobot.heightDiam);
        return computerRobotModel.intersects(computerRobotTargetModel);
    }

    private void setStartPositions(){
        food.x = 300;
        food.y = 300;

        userRobot.x = 200;
        userRobot.y = 200;

        computerRobot.x = 100;
        computerRobot.y = 100;
    }

    private void deleteAccelerator(){
        accelerator.x = -100;
        accelerator.y = -100;
    }

    private void generateNewFood() {
        int newX = MathLogic.generateRandomLimitInt(fieldWidth);
        int newY = MathLogic.generateRandomLimitInt(fieldHeight);
        food.x = newX;
        food.y = newY;
    }

    private void generateNewAccelerator(){
        int newX = MathLogic.generateRandomLimitInt(fieldWidth);
        int newY = MathLogic.generateRandomLimitInt(fieldHeight);
        accelerator.x = newX;
        accelerator.y = newY;
    }

    private void updateScore(int diff){
        int newScore = score + diff;
        score = Math.max(newScore, 0);
        gameStateChangeDispatcher.firePropertyChange(GameState.SCORE_CHANGED.state, null, newScore);
    }

    public void startGame(){
        score = 0;
        gameStateChangeDispatcher.firePropertyChange(GameState.SCORE_CHANGED.state, null, score);
        deleteAccelerator();
        initUserMovementState();
        setStartPositions();

        isGameOn = true;
    }
    
    private void stopGame(Winner winner){
        isGameOn = false;
        gameStateChangeDispatcher.firePropertyChange(GameState.GAME_END.state, null, winner);
    }

    private void moveRobots(){
        moveUserRobot();
        if (isComputerRobotOn){
            setUserRobotToComputerRobotTarget();
        }
        computerRobot.turnToTarget(computerRobotTarget);
        computerRobot.move();
    }

    private void setUserRobotToComputerRobotTarget(){
        updateComputerRobotTarget(userRobot.getRoundedX(), userRobot.getRoundedY(), userRobot.widthDiam);
    }

    private void changeComputerRobotTarget(){
        int newTargetX;
        int newTargetY;
        if (userRobot.x > fieldWidth / 2.0){
            newTargetX = 0;
        }
        else {
            newTargetX = fieldWidth;
        }

        if (userRobot.y > fieldHeight / 2.0){
            newTargetY = 0;
        }
        else {
            newTargetY = fieldHeight;
        }
        updateComputerRobotTarget(newTargetX, newTargetY, 0);
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                isComputerRobotOn = true;
            }
        }, 2000);

    }

    public void onModelUpdateEvent() {
        if (!isGameOn){
            return;
        }
        moveRobots();

        if (isComputerRobotOn && isComputerRobotReachedUserRobot()){
            isComputerRobotOn = false;
            updateScore(-1);
            changeComputerRobotTarget();
            if (score == 0){
                stopGame(Winner.COMPUTER);
                return;
            } else {
                changeComputerRobotTarget();
            }
        }

        if (isUserRobotReachedTarget(food)) {
            updateScore(1);
             if (score == VICTORY_SCORE){
                 stopGame(Winner.USER);
                 return;
             } else {
                 userRobot.decreaseVelocity();
                 generateNewFood();
                 generateNewAccelerator();
             }
        }

        if (isUserRobotReachedTarget(accelerator)) {
            userRobot.increaseVelocity();
            deleteAccelerator();
        }
    }

    public void addGameStateListener(PropertyChangeListener listener){
        gameStateChangeDispatcher.addPropertyChangeListener(GameState.SCORE_CHANGED.state, listener);
        gameStateChangeDispatcher.addPropertyChangeListener(GameState.GAME_END.state, listener);
    }
}
