package main.game.geometry;

public class Vector {
    public double x, y;

    Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    Vector(double magnitude, Angle angle) {
        this.x = magnitude * Math.cos(angle.value);
        this.y = magnitude * Math.sin(angle.value);
    }

    public Vector copy() {
        return new Vector(this.x, this.y);
    }

    public Vector add(Vector B) {
        this.x += B.x;
        this.y += B.y;

        return this;
    }

    public Angle getAngle() {
        return new Angle(Math.atan2(this.y, this.x));
    }

    public double getMagnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public Vector scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public double dot(Vector B) {
        return (this.x * B.x) + (this.y * B.y);
    }

    public static Vector copy(Vector A) {
        return A.copy();
    }

    public static Vector add(Vector A, Vector B) {
        return A.copy().add(B);
    }

    public static Vector scale(Vector A, double scalar) {
        return A.copy().scale(scalar);
    }

    public static double dot(Vector A, Vector B) {
        return A.dot(B);
    }
}