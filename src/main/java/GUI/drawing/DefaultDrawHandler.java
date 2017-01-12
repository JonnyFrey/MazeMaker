package GUI.drawing;

import GUI.MazeDisplay;
import model.Cell;
import model.Grid;

import java.awt.*;

/**
 * Created by Jonny on 1/14/2016.
 */
public class DefaultDrawHandler extends DrawHandler {

    @Override
    public void draw(Graphics2D g, Grid maze) {
        background(g, Color.WHITE);
        g.setColor(Color.BLACK);
        int rows = maze.getRows();
        int columns = maze.getColumns();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                //draw north if link not i = 0
                Cell cell = maze.getCell(i, j);
                if (cell == null) {
                    continue;
                }
                if (cell.getNorth() == null) {
                    drawNorthBorder(g, i, j);
                } else {
                    if (!cell.isLinkedNorth()) {
                        drawNorthBorder(g, i, j);
                    }
                }
                if (cell.getWest() == null) {
                    drawWestBorder(g, i, j);
                } else {
                    if (!cell.isLinkedWest()) {
                        drawWestBorder(g, i, j);
                    }
                }
            }
        }
        drawOtherBorder(g);
    }


    public void background(Graphics2D g, Color c) {
        g.setColor(c);
        g.fillRect(0, 0, MazeDisplay.MAIN_SIZE, MazeDisplay.MAIN_SIZE);
    }

}
