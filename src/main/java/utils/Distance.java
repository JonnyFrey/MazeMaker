package utils;

import model.Cell;
import model.Grid;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by jonnyfrey on 1/14/16.
 */
public class Distance {

    private Map<Cell, Integer> cells;
    private List<Cell> breadCrumbs;
    private List<Cell> path;
    private Grid maze;
    private Cell root;
    private Cell goal;
    private boolean complete;
    private boolean noValidPath;
    private boolean solve;
    private int maxDistance;
    private ColorTracker tracker;

    public Distance(Grid maze, Cell root) {
        this.maze = maze;
        cells = new HashMap();
        breadCrumbs = new LinkedList();
        cells.put(root, 0);
        breadCrumbs.add(root);
        this.solve = true;
        this.tracker = new ColorTracker(Color.RED, Color.BLUE);
    }

    public Distance(Grid maze, Cell root, Cell goal, boolean solve) {
        this.maze = maze;
        this.root = root;
        this.goal = goal;
        cells = new HashMap();
        breadCrumbs = new LinkedList();
        path = new LinkedList();
        cells.put(root, 0);
        path.add(goal);
        breadCrumbs.add(root);
        noValidPath = false;
        complete = false;
        this.solve = solve;
        this.tracker = new ColorTracker(Color.RED, Color.BLUE);
    }

    public Distance(Grid maze, Cell root, Cell goal, boolean solve, ColorTracker tracker) {
        this(maze, root, goal, solve);
        this.tracker = tracker;
    }

    public Distance recalculate(Cell root, Cell goal) {
        Cell newRoot = root;
        Cell newGoal = goal;
        if (root == null) {
            newRoot = this.root;
        }

        if (goal == null) {
            newGoal = this.goal;
        }

        return new Distance(this.maze, newRoot, newGoal, this.solve, this.tracker);
    }

    public void update() {
        if (complete || noValidPath) {
            return;
        }
        populateMap();
        populatePath();
        this.maxDistance = maxDistance();
        this.tracker.setMaxDistance(this.maxDistance);
    }

    private void populateMap() {
        while (breadCrumbs.size() != 0) {
            List<Cell> temp = new LinkedList();
            for (int i = 0; i < breadCrumbs.size(); i++) {
                Cell point = breadCrumbs.get(i);
                Cell[] neighbors = point.neighbors();

                for (int j = 0; j < neighbors.length; j++) {
                    Cell neighborPoint = neighbors[j];
                    if (!cells.containsKey(neighborPoint) && point.isLinked(neighborPoint)) {
                        cells.put(neighborPoint, cells.get(point) + 1);
                        temp.add(neighborPoint);
                    }
                }
                breadCrumbs = temp;
            }
        }
    }

    private void populatePath() {
        while (!complete && !noValidPath) {
            Cell current = path.get(path.size() - 1);
            Integer targetValue = cells.get(current);
            if (targetValue == null) {
                noValidPath = true;
            } else {
                targetValue--;
                Cell[] neighbors = current.neighbors();
                noValidPath = true;
                for (int i = 0; i < neighbors.length; i++) {
                    Cell neighborPoint = neighbors[i];
                    if (current.isLinked(neighborPoint) && cells.get(neighborPoint).intValue() == targetValue.intValue()) {
                        path.add(neighborPoint);
                        if (neighborPoint == root) {
                            complete = true;
                        }
                        noValidPath = false;
                    }
                }
            }
        }
    }

    public void drawDigits(Graphics2D g, int cellSize, int fontSize) {
        if (fontSize == 0) {
            return;
        }
        g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
        g.setColor(Color.BLUE);
        for (Cell cell : cells.keySet()) {
            String text = "" + cells.get(cell);
            int width = g.getFontMetrics().stringWidth(text);
            int height = g.getFontMetrics().getHeight();
            g.drawString(text, cell.getColumn() * cellSize + (cellSize / 2) - width / 2, cell.getRow() * cellSize + (cellSize / 2) + height / 4);
        }
        g.setColor(null);
    }

    public void drawPath(Graphics2D g, int cellSize) {
        if (!complete || noValidPath) {
            return;
        }
        if (solve) {
            drawStack(Color.PINK, g, path, cellSize, 2);
        } else {
            drawFlood(g, cellSize, 2);
        }
    }

    private void drawFlood(Graphics2D g, int cellSize, int padding) {
        List<Cell> allCells = this.maze.getAllCells();
        for (Cell cell : allCells) {
            int x = cell.getColumn() * cellSize + padding / 2;
            int y = cell.getRow() * cellSize + padding / 2;
            g.setColor(tracker.getColorFromFloat(this.cells.getOrDefault(cell, 1)));
            g.fillRect(x, y, cellSize - padding, cellSize - padding);
            if (cell.isLinkedNorth()) {
                myNorthBorder(g, cell.getRow(), cell.getColumn(), cellSize, padding);
            }
            if (cell.isLinkedSouth()) {
                myNorthBorder(g, cell.getRow() + 1, cell.getColumn(), cellSize, padding);
            }
            if (cell.isLinkedWest()) {
                myWestBorder(g, cell.getRow(), cell.getColumn(), cellSize, padding);
            }
            if (cell.isLinkedEast()) {
                myWestBorder(g, cell.getRow(), cell.getColumn() + 1, cellSize, padding);
            }
        }
    }

    private void drawStack(Color c, Graphics2D g, List<Cell> list, int cellSize, int padding) {
        if (list == null) {
            return;
        }
        g.setColor(c);
        for (Cell cell : list) {
            int x = cell.getColumn() * cellSize + padding / 2;
            int y = cell.getRow() * cellSize + padding / 2;
            g.fillRect(x, y, cellSize - padding, cellSize - padding);
            if (cell.isLinkedNorth()) {
                myNorthBorder(g, cell.getRow(), cell.getColumn(), cellSize, padding);
            }
            if (cell.isLinkedSouth()) {
                myNorthBorder(g, cell.getRow() + 1, cell.getColumn(), cellSize, padding);
            }
            if (cell.isLinkedWest()) {
                myWestBorder(g, cell.getRow(), cell.getColumn(), cellSize, padding);
            }
            if (cell.isLinkedEast()) {
                myWestBorder(g, cell.getRow(), cell.getColumn() + 1, cellSize, padding);
            }
        }
    }

    private void myNorthBorder(Graphics2D g, int i, int j, int cellSize, int padding) {
        g.setStroke(new BasicStroke(padding));
        int x = j * cellSize + (padding);
        int y = i * cellSize;
        int x1 = (j + 1) * cellSize - (padding);
        g.drawLine(x, y, x1, y);
    }

    private void myWestBorder(Graphics2D g, int i, int j, int cellSize, int padding) {
        g.setStroke(new BasicStroke(padding));
        int x = j * cellSize;
        int y = i * cellSize + (padding);
        int y1 = (i + 1) * cellSize - (padding);
        g.drawLine(x, y, x, y1);
    }

    public Map<Cell, Integer> getCells() {
        return cells;
    }

    public Cell maxCell() {
        int maxDistance = 0;
        Cell maxCell = root;
        populateMap();
        for (Cell cell : cells.keySet()) {
            if (cells.get(cell) > maxDistance) {
                maxCell = cell;
                maxDistance = cells.get(cell);
            }
        }
        return maxCell;
    }

    public int maxDistance() {
        int maxDistance = 0;
        populateMap();
        for (Cell cell : cells.keySet()) {
            if (cells.get(cell) > maxDistance) {
                maxDistance = cells.get(cell);
            }
        }
        return maxDistance;
    }

    public void setColors(Color from, Color to) {
        tracker.setFrom(from);
        tracker.setTo(to);
    }

    public void setRainbow(boolean rainbow) {
        tracker.setRainbow(rainbow);
    }
}
