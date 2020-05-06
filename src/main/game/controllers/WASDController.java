package main.game.controllers;

import java.awt.event.KeyEvent;

public class WASDController implements Controller {
    private KeyLookup keyLookup = KeyLookup.getInstance();
    private boolean lastCollectKeyState = false;
    private boolean lastEjectKeyState = false;

    public boolean goForward() {
        return keyLookup.get(KeyEvent.VK_W);
    }

    public boolean goBackward() {
        return keyLookup.get(KeyEvent.VK_S);
    }

    public boolean turnRight() {
        return keyLookup.get(KeyEvent.VK_D);
    }

    public boolean turnLeft() {
        return keyLookup.get(KeyEvent.VK_A);
    }

    public boolean collect() {
        boolean collectKeyState = keyLookup.get(KeyEvent.VK_Q);
        boolean collect = collectKeyState && !lastCollectKeyState;
        lastCollectKeyState = collectKeyState;

        return collect;
    }

    public boolean eject() {
        boolean ejectKeyState = keyLookup.get(KeyEvent.VK_E);
        boolean eject = ejectKeyState && !lastEjectKeyState;
        lastEjectKeyState = ejectKeyState;

        return eject;
    }
}