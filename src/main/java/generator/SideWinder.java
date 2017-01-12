package generator;

import model.Cell;

/**
 * Created by Jonny on 1/13/2016.
 */
public class SideWinder extends Algorithm {

    private int run;

    public SideWinder(int size) {
        super(size);
    }

    @Override
    public void init() {
        run = 0;
    }

    @Override
    public void update() {
        int x = count / maze.getColumns();
        int y = count % maze.getColumns();

        Cell cell = maze.getCell(x, y);

        if (x == (maze.getRows() - 1)) {
            cell.linkEast();
        } else {
            if (y == (maze.getColumns() - 1)) {
                chooseRandomCellFromRun(cell).linkSouth();
            } else if (coinFlip()) {
                chooseRandomCellFromRun(cell).linkSouth();
            } else {
                cell.linkEast();
                run++;
            }
        }
    }

    private Cell chooseRandomCellFromRun(Cell origin) {
        int winner = (int) (Math.random() * (run + 1));
        Cell cell = maze.getCell(origin.getRow(), origin.getColumn() - winner);
        run = 0;
        return cell;
    }

    @Override
    public boolean complete() {
        return count == maze.size();
    }
}
