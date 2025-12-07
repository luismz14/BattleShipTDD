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

    /**
     * Test Case: Verify the initial state of a newly created cell.
     * * Type: Unit Testing
     * * Technique: Statement Coverage (Constructor).
     * * Description: Verifies that a new Cell object is initialized with the 'EMPTY' state
     * by default, ensuring a clean starting point for the board logic.
     */
    @Test
    public void testInitialStateIsEmpty() {
        assertEquals(CellState.EMPTY, cell.getState());
    }

    /**
     * Test Case: Verify that a new cell contains no ship references.
     * * Type: Unit Testing
     * * Technique: Statement Coverage.
     * * Description: Verifies that the relationship between the Cell and a Ship is null
     * upon initialization, preventing 'ghost' ships or null pointer errors later.
     */
    @Test
    public void testInitialCellHasNoShip() {
        assertFalse(cell.hasShip());
        assertNull(cell.getShip());
    }

    /**
     * Test Case: Verify placing a ship in a cell.
     * * Type: White Box Testing
     * * Technique: State Transition Testing (EMPTY -> SHIP).
     * * Description: Verifies that assigning a ship to a cell correctly updates its state
     * from EMPTY to SHIP and stores the reference to the ship object correctly.
     */
    @Test
    public void testSetShip() {
        Ship ship = new Ship(ShipType.DESTROYER);
        cell.setShip(ship);

        assertTrue(cell.hasShip());
        assertEquals(ship, cell.getShip());
        assertEquals(CellState.SHIP, cell.getState());
    }

    /**
     * Test Case: Verify attacking an empty cell.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Empty Cell Attack.
     * * Description: Verifies that attacking a cell with no ship results in a 'MISS' state,
     * returns false (indicating no hit), and marks the cell as visited.
     */
    @Test
    public void testAttackEmptyCell() {
        boolean hit = cell.attack();

        assertFalse(hit);
        assertEquals(CellState.MISS, cell.getState());
        assertTrue(cell.isAlreadyAttacked());
    }

    /**
     * Test Case: Verify attacking an occupied cell.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Occupied Cell Attack.
     * * Description: Verifies that attacking a cell with a ship results in a 'HIT' state,
     * returns true, marks the cell as visited, and importantly, increments the ship's hit counter.
     */
    @Test
    public void testAttackCellWithShip() {
        Ship ship = new Ship(ShipType.DESTROYER);
        cell.setShip(ship);

        int initialHits = ship.getHitCount();
        boolean hit = cell.attack();

        assertTrue(hit);
        assertEquals(CellState.HIT, cell.getState());
        assertTrue(cell.isAlreadyAttacked());
        assertEquals(initialHits + 1, ship.getHitCount());
    }
    
    /**
     * Test Case: Verify the lifecycle transition from Empty to Miss.
     * * Type: White Box Testing
     * * Technique: State Transition Testing (Path: Empty -> Miss).
     * * Description: Explicitly verifies the state change sequence: starting at EMPTY,
     * receiving an attack, and ending at MISS.
     */
    @Test
    public void testStateTransitionEmptyToMiss() {
        assertEquals(CellState.EMPTY, cell.getState());
        cell.attack();
        assertEquals(CellState.MISS, cell.getState());
    }

    /**
     * Test Case: Verify the lifecycle transition from Empty to Ship to Hit.
     * * Type: White Box Testing
     * * Technique: State Transition Testing (Path: Empty -> Ship -> Hit).
     * * Description: Explicitly verifies the complex state change sequence: starting at EMPTY,
     * placing a ship (SHIP), receiving an attack, and ending at HIT.
     */
    @Test
    public void testStateTransitionEmptyToShipToHit() {
        assertEquals(CellState.EMPTY, cell.getState());

        Ship ship = new Ship(ShipType.CRUISER);
        cell.setShip(ship);
        assertEquals(CellState.SHIP, cell.getState());

        cell.attack();
        assertEquals(CellState.HIT, cell.getState());
    }

    /**
     * Test Case: Verify 'isAlreadyAttacked' for untouched cells.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Unvisited State.
     * * Description: Verifies that the cell reports false for isAlreadyAttacked() regardless
     * of whether it is empty or has a ship, as long as the attack() method hasn't been called.
     */
    @Test
    public void testIsAttackedFalseForUntouchedCell() {
        assertFalse(cell.isAlreadyAttacked());

        Ship ship = new Ship(ShipType.SUBMARINE);
        cell.setShip(ship);
        assertFalse(cell.isAlreadyAttacked());
    }

    /**
     * Test Case: Verify 'isAlreadyAttacked' for hit cells.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Visited State (Hit).
     * * Description: Verifies that the cell correctly reports true for isAlreadyAttacked()
     * after being successfully attacked (HIT).
     */
    @Test
    public void testIsAttackedTrueForHitCell() {
        Ship ship = new Ship(ShipType.BATTLESHIP);
        cell.setShip(ship);
        cell.attack();

        assertTrue(cell.isAlreadyAttacked());
    }

    /**
     * Test Case: Verify 'isAlreadyAttacked' for missed cells.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Visited State (Miss).
     * * Description: Verifies that the cell correctly reports true for isAlreadyAttacked()
     * after being attacked without a ship (MISS).
     */
    @Test
    public void testIsAttackedTrueForMissCell() {
        cell.attack();
        assertTrue(cell.isAlreadyAttacked());
    }

    /**
     * Test Case: Verify coordinate retrieval.
     * * Type: Unit Testing
     * * Technique: Statement Coverage.
     * * Description: Simple verification that the coordinate injected in the constructor
     * is correctly returned.
     */
    @Test
    public void testGetCoordinate() {
        assertEquals(coordinate, cell.getCoordinate());
    }

    /**
     * Test Case: Verify manual state setting.
     * * Type: Unit Testing
     * * Technique: Statement Coverage.
     * * Description: Verifies that the state can be manually set, ensuring flexibility
     * for other board operations (like setup or resets).
     */
    @Test
    public void testSetState() {
        cell.setState(CellState.HIT);
        assertEquals(CellState.HIT, cell.getState());
    }
}
