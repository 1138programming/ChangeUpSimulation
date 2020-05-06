package main.game;

import main.game.geometry.shapes.Shape;
import main.game.geometry.shapes.Circle;
import main.game.geometry.shapes.Wall;
import main.game.geometry.data.Point;
import main.game.display.Display;
import main.game.display.DisplayObject;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Field implements DisplayObject {
    public static final double width = 3.5687; // In meters. 140.5 inches
    public static final double height = 3.5687; // In meters. 140.5 inches
    //public static final double displayRatio = DisplayFrame.WIDTH / width;
    public static final double displayRatio = 960 / width;
    //public static final double displayRatio = 1;

    public static final long dt = 20; // In seconds. 20 milliseconds, 50 iterations per second

    // Field object position data
    public static ArrayList<Point> goalPositions = new ArrayList<Point>(9);
    public static ArrayList<Point> activeRedBallStartingPositions = new ArrayList<Point>(5);
    public static ArrayList<Point> activeBlueBallStartingPositions = new ArrayList<Point>(5);
    public static ArrayList<Point> redAllianceStartingPositions = new ArrayList<Point>(2);
    public static ArrayList<Point> blueAllianceStartingPositions = new ArrayList<Point>(2);

    // Goal starting configuration data
    public static ArrayList<ArrayList<AllianceColor>> goalStartingData = new ArrayList<ArrayList<AllianceColor>>(9);
    {
        ArrayList<AllianceColor> leftData = new ArrayList<AllianceColor>(2);
        ArrayList<AllianceColor> rightData = new ArrayList<AllianceColor>(2);
        ArrayList<AllianceColor> bottomData = new ArrayList<AllianceColor>(3);
        ArrayList<AllianceColor> topData = new ArrayList<AllianceColor>(3);

        leftData.add(AllianceColor.BLUE);
        leftData.add(AllianceColor.RED);

        rightData.add(AllianceColor.RED);
        rightData.add(AllianceColor.BLUE);

        bottomData.add(AllianceColor.BLUE);
        bottomData.add(AllianceColor.RED);
        bottomData.add(AllianceColor.BLUE);

        topData.add(AllianceColor.RED);
        topData.add(AllianceColor.BLUE);
        topData.add(AllianceColor.RED);

        goalStartingData.add(rightData);
        goalStartingData.add(bottomData);
        goalStartingData.add(leftData);
        goalStartingData.add(rightData);
        goalStartingData.add(new ArrayList<AllianceColor>(0));
        goalStartingData.add(leftData);
        goalStartingData.add(rightData);
        goalStartingData.add(topData);
        goalStartingData.add(leftData);
    }

    // Create starting points for balls and goals
    {
        double goalOffset = 0.14732; // In meters. 5.8 inches
        double goalDistance = 1.6383; // In meters. 64.5 inches

        // Calculate grid of goal positions
        for (int i = 0; i < 3; i++) {
            double y = goalOffset + i * goalDistance;
            for (int j = 0; j < 3; j++) {
                double x = goalOffset + j * goalDistance;
                goalPositions.add(new Point(x, y));
            }
        }

        double ballPlusGoalRadius = Ball.radius + Goal.radius;
        double cornerBallOffset = (Math.sqrt(2) * goalOffset + ballPlusGoalRadius) * Math.cos(Math.PI / 4);
        double centerLineOffset = 0.889; // Offset for balls on the center line to the middle goal

        // Add positions for balls on the corners
        activeBlueBallStartingPositions.add(new Point(width - cornerBallOffset, cornerBallOffset));
        activeBlueBallStartingPositions.add(new Point(width - cornerBallOffset, height - cornerBallOffset));
        activeRedBallStartingPositions.add(new Point(cornerBallOffset, cornerBallOffset));
        activeRedBallStartingPositions.add(new Point(cornerBallOffset, height - cornerBallOffset));

        // Add positions for balls in the middle
        activeBlueBallStartingPositions.add(new Point(width / 2 - ballPlusGoalRadius, height / 2));
        activeBlueBallStartingPositions.add(new Point(width / 2, height / 2 - ballPlusGoalRadius));
        activeRedBallStartingPositions.add(new Point(width / 2 + ballPlusGoalRadius, height / 2));
        activeRedBallStartingPositions.add(new Point(width / 2, height / 2 + ballPlusGoalRadius));

        // Add positions for balls on the center line offset from the middle goal
        activeBlueBallStartingPositions.add(new Point(width / 2, height / 2 - centerLineOffset));
        activeRedBallStartingPositions.add(new Point(width / 2, height / 2 + centerLineOffset));

        double robotXOffset = 0.29718; // In meters. 11.7 inches

        // Add starting positions for robots
        blueAllianceStartingPositions.add(new Point(width - robotXOffset, centerLineOffset));
        blueAllianceStartingPositions.add(new Point(width - robotXOffset, height - centerLineOffset));
        redAllianceStartingPositions.add(new Point(robotXOffset, centerLineOffset));
        redAllianceStartingPositions.add(new Point(robotXOffset, height - centerLineOffset));
    }

    // Arrays to hold the field objects
    public ArrayList<Wall> fieldWalls = new ArrayList<Wall>(4);
    public ArrayList<Goal> goals = new ArrayList<Goal>(9);
    public ArrayList<Ball> redBalls = new ArrayList<Ball>(16);
    public ArrayList<Ball> blueBalls = new ArrayList<Ball>(16);
    public ArrayList<Robot> redAlliance = new ArrayList<Robot>(2);
    public ArrayList<Robot> blueAlliance = new ArrayList<Robot>(2);

    // Additional groupings of field objects
    public ArrayList<Ball> balls = new ArrayList<Ball>(32); // All the balls on the field
    public ArrayList<Robot> robots = new ArrayList<Robot>(4); // All the robots on the field
    public ArrayList<Circle> moveableObjects = new ArrayList<Circle>(36); // The balls and the robots
    public ArrayList<Shape> stationaryObjects = new ArrayList<Shape>(13); // The balls and the robots

    protected Display display;
    public static BufferedImage fieldImg;
    {
        try {
            //fieldImg = ImageIO.read(new File("../../../resources/Vex Field.png"));
            fieldImg = ImageIO.read(new File("resources/Vex Field.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Initialize all field objects
    public Field() {
        // The order of the points matters because they determine which direction the collision detection of the wall acts in
        Wall bottomWall = new Wall(new Point(0, 0), new Point(width, 0));
        Wall rightWall = new Wall(new Point(width, 0), new Point(width, height));
        Wall topWall = new Wall(new Point(width, height), new Point(0, height));
        Wall leftWall = new Wall(new Point(0, height), new Point(0, 0));
        fieldWalls.add(bottomWall);
        fieldWalls.add(rightWall);
        fieldWalls.add(topWall);
        fieldWalls.add(leftWall);
        stationaryObjects.add(bottomWall);
        stationaryObjects.add(rightWall);
        stationaryObjects.add(topWall);
        stationaryObjects.add(leftWall);

        for (int i = 0; i < 9; i++) {
            Goal goal = new Goal(goalPositions.get(i));
            goals.add(goal);
            stationaryObjects.add(goal);
        }

        for (int i = 0; i < 16; i++) {
            Ball redBall = new Ball(AllianceColor.RED);
            Ball blueBall = new Ball(AllianceColor.BLUE);
            redBalls.add(redBall);
            blueBalls.add(blueBall);
            balls.add(redBall);
            balls.add(blueBall);
            moveableObjects.add(redBall);
            moveableObjects.add(blueBall);
        }

        for (int i = 0; i < 2; i++) {
            Robot redRobot = new Robot(AllianceColor.RED);
            Robot blueRobot = new Robot(AllianceColor.BLUE);
            redRobot.setField(this);
            blueRobot.setField(this);
            redAlliance.add(redRobot);
            blueAlliance.add(blueRobot);
            robots.add(redRobot);
            robots.add(blueRobot);
            moveableObjects.add(redRobot);
            moveableObjects.add(blueRobot);
        }

        reset();
    }

    // Reset field to how it should be at the start of a match
    public void reset() {
        // Set balls up at their starting position
        for (int i = 0; i < 5; i++) {
            redBalls.get(i).setPos(activeRedBallStartingPositions.get(i).copy());
            blueBalls.get(i).setPos(activeBlueBallStartingPositions.get(i).copy());
        }

        // Variables for keeping track of which ball to get
        int redIndex = 5;
        int blueIndex = 5;

        // Fill up goals with the correct starting configuration of balls
        for (int i = 0; i < 9; i++) {
            Goal goal = goals.get(i);
            goal.clear();
            for (AllianceColor color : goalStartingData.get(i)) {
                if (color == AllianceColor.RED) {
                    goal.scoreBall(redBalls.get(redIndex));
                    redIndex++;
                } else {
                    goal.scoreBall(blueBalls.get(blueIndex));
                    blueIndex++;
                }
            }
        }

        // Put preloads in robots and put them in their starting positions
        for (int i = 0; i < 2; i++) {
            Robot redRobot = redAlliance.get(i);
            Robot blueRobot = blueAlliance.get(i);

            redRobot.setPos(redAllianceStartingPositions.get(i).copy());
            blueRobot.setPos(blueAllianceStartingPositions.get(i).copy());
            redRobot.setAngle(0);
            blueRobot.setAngle(Math.PI);

            redRobot.addBall(redBalls.get(redIndex));
            blueRobot.addBall(blueBalls.get(blueIndex));
            redIndex++;
            blueIndex++;
        }
    }

    @Override
    public void setDisplay(Display display) {
        this.display = display;
        for (Circle moveable : moveableObjects) {
            moveable.setDisplay(display);
        }
        for (Shape stationary : stationaryObjects) {
            stationary.setDisplay(display);
        }
    }

    @Override
    public void tick(long dt) {
        for (Circle moveable : moveableObjects) {
            moveable.tick(Field.dt);
        }
        for (Shape stationary : stationaryObjects) {
            stationary.tick(Field.dt);
        }

        for (int i = 0; i < moveableObjects.size(); i++) {
            Circle object1 = moveableObjects.get(i);
            if (object1 instanceof Ball && !((Ball)object1).isActive()) {
                continue;
            }
            for (int j = i + 1; j < moveableObjects.size(); j++) {
                Circle object2 = moveableObjects.get(j);
                if (object2 instanceof Ball && !((Ball)object2).isActive()) {
                    continue;
                }
                Shape.getCollisionManifold(object1, object2).resolve();
            }
        }

        for (int i = 0; i < moveableObjects.size(); i++) {
            Circle object1 = moveableObjects.get(i);
            if (object1 instanceof Ball && !((Ball)object1).isActive()) {
                continue;
            }
            for (int j = 0; j < stationaryObjects.size(); j++) {
                Shape stationaryObject = stationaryObjects.get(j);
                if (stationaryObject instanceof Circle) {
                    Shape.getCollisionManifold((Circle)stationaryObjects.get(j), object1).resolve();
                } else {
                    Shape.getCollisionManifold((Wall)stationaryObjects.get(j), object1).resolve();
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (Field.fieldImg != null) {
            g.drawImage(Field.fieldImg, 0, 0, display.getDisplayFrame());
        }

        /*for (Circle moveable : moveableObjects) {
            moveable.render(g);
        }*/
        for (Robot robot : robots) {
            robot.render(g);
        }
        for (Ball ball : balls) {
            ball.render(g);
        }
        for (Shape stationary : stationaryObjects) {
            stationary.render(g);
        }
    }

    @Override
    public void close() {
        for (Shape stationary : stationaryObjects) {
            stationary.close();
        }
        for (Circle moveable : moveableObjects) {
            moveable.close();
        }
    }
}