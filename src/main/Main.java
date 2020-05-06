package main;

import main.game.display.Display;
import main.game.Ball;
import main.game.Goal;
import main.game.Robot;
import main.game.Field;
import main.game.controllers.ArrowKeyController;
import main.game.controllers.WASDController;
import java.awt.Color;

public class Main extends Display {
    public static final String name = "Main";
    public static final Color bgColor = Color.GREEN;

    public static void main(String[] args) {
        boolean debugMode = false;

        if (args.length > 0) {
            debugMode = args[0].equals("-d");
        }

        new Main(debugMode);
    }

    Main(boolean debugMode) {
        super(debugMode);

        Field field = new Field();

        field.blueAlliance.get(0).setController(new ArrowKeyController());
        field.redAlliance.get(0).setController(new WASDController());

        addObject(field);

        if (debugMode) {
            for (Ball ball : field.balls) {
                System.out.println(ball.toString());
            }

            for (Goal goal : field.goals) {
                System.out.println(goal.toString());
            }

            for (Robot robot : field.robots) {
                System.out.println(robot.toString());
            }
        }

        start();
    }

    Main() {
        this(false);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Color getBgColor() {
        return bgColor;
    }
}