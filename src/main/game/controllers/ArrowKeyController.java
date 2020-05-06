package main.game.controllers;

import java.awt.event.KeyEvent;

public class ArrowKeyController implements Controller {
    private KeyLookup keyLookup = KeyLookup.getInstance();

    public boolean goForward() {
        return keyLookup.get(KeyEvent.VK_UP);
    }

    public boolean goBackward() {
        return keyLookup.get(KeyEvent.VK_DOWN);
    }

    public boolean turnRight() {
        return keyLookup.get(KeyEvent.VK_RIGHT);
    }

    public boolean turnLeft() {
        return keyLookup.get(KeyEvent.VK_LEFT);
    }
}