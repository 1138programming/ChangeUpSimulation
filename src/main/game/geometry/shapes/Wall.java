package main.game.geometry.shapes;

import main.game.geometry.data.Point;
import main.game.geometry.data.Vector;
import main.game.geometry.data.Line;
import main.game.geometry.data.MathUtil;

public class Wall extends Shape {
    public final Point A; // Leftmost point on the wall, with the wall normal facing up
    public final Point B; // Rightmost point on the wall, with the wall normal facing up
    public final Vector normal; // Wall normal
    public final Line line; // Constants for the equation of the line representing this wall
    private double coefRes = 1;
    private double mass = 1;
    private double invMass = 1;
    private Vector accel = new Vector(0, 0);
    private Vector velocity = new Vector(0, 0);
    private Point pos;

    public Wall(Point A, Point B) {
        this.A = A;
        this.B = B;
        this.normal = new Vector(1, A.getAngle(B).add(Math.PI / 2));
        this.line = new Line(A, B);
        pos = A.midpoint(B);
    }

    /**
     * @brief Tests whether an intersection point (i.e. a point known to be on the line corresponding to this wall) is contained in the wall
     * 
     * A point obtained using Line.getIntersection with this wall's line and another line is guaranteed to be on this wall's line, 
     * but since the wall is only a segment of the line, it may not be contained within the wall. This function returns true if it is
     * and false if not.
     *
     * @param intersection  The intersection point to test
     * @return              Whether the wall contains the intersection point
     */
    public boolean containsIntersection(Point intersection) {
        if (Point.isNaP(intersection)) {
            return false;
        }
        return MathUtil.inRange(A.x, B.x, intersection.x) && MathUtil.inRange(A.y, B.y, intersection.y); // Checks if the given point is on the wall
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
}