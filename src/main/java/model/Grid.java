package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonny on 1/11/2016.
 */
public class Grid {

    public boolean complete;

    private int rows;
    private int columns;

    private Cell[][] grid;

    public Grid() {

    }

    public Grid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        init();
    }

    public Cell randomCell() {
        int row = (int) (Math.random() * rows);
        int column = (int) (Math.random() * columns);
        return getCell(row, column);
    }

    public int size() {
        return rows * columns;
    }

    private void configureCells() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = grid[i][j];
                int row = cell.getRow();
                int column = cell.getColumn();
                cell.setNorth(getCell(row - 1, column));
                cell.setSouth(getCell(row + 1, column));
                cell.setWest(getCell(row, column - 1));
                cell.setEast(getCell(row, column + 1));
            }
        }
    }

    public Cell getCell(int row, int column) {
        if (row < 0 || row >= rows) {
            return null;
        }
        if (column < 0 || column >= columns) {
            return null;
        }
        return grid[row][column];
    }

    public Cell getFirstCell() {
        return getCell(0, 0);
    }

    public Cell getLastCell() {
        return getCell(rows - 1, columns - 1);
    }

    private void prepareGrid() {
        grid = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    public void init() {
        prepareGrid();
        configureCells();
    }

    public boolean isComplete() {
        return complete;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public List<Cell> getAllCells() {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells.add(grid[i][j]);
            }
        }
        return cells;
    }
}
