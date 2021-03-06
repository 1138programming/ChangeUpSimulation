package main.game.controllers;

import java.awt.event.KeyEvent;

public class ArrowKeyController implements Controller {
    private KeyLookup keyLookup = KeyLookup.getInstance();
    private boolean lastCollectKeyState = false;
    private boolean lastEjectKeyState = false;

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

    public boolean collect() {
        boolean collectKeyState = keyLookup.get(KeyEvent.VK_PAGE_UP);
        boolean collect = collectKeyState && !lastCollectKeyState;
        lastCollectKeyState = collectKeyState;

        return collect;
    }

    public boolean eject() {
        boolean ejectKeyState = keyLookup.get(KeyEvent.VK_PAGE_DOWN);
        boolean eject = ejectKeyState && !lastEjectKeyState;
        lastEjectKeyState = ejectKeyState;

        return eject;
    }
}