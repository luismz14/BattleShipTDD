package es.uab.tqs.battleship.model;

public class Boat {
    int initRow;
    int initCol;
    int size;
    boolean orientation; // true = horizontal, false = vertical

    public Boat(int initRow, int initCol, int size, boolean orientation) {
        if(initRow < 0 || initCol < 0 || size <= 0 || size > 5) {
            throw new IllegalArgumentException("Position must be non-negative and size must be between 1 and 5.");
        }
        this.initCol = initCol;
        this.initRow = initRow;
        this.size = size;
        this.orientation = orientation;
    }

    public int getInitRow() {
        return initRow;
    }

    public int getInitCol() {
        return initCol;
    }

    public int getSize() {
        return size;
    }

    public boolean isHorizontal() {
        return orientation;
    }

    public int getLastPos(){
        if(orientation) // horizontal
            return initCol + size - 1;
        else // vertical
            return initRow + size - 1;
    }
}