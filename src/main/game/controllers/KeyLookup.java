package main.game.controllers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class KeyLookup extends KeyAdapter {
    private static KeyLookup instance = new KeyLookup();
    private HashMap<Integer, Boolean> table = new HashMap<Integer, Boolean>();

    private KeyLookup() {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        table.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        table.put(e.getKeyCode(), false);
    }

    public boolean get(int keyCode) {
        Boolean value = table.get(keyCode);
        if (value == null) {
            return false;
        } else {
            return value;
        }
    }

    public static KeyLookup getInstance() {
        return instance;
    }
}