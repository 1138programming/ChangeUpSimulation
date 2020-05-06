package main.game;

import main.game.geometry.shapes.Circle;
import main.game.geometry.data.Point;
import main.game.geometry.data.Vector;
import main.game.geometry.data.Angle;
import main.game.controllers.Controller;
import main.game.controllers.NullController;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class Robot extends Circle {
    public static final double radius = 0.2286;
    public final AllianceColor color;
    private int maxBalls = 3;
    private Angle angle = new Angle(0);
    private Angle turnAngle = new Angle(Math.PI / 100);
    private Point facingPos = new Point(0, 0);
    private double movingAccel = 3;
    private double maxVel = 3;
    private double frictionCoef = 0.9;
    private ArrayDeque<Ball> balls = new ArrayDeque<Ball>(maxBalls);
    private Controller controller = new NullController();
    private Field field;

    public Robot(AllianceColor color) {
        // Starts at (0, 0), has a radius of 0.2286 meters (18 inch diameter), has a coefficient of restitution of 0.9, and weighs 4.5 kilograms (10 pounds)
        super(new Point(0, 0), radius, 0.9, 4.5);
        this.color = color;
    }

    public boolean addBall(Ball ball) {
        if (balls.size() < maxBalls) {
            balls.addFirst(ball);
            ball.setActive(false);
            ball.setPos(getPos().copy());
            return true;
        } else {
            return false;
        }
    }

    public Ball removeLastBall() {
        return balls.pollLast();
    }

    public Ball removeFirstBall() {
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

    private final double goalScoringCosAngle = Math.cos(Math.PI / 6);
    private Goal facingGoal() {
        ArrayList<Goal> candidates = new ArrayList<Goal>(9);
        for (Goal goal : field.goals) {
            Vector vec1 = new Vector(pos, facingPos);
            Vector vec2 = new Vector(pos, goal.getPos());
            if (vec1.dot(vec2) > 0 && vec1.getCosAngle(vec2) > goalScoringCosAngle) {
                candidates.add(goal);
            }
        }

        if (candidates.size() > 0) {
            double leastSqrDistance = candidates.get(0).getPos().sqrDistance(pos);
            Goal closest = candidates.get(0);
            for (int i = 1; i < candidates.size(); i++) {
                double sqrDistance = candidates.get(i).getPos().sqrDistance(pos);
                if (sqrDistance < leastSqrDistance) {
                    leastSqrDistance = sqrDistance;
                    closest = candidates.get(i);
                }
            }

            double minSqrDistance = (radius + 0.15) * (radius + 0.15);
            if (leastSqrDistance < minSqrDistance) {
                return closest;
            }
        }
        return null;
    }

    private final double ballCollectingCosAngle = Math.cos(Math.PI / 12);
    private Ball facingBall() {
        ArrayList<Ball> candidates = new ArrayList<Ball>(36);
        for (Ball ball : field.balls) {
            if (ball.isActive()) {
                Vector vec1 = new Vector(pos, facingPos);
                Vector vec2 = new Vector(pos, ball.getPos());
                if (vec1.dot(vec2) > 0 && vec1.getCosAngle(vec2) > ballCollectingCosAngle) {
                    candidates.add(ball);
                }
            }
        }

        if (candidates.size() > 0) {
            double leastSqrDistance = candidates.get(0).getPos().sqrDistance(pos);
            Ball closest = candidates.get(0);
            for (int i = 1; i < candidates.size(); i++) {
                double sqrDistance = candidates.get(i).getPos().sqrDistance(pos);
                if (sqrDistance < leastSqrDistance) {
                    leastSqrDistance = sqrDistance;
                    closest = candidates.get(i);
                }
            }
            
            double minSqrDistance = (radius + 0.15) * (radius + 0.15);
            if (leastSqrDistance < minSqrDistance) {
                return closest;
            }
        }
        return null;
    }

    public void collect() {
        if (balls.size() >= maxBalls) {
            return;
        }

        Goal goal = facingGoal();
        if (goal != null) {
            Ball ball = goal.descoreBall();
            if (ball != null) {
                addBall(ball);
            }
        } else {
            Ball ball = facingBall();
            if (ball != null) {
                addBall(ball);
            }
        }
    }

    public void eject() {
        if (balls.size() == 0) {
            return;
        }

        Ball ball = removeFirstBall();
        Goal goal = facingGoal();
        if (goal != null) {
            if (!goal.scoreBall(ball)) {
                addBall(ball);
            }
        } else {
            ball.setPos(facingPos.copy().add(new Vector(Ball.radius, angle).toPoint()));
            ball.setActive(true);
        }
    }

    public void setField(Field field) {
        this.field = field;
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

        if (controller.collect()) {
            collect();
        }

        if (controller.eject()) {
            eject();
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

    private int bRadius = 10;

    @Override
    public void render(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval((int)(Field.displayRatio * (pos.x - radius)), (int)(Field.displayRatio * (Field.height - pos.y - radius)), (int)(Field.displayRatio * radius * 2), (int)(Field.displayRatio * radius * 2));
        g.setColor(color.javaColor);
        g.drawOval((int)(Field.displayRatio * (pos.x - radius)), (int)(Field.displayRatio * (Field.height - pos.y - radius)), (int)(Field.displayRatio * radius * 2), (int)(Field.displayRatio * radius * 2));
        g.setColor(Color.BLACK);
        g.drawLine((int)(Field.displayRatio * pos.x), (int)(Field.displayRatio * (Field.height - pos.y)), (int)(Field.displayRatio * facingPos.x), (int)(Field.displayRatio * (Field.height - facingPos.y)));

        if (balls.size() > 0) {
            Point ballDisplayPoint = facingPos.copy().scale(Field.displayRatio);
            Vector ballDisplacement = new Vector(bRadius * 2, angle);
            for (Ball ball : balls) {
                ballDisplayPoint.subtract(ballDisplacement.toPoint());

                g.setColor(ball.color.javaColor);
                g.fillOval((int)(ballDisplayPoint.x - bRadius), (int)(Field.displayRatio * Field.height - ballDisplayPoint.y - bRadius), bRadius * 2, bRadius * 2);
            }
        }
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