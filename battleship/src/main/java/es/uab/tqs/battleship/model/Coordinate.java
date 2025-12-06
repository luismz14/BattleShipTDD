package es.uab.tqs.battleship.model;

import java.util.Objects;

public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** 
     * @param boardSize
     * @return boolean
     */
    public boolean isValid(int boardSize) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    /** 
     * @return int
     */
    public int getX() {
        return x;
    }

    /** 
     * @return int
     */
    public int getY() {
        return y;
    }

    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

