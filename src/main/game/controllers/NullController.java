package main.game.controllers;

public class NullController implements Controller {
    public NullController() {
    }

    public boolean goForward() {
        return false;
    }

    public boolean goBackward() {
        return false;
    }

    public boolean turnRight() {
        return false;
    }

    public boolean turnLeft() {
        return false;
    }
}