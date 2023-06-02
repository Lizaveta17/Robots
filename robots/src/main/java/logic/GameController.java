package logic;

import entity.ComputerRobot;
import entity.RobotDirection;
import entity.Target;
import entity.UserRobot;

import java.awt.*;
import java.util.HashMap;

public class GameController {
    private int fieldWidth;
    private int fieldHeight;
    private final ComputerRobot computerRobot = new ComputerRobot(100, 100, 0, 0.1, Color.MAGENTA, 30, 20, 5);
    private final UserRobot userRobot = new UserRobot(200, 100, 1, 3, Color.BLUE, 30, 30);
    private final Target target;

    private final HashMap<RobotDirection, Boolean> userMovementState = new HashMap<>();

    public GameController(int width, int height){
        fieldWidth = width;
        fieldHeight = height;
        target = new Target(MathLogic.round(width / 2.0), MathLogic.round(height / 2.0), Color.RED);
        for (RobotDirection direction : RobotDirection.values()){
            userMovementState.put(direction, false);
        }
    }

    public UserRobot getUserRobot(){
        return userRobot;
    }

    public ComputerRobot getComputerRobot(){
        return computerRobot;
    }

    public Target getTarget(){
        return target;
    }

    public void updateDirection(RobotDirection direction, boolean state) {
        userMovementState.put(direction, state);
    }

    public void moveUserRobot(){
        if (userMovementState.get(RobotDirection.UP)){
            userRobot.goUp();
        }
        if (userMovementState.get(RobotDirection.DOWN)){
            userRobot.goDown();
        }
        if (userMovementState.get(RobotDirection.LEFT)){
            userRobot.goLeft();
        }
        if (userMovementState.get(RobotDirection.RIGHT)){
            userRobot.goRight();
        }
        correctUserRobotCoordinates();
    }

    private void correctUserRobotCoordinates(){
        if (fieldWidth != 0) {
            double newX = MathLogic.applyLimits(userRobot.x, 0, fieldWidth);
            userRobot.x = newX;
        }

        if (fieldHeight != 0) {
            double newY = MathLogic.applyLimits(userRobot.y, 0, fieldHeight);
            userRobot.y = newY;
        }
    }

    private boolean isUserRobotReachedTarget(){
        double distance = Math.sqrt(Math.pow(userRobot.x - target.x, 2) + Math.pow(userRobot.y - target.y, 2));
        return distance <= userRobot.widthDiam || distance <= userRobot.heightDiam;
    }
    private void generateNewTarget() {
        int newX = MathLogic.generateRandomLimitInt(fieldWidth);
        int newY = MathLogic.generateRandomLimitInt(fieldHeight);
        target.x = newX;
        target.y = newY;
    }

    public void onModelUpdateEvent(){
        moveUserRobot();
        if( isUserRobotReachedTarget()){
            generateNewTarget();
        }
    }
}
