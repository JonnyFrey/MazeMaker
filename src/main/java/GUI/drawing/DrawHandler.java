package GUI.drawing;

import model.Grid;

import java.awt.*;

/**
 * Created by Jonny on 1/12/2016.
 */
public abstract class DrawHandler {
    protected static int padding = 2;
    protected int cellSize;
    protected int mainSize;

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public void setMainSize(int mainSize) {
        this.mainSize = mainSize - 1;
    }

    public void drawNorthBorder(Graphics2D g, int i, int j) {
        g.setStroke(new BasicStroke(padding));
        int x = j * cellSize;
        int y = i * cellSize;
        int x1 = (j + 1) * cellSize;
        g.drawLine(x, y, x1, y);
    }

    public void drawWestBorder(Graphics2D g, int i, int j) {
        g.setStroke(new BasicStroke(padding));
        int x = j * cellSize;
        int y = i * cellSize;
        int y1 = (i + 1) * cellSize;
        g.drawLine(x, y, x, y1);
    }

    public void drawOtherBorder(Graphics2D g) {
        g.setStroke(new BasicStroke(padding));
        g.drawLine(mainSize, 0, mainSize, mainSize);
        g.drawLine(0, mainSize, mainSize, mainSize);
    }

    public abstract void draw(Graphics2D g, Grid maze);

    public void drawStartFinish(Graphics2D g, Grid maze, int x1, int y1, int x2, int y2) {
        int size = (cellSize - padding) / 2;
        int offset = padding / 2 + size / 2;
        g.setColor(Color.GREEN);
        g.fillRect((x1 * cellSize) + offset, (y1 * cellSize) + offset, size, size);
        g.setColor(Color.RED);
        g.fillRect((x2 * cellSize) + offset, (y2 * cellSize) + offset, size, size);
        g.setColor(null);
    }
}
