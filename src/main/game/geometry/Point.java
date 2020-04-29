package main.game.geometry;

public class Point {
    /**
     * @brief The x coordinate of the point
     */
    public double x;

    /**
     * @brief The y coordinate of the point
     */
    public double y;

    /**
     * @brief Creates a point with coordinates x and y
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @brief Creates a point at (0, 0)
     */
    public Point() {
        this(0, 0);
    }

    /**
     * @brief Creates a copy of this point
     */
    public Point copy() {
        return new Point(x, y);
    }

    /**
     * @brief Gets a copy of a given point
     * 
     * @param point Point to copy
     * @return      Copy of the given point
     */
    public static Point copy(Point point) {
        return point.copy();
    }

    /**
     * @brief Adds the coordinates of a given point to this point's coordinates
     * 
     * Modifies this point, but not the given one
     * 
     * @param point Given point
     * @return      This point after the addition
     */
    public Point add(Point point) {
        x += point.x;
        y += point.y;
        return this;
    }

    /**
     * @brief Creates a point which is the result of the addition of the two given points.
     * 
     * Does not modify either of the given points
     * 
     * @param point1    First point
     * @param point2    Second point, to be added to the first point
     * @return          Resulting point
     */
    public static Point add(Point point1, Point point2) {
        return point1.copy().add(point2);
    }

    /**
     * @brief Subtracts the coordinates of a given point to this point's coordinates
     * 
     * Modifies this point, but not the given one
     * 
     * @param point Given point
     * @return      This point after the subtraction
     */
    public Point subtract(Point point) {
        x -= point.x;
        y -= point.y;
        return this;
    }

    /**
     * @brief Creates a point which is the result of the subtraction of the second given point from the first given point.
     * 
     * Does not modify either of the given points
     * 
     * @param point1    First point
     * @param point2    Second point, to be subtracted from the first point
     * @return          Resulting point
     */
    public static Point subtract(Point point1, Point point2) {
        return point1.copy().subtract(point2);
    }

    /**
     * @brief Scales the coordinates of this point by a given scalar
     * 
     * Modifies this point
     * 
     * @param scalar    Given scalar
     * @return          This point after the scaling
     */
    public Point scale(double scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    /**
     * @brief Creates a point which is the result of scaling a given point by a given scalar
     * 
     * Does not modify the given point
     * 
     * @param point1    Point to scale
     * @param scalar    Scalar to scale by
     * @return          Resulting point
     */
    public static Point scale(Point point1, double scalar) {
        return point1.copy().scale(scalar);
    }

    /**
     * @brief Rotates this point about a given reference point by a given angle
     * 
     * This method avoids the infinities that arise when using tan and inverse tan to rotate points by instead using a rotation matrix. Only the sine and cosine of the angle are calculated. The rest of the operations are simple addition and multiplication
     * 
     * @param reference Point to rotate about
     * @param angle     Angle to rotate by
     * @return          This point after the rotation
     */
    public Point rotate(Point reference, double angle) {
        if (reference == this) {
            return this;
        }

        this.subtract(reference);

        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        this.x = this.x * cos + this.y * -sin;
        this.y = this.x * sin + this.y * cos;

        this.add(reference);
        return this;
    }

    /**
     * @brief Rotates a given point around another given reference point by a given angle
     * 
     * Does not modify either of the given points
     * 
     * @param point     Point to be rotated
     * @param reference Reference point to rotate about
     * @param angle     Angle to rotate by
     * @return          Resulting point of the rotation
     */
    public static Point rotate(Point point, Point reference, double angle) {
        return point.copy().rotate(reference, angle);
    }

    /**
     * @brief Gets the angle between the x axis and the line between this point and the given point
     * 
     * @param point Point to get the angle to
     * @return      The angle
     */
    public double getAngle(Point point) {
        double angle = Math.atan2(point.y - y, point.x - x);
        if (angle < 0) {
            angle += 2 * Math.PI;
        }
        return angle;
    }

    /**
     * @brief Gets the cosine of the angle between the x axis and the line between this point and the given point
     * 
     * Uses the equation for the dot product to get the angle between the vector made by the two points, and the vector representing the x axis
     * 
     * @param point Point to get the cosine of the angle with
     * @return      The cosine of the angle
     */
    public double getCosAngle(Point point) {
        
        double dx = x - point.x;
        double dy = y - point.y;
        double cosAngle =  dx / Math.sqrt(dx * dx + dy * dy);

        return cosAngle;
    }

    /**
     * @brief Gets the square of the distance between this point and a given point.
     * 
     * This is useful to save time when comparing square differences in, say, a rectangle intersection test
     * 
     * @param point Point to calculate the square distance to
     * @return      Square distance
     */
    public double sqrDistance(Point point) {
        double xDiff = this.x - point.x;
        double yDiff = this.y - point.y;
        return xDiff * xDiff + yDiff * yDiff;
    }

    /**
     * @brief Gets the distance to a given point
     * 
     * This is just the square root of the square distance
     * 
     * @param point Point to calculate the distance to
     * @return      Distance
     */
    public double distance(Point point) {
        return Math.sqrt(this.sqrDistance(point));
    }

    /**
     * @brief Gets the midpoint between this and a given point
     * 
     * Does not modify this or the given point
     * 
     * @param point Point to get the midpoint to
     * @return      The midpoint
     */
    public Point midpoint(Point point) {
        return this.copy().add(point).scale(0.5);
    }

    /**
     * @brief A point that is not a point
     */
    public static final Point NaP = new Point(Double.NaN, Double.NaN);

    /**
     * @brief Checks whether a point is a point
     * 
     * @param point Point to check
     * @return      Whether the point is a point
     */
    public static final boolean isNaP(Point point) {
        if (point == NaP) {
            return true;
        }
        return Double.isNaN(point.x) || Double.isNaN(point.y);
    }
}