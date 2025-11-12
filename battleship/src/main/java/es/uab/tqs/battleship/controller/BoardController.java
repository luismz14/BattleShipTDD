package es.uab.tqs.battleship.controller;

import es.uab.tqs.battleship.model.Board;
import es.uab.tqs.battleship.model.Cell;
import es.uab.tqs.battleship.model.Coordinate;
import es.uab.tqs.battleship.model.Orientation;
import es.uab.tqs.battleship.model.Ship;
import es.uab.tqs.battleship.model.ShipType;
import es.uab.tqs.battleship.view.GameView;

public class BoardController {
    private final GameView view;

    public BoardController(GameView view) {
        this.view = view;
    }

    public void setupPlayerShips(Board board) {
        ShipType[] shipTypes = ShipType.values();

        for (ShipType type : shipTypes) {
            boolean placed = false;

            while (!placed) {
                view.displayMessage("\nCurrent board:");
                view.displayBoard(board, false);

                view.displayMessage("\nPlace your " + type.getDisplayName() 
                    + " (length: " + type.getLength() + ")");

                Coordinate start = view.getCoordinateInput("Initial coordinate");
                Orientation orientation = getOrientationInput();

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

    private Orientation getOrientationInput() {
        while (true) {
            view.displayMessage("Orientation (H=horizontal, V=vertical): ");
            String input = System.console() != null 
                ? System.console().readLine() 
                : new java.util.Scanner(System.in).nextLine();

            input = input.trim().toUpperCase();

            if (input.equals("H") || input.equals("HORIZONTAL")) {
                return Orientation.HORIZONTAL;
            } else if (input.equals("V") || input.equals("VERTICAL")) {
                return Orientation.VERTICAL;
            } else {
                view.displayMessage("Invalid entry. Use H o V.");
            }
        }
    }

    public boolean attemptPlaceShip(Board board, Ship ship, 
                                   Coordinate start, Orientation orientation) {
        if (!board.isValidPlacement(ship, start, orientation)) {
            return false;
        }

        return board.placeShip(ship, start, orientation);
    }

    public boolean isValidAttack(Board board, Coordinate coordinate) {
        if (!coordinate.isValid(board.getSize())) {
            return false;
        }

        Cell cell = board.getCell(coordinate);
        return !cell.isAlreadyAttacked();
    }

    public int getRemainingShips(Board board) {
        int remaining = 0;

        for (Ship ship : board.getShips()) {
            if (!ship.isSunk()) {
                remaining++;
            }
        }

        return remaining;
    }

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
