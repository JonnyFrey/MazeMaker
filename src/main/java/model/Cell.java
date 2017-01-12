package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jonny on 1/11/2016.
 */
public class Cell {

    private int row;
    private int column;

    private Map<Cell, Boolean> links;

    private Cell north;
    private Cell south;
    private Cell east;
    private Cell west;


    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        links = new HashMap();
    }

    //Actual Mehtods

    public Cell link(Cell cell, Boolean link) {
        links.put(cell, true);
        if (link) {
            cell.link(this, false);
        }
        return this;
    }

    public Cell unlink(Cell cell, Boolean unlink) {
        links.remove(cell);
        if (unlink) {
            cell.unlink(this, false);
        }
        return this;
    }

    public Set<Cell> getLinks() {
        return links.keySet();
    }

    public boolean isLinked(Cell cell) {
        Boolean result = links.get(cell);
        if (result == null) {
            return false;
        }
        return result.booleanValue();
    }

    public Cell[] neighbors() {
        int size = north != null ? 1 : 0;
        size += south != null ? 1 : 0;
        size += west != null ? 1 : 0;
        size += east != null ? 1 : 0;

        Cell[] list = new Cell[size];

        int count = 0;
        if (north != null) {
            list[count++] = north;
        }

        if (south != null) {
            list[count++] = south;
        }

        if (west != null) {
            list[count++] = west;
        }

        if (east != null) {
            list[count++] = east;
        }
        return list;
    }

    //Helper Methods

    public Cell linkNorth() {
        if (north == null) {
            return this;
        }
        return link(north, true);
    }

    public Cell linkSouth() {
        if (south == null) {
            return this;
        }
        return link(south, true);
    }

    public Cell linkWest() {
        if (west == null) {
            return this;
        }
        return link(west, true);
    }

    public Cell linkEast() {
        if (east == null) {
            return this;
        }
        return link(east, true);
    }

    public Cell unlinkNorth() {
        if (north == null) {
            return this;
        }
        return unlink(north, true);
    }

    public Cell unlinkSouth() {
        if (south == null) {
            return this;
        }
        return unlink(south, true);
    }

    public Cell unlinkWest() {
        if (west == null) {
            return this;
        }
        return unlink(west, true);
    }

    public Cell unlinkEast() {
        if (east == null) {
            return this;
        }
        return unlink(east, true);
    }

    public boolean isLinkedNorth() {
        return isLinked(north);
    }

    public boolean isLinkedSouth() {
        return isLinked(south);
    }

    public boolean isLinkedWest() {
        return isLinked(west);
    }

    public boolean isLinkedEast() {
        return isLinked(east);
    }


    //Setters and Getters

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Cell getNorth() {
        return north;
    }

    public void setNorth(Cell north) {
        this.north = north;
    }

    public Cell getSouth() {
        return south;
    }

    public void setSouth(Cell south) {
        this.south = south;
    }

    public Cell getEast() {
        return east;
    }

    public void setEast(Cell east) {
        this.east = east;
    }

    public Cell getWest() {
        return west;
    }

    public void setWest(Cell west) {
        this.west = west;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "row=" + row +
                ", column=" + column +
                ", north=" + (north != null) +
                ", south=" + (south != null) +
                ", east=" + (east != null) +
                ", west=" + (west != null) +
                '}';
    }
}
