package gui;

import entity.*;
import logic.GameController;
import gui.adapters.KeyPressAdapter;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel
{
    private final Timer m_timer = new Timer("events generator", true);

    private int fieldWidth;
    private int fieldHeight;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;
    private final GameController gameController;

    public GameVisualizer(GameController gameController)
    {
        this.gameController = gameController;
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
//        addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseClicked(MouseEvent e)
//            {
////                setTargetPosition(e.getPoint());
//                System.out.println("WDECFVD");
//                repaint();
//            }
//        });
//        addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                gameController.applyLimits(getWidth(), getHeight());
//            }
//        });
        addKeyListener(new KeyPressAdapter(gameController));
        setFocusable(true);
        setDoubleBuffered(true);
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawComputerRobot(g2d);
        drawUserRobot(g2d);
        drawTarget(g2d, gameController.getFood());
        drawTarget(g2d, gameController.getAccelerator());
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawUserRobot(Graphics2D g)
    {
        UserRobot robot = gameController.getUserRobot();
        int robotCenterX = robot.getRoundedX();
        int robotCenterY = robot.getRoundedY();
        AffineTransform t = AffineTransform.getRotateInstance(robot.direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        drawRobotBody(g, robotCenterX, robotCenterY, robot);
    }

    private void drawComputerRobot(Graphics2D g)
    {
        ComputerRobot robot = gameController.getComputerRobot();
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

    private void drawTarget(Graphics2D g, ColorTarget target) {
        int diam = target.diam;
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(target.color);
        fillOval(g, target.x, target.y, diam, diam);
        g.setColor(Color.BLACK);
        drawOval(g, target.x, target.y, diam, diam);
    }
}
