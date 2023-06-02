package game;

import models.ComputerRobot;
import models.Robot;
import models.RobotDirection;
import models.UserRobot;

import java.awt.*;
import java.util.HashMap;

public class GameController {
    private final ComputerRobot computerRobot = new ComputerRobot(100, 100, 0, 0.1, Color.MAGENTA, 30, 20, 5);
    private final UserRobot userRobot = new UserRobot(200, 100, 1, 5, Color.BLUE, 30, 30);

    private final HashMap<RobotDirection, Boolean> userMovementState = new HashMap<>();

    public GameController(){
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
    }

    public void onModelUpdateEvent(){
        moveUserRobot();
    }
}
