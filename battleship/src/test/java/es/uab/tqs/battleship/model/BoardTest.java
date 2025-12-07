package es.uab.tqs.battleship.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board(10);
    }

    /**
     * Test Case: Verify the initial state of the Board upon creation.
     * * Type: Unit Testing / State Verification
     * * Technique: Statement Coverage (Constructor Logic).
     * * Description: Verifies that the Board constructor correctly initializes its key attributes:
     * the grid size (10), the ship counter (0), and the game-over flag (allShipsSunk = false).
     * This ensures the object starts in a consistent and valid state.
     */
    @Test
    public void testBoardInitialization() {
        Board testBoard = new Board(10);
        assertEquals(10, testBoard.getSize());
        assertEquals(0, testBoard.getShipCount());
        assertFalse(testBoard.allShipsSunk());
    }

    /**
     * Test Case: Verify that all board cells are correctly instantiated.
     * * Type: Unit Testing / State Verification
     * * Technique: Loop Testing.
     * * Description: Iterates through the entire grid (10x10) to ensure that every single cell
     * object is not null and is initialized to the 'EMPTY' state. This verifies the integrity
     * of the board structure immediately after creation.
     */
    @Test
    public void testAllCellsInitialized() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Cell cell = board.getCell(i, j);
                assertNotNull(cell);
                assertEquals(CellState.EMPTY, cell.getState());
            }
        }
    }

    /**
     * Test Case: Verify valid ship placement (Horizontal).
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Valid Class.
     * * Description: Verifies that isValidPlacement returns true for a standard ship
     * placed within the board boundaries in a horizontal orientation.
     */
    @Test
    public void testValidPlacementHorizontal() {
        Ship ship = new Ship(ShipType.DESTROYER);
        assertTrue(board.isValidPlacement(ship, new Coordinate(0, 0), Orientation.HORIZONTAL));
    }

    /**
     * Test Case: Verify valid ship placement (Vertical).
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Valid Class.
     * * Description: Verifies that isValidPlacement returns true for a standard ship
     * placed within the board boundaries in a vertical orientation.
     */
    @Test
    public void testValidPlacementVertical() {
        Ship ship = new Ship(ShipType.DESTROYER);
        assertTrue(board.isValidPlacement(ship, new Coordinate(0, 0), Orientation.VERTICAL));
    }

    /**
     * Test Case: Attempt placement with negative X coordinate.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Invalid Lower Bound).
     * * Description: Verifies that the system correctly identifies and rejects coordinates
     * that are below the minimum valid index (0).
     */
    @Test
    public void testInvalidPlacementNegativeX() {
        Ship ship = new Ship(ShipType.CRUISER);
        assertFalse(board.isValidPlacement(ship, new Coordinate(-1, 5), Orientation.HORIZONTAL));
    }

    /**
     * Test Case: Attempt placement with negative Y coordinate.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Invalid Lower Bound).
     * * Description: Verifies that the system correctly identifies and rejects coordinates
     * that are below the minimum valid index (0) for the Y axis.
     */
    @Test
    public void testInvalidPlacementNegativeY() {
        Ship ship = new Ship(ShipType.SUBMARINE);
        assertFalse(board.isValidPlacement(ship, new Coordinate(5, -1), Orientation.VERTICAL));
    }

    /**
     * Test Case: Attempt placement with X coordinate exceeding board size.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Invalid Upper Bound).
     * * Description: Verifies that the system rejects coordinates that match or exceed
     * the board size (index 10 is invalid for size 10).
     */
    @Test
    public void testInvalidPlacementXOutOfBounds() {
        Ship ship = new Ship(ShipType.BATTLESHIP);
        assertFalse(board.isValidPlacement(ship, new Coordinate(10, 0), Orientation.HORIZONTAL));
    }

    /**
     * Test Case: Attempt placement with Y coordinate exceeding board size.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Invalid Upper Bound).
     * * Description: Verifies that the system rejects coordinates that match or exceed
     * the board size for the Y axis.
     */
    @Test
    public void testInvalidPlacementYOutOfBounds() {
        Ship ship = new Ship(ShipType.CARRIER);
        assertFalse(board.isValidPlacement(ship, new Coordinate(0, 10), Orientation.VERTICAL));
    }

    /**
     * Test Case: Attempt placement where ship extends beyond board limits.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Compound Boundary).
     * * Description: Verifies that even if the starting coordinate (8,0) is valid, the system
     * correctly calculates that a ship of length 5 (CARRIER) would extend to index 12, which
     * is out of bounds. The placement must be rejected.
     */
    @Test
    public void testInvalidPlacementHorizontalOutInLoop() {
        Ship ship = new Ship(ShipType.CARRIER);
        assertFalse(board.isValidPlacement(ship, new Coordinate(8, 0), Orientation.HORIZONTAL));
    }

    /**
     * Test Case: Attempt placement where ship extends beyond board limits.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Compound Boundary).
     * * Description: Verifies that the system correctly rejects a vertical placement where
     * the starting Y coordinate is valid, but the ship length causes it to cross the lower
     * boundary of the board.
     */
    @Test
    public void testInvalidPlacementVerticalOutInLoop() {
        Ship ship = new Ship(ShipType.BATTLESHIP);
        assertFalse(board.isValidPlacement(ship, new Coordinate(0, 8), Orientation.VERTICAL));
    }

    /**
     * Test Case: Attempt to place a ship on top of another.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Invalid Class (Occupied Space).
     * * Description: Verifies that the system detects a collision when trying to place a
     * ship in coordinates that are already occupied by a previously placed ship.
     * The second placement must be rejected.
     */
    @Test
    public void testInvalidPlacementOverlappingShips() {
        Ship ship1 = new Ship(ShipType.CRUISER);
        Ship ship2 = new Ship(ShipType.SUBMARINE);

        board.placeShip(ship1, new Coordinate(3, 3), Orientation.HORIZONTAL);
        assertFalse(board.isValidPlacement(ship2, new Coordinate(3, 3), Orientation.VERTICAL));
    }

    /**
     * Test Case: Verify 'placeShip' execution path for invalid input.
     * * Type: White Box Testing
     * * Technique: Path Coverage.
     * * Description: Verifies that if 'isValidPlacement' returns false (due to boundaries),
     * the 'placeShip' method aborts the operation, returns false, and does NOT increment
     * the ship count or modify the state.
     */
    @Test
    public void testPlaceShipPathInvalidPosition() {
        Ship ship = new Ship(ShipType.CARRIER);
        boolean result = board.placeShip(ship, new Coordinate(8, 0), Orientation.HORIZONTAL);

        assertFalse(result);
        assertEquals(0, board.getShipCount());
    }

    /**
     * Test Case: Verify 'placeShip' execution path for valid input.
     * * Type: White Box Testing
     * * Technique: Path Coverage.
     * * Description: Verifies that for a valid placement, the method:
     * 1. Returns true.
     * 2. Increments the ship count.
     * 3. Updates the grid cells to reference the new ship (State Change).
     */
    @Test
    public void testPlaceShipPathSuccessful() {
        Ship ship = new Ship(ShipType.DESTROYER);
        boolean result = board.placeShip(ship, new Coordinate(0, 0), Orientation.HORIZONTAL);

        assertTrue(result);
        assertEquals(1, board.getShipCount());
        assertTrue(board.getCell(0, 0).hasShip());
        assertTrue(board.getCell(1, 0).hasShip());
    }

    /**
     * Test Case: Verify placing multiple ships sequentially.
     * * Type: Black Box Testing
     * * Technique: State Transition Testing.
     * * Description: Verifies that the board correctly handles multiple valid placements,
     * maintaining the state of each and correctly updating the total ship count.
     */
    @Test
    public void testPlaceMultipleShips() {
        Ship ship1 = new Ship(ShipType.CARRIER);
        Ship ship2 = new Ship(ShipType.CRUISER);
        Ship ship3 = new Ship(ShipType.DESTROYER);

        assertTrue(board.placeShip(ship1, new Coordinate(0, 0), Orientation.HORIZONTAL));
        assertTrue(board.placeShip(ship2, new Coordinate(0, 2), Orientation.VERTICAL));
        assertTrue(board.placeShip(ship3, new Coordinate(5, 5), Orientation.HORIZONTAL));

        assertEquals(3, board.getShipCount());
    }

    /**
     * Test Case: Verify game over condition with no ships.
     * * Type: Black Box Testing
     * * Technique: Edge Case
     * * Description: Verifies that if no ships are on the board, 'allShipsSunk' returns false
     * (or arguably true depending on logic, but here false implies game hasn't been lost/played).
     */
    @Test
    public void testAllShipsSunkNoShips() {
        assertFalse(board.allShipsSunk());
    }

    /**
     * Test Case: Verify game over condition with active ships.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Active Game State.
     * * Description: Verifies that 'allShipsSunk' returns false as long as there is at least
     * one ship that has not been completely destroyed.
     */
    @Test
    public void testAllShipsSunkOneShipNotSunk() {
        Ship ship = new Ship(ShipType.DESTROYER);
        board.placeShip(ship, new Coordinate(0, 0), Orientation.HORIZONTAL);

        assertFalse(board.allShipsSunk());
    }

    /**
     * Test Case: Verify game over condition when all ships are destroyed.
     * * Type: Black Box Testing
     * * Technique: State Transition Testing (Alive -> Dead).
     * * Description: Verifies that 'allShipsSunk' transitions to true exactly when the last
     * remaining ship is fully hit.
     */
    @Test
    public void testAllShipsSunkOneShipSunk() {
        Ship ship = new Ship(ShipType.DESTROYER);
        board.placeShip(ship, new Coordinate(0, 0), Orientation.HORIZONTAL);

        board.processAttack(new Coordinate(0, 0));
        board.processAttack(new Coordinate(1, 0));

        assertTrue(board.allShipsSunk());
    }

    /**
     * Test Case: Verify game over condition with multiple ships.
     * * Type: White Box Testing
     * * Technique: Loop Testing.
     * * Description: Verifies that the check correctly iterates over the entire list of ships
     * and only returns true if EVERY single one (AND logic) is sunk.
     */
    @Test
    public void testAllShipsSunkMultipleShipsAllSunk() {
        Ship ship1 = new Ship(ShipType.DESTROYER);
        Ship ship2 = new Ship(ShipType.CRUISER);

        board.placeShip(ship1, new Coordinate(0, 0), Orientation.HORIZONTAL);
        board.placeShip(ship2, new Coordinate(3, 3), Orientation.VERTICAL);

        board.processAttack(new Coordinate(0, 0));
        board.processAttack(new Coordinate(1, 0));

        board.processAttack(new Coordinate(3, 3));
        board.processAttack(new Coordinate(3, 4));
        board.processAttack(new Coordinate(3, 5));

        assertTrue(board.allShipsSunk());
    }


    @Test
    public void testAllShipsSunkMultipleShipsOneNotSunk() {
        Ship ship1 = new Ship(ShipType.DESTROYER);
        Ship ship2 = new Ship(ShipType.CRUISER);

        board.placeShip(ship1, new Coordinate(0, 0), Orientation.HORIZONTAL);
        board.placeShip(ship2, new Coordinate(3, 3), Orientation.VERTICAL);

        board.processAttack(new Coordinate(0, 0));
        board.processAttack(new Coordinate(1, 0));

        assertFalse(board.allShipsSunk());
    }

    /**
     * Test Case: Verify attack logic for a Hit.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Hit Class.
     * * Description: Verifies that attacking a coordinate occupied by a ship returns the
     * correct 'HIT' result status.
     */
    @Test
    public void testProcessAttackHit() {
        Ship ship = new Ship(ShipType.DESTROYER);
        board.placeShip(ship, new Coordinate(5, 5), Orientation.HORIZONTAL);

        AttackResult result = board.processAttack(new Coordinate(5, 5));
        assertEquals(AttackResult.HIT, result);
    }

    /**
     * Test Case: Verify attack logic for a Miss.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Miss Class.
     * * Description: Verifies that attacking an empty coordinate returns the correct 'MISS'
     * result status.
     */
    @Test
    public void testProcessAttackMiss() {
        AttackResult result = board.processAttack(new Coordinate(0, 0));
        assertEquals(AttackResult.MISS, result);
    }

    /**
     * Test Case: Verify attack logic resulting in a Sunk ship.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Last Hit).
     * * Description: Verifies that the system correctly identifies the specific hit that
     * transitions a ship from 'Damaged' to 'Sunk', returning the specialized 'SUNK' result.
     */
    @Test
    public void testProcessAttackSunk() {
        Ship ship = new Ship(ShipType.DESTROYER);
        board.placeShip(ship, new Coordinate(0, 0), Orientation.HORIZONTAL);

        board.processAttack(new Coordinate(0, 0));
        AttackResult result = board.processAttack(new Coordinate(1, 0));

        assertEquals(AttackResult.SUNK, result);
    }

    /**
     * Test Case: Verify attack logic for duplicate attacks.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Invalid History.
     * * Description: Verifies that attacking the same coordinate twice results in a specific
     * 'ALREADY_ATTACKED' status, preventing game logic errors.
     */
    @Test
    public void testProcessAttackAlreadyAttacked() {
        board.processAttack(new Coordinate(5, 5));
        AttackResult result = board.processAttack(new Coordinate(5, 5));

        assertEquals(AttackResult.ALREADY_ATTACKED, result);
    }

    /**
     * Test Case: Verify exception handling for out-of-bounds attacks.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Invalid Input).
     * * Description: Verifies that the method throws an IllegalArgumentException when receiving
     * invalid coordinates, protecting the internal grid state integrity.
     */
    @Test
    public void testProcessAttackInvalidCoordinate() {
        assertThrows(IllegalArgumentException.class, 
            () -> board.processAttack(new Coordinate(10, 10)));
    }

    /**
     * Test Case: Verify cell retrieval with raw indices.
     * * Type: White Box Testing
     * * Technique: Statement Coverage.
     * * Description: Verifies that getCell correctly retrieves the cell object corresponding
     * to the provided X and Y indices.
     */
    @Test
    public void testGetCellValid() {
        Cell cell = board.getCell(5, 5);
        assertNotNull(cell);
        assertEquals(new Coordinate(5, 5), cell.getCoordinate());
    }

    /**
     * Test Case: Verify cell retrieval using a Coordinate object.
     * * Type: White Box Testing
     * * Technique: Statement Coverage.
     * * Description: Verifies that the overloaded getCell(Coordinate) method correctly
     * identifies and returns the corresponding cell. This ensures that the abstraction
     * layer (Coordinate object) maps correctly to the internal grid implementation,
     * maintaining consistency with direct index access.
     */
    @Test
    public void testGetCellWithCoordinate() {
        Coordinate coord = new Coordinate(3, 7);
        Cell cell = board.getCell(coord);
        assertNotNull(cell);
        assertEquals(coord, cell.getCoordinate());
    }

    /**
     * Test Case: Verify cell retrieval protection against invalid indices.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Upper and Lower bounds).
     * * Description: Verifies that attempting to access cells outside the grid limits (both
     * negative and overflow) throws the expected IllegalArgumentException.
     */
    @Test
    public void testGetCellOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> board.getCell(10, 10));
        assertThrows(IllegalArgumentException.class, () -> board.getCell(-1, 5));
    }

    /**
     * Test Case: Verify ship list retrieval.
     * * Type: White Box Testing
     * * Technique: Statement Coverage.
     * * Description: Verifies that getShips returns the correct collection of ships placed
     * on the board, maintaining data integrity.
     */
    @Test
    public void testGetShips() {
        Ship ship1 = new Ship(ShipType.CARRIER);
        Ship ship2 = new Ship(ShipType.DESTROYER);

        board.placeShip(ship1, new Coordinate(0, 0), Orientation.HORIZONTAL);
        board.placeShip(ship2, new Coordinate(0, 2), Orientation.HORIZONTAL);

        assertEquals(2, board.getShips().size());
        assertTrue(board.getShips().contains(ship1));
        assertTrue(board.getShips().contains(ship2));
    }
}