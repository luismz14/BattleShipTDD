package es.uab.tqs.battleship.controller;

import es.uab.tqs.battleship.model.Board;
import es.uab.tqs.battleship.model.Cell;
import es.uab.tqs.battleship.model.Coordinate;
import es.uab.tqs.battleship.model.Orientation;
import es.uab.tqs.battleship.model.Ship;
import es.uab.tqs.battleship.model.ShipType;
import es.uab.tqs.battleship.view.GameView;

/**
 * Controls the interactions related to the game board.
 * This controller handles ship placement, validates attacks, and manages board
 * statistics.
 * It interacts with the Board model and the GameView to display information to
 * the user.
 */
public class BoardController {

    private final GameView view;

    /**
     * Constructs a new BoardController with the specified view.
     *
     * @param view The game view interface.
     */
    public BoardController(GameView view) {
        this.view = view;
    }

    /**
     * Orchestrates the setup phase where the player places all their ships on the
     * board.
     * Iterates through all available ship types and prompts the user for
     * coordinates and orientation.
     * Validates the placement and provides feedback (success or failure) to the
     * user.
     *
     * @param board The board where the ships will be placed.
     */
    public void setupPlayerShips(Board board) {
        ShipType[] shipTypes = ShipType.values();

        for (ShipType type : shipTypes) {
            boolean placed = false;

            // Loop until the current ship is successfully placed
            while (!placed) {
                view.displayMessage("\nCurrent board:");
                view.displayBoard(board, false);

                view.displayMessage("\nPlace your " + type.getDisplayName()
                        + " (length: " + type.getLength() + ")");

                // Get input from the user via the view
                Coordinate start = view.getCoordinateInput("Initial coordinate");
                Orientation orientation = view.getOrientationInput();

                Ship ship = new Ship(type);
                placed = attemptPlaceShip(board, ship, start, orientation);

                if (!placed) {
                    view.displayMessage("\nThe ship cannot be placed there. Try another position.");
                }
            }

            view.displayMessage("\n¡" + type.getDisplayName() + " successfully placed!\n");
        }

        view.displayMessage("\n¡All your ships are in place!\n");
    }

    /**
     * Attempts to place a ship on the board at the specified location.
     * Delegates the validation and placement logic to the Board model.
     *
     * @param board       The board to place the ship on.
     * @param ship        The ship object to be placed.
     * @param start       The starting coordinate for the ship.
     * @param orientation The orientation of the ship.
     * @return true if the ship was successfully placed; false otherwise.
     */
    public boolean attemptPlaceShip(Board board, Ship ship,
            Coordinate start, Orientation orientation) {

        if (!board.isValidPlacement(ship, start, orientation)) {
            return false;
        }

        return board.placeShip(ship, start, orientation);
    }

    /**
     * Validates if an attack on a specific coordinate is legal.
     * An attack is valid if the coordinate is within bounds and the cell has not
     * been attacked yet.
     *
     * @param board      The target board.
     * @param coordinate The coordinate to attack.
     * @return true if the attack is valid; false otherwise.
     */
    public boolean isValidAttack(Board board, Coordinate coordinate) {

        if (!coordinate.isValid(board.getSize())) {
            return false;
        }

        Cell cell = board.getCell(coordinate);
        return !cell.isAlreadyAttacked();
    }

    /**
     * Calculates the number of ships remaining (not sunk) on the board.
     *
     * @param board The board to check.
     * @return The count of active ships.
     */
    public int getRemainingShips(Board board) {
        int remaining = 0;

        for (Ship ship : board.getShips()) {
            if (!ship.isSunk()) {
                remaining++;
            }
        }

        return remaining;
    }

    /**
     * Displays statistical information about the board to the user.
     * Shows total ships, remaining active ships, and sunk ships.
     *
     * @param board The board to display stats for.
     */
    public void displayBoardStats(Board board) {
        int total = board.getShipCount();
        int remaining = getRemainingShips(board);
        int sunk = total - remaining;

        view.displayMessage("\nBoard stats:");
        view.displayMessage("  Total amount of ships: " + total);
        view.displayMessage("  Remaining ships: " + remaining);
        view.displayMessage("  Sunk ships: " + sunk);
    }
}