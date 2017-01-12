package generator;

import GUI.drawing.RBTDrawHandler;
import model.Cell;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jonny on 1/14/2016.
 */
public class RecursiveBackTracking extends Algorithm {

    private List<Cell> used;
    private List<Cell> stack;

    public RecursiveBackTracking(int size) {
        super(size);
    }

    @Override
    public void init() {
        stack = new LinkedList<>();
        used = new LinkedList<>();
        count = 0;
    }

    @Override
    public void update() {
        if (count == 0) {
            Cell start = maze.randomCell();
            stack.add(start);
            return;
        }

        Cell point = stack.get(stack.size() - 1);
        Cell link = validRandCell(point);
        if (link != null) {
            point.link(link, true);
            stack.add(link);
        } else {
            stack.remove(point);
            used.add(point);
        }
    }

    @Override
    public boolean complete() {
        return stack.isEmpty();
    }

    @Override
    public void createDrawHandler() {
        setDrawHandler(new RBTDrawHandler(this));
    }

    private Cell validRandCell(Cell origin) {
        List<Cell> neighbors = Arrays.asList(origin.neighbors());
        Collections.shuffle(neighbors);
        int index = 0;
        while (index < neighbors.size()) {
            Cell test = neighbors.get(index);
            if (test.getLinks().isEmpty())
                return test;
            index++;
        }
        return null;
    }

    public List<Cell> getStack() {
        return stack;
    }

    public List<Cell> getUsed() {
        return used;
    }
}
