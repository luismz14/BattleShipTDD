package es.uab.tqs.battleship.model;

/**
 * Represents a single cell on the game board.
 * A cell has a specific coordinate and can be in different states (EMPTY, SHIP,
 * HIT, MISS).
 * It may also hold a reference to a ship if one is placed on it.
 */
public class Cell {

    private final Coordinate coordinate;
    private CellState state;
    private Ship ship;

    /**
     * Constructs a new Cell with the given coordinate.
     * The cell is initially empty with no ship.
     *
     * @param coordinate The coordinate of the cell.
     */
    public Cell(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.state = CellState.EMPTY;
        this.ship = null;
    }

    /**
     * Gets the current state of the cell.
     *
     * @return The state of the cell.
     */
    public CellState getState() {
        return state;
    }

    /**
     * Sets the state of the cell.
     * This is typically used internally or during testing to force a specific
     * state.
     *
     * @param state The new state of the cell.
     */
    public void setState(CellState state) {
        this.state = state;
    }

    /**
     * Gets the coordinate of the cell.
     *
     * @return The coordinate.
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Checks if the cell contains a ship.
     *
     * @return true if a ship is present; false otherwise.
     */
    public boolean hasShip() {
        return ship != null;
    }

    /**
     * Places a ship in this cell.
     * If a ship is placed, the cell's state is updated to SHIP.
     *
     * @param ship The ship to place in this cell.
     */
    public void setShip(Ship ship) {
        this.ship = ship;
        if (ship != null) {
            this.state = CellState.SHIP;
        }
    }

    /**
     * Retrieves the ship occupying this cell.
     *
     * @return The ship object, or null if no ship is present.
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * Processes an attack on this cell.
     * Updates the cell's state to HIT or MISS based on whether a ship is present.
     * If a ship is hit, the hit is registered on the ship object.
     *
     * @return true if the attack was a hit; false if it was a miss.
     */
    public boolean attack() {
        if (hasShip()) {
            state = CellState.HIT;
            ship.registerHit();
            return true;
        } else {
            // No ship present, so it's a miss (water)
            state = CellState.MISS;
            return false;
        }
    }

    /**
     * Checks if this cell has already been attacked.
     * A cell is considered attacked if its state is either HIT or MISS.
     *
     * @return true if the cell has been attacked; false otherwise.
     */
    public boolean isAlreadyAttacked() {
        return state == CellState.HIT || state == CellState.MISS;
    }
}