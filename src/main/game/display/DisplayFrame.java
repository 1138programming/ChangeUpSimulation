package main.game.display;

import java.awt.Canvas;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.Dimension;

import javax.swing.JFrame;

public class DisplayFrame extends Canvas implements Runnable {
    public static final int WIDTH = 1000, HEIGHT = WIDTH / 4 * 3;
    public static final int FPS = 60;
    
    private Thread thread;
    private boolean running = false;
    private boolean debugMode = false;

    private Display display;
    private DisplayWindow displayWindow;

    private Dimension size;

    public DisplayFrame(Display display, boolean debugMode) {
        this.display = display;
        this.debugMode = debugMode;

        size = new Dimension(WIDTH, HEIGHT);
        displayWindow = new DisplayWindow(size, display.getName(), this);
    }

    public DisplayFrame(Display display) {
        this(display, false);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        long tickNow = lastTime;
        double amountOfTicks = (double)FPS;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        double interval = 1000000000 / (double)FPS;
        System.out.printf("%f\n", interval);
        while (running) {
            long now = System.nanoTime();

            if (interval > (now - lastTime)) {
                continue;
            }

            delta += (now - lastTime) / ns;
            lastTime = now;
            
            while(delta >= 1) {
                tick(System.nanoTime() - tickNow);
                tickNow = System.nanoTime();
                delta--;
            }

            if (running) {
                render();
            }
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                
                if (debugMode) {
                    System.out.println("FPS: " + frames);
                }

                frames = 0;
            }
        }
        stop();
    }

    private void tick(long dt) {
        display.tick(dt);
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();

        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        display.render(g);

        g.dispose();
        bs.show();
    }

    public void forceRender() {
        render();
    }
}