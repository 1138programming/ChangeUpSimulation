package main.game.geometry.shapes;

import main.game.display.Display;
import main.game.display.DisplayObject;
import main.game.geometry.data.Point;
import main.game.geometry.data.Vector;
import main.game.geometry.data.Line;
import main.game.geometry.data.Manifold;
import main.game.Robot;

/**
 * Parent class for shapes
 * Handles collision detection between shapes
 */
public abstract class Shape implements DisplayObject {
    protected Display display;
    protected double coefRes;
    protected double mass;
    protected double invMass;
    protected Vector accel = new Vector(0, 0);
    protected Vector velocity = new Vector(0, 0);
    protected Point pos;

    public abstract double getCoefRes();
    public abstract double getMass();
    public abstract double getInvMass();
    public abstract Vector getAccel();
    public abstract Vector getVelocity();
    public abstract Point getPos();

    public void updateVel(long dt) {
        velocity.add(Vector.scale(accel, (double)dt / 1000));
    }

    public void updatePos(long dt) {
        pos.add(Vector.scale(velocity, (double)dt / 1000).toPoint());
        //pos.add(velocity.toPoint());
    }

    /**
     * @brief Generates a manifold for the collision between the two circles, if there is one.
     */
    public static Manifold getCollisionManifold(Circle circle1, Circle circle2) {
        Point posA = circle1.getPos();
        Point posB = circle2.getPos();
        double sqrDistance = posA.sqrDistance(posB);
        double minSqrDistance = (circle1.radius + circle2.radius) * (circle1.radius + circle2.radius);

        if (sqrDistance > minSqrDistance) {
            return Manifold.NaM;
        }

        Vector normal = new Vector(1, posA.getAngle(posB));
        double depth = Math.sqrt(minSqrDistance) - Math.sqrt(sqrDistance);

        return new Manifold(circle1, circle2, normal, depth);
    }

    /**
     * @brief Generates a manifold for the collision between the wall and the circle, if there is one
     */
    public static Manifold getCollisionManifold(Wall wall, Circle circle) {
        Point p1 = wall.A;
        Point p2 = wall.B;
        Point p3 = circle.getPos();

        // Calculates the magnitude of the cross product between the vectosr P1 -> P2  and P1 -> P3
        double crossProduct = (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);

        // If the cross product is negative, it means that the circle is on the opposite side of the wall to which the wall's normal vector points
        if (crossProduct < 0) {
            return Manifold.NaM;
        }

        // Gets the line perpendicular to the wall line, and that passes through the circle's center
        Line line1 = wall.line;
        Line line2 = new Line(-1 * line1.B, line1.A, (line1.A * p3.y) - (line1.B * p3.x));
        Point intersection = Line.getIntersection(line1, line2);

        if (!wall.containsIntersection(intersection)) {
            return endpointCheck(wall, circle);
        }

        double sqrDistance = p3.sqrDistance(intersection);
        double minSqrDistance = circle.radius * circle.radius;

        if (sqrDistance > minSqrDistance) {
            return Manifold.NaM;
        }

        double depth = circle.radius - intersection.distance(p3);

        return new Manifold(wall, circle, wall.normal.copy(), depth);
    }

    /**
     * @brief Generates a manifold for the collision between one of the wall's endpoints and the circle
     * 
     * Checks a wall's endpoints for a collision with a circle and generates the corresponding manifold, assuming that some conditions 
     * have been met
     * 
     * Condition 1: The center of the circle is on the side of the wall to which the wall's normal vector points
     * 
     * Condition 2: The wall does not contain the intersection between the wall line and the line perpendicular to the wall line that 
     * passes through the circle's center
     * 
     * Condition 3: The wall is longer than the circle's radius
     * 
     * Note: The circle cannot be colliding with both endpoints if condition 3 is met. If it is not met, a collision manifold will 
     * still be generated, but it may give an incorrect penetration depth. Its probably fine though
     * 
     * @param wall      Wall whose endpoints are to be tested
     * @param circle    Circle to test
     * @return          The manifold representing the collision between one of the wall's endpoints and the circle
     */
    private static Manifold endpointCheck(Wall wall, Circle circle) {
        Point endpoint = wall.A;
        Manifold col = Manifold.NaM;

        // For loop to go through both the wall's endpoints
        // If a collision is found
        for (int i = 0; i < 2; i++) {
            Point center = circle.getPos();
            double sqrDistance = endpoint.sqrDistance(center);
            double minSqrDistance = circle.radius * circle.radius;

            // If the currently tested endpoint is not inside the circle, set the endpoint to the wall's second endpoint and continue
            if (sqrDistance > minSqrDistance) {
                endpoint = wall.B;
                continue;
            }

            double depth = Math.sqrt(minSqrDistance) - Math.sqrt(sqrDistance);

            return col = new Manifold(wall, circle, wall.normal.copy(), depth);
        }

        return col;
    }
}