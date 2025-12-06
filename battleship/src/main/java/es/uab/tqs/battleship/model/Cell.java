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

    /** 
     * @return CellState
     */
    public CellState getState() {
        return state;
    }

    /** 
     * @param state
     */
    public void setState(CellState state) {
        this.state = state;
    }

    /** 
     * @return Coordinate
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /** 
     * @return boolean
     */
    public boolean hasShip() {
        return ship != null;
    }

    /** 
     * @param ship
     */
    public void setShip(Ship ship) {
        this.ship = ship;
        if (ship != null) {
            this.state = CellState.SHIP;
        }
    }

    /** 
     * @return Ship
     */
    public Ship getShip() {
        return ship;
    }

    /** 
     * @return boolean
     */
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

    /** 
     * @return boolean
     */
    public boolean isAlreadyAttacked() {
        return state == CellState.HIT || state == CellState.MISS;
    }
}
