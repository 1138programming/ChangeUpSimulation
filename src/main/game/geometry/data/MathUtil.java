package main.game.geometry.data;

public class MathUtil {
    /**
     * @brief Epsilon value to use when comparing doubles
     * 
     * If two values differ by less than epsilon, they are considered equal
     */
    public static final double epsilon = 0.000001;

    /**
     * @brief Returns whether a value is in range of two bounds.
     * 
     * The order of the bounds does not matter
     * 
     * @param x     The value
     * @param lower The lower bound
     * @param upper The upper bound
     * @return      Whether x is within [lower, upper] or [upper, lower]
     */
    public static boolean inRange(double x, double lower, double upper) {
        double trueLower = lower;
        double trueUpper = upper;
        if (lower > upper) {
            trueLower = upper;
            trueUpper = lower;
        }

        return trueLower - epsilon <= x && x <= trueUpper + epsilon;
    }

    /**
     * @brief Returns whether two values are equal using epsilon
     * 
     * If the two values differ by less than epsilon, this returns true, otherwise it returns false
     * 
     * @param x The first value
     * @param y The second value
     * @return  Whether they differ by or less than epsilon
     */
    public static boolean epsilonEquals(double x, double y) {
        return Math.abs(x - y) <= epsilon;
    }
}