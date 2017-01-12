package generator;

import model.Cell;

/**
 * Created by Jonny on 1/11/2016.
 */
public class BinaryTree extends Algorithm {

    public BinaryTree(int size) {
        super(size);
    }

    @Override
    public void init() {
        count++;
    }

    @Override
    public void update() {
        int x = count / maze.getColumns();
        int y = count % maze.getColumns();

        Cell cell = maze.getCell(x, y);

        if (x == 0) {
            cell.linkWest();
        } else if (y == 0) {
            cell.linkNorth();
        } else {
            if (coinFlip()) {
                cell.linkNorth();
            } else {
                cell.linkWest();
            }
        }

    }

    @Override
    public boolean complete() {
        return count == maze.size();
    }
}
