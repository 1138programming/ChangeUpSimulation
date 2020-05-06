package main.game.geometry.data;

/**
 * Class to hold the three constants that describe a line in ABC form
 */
public class Line {
    public final double A;
    public final double B;
    public final double C;
    public static final Line NaL = new Line();

    public Line(double A, double B, double C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }

    public Line (Point point1, Point point2) {
        double dx = point2.x - point1.x;
        double dy = point1.y - point2.y;
        
        A = dy;
        B = dx;
        C = dx * point1.y + dy * point1.x;
    }

    private Line() {
        this(Double.NaN, Double.NaN, Double.NaN);
    }

    /**
     * @brief Returns the point that is the intersection between this line and a give line
     */
    public Point getIntersection(Line line) {
        double invDet = 1 / (this.A * line.B - line.A * this.B);
        
        if (Double.isInfinite(invDet)) {
            return Point.NaP;
        }
        
        double a = invDet * line.B;
        double b = invDet * -1 * this.B;
        double c = invDet * -1 * line.A;
        double d = invDet * this.A;
        
        double x = this.C * a + line.C * b;
        double y = this.C * c + line.C * d;
        
        return new Point(x, y);
    }

    public static Point getIntersection(Line A, Line B) {
        return A.getIntersection(B);
    }

    public static boolean isNaL(Line line) {
        if (line == NaL) {
            return true;
        }
        return Double.isNaN(line.A) || Double.isNaN(line.B) || Double.isNaN(line.C);
    }
}