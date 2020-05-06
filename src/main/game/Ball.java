package main.game;

import main.game.geometry.shapes.Circle;
import main.game.geometry.data.Point;
import main.game.display.DisplayObject;

public class Ball extends Circle implements DisplayObject {
    public static final double radius = 0.08;
    public final AllianceColor color;
    private boolean active = true;

    public Ball(AllianceColor color) {
        // Starts at (0, 0), has a radius of 0.08 meters (6.3 inch diameter), has a coefficient of restitution of 0.8, and weighs 0.168 kilograms (168 grams or about 6 ounces)
        super(new Point(0, 0), radius, 0.8, 0.168);
        this.color = color;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void tick(long dt) {
        if (active) {
            
        }
    }

    @Override
    public void close() {
    }
}