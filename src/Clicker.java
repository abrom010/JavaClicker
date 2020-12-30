import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class Clicker implements NativeMouseInputListener { // upper button 5, lower 4
    Robot robot = new Robot();
    public boolean enabled = false;

    public Clicker() throws AWTException {
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            System.err.println("Problem registering native hook");
            System.err.println(e.getMessage());
            System.exit(1);
        }

        NativeMouseInputListener listener = this;
        GlobalScreen.addNativeMouseListener(listener);
    }

    class SpamRunnable implements Runnable {
        boolean exit = false;

        @Override
        public void run() {
            while(!exit) {
                try {
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    Thread.sleep(50);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    cease();
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }

        public void cease() {
            Thread.currentThread().interrupt();
            exit = true;
        }
    }

    class HoldRunnable implements Runnable {
        boolean exit = false;

        @Override
        public void run() {
            try {
                Thread.sleep(0);
                robot.mousePress(InputEvent.BUTTON1_MASK);
                while(!exit);
            } catch (InterruptedException e) {
                cease();
                e.printStackTrace();
                System.exit(0);
            }
        }

        public void cease() {
            exit = true;
        }
    }

    class FishRunnable implements Runnable {
        boolean exit = false;

        @Override
        public void run() {
            try {
                while (!exit) {
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    Thread.sleep(10);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    Thread.sleep(5000);
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    Thread.sleep(10);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                }
            } catch(InterruptedException e) {
                cease();
                e.printStackTrace();
                System.exit(0);
            }
        }

        public void cease() {
            exit = true;
        }
    }

    Thread spamThread = new Thread(new SpamRunnable());
    Thread holdThread = new Thread(new HoldRunnable());
    Thread fishThread = new Thread(new FishRunnable());

    boolean running = false;

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        // NOTHING
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        if(!enabled) {
            spamThread.stop();
            holdThread.stop();
            fishThread.stop();
            running = false;
            return;
        }
        if(e.getButton() == NativeMouseEvent.BUTTON5) { // SPAM
            if(running) {
                spamThread.stop();
                running = false;
                releaseMouse();
            } else {
                running = true;
                spamThread = new Thread(new SpamRunnable());
                spamThread.start();
            }
        } else if(e.getButton() == NativeMouseEvent.BUTTON4) { // HOLD
            if(running) {
                holdThread.stop();
                running = false;
                releaseMouse();
            } else {
                running = true;
                holdThread = new Thread(new HoldRunnable());
                holdThread.start();
            }
        } else if(e.getButton() == NativeMouseEvent.BUTTON3) { // FISH
            if(running) {
                fishThread.stop();
                running = false;
                releaseMouse();
            } else {
                running = true;
                fishThread = new Thread(new FishRunnable());
                fishThread.start();
            }
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        // NOTHING
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        // NOTHING
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
        // NOTHING
    }

    void releaseMouse() {
        try {
            Robot robot = new Robot();
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
