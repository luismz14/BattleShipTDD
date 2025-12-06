package es.uab.tqs.battleship.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Cell[][] cells;
    private final List<Ship> ships;
    private final int size;

    public Board(int size) {
        this.size = size;
        this.ships = new ArrayList<>();
        this.cells = new Cell[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(new Coordinate(i, j));
            }
        }
    }

    /** 
     * @return int
     */
    public int getSize() {
        return size;
    }

    /** 
     * @param x
     * @param y
     * @return Cell
     */
    public Cell getCell(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            throw new IllegalArgumentException("Invalid coordinates: (" + x + ", " + y + ")");
        }
        return cells[x][y];
    }

    /** 
     * @param coordinate
     * @return Cell
     */
    public Cell getCell(Coordinate coordinate) {
        return getCell(coordinate.getX(), coordinate.getY());
    }

    /** 
     * @param ship
     * @param start
     * @param orientation
     * @return boolean
     */
    public boolean isValidPlacement(Ship ship, Coordinate start, Orientation orientation) {
        int length = ship.getLength();
        int x = start.getX();
        int y = start.getY();

        if (x < 0 || x >= size || y < 0 || y >= size) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            int currentX = x;
            int currentY = y;

            if (orientation == Orientation.HORIZONTAL) {
                currentX += i;
            } else {
                currentY += i;
            }

            if (currentX >= size || currentY >= size) {
                return false;
            }

            if (cells[currentX][currentY].hasShip()) {
                return false;
            }
        }

        return true;
    }

    /** 
     * @param ship
     * @param start
     * @param orientation
     * @return boolean
     */
    public boolean placeShip(Ship ship, Coordinate start, Orientation orientation) {
        if (!isValidPlacement(ship, start, orientation)) {
            return false;
        }

        ship.setPosition(start, orientation);

        for (Coordinate coord : ship.getCoordinates()) {
            cells[coord.getX()][coord.getY()].setShip(ship);
        }

        ships.add(ship);
        return true;
    }

    /** 
     * @param coordinate
     * @return AttackResult
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
     * @return boolean
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
     * @return List<Ship>
     */
    public List<Ship> getShips() {
        return new ArrayList<>(ships);
    }

    /** 
     * @return int
     */
    public int getShipCount() {
        return ships.size();
    }
}
