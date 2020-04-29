package main;

import main.display.Display;
import java.awt.Dimension;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Display {
    public static final String name = "Main";
    public static final Color bgColor = Color.BLACK;

    public static void main(String[] args) {
        boolean debugMode = false;

        if (args.length > 0) {
            debugMode = args[0].equals("-d");
        }

        new Main(debugMode);
    }

    Main(boolean debugMode) {
        super(debugMode);

        start();
    }

    Main() {
        this(false);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Color getBgColor() {
        return bgColor;
    }
}