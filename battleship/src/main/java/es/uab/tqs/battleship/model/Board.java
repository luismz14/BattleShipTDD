package es.uab.tqs.battleship.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board consisting of a grid of cells.
 * The board manages the placement of ships and processing of attacks.
 */
public class Board {

    /**
     * A 2D array of cells representing the grid of the board.
     */
    private final Cell[][] cells;

    /**
     * A list of ships placed on the board.
     */
    private final List<Ship> ships;

    /**
     * The size of the board (e.g., 10 for a 10x10 grid).
     */
    private final int size;

    /**
     * Constructs a new Board with the specified size.
     * Initializes the grid with empty cells.
     *
     * @param size The dimension of the square board.
     */
    public Board(int size) {
        this.size = size;
        this.ships = new ArrayList<>();
        this.cells = new Cell[size][size];

        // Initialize each cell in the grid with its corresponding coordinate.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(new Coordinate(i, j));
            }
        }
    }

    /**
     * Gets the size of the board.
     *
     * @return The size of the board (width/height).
     */
    public int getSize() {
        return size;
    }

    /**
     * Retrieves a specific cell from the board using x and y coordinates.
     * Validates the coordinates before access.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The cell at the specified position.
     * @throws IllegalArgumentException if the coordinates are out of bounds.
     */
    public Cell getCell(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            throw new IllegalArgumentException("Invalid coordinates: (" + x + ", " + y + ")");
        }
        return cells[x][y];
    }

    /**
     * Retrieves a specific cell from the board using a Coordinate object.
     *
     * @param coordinate The coordinate of the cell.
     * @return The cell at the specified coordinate.
     */
    public Cell getCell(Coordinate coordinate) {
        return getCell(coordinate.getX(), coordinate.getY());
    }

    /**
     * Checks if a ship can be validly placed at the specified position and
     * orientation.
     * Ensures the ship fits within the board boundaries and does not overlap with
     * existing ships.
     *
     * @param ship        The ship to be placed.
     * @param start       The starting coordinate for the ship.
     * @param orientation The orientation of the ship (HORIZONTAL or VERTICAL).
     * @return true if the placement is valid; false otherwise.
     */
    public boolean isValidPlacement(Ship ship, Coordinate start, Orientation orientation) {
        int length = ship.getLength();
        int x = start.getX();
        int y = start.getY();

        // Check if the starting position is outside the board.
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return false;
        }

        // Iterate through each segment of the ship to check bounds and overlap.
        for (int i = 0; i < length; i++) {
            int currentX = x;
            int currentY = y;

            if (orientation == Orientation.HORIZONTAL) {
                currentX += i;
            } else {
                currentY += i;
            }

            // Check if the ship extends beyond the board boundaries.
            if (currentX >= size || currentY >= size) {
                return false;
            }

            // Check if the cell is already occupied by another ship.
            if (cells[currentX][currentY].hasShip()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Places a ship on the board at the specified position and orientation if
     * valid.
     * Updates the ship's internal state and the corresponding cells on the board.
     *
     * @param ship        The ship to place.
     * @param start       The starting coordinate.
     * @param orientation The orientation of the ship.
     * @return true if the ship was successfully placed; false if the placement was
     *         invalid.
     */
    public boolean placeShip(Ship ship, Coordinate start, Orientation orientation) {
        // First, validate the placement before making any changes.
        if (!isValidPlacement(ship, start, orientation)) {
            return false;
        }

        // Update the ship's position and orientation.
        ship.setPosition(start, orientation);

        // Mark the cells on the board as occupied by this ship.
        for (Coordinate coord : ship.getCoordinates()) {
            cells[coord.getX()][coord.getY()].setShip(ship);
        }

        ships.add(ship);
        return true;
    }

    /**
     * Processes an attack on a specific coordinate.
     * Handles logic for hits, misses, sinking ships, and repeated attacks.
     *
     * @param coordinate The target coordinate of the attack.
     * @return The result of the attack (HIT, MISS, SUNK, ALREADY_ATTACKED).
     * @throws IllegalArgumentException if the coordinate is invalid.
     */
    public AttackResult processAttack(Coordinate coordinate) {
        if (!coordinate.isValid(size)) {
            throw new IllegalArgumentException("Invalid coordinate: " + coordinate);
        }

        Cell cell = getCell(coordinate);

        if (cell.isAlreadyAttacked()) {
            return AttackResult.ALREADY_ATTACKED;
        }

        boolean hit = cell.attack();

        if (hit) {
            Ship ship = cell.getShip();
            if (ship.isSunk()) {
                return AttackResult.SUNK;
            }
            return AttackResult.HIT;
        } else {
            return AttackResult.MISS;
        }
    }

    /**
     * Checks if all ships on the board have been sunk.
     * This condition typically signals the end of the game.
     *
     * @return true if all ships are sunk or if there are no ships; false otherwise.
     */
    public boolean allShipsSunk() {
        if (ships.isEmpty()) {
            return false;
        }

        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a copy of the list of ships on the board.
     * Returns a new list to protect the internal ships list from modification.
     *
     * @return A list of ships.
     */
    public List<Ship> getShips() {
        return new ArrayList<>(ships);
    }

    /**
     * Gets the number of ships currently placed on the board.
     *
     * @return The count of ships.
     */
    public int getShipCount() {
        return ships.size();
    }
}
