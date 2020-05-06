package main.game.geometry.shapes;

import main.game.geometry.data.Point;
import main.game.geometry.data.Vector;

public class Circle extends Shape {
    public final double radius;
    private double coefRes;
    private double mass;
    private double invMass;
    private Vector accel = new Vector(0, 0);
    private Vector velocity = new Vector(0, 0);
    private Point pos;

    public Circle(Point pos, double radius, double coefRes, double mass) {
        this.pos = pos;
        this.radius = radius;
        this.mass = mass;

        if (mass == 0) {
            invMass = 0;
        } else {
            invMass = 1 / mass;
        }
    }

    public Circle(Point pos, double radius, double coefRes) {
        this(pos, radius, coefRes, 0);
    }

    public Circle(Point pos, double radius) {
        this(pos, radius, 1);
    }

    @Override
    public double getCoefRes() {
        return coefRes;
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public double getInvMass() {
        return invMass;
    }

    @Override
    public Vector getAccel() {
        return accel;
    }

    @Override
    public Vector getVelocity() {
        return velocity;
    }

    @Override
    public Point getPos() {
        return pos;
    }

    public void setPos(Point newPos) {
        pos = newPos;
    }

    public void setPos(double x, double y) {
        pos = new Point(x, y);
    }
}