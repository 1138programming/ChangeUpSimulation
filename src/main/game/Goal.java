package main.game;

import main.game.geometry.shapes.Circle;
import main.game.geometry.data.Point;
import main.game.display.DisplayObject;
import java.util.ArrayDeque;

public class Goal extends Circle implements DisplayObject {
    public static final double radius = 0.1435;
    private ArrayDeque<Ball> balls = new ArrayDeque<Ball>(3);

    public Goal(Point pos) {
        // Starts at (0, 0), radius of 0.1435 meters (11.29 inch outer diameter), coefficient of restitution of 0.8, and infinite mass (since its stationary)
        super(pos, radius, 0.8, 0);
    }

    public boolean scoreBall(Ball ball) {
        if (balls.size() < 3) {
            balls.add(ball);
            ball.setActive(false);
            return true;
        } else {
            return false;
        }
    }

    public Ball descoreBall() {
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