package main.display;

import java.awt.Graphics;

public abstract class DisplayObject {
    protected static Display display;

    public DisplayObject() {
    }

    public abstract void tick(long dt);
    public abstract void render(Graphics g);
    public abstract void close();

    public static void setDisplay(Display display) {
        DisplayObject.display = display;
    }
}