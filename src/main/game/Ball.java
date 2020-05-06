package main.game;

import main.game.geometry.shapes.Circle;
import main.game.geometry.data.Point;
import main.game.geometry.data.Vector;
import java.awt.Graphics;

public class Ball extends Circle {
    public static final double frictionCoef = 0.99;
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
        if (active == false) {
            velocity = new Vector(0, 0);
        }
    }

    @Override
    public void tick(long dt) {
        if (active) {
            getVelocity().scale(frictionCoef);
            updatePos(dt);
        }
    }

    @Override
    public void render(Graphics g) {
        if (active) {
            g.setColor(color.javaColor);
            g.fillOval((int)(Field.displayRatio * (pos.x - radius)), (int)(Field.displayRatio * (Field.height - pos.y - radius)), (int)(Field.displayRatio * radius * 2), (int)(Field.displayRatio * radius * 2));
        }
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return "Ball: x = " + Field.displayRatio * pos.x + ", y = " + Field.displayRatio * pos.y + ", radius = " + Field.displayRatio * radius + ". Active: " + active;
    }
}