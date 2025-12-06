package es.uab.tqs.battleship.model;

import java.util.Objects;

/**
 * Represents a 2D coordinate on the game board.
 * This class is immutable and is used to identify positions for cells and
 * ships.
 */
public class Coordinate {
    private final int x;
    private final int y;

    /**
     * Constructs a new Coordinate with the specified position.
     *
     * @param x The horizontal position.
     * @param y The vertical position.
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Validates if the coordinate lies within the boundaries of a square board.
     *
     * @param boardSize The size of the board (e.g., 10 for a 10x10 grid).
     * @return true if the coordinate is >= 0 and < boardSize for both axes; false
     *         otherwise.
     */
    public boolean isValid(int boardSize) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    /**
     * Gets the X-coordinate.
     *
     * @return The x value.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Y-coordinate.
     *
     * @return The y value.
     */
    public int getY() {
        return y;
    }

    /**
     * Compares this coordinate with another object for equality.
     * Two coordinates are considered equal if they share the same x and y values.
     *
     * @param o The object to compare with.
     * @return true if the objects are the same or represent the same position.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Coordinate))
            return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    /**
     * Generates a hash code for this coordinate.
     * Required for using Coordinate as a key in HashMaps or inside HashSets.
     *
     * @return The hash code based on x and y.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Returns a string representation of this coordinate.
     * The format is "(x, y)".
     *
     * @return A string representation of the coordinate.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
