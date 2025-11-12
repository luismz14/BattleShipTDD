package es.uab.tqs.battleship.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CellTest {

    private Cell cell;
    private Coordinate coordinate;

    @BeforeEach
    public void setUp() {
        coordinate = new Coordinate(5, 5);
        cell = new Cell(coordinate);
    }

    @Test
    public void testInitialStateIsEmpty() {
        assertEquals(CellState.EMPTY, cell.getState());
    }

    @Test
    public void testInitialCellHasNoShip() {
        assertFalse(cell.hasShip());
        assertNull(cell.getShip());
    }

    @Test
    public void testSetShip() {
        Ship ship = new Ship(ShipType.DESTROYER);
        cell.setShip(ship);

        assertTrue(cell.hasShip());
        assertEquals(ship, cell.getShip());
        assertEquals(CellState.SHIP, cell.getState());
    }

    @Test
    public void testAttackEmptyCell() {
        boolean hit = cell.attack();

        assertFalse(hit);
        assertEquals(CellState.MISS, cell.getState());
        assertTrue(cell.isAttacked());
    }

    @Test
    public void testAttackCellWithShip() {
        Ship ship = new Ship(ShipType.DESTROYER);
        cell.setShip(ship);

        int initialHits = ship.getHitCount();
        boolean hit = cell.attack();

        assertTrue(hit);
        assertEquals(CellState.HIT, cell.getState());
        assertTrue(cell.isAttacked());
        assertEquals(initialHits + 1, ship.getHitCount());
    }

    @Test
    public void testStateTransitionEmptyToMiss() {
        assertEquals(CellState.EMPTY, cell.getState());
        cell.attack();
        assertEquals(CellState.MISS, cell.getState());
    }

    @Test
    public void testStateTransitionEmptyToShipToHit() {
        assertEquals(CellState.EMPTY, cell.getState());

        Ship ship = new Ship(ShipType.CRUISER);
        cell.setShip(ship);
        assertEquals(CellState.SHIP, cell.getState());

        cell.attack();
        assertEquals(CellState.HIT, cell.getState());
    }

    @Test
    public void testIsAttackedFalseForUntouchedCell() {
        assertFalse(cell.isAttacked());

        Ship ship = new Ship(ShipType.SUBMARINE);
        cell.setShip(ship);
        assertFalse(cell.isAttacked());
    }

    @Test
    public void testIsAttackedTrueForHitCell() {
        Ship ship = new Ship(ShipType.BATTLESHIP);
        cell.setShip(ship);
        cell.attack();

        assertTrue(cell.isAttacked());
    }

    @Test
    public void testIsAttackedTrueForMissCell() {
        cell.attack();
        assertTrue(cell.isAttacked());
    }

    @Test
    public void testGetCoordinate() {
        assertEquals(coordinate, cell.getCoordinate());
    }

    @Test
    public void testSetState() {
        cell.setState(CellState.HIT);
        assertEquals(CellState.HIT, cell.getState());
    }
}
