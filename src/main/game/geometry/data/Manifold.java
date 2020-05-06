package main.game.geometry.data;

import main.game.geometry.shapes.Shape;
import main.game.Field;

/**
 * Class to hold information about a collision
 */
public class Manifold {
    public final Shape A;
    public final Shape B;
    public final Vector normal;
    public final double depth;
    public static final Manifold NaM = new Manifold();
    public static final double percent = 0.2; // Positional correction percentage
    public static final double slop = 0.01 / Field.displayRatio; // Positional correction slop to avoid jittering
    private boolean resolved = false;

    public Manifold(Shape A, Shape B, Vector normal, double depth) {
        this.A = A;
        this.B = B;
        this.normal = normal;
        this.depth = depth;
    }

    private Manifold() {
        A = null;
        B = null;
        normal = Vector.NaV;
        depth = Double.NaN;
    }

    public static boolean isNaM(Manifold manifold) {
        return manifold == NaM;
    }

    public boolean resolved() {
        return resolved;
    }

    /**
     * @brief Resolves the manifold by applying an impulse to both objects
     * 
     * Returns whether the manifold was resolved succesfully. If the manifold was already resolved or if it is not a Manifold, the function returns false.
     * Otherwise, it returns true.
     * 
     * @return  Whether the manifold was resolved succesfully
     */
    public boolean resolve() {
        // Make sure the manifold exists and isn't resolved more than once
        if (isNaM(this) || resolved) {
            return false;
        }
        resolved = true;

        Vector velA = A.getVelocity();
        Vector velB = B.getVelocity();
        Vector relativeVelocity = Vector.subtract(velA, velB);
        double velAlongNormal = Vector.dot(relativeVelocity, normal);

        if (velAlongNormal < 0) {
            return true;
        }

        double invMassA = A.getInvMass();
        double invMassB = B.getInvMass();
        double e = Math.min(A.getCoefRes(), B.getCoefRes());
        double j = (1 - e) * velAlongNormal;
        j /= invMassA + invMassB;

        Vector deltaVA = Vector.scale(normal, -j * invMassA);
        Vector deltaVB = Vector.scale(normal, j * invMassB);

        velA.add(deltaVA);
        velB.add(deltaVB);

        positionalCorrection();

        return true;
    }

    private void positionalCorrection() {
        double invMassA = A.getInvMass();
        double invMassB = B.getInvMass();
        Point correction = Vector.scale(normal, Math.max(depth - slop, 0) / (invMassA + invMassB) * percent).toPoint();
        A.getPos().add(Point.scale(correction, -invMassA));
        B.getPos().add(Point.scale(correction, invMassB));
    }
}