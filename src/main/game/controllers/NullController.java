package main.game.controllers;

public class NullController implements Controller {
    private int i = 0;

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

    public boolean collect() {
        return false;
    }

    public boolean eject() {
        if (i == 50) {
            return true;
        } else if (i < 50) {
            i++;
        }
        return false;
    }
}