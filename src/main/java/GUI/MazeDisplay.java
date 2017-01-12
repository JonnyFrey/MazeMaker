package GUI;

import GUI.drawing.DrawHandler;
import generator.Algorithm;
import model.Cell;
import model.Grid;
import utils.Distance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Created by Jonny on 1/11/2016.
 */
public class MazeDisplay extends JPanel implements Runnable, KeyListener {

    public static final int MAIN_SIZE = 640;

    private int cellSize;

    private Thread thread;
    private boolean running;
    private int FPS = 5;
    private long targetTime = 1000 / FPS;

    private BufferedImage image;
    private Graphics2D g;

    private Map<String, Algorithm> list;
    private Algorithm handler;
    private String current;
    private DrawHandler drawHandler;
    private Distance distance;
    private int fontSize;

    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private boolean bounded;
    private boolean shiftHeld;

    public MazeDisplay(java.util.Map<String, Algorithm> list, String current) {
        super();
        this.list = list;
        this.current = current;
        handler = list.get(current);
        setPreferredSize(new Dimension(MAIN_SIZE, MAIN_SIZE));
        startX = 0;
        startY = 0;
        endX = handler.getMaze().getRows() - 1;
        endY =handler.getMaze().getColumns() - 1;
        setBounded(true);
    }

    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }


    public void run() {
        init();

        long start;
        long time;
        long wait;

        while (running) {
            start = System.nanoTime();

            if (handler != null) {
                update();
                draw();
            } else {
                blankDraw();
            }
            drawToScreen();

            time = System.nanoTime() - start;

            wait = targetTime - time / 1000000;
            if (wait < 0)
                wait = 5;
            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        handler.apply();
        if (distance != null) {
            distance.update();
        }
    }


    private void draw() {


        drawHandler.draw(g, handler.getMaze());
        if (distance != null) {
            distance.drawPath(g, cellSize);
        }
        if (handler.isComplete()) {
            drawHandler.drawStartFinish(g, handler.getMaze(), startY, startX, endY, endX);
        }
        if (distance != null) {
            distance.drawDigits(g, cellSize, fontSize);
        }
    }

    private void blankDraw() {
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, MAIN_SIZE, MAIN_SIZE);
        g.setColor(Color.RED);
        g.drawString("NO ALGORITHM FOUND", MAIN_SIZE / 2, MAIN_SIZE / 2);
    }

    private void drawToScreen() {
        Graphics tempG = getGraphics();
        tempG.drawImage(image, 0, 0, MAIN_SIZE, MAIN_SIZE, null);
        tempG.dispose();
    }

    private void init() {
        image = new BufferedImage(MAIN_SIZE, MAIN_SIZE, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        running = true;
        updateDrawHandler();
    }

    public synchronized void updateDrawHandler() {
        cellSize = MAIN_SIZE / handler.getMaze().getRows();
        drawHandler = handler.getDrawHandler();
        drawHandler.setCellSize(cellSize);
        drawHandler.setMainSize(MAIN_SIZE);
        setEndPoints(false);
    }

    public synchronized void setSpeed(int speed) {
        this.FPS = speed;
        this.targetTime = 1000 / FPS;
    }

    public Algorithm getHandler() {
        return handler;
    }

    public void setHandler(Algorithm handler) {
        this.handler = handler;
    }

    public synchronized String getCurrent() {
        return current;
    }

    public synchronized MazeDisplay setCurrent(String current) {
        this.current = current;
        handler = list.get(current);
        return this;
    }

    public synchronized void solve(boolean b, boolean selected, boolean xray, boolean rainbow) {
        if (b) {
            Grid grid = handler.getMaze();
            distance = new Distance(grid, grid.getCell(startX, startY), grid.getCell(endX, endY), true);
            setNumbers(selected);
        } else if (xray) {
            Grid grid = handler.getMaze();
            distance = new Distance(grid, grid.getCell(startX, startY), grid.getCell(endX, endY), false);
            distance.setRainbow(rainbow);
            setNumbers(selected);
        } else {
            distance = null;
        }
    }

    public synchronized void setNumbers(boolean selected) {
        int size = handler.getMaze().size();
        if (!selected || size > 1024) {
            fontSize = 0;
        } else {
            if (size <= 16) {//4 //1
                fontSize = 25;
            } else if (size <= 64) {//8 //2
                fontSize = 20;
            } else if (size <= 256) {//16 //4
                fontSize = 15;
            } else if (size <= 1024) {//32 //8
                fontSize = 10;
            }
        }
    }
    public synchronized boolean isSolve() {
        return distance != null;
    }

    public synchronized void setEndPoints(boolean max) {
        Grid grid = handler.getMaze();
        if (max) {
            Distance distance = new Distance(grid, grid.getFirstCell());
            Cell startCell = distance.maxCell();
            if (startCell == null) {
                System.out.println("SOMTHING WHENT RWONG");
            }
            distance = new Distance(grid, startCell);
            Cell finalCell = distance.maxCell();
            if (startCell.getRow() < finalCell.getRow()) {
                setPoints(startCell, finalCell);
            } else {
                setPoints(finalCell, startCell);
            }
        } else {
            setPoints(grid.getFirstCell(), grid.getLastCell());
        }
    }

    private void setPoints(Cell start, Cell end) {
        if (start != null) {
            startX = start.getRow();
            startY = start.getColumn();
        }
        if (end != null) {
            endX = end.getRow();
            endY = end.getColumn();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        Grid grid = handler.getMaze();

        boolean shift = (e.getModifiers() & KeyEvent.SHIFT_MASK) != 0;

        int x;
        int y;

        if (shift) {
            x = endX;
            y = endY;
        } else {
            x = startX;
            y = startY;
        }

        Cell cell = grid.getCell(x, y);

        if (handler.isComplete()) {
            int startXMod = 0;
            int startYMod = 0;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (bounded && !cell.isLinkedNorth()) {
                        return;
                    }
                    startXMod = -1;
                    break;
                case KeyEvent.VK_DOWN:
                    if (bounded && !cell.isLinkedSouth()) {
                        return;
                    }
                    startXMod = 1;
                    break;
                case KeyEvent.VK_LEFT:
                    if (bounded && !cell.isLinkedWest()) {
                        return;
                    }
                    startYMod = -1;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (bounded && !cell.isLinkedEast()) {
                        return;
                    }
                    startYMod = 1;
                    break;
                default:
                    break;
            }

            if (startXMod == 0 && startYMod == 0) {
                return;
            }

            Cell startCell = grid.getCell(x + startXMod, y + startYMod);

            if (shift) {
                setPoints(null, startCell);
            } else {
                setPoints(startCell, null);
            }

            if (distance != null && startCell != null) {
                if (shift){
                    distance = distance.recalculate(null, startCell);
                } else {
                    distance = distance.recalculate(startCell, null);
                }
            }
        }
    }

    public void setBounded(boolean bounded) {
        this.bounded = bounded;
    }

    public void setFromTo(Color from, Color to) {
        if (distance != null) {
            distance.setColors(from, to);
        }
    }

    public void setRainbow(boolean rainbow) {
        if (distance != null) {
            distance.setRainbow(rainbow);
        }
    }
}
