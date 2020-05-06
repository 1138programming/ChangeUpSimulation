package main.game.display;

import java.awt.Canvas;
import javax.swing.JFrame;
import java.awt.Dimension;

public class DisplayWindow extends Canvas {
    DisplayWindow(Dimension size, String title, DisplayFrame displayFrame) {
        JFrame frame = new JFrame(title);

        frame.setPreferredSize(size);
        frame.setMaximumSize(size);
        frame.setMinimumSize(size);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.add(displayFrame);
        frame.setVisible(true);
    }
}