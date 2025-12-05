package es.uab.tqs.battleship.model;

public class Cell {
    private final Coordinate coordinate;
    private CellState state;
    private Ship ship;

    public Cell(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.state = CellState.EMPTY;
        this.ship = null;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public boolean hasShip() {
        return ship != null;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
        if (ship != null) {
            this.state = CellState.SHIP;
        }
    }

    public Ship getShip() {
        return ship;
    }

    public boolean attack() {
        if (hasShip()) {
            state = CellState.HIT;
            ship.registerHit();
            return true;
        } else {
            // water
            state = CellState.MISS;
            return false;
        }
    }

    public boolean isAlreadyAttacked() {
        return state == CellState.HIT || state == CellState.MISS;
    }
}
