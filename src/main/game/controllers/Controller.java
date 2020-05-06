package main.game.controllers;

public interface Controller {
    public boolean goForward();
    public boolean goBackward();
    public boolean turnRight();
    public boolean turnLeft();
    public boolean collect();
    public boolean eject();
}