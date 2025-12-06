package es.uab.tqs.battleship.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a ship on the game board.
 * A ship has a type, a set of coordinates it occupies, a hit count, and an
 * orientation.
 */
public class Ship {

    private final ShipType type;
    private final List<Coordinate> coordinates;
    private int hitCount;
    private Orientation orientation;

    /**
     * Constructs a new Ship of the specified type.
     * Initially, the ship has no position and zero hits.
     *
     * @param type The type of ship to create.
     */
    public Ship(ShipType type) {
        this.type = type;
        this.coordinates = new ArrayList<>();
        this.hitCount = 0;
        this.orientation = null;
    }

    /**
     * Gets the type of the ship.
     *
     * @return The ShipType.
     */
    public ShipType getType() {
        return type;
    }

    /**
     * Gets the length of the ship based on its type.
     *
     * @return The length of the ship (number of cells).
     */
    public int getLength() {
        return type.getLength();
    }

    /**
     * Returns a copy of the list of coordinates occupied by the ship.
     * Returns a new list to protect the internal state from modification.
     *
     * @return A list of coordinates.
     */
    public List<Coordinate> getCoordinates() {
        return new ArrayList<>(coordinates);
    }

    /**
     * Sets the position of the ship on the board based on a starting coordinate and
     * orientation.
     * Calculates all occupied coordinates based on the ship's length.
     *
     * @param startCoordinate The starting coordinate (bow of the ship).
     * @param orientation     The orientation (HORIZONTAL or VERTICAL).
     */
    public void setPosition(Coordinate startCoordinate, Orientation orientation) {
        this.orientation = orientation;
        this.coordinates.clear();

        for (int i = 0; i < getLength(); i++) {
            int x = startCoordinate.getX();
            int y = startCoordinate.getY();

            if (orientation == Orientation.HORIZONTAL) {
                x += i;
            } else {
                y += i;
            }

            coordinates.add(new Coordinate(x, y));
        }
    }

    /**
     * Gets the orientation of the ship.
     *
     * @return The orientation (HORIZONTAL or VERTICAL), or null if not placed.
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Registers a hit on the ship.
     * Increments the hit count.
     */
    public void registerHit() {
        hitCount++;
    }

    /**
     * Gets the current number of hits sustained by the ship.
     *
     * @return The hit count.
     */
    public int getHitCount() {
        return hitCount;
    }

    /**
     * Checks if the ship is sunk.
     * A ship is sunk if the number of hits equals or exceeds its length.
     *
     * @return true if the ship is sunk; false otherwise.
     */
    public boolean isSunk() {
        return hitCount >= getLength();
    }

    /**
     * Checks if the ship occupies a specific coordinate.
     *
     * @param coordinate The coordinate to check.
     * @return true if the ship occupies the given coordinate; false otherwise.
     */
    public boolean occupiesCoordinate(Coordinate coordinate) {
        for (Coordinate coord : coordinates) {
            if (coord.equals(coordinate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a string representation of the ship.
     * Useful for debugging or displaying ship info.
     *
     * @return A string describing the ship (e.g., "Carrier (5 cells)").
     */
    @Override
    public String toString() {
        return type.getDisplayName() + " (" + getLength() + " cells)";
    }
}