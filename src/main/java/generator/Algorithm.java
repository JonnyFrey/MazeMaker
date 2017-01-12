package generator;

import GUI.drawing.DefaultDrawHandler;
import GUI.drawing.DrawHandler;
import model.Grid;

/**
 * Created by Jonny on 1/11/2016.
 */
public abstract class Algorithm {

    protected Grid maze;
    protected int count;

    private boolean started;
    private boolean complete;
    private boolean updateInstance;
    private boolean updateTillComplete;

    private DrawHandler drawHandler;
    private double startTime;
    private boolean loop = false;

    public Algorithm(int size) {
        generateGrid(size);
        complete = false;
        started = false;
        updateInstance = false;
        updateTillComplete = false;
        count = 0;
        createDrawHandler();
        init();
    }

    public void generateGrid(int size) {
        maze = new Grid(size, size);
    }

    public Grid getMaze() {
        return maze;
    }

    public abstract void init();

    public abstract void update();

    public abstract boolean complete();

    public boolean apply() {
        //Check ability conditions
        if (complete || maze == null) {
            return false;
        }
        //Check update conditions
        if (!updateInstance && !updateTillComplete) {
            return false;
        }

        //Update
        update();
        count++;
        //Check complete conditions
        complete = complete();

        if (complete && updateTillComplete) {
            System.out.format(toString() + " took: %.3f secs", ((System.nanoTime() - startTime) / 1000000000));
            System.out.println();
        }

        //Reset instances
        if (updateInstance) {
            updateInstance = false;
        }

        return true;
    }

    public synchronized void step() {
        updateInstance = true;
    }

    public synchronized void run() {
        updateTillComplete = true;
        startTime = System.nanoTime();
    }

    public synchronized void stop() {
        updateTillComplete = false;
    }

    public synchronized void reset() {
        complete = updateInstance = updateTillComplete = started = false;
        count = 0;
        maze.init();
        init();
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isStarted() {
        return started;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " - " + maze.getRows() + "x" + maze.getColumns();
    }

    public DrawHandler getDrawHandler() {
        return drawHandler;
    }

    public void setDrawHandler(DrawHandler drawHandler) {
        this.drawHandler = drawHandler;
    }

    public void createDrawHandler() {
        setDrawHandler(new DefaultDrawHandler());
    }

    //Helper Method
    public boolean coinFlip() {
        return (int) (Math.random() * 2) == 0;
    }

    public int randomValue(int from, int to) {
        return (int) (Math.random() * (to - from + 1)) + from;
    }

    public void finish() {
        loop = true;
        updateTillComplete = true;
        while (loop) {
            loop = apply();
        }
        loop = false;
    }

    public boolean isLoop() {
        return loop;
    }
}
