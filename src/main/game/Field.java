package main.game;

import main.game.geometry.shapes.Shape;
import main.game.geometry.shapes.Circle;
import main.game.geometry.shapes.Wall;
import main.game.geometry.data.Point;
import main.game.display.DisplayObject;
import java.util.ArrayList;

public class Field implements DisplayObject {
    public static final double width = 3.5687; // In meters. 140.5 inches
    public static final double height = 3.5687; // In meters. 140.5 inches
    public static final long dt = (long)0.02; // In seconds. 20 milliseconds, 50 iterations per second

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

        leftData.add(AllianceColor.RED);
        leftData.add(AllianceColor.BLUE);

        rightData.add(AllianceColor.BLUE);
        rightData.add(AllianceColor.RED);

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
        double centerLineOffset = 0.889; // Offset for balls on the center line to the middle goal

        // Add positions for balls on the corners
        activeRedBallStartingPositions.add(new Point(ballPlusGoalRadius, ballPlusGoalRadius));
        activeRedBallStartingPositions.add(new Point(ballPlusGoalRadius, height - ballPlusGoalRadius));
        activeBlueBallStartingPositions.add(new Point(width - ballPlusGoalRadius, ballPlusGoalRadius));
        activeBlueBallStartingPositions.add(new Point(width - ballPlusGoalRadius, height - ballPlusGoalRadius));

        // Add positions for balls in the middle
        activeRedBallStartingPositions.add(new Point(width / 2 + ballPlusGoalRadius, height / 2));
        activeRedBallStartingPositions.add(new Point(width / 2, height / 2 + ballPlusGoalRadius));
        activeBlueBallStartingPositions.add(new Point(width / 2 - ballPlusGoalRadius, height / 2));
        activeBlueBallStartingPositions.add(new Point(width / 2, height / 2 - ballPlusGoalRadius));

        // Add positions for balls on the center line offset from the middle goal
        activeRedBallStartingPositions.add(new Point(width / 2, height / 2 + centerLineOffset));
        activeBlueBallStartingPositions.add(new Point(width / 2, height / 2 - centerLineOffset));

        double robotXOffset = 0.29718; // In meters. 11.7 inches

        // Add starting positions for robots
        redAllianceStartingPositions.add(new Point(robotXOffset, centerLineOffset));
        redAllianceStartingPositions.add(new Point(robotXOffset, height - centerLineOffset));
        blueAllianceStartingPositions.add(new Point(width - robotXOffset, centerLineOffset));
        blueAllianceStartingPositions.add(new Point(width - robotXOffset, height - centerLineOffset));
    }

    // Arrays to hold the field objects
    public static ArrayList<Wall> fieldWalls = new ArrayList<Wall>(4);
    public static ArrayList<Goal> goals = new ArrayList<Goal>(9);
    public static ArrayList<Ball> redBalls = new ArrayList<Ball>(16);
    public static ArrayList<Ball> blueBalls = new ArrayList<Ball>(16);
    public static ArrayList<Robot> redAlliance = new ArrayList<Robot>(2);
    public static ArrayList<Robot> blueAlliance = new ArrayList<Robot>(2);

    // Additional groupings of field objects
    public static ArrayList<Ball> balls = new ArrayList<Ball>(32); // All the balls on the field
    public static ArrayList<Robot> robots = new ArrayList<Robot>(4); // All the robots on the field
    public static ArrayList<Circle> moveableObjects = new ArrayList<Circle>(36); // The balls and the robots
    public static ArrayList<Shape> stationaryObjects = new ArrayList<Shape>(13); // The balls and the robots

    // Initialize all field objects
    {
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
            redAlliance.add(redRobot);
            blueAlliance.add(blueRobot);
            robots.add(redRobot);
            robots.add(blueRobot);
            moveableObjects.add(redRobot);
            moveableObjects.add(blueRobot);
        }
    }

    // Reset field to how it should be at the start of a match
    public static void reset() {
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

            redRobot.collectBall(redBalls.get(redIndex));
            blueRobot.collectBall(blueBalls.get(blueIndex));
            redIndex++;
            blueIndex++;

            redRobot.setPos(redAllianceStartingPositions.get(i).copy());
            blueRobot.setPos(blueAllianceStartingPositions.get(i).copy());
        }
    }
}