package main.game.display;

import java.awt.Graphics;

public interface DisplayObject {
    public abstract void tick(long dt);
    public abstract void render(Graphics g);
    public abstract void close();
}