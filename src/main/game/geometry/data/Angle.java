package main.game.geometry.data;

public class Angle {
    public static final double TAU = Math.PI * 2;
    double value;

    public Angle(double value) {
        this.value = value;
        normalize();
    }

    public double normalize() {
        value = Angle.normalize(value);
        return value;
    }

    public Angle copy() {
        return new Angle(value);
    }

    public Angle add(Angle B) {
        value += B.getValue();
        normalize();

        return this;
    }

    public Angle add(double B) {
        value += B;
        normalize();
        
        return this;
    }

    public Angle scale(double scalar) {
        value *= scalar;
        normalize();

        return this;
    }

    public Angle getDist(Angle B) {
        double diff = Angle.normalize(value - B.getValue());
        double dist = diff > Math.PI ? TAU - diff : diff;
        return new Angle(dist);
    }

    public Angle getDiff(Angle B) {
        double diff = Angle.normalize(value - B.getValue());
        return new Angle(diff);
    }

    public double getValue() {
        return value;
    }

    public static double normalize(double value) {
        return value - Math.floor(value / TAU) * TAU;
    }

    public static Angle copy(Angle A) {
        return A.copy();
    }

    public static Angle add(Angle A, Angle B) {
        return A.copy().add(B);
    }

    public static Angle scale(Angle A, double scalar) {
        return A.copy().scale(scalar);
    }

    public static Angle getDist(Angle A, Angle B) {
        return A.getDist(B);
    }

    public static Angle getDiff(Angle A, Angle B) {
        return A.getDiff(B);
    }
}