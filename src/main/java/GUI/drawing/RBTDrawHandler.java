package GUI.drawing;

import generator.RecursiveBackTracking;
import model.Cell;
import model.Grid;

import java.awt.*;
import java.util.List;


/**
 * Created by Jonny on 1/14/2016.
 */
public class RBTDrawHandler extends DefaultDrawHandler {

    private RecursiveBackTracking algorithm;

    public RBTDrawHandler(RecursiveBackTracking algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void draw(Graphics2D g, Grid maze) {
        if (!algorithm.isLoop()) {
            g.setColor(Color.GRAY);
            super.draw(g, maze);
            drawStack(Color.PINK, g, algorithm.getStack());
            drawStack(Color.WHITE, g, algorithm.getUsed());
            g.setColor(null);
        }
    }

    private void drawStack(Color c, Graphics2D g, List<Cell> list) {
        if (list == null) {
            return;
        }
        g.setColor(c);
        for (Cell cell : list) {
            int x = cell.getColumn() * cellSize + padding / 2;
            int y = cell.getRow() * cellSize + padding / 2;
            g.fillRect(x, y, cellSize - padding, cellSize - padding);
            if (cell.isLinkedNorth()) {
                myNorthBorder(g, cell.getRow(), cell.getColumn());
            }
            if (cell.isLinkedSouth()) {
                myNorthBorder(g, cell.getRow() + 1, cell.getColumn());
            }
            if (cell.isLinkedWest()) {
                myWestBorder(g, cell.getRow(), cell.getColumn());
            }
            if (cell.isLinkedEast()) {
                myWestBorder(g, cell.getRow(), cell.getColumn() + 1);
            }
        }
    }

    @Override
    public void background(Graphics2D g, Color c) {
        super.background(g, Color.GRAY);
    }

    public void myNorthBorder(Graphics2D g, int i, int j) {
        g.setStroke(new BasicStroke(padding * 2));
        int x = j * cellSize + padding * 3 / 2;
        int y = i * cellSize + padding / 2;
        int x1 = (j + 1) * cellSize - padding * 3 / 2;
        g.drawLine(x, y, x1, y);
    }

    public void myWestBorder(Graphics2D g, int i, int j) {
        g.setStroke(new BasicStroke(padding * 2));
        int x = j * cellSize + padding / 2;
        int y = i * cellSize + padding * 3 / 2;
        int y1 = (i + 1) * cellSize - padding * 3 / 2;
        g.drawLine(x, y, x, y1);
    }


}
