package main.game.display;

import main.game.controllers.KeyLookup;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public abstract class Display {
    private DisplayFrame displayFrame;
    private ArrayList<DisplayObject> objects = new ArrayList<DisplayObject>();

    public boolean debugMode;

    public Display(boolean debugMode) {
        this.debugMode = debugMode;

        displayFrame = new DisplayFrame(this, debugMode);
        displayFrame.addKeyListener(KeyLookup.getInstance());
    }

    public Display() {
        this(false);
    }

    public abstract String getName();
    public abstract Color getBgColor();

    public DisplayFrame getDisplayFrame() {
        return displayFrame;
    }

    public void tick(long dt) {
        for (DisplayObject object : objects) {
            object.tick(dt);
        }
    }

    public void render(Graphics g) {
        Dimension displaySize = displayFrame.getSize();
        g.setColor(getBgColor());
        g.fillRect(0, 0, displaySize.width, displaySize.height);
        for (DisplayObject object : objects) {
            object.render(g);
        }
    }

    public void forceRender() {
        displayFrame.forceRender();
    }

    public void start() {
        displayFrame.start();
    }

    protected void addObject(DisplayObject object) {
        objects.add(object);
        object.setDisplay(this);
    }

    protected void removeObject(DisplayObject object) {
        objects.remove(object);
        object.close();
    }

    protected Dimension getSize() {
        return displayFrame.getSize();
    }
}