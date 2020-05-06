package main.game;

import main.game.geometry.shapes.Circle;
import main.game.geometry.data.Point;
import main.game.display.DisplayObject;
import java.util.ArrayDeque;

public class Robot extends Circle implements DisplayObject {
    public static final double radius = 0.2286;
    public final AllianceColor color;
    private ArrayDeque<Ball> balls = new ArrayDeque<Ball>(3);

    public Robot(AllianceColor color) {
        // Starts at (0, 0), has a radius of 0.2286 meters (18 inch diameter), has a coefficient of restitution of 0.9, and weighs 4.5 kilograms (10 pounds)
        super(new Point(0, 0), radius, 0.9, 4.5);
        this.color = color;
    }

    public boolean collectBall(Ball ball) {
        if (balls.size() < 3) {
            balls.add(ball);
            ball.setActive(false);
            return true;
        } else {
            return false;
        }
    }

    public Ball ejectBall() {
        return balls.pollLast();
    }

    public Ball spitOutBall() {
        return balls.pollFirst();
    }

    public void clear() {
        balls.clear();
    }

    public boolean isEmpty() {
        for (Ball ball : balls) {
            ball.setActive(true);
        }
        return balls.isEmpty();
    }
}