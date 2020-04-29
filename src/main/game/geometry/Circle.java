package main.game.geometry;

public class Circle {
    private Point pos;
    public final double radius;
    public final boolean stationary;
    private double restitution = 1;

    public Circle(Point pos, double radius, boolean stationary) {
        this.pos = pos;
        this.radius = radius;
        this.stationary = stationary;
    }

    public Circle(Point pos, double radius) {
        this(pos, radius, false);
    }

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