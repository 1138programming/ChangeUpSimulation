package main.game;

import main.game.geometry.shapes.Circle;
import main.game.geometry.data.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Goal extends Circle {
    public static final double radius = 0.1435; // In meters. 11.29 inch outer diameter
    public static final double innerRadius = 0.089154; // In meters. 7.02 inch outer diameter
    private ArrayDeque<Ball> balls = new ArrayDeque<Ball>(3);
    private ArrayList<Point> ballDisplayPoints = new ArrayList<Point>(3);
    public static BufferedImage goalImg;
    {
        try {
            goalImg = ImageIO.read(new File("resources/Goal.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Goal(Point pos) {
        // Starts at (0, 0), radius of 0.1435 meters (11.29 inch outer diameter), coefficient of restitution of 0.8, and infinite mass (since its stationary)
        super(pos, radius, 0.8, 0);

        for (int i = -1; i < 2; i++) {
            Point ballDisplayPoint = Point.scale(pos, Field.displayRatio);
            ballDisplayPoint.y += i * bRadius * 2;
            ballDisplayPoint.y = (Field.displayRatio * Field.height) - ballDisplayPoint.y;
            ballDisplayPoints.add(ballDisplayPoint);
        }
    }

    public boolean scoreBall(Ball ball) {
        if (balls.size() < 3) {
            balls.add(ball);
            ball.setActive(false);
            ball.setPos(getPos().copy());
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

    @Override
    public void tick(long dt) {
    }

    public int bRadius = 10;

    @Override
    public void render(Graphics g) {
        if (Goal.goalImg != null) {
            g.drawImage(Goal.goalImg, (int)(Field.displayRatio * (pos.x - radius)), (int)(Field.displayRatio * (Field.height - pos.y - radius)), display.getDisplayFrame());
        } else {
            g.setColor(Color.BLACK);
            g.fillOval((int)(Field.displayRatio * (pos.x - radius)), (int)(Field.displayRatio * (Field.height - pos.y - radius)), (int)(Field.displayRatio * radius * 2), (int)(Field.displayRatio * radius * 2));
        }

        int i = 0;
        for (Ball ball : balls) {
            Point ballDisplayPoint = ballDisplayPoints.get(i);
            g.setColor(ball.color.javaColor);
            g.fillOval((int)ballDisplayPoint.x - bRadius, (int)ballDisplayPoint.y - bRadius, bRadius * 2, bRadius * 2);
            i++;
        }
    }

    @Override
    public void close() {
        clear();
    }

    @Override
    public String toString() {
        return "Goal: x = " + Field.displayRatio * getPos().x + ", y = " + Field.displayRatio * getPos().y + ", radius = " + Field.displayRatio * radius;
    }
}