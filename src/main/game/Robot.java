package main.game;

import main.game.geometry.shapes.Circle;
import main.game.geometry.data.Point;
import main.game.geometry.data.Vector;
import main.game.geometry.data.Angle;
import main.game.controllers.Controller;
import main.game.controllers.NullController;
import java.util.ArrayDeque;
import java.awt.Graphics;
import java.awt.Color;

public class Robot extends Circle {
    public static final double radius = 0.2286;
    public final AllianceColor color;
    private Angle angle = new Angle(0);
    private Angle turnAngle = new Angle(Math.PI / 100);
    private Point facingPos = new Point(0, 0);
    private double movingAccel = 3;
    private double maxVel = 3;
    private double frictionCoef = 0.9;
    private ArrayDeque<Ball> balls = new ArrayDeque<Ball>(3);
    private Controller controller = new NullController();

    public Robot(AllianceColor color) {
        // Starts at (0, 0), has a radius of 0.2286 meters (18 inch diameter), has a coefficient of restitution of 0.9, and weighs 4.5 kilograms (10 pounds)
        super(new Point(0, 0), radius, 0.9, 4.5);
        this.color = color;
    }

    public boolean collectBall(Ball ball) {
        if (balls.size() < 3) {
            balls.add(ball);
            ball.setActive(false);
            ball.setPos(getPos().copy());
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

    public void setAngle(double angle) {
        this.angle = new Angle(angle);
    }

    public void setAngle(Angle angle) {
        this.angle = angle.copy();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void tick(long dt) {
        if (controller.turnRight()) {
            angle.subtract(turnAngle);
        } else if (controller.turnLeft()) {
            angle.add(turnAngle);
        }

        if (controller.goForward()) {
            accel = new Vector(movingAccel, angle);
        } else if (controller.goBackward()) {
            accel = new Vector(-movingAccel, angle);
        } else {
            accel = new Vector(0, 0);
        }

        // Add code to set acceleration based on controller's input
        updateVel(dt);

        double velMagnitude = velocity.getMagnitude();
        if (velMagnitude > maxVel) {
            velocity.scale(maxVel / velMagnitude);
        }

        updatePos(dt);
        velocity.scale(frictionCoef);

        facingPos = Point.add(pos, new Vector(radius, angle).toPoint());
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval((int)(Field.displayRatio * (pos.x - radius)), (int)(Field.displayRatio * (Field.height - pos.y - radius)), (int)(Field.displayRatio * radius * 2), (int)(Field.displayRatio * radius * 2));
        g.setColor(color.javaColor);
        g.drawOval((int)(Field.displayRatio * (pos.x - radius)), (int)(Field.displayRatio * (Field.height - pos.y - radius)), (int)(Field.displayRatio * radius * 2), (int)(Field.displayRatio * radius * 2));
        g.setColor(Color.BLACK);
        g.drawLine((int)(Field.displayRatio * pos.x), (int)(Field.displayRatio * (Field.height - pos.y)), (int)(Field.displayRatio * facingPos.x), (int)(Field.displayRatio * (Field.height - facingPos.y)));
    }

    @Override
    public void close() {
        clear();
    }

    @Override
    public String toString() {
        return "Robot: x = " + Field.displayRatio * getPos().x + ", y = " + Field.displayRatio * getPos().y + ", radius = " + Field.displayRatio * radius;
    }
}