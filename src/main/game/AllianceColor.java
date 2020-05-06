package main.game;

import java.awt.Color;

public enum AllianceColor {
    BLUE(0), RED(1);

    public final Color javaColor;

    private AllianceColor(int id) {
        if (id == 0) {
            javaColor = Color.BLUE;
        } else {
            javaColor = Color.RED;
        }
    }
}