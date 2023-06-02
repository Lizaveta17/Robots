package gui;

import logic.GameController;
import gui.adapters.KeyPressAdapter;
import entity.ComputerRobot;
import entity.Robot;
import entity.UserRobot;
import logic.MathLogic;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();

    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private final Point target = new Point(100, 100);

    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;
    private final GameController gameController = new GameController();

    public GameVisualizer()
    {
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 10);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                gameController.onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
        addKeyListener(new KeyPressAdapter(gameController));
        setFocusable(true);
        setDoubleBuffered(true);
    }

    protected void setTargetPosition(Point p)
    {
        target.setLocation(p);
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    protected void onModelUpdateEvent()
    {
//        SCREEN_WIDTH = getWidth();
//        SCREEN_HEIGHT = getHeight();
//        double distance = distance(target.x, target.y,
//                computerRobot.positionX, computerRobot.positionY);
//        if (distance < 1)
//        {
//            return;
//        }
//        double velocity = maxVelocity;
//        double angleToTarget = angleTo(computerRobot.positionX, computerRobot.positionY, target.x, target.y);
//        double angularVelocity = 0;
//        if (angleToTarget > computerRobot.direction) {
//            angularVelocity = maxAngularVelocity;
//        }
//        if (angleToTarget < computerRobot.direction) {
//            angularVelocity = -maxAngularVelocity;
//        }
//
//        moveRobot(velocity, angularVelocity, 10);
    }

    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        Robot robot = gameController.getComputerRobot();
        velocity = MathLogic.applyLimits(velocity, 0, maxVelocity);
        angularVelocity = MathLogic.applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = robot.positionX + velocity / angularVelocity *
                (Math.sin(robot.direction  + angularVelocity * duration) -
                        Math.sin(robot.direction));
        if (!Double.isFinite(newX))
        {
            newX = robot.positionX + velocity * duration * Math.cos(robot.direction);
        }
        double newY = robot.positionY - velocity / angularVelocity *
                (Math.cos(robot.direction  + angularVelocity * duration) -
                        Math.cos(robot.direction));
        if (!Double.isFinite(newY))
        {
            newY = robot.positionY + velocity * duration * Math.sin(robot.direction);
        }

        robot.positionX = newX;
        robot.positionY = newY;
        double newDirection = MathLogic.asNormalizedRadians(robot.direction + angularVelocity * duration);
        if (Math.abs(newX - SCREEN_WIDTH) <= 1 | Math.abs(newY - SCREEN_HEIGHT) <= 1 | newX <= 1 | newY <= 1){
            newDirection = (newDirection + Math.PI) % 2 * Math.PI;
        }
        robot.direction = newDirection;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawComputerRobot(g2d, gameController.getComputerRobot());
        drawUserRobot(g2d, gameController.getUserRobot());
        drawTarget(g2d);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawUserRobot(Graphics2D g, UserRobot robot)
    {
        int robotCenterX = robot.getRoundedX();
        int robotCenterY = robot.getRoundedY();
        AffineTransform t = AffineTransform.getRotateInstance(robot.direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        drawRobotBody(g, robotCenterX, robotCenterY, robot);
    }

    private void drawComputerRobot(Graphics2D g, ComputerRobot robot)
    {
        int robotCenterX = robot.getRoundedX();
        int robotCenterY = robot.getRoundedY();
        AffineTransform t = AffineTransform.getRotateInstance(robot.direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        drawRobotBody(g, robotCenterX, robotCenterY, robot);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, robot.headDiam, robot.headDiam);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, robot.headDiam, robot.headDiam);
    }

    private void drawRobotBody(Graphics2D g, int robotCenterX, int robotCenterY, Robot robot){
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, robot.widthDiam, robot.heightDiam);
        g.setColor(robot.bodyColor);
        fillOval(g, robotCenterX, robotCenterY, robot.widthDiam, robot.heightDiam);
    }

    private void drawTarget(Graphics2D g) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, target.x, target.y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, target.x, target.y, 5, 5);
    }
}
