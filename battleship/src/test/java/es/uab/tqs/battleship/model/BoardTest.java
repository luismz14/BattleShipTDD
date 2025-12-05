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

    @Test
    public void testBoardInitialization() {
        Board testBoard = new Board(10);
        assertEquals(10, testBoard.getSize());
        assertEquals(0, testBoard.getShipCount());
        assertFalse(testBoard.allShipsSunk());
    }

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

    @Test
    public void testValidPlacementHorizontal() {
        Ship ship = new Ship(ShipType.DESTROYER);
        assertTrue(board.isValidPlacement(ship, new Coordinate(0, 0), Orientation.HORIZONTAL));
    }

    @Test
    public void testValidPlacementVertical() {
        Ship ship = new Ship(ShipType.DESTROYER);
        assertTrue(board.isValidPlacement(ship, new Coordinate(0, 0), Orientation.VERTICAL));
    }

    @Test
    public void testInvalidPlacementNegativeX() {
        Ship ship = new Ship(ShipType.CRUISER);
        assertFalse(board.isValidPlacement(ship, new Coordinate(-1, 5), Orientation.HORIZONTAL));
    }

    @Test
    public void testInvalidPlacementNegativeY() {
        Ship ship = new Ship(ShipType.SUBMARINE);
        assertFalse(board.isValidPlacement(ship, new Coordinate(5, -1), Orientation.VERTICAL));
    }

    @Test
    public void testInvalidPlacementXOutOfBounds() {
        Ship ship = new Ship(ShipType.BATTLESHIP);
        assertFalse(board.isValidPlacement(ship, new Coordinate(10, 0), Orientation.HORIZONTAL));
    }

    @Test
    public void testInvalidPlacementYOutOfBounds() {
        Ship ship = new Ship(ShipType.CARRIER);
        assertFalse(board.isValidPlacement(ship, new Coordinate(0, 10), Orientation.VERTICAL));
    }

    @Test
    public void testInvalidPlacementHorizontalOutInLoop() {
        Ship ship = new Ship(ShipType.CARRIER);
        assertFalse(board.isValidPlacement(ship, new Coordinate(8, 0), Orientation.HORIZONTAL));
    }

    @Test
    public void testInvalidPlacementVerticalOutInLoop() {
        Ship ship = new Ship(ShipType.BATTLESHIP);
        assertFalse(board.isValidPlacement(ship, new Coordinate(0, 8), Orientation.VERTICAL));
    }

    @Test
    public void testInvalidPlacementOverlappingShips() {
        Ship ship1 = new Ship(ShipType.CRUISER);
        Ship ship2 = new Ship(ShipType.SUBMARINE);

        board.placeShip(ship1, new Coordinate(3, 3), Orientation.HORIZONTAL);
        assertFalse(board.isValidPlacement(ship2, new Coordinate(3, 3), Orientation.VERTICAL));
    }

    @Test
    public void testPlaceShipPathInvalidPosition() {
        Ship ship = new Ship(ShipType.CARRIER);
        boolean result = board.placeShip(ship, new Coordinate(8, 0), Orientation.HORIZONTAL);

        assertFalse(result);
        assertEquals(0, board.getShipCount());
    }

    @Test
    public void testPlaceShipPathSuccessful() {
        Ship ship = new Ship(ShipType.DESTROYER);
        boolean result = board.placeShip(ship, new Coordinate(0, 0), Orientation.HORIZONTAL);

        assertTrue(result);
        assertEquals(1, board.getShipCount());
        assertTrue(board.getCell(0, 0).hasShip());
        assertTrue(board.getCell(1, 0).hasShip());
    }

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

    @Test
    public void testAllShipsSunkNoShips() {
        assertFalse(board.allShipsSunk());
    }

    @Test
    public void testAllShipsSunkOneShipNotSunk() {
        Ship ship = new Ship(ShipType.DESTROYER);
        board.placeShip(ship, new Coordinate(0, 0), Orientation.HORIZONTAL);

        assertFalse(board.allShipsSunk());
    }

    @Test
    public void testAllShipsSunkOneShipSunk() {
        Ship ship = new Ship(ShipType.DESTROYER);
        board.placeShip(ship, new Coordinate(0, 0), Orientation.HORIZONTAL);

        board.processAttack(new Coordinate(0, 0));
        board.processAttack(new Coordinate(1, 0));

        assertTrue(board.allShipsSunk());
    }

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

    @Test
    public void testProcessAttackHit() {
        Ship ship = new Ship(ShipType.DESTROYER);
        board.placeShip(ship, new Coordinate(5, 5), Orientation.HORIZONTAL);

        AttackResult result = board.processAttack(new Coordinate(5, 5));
        assertEquals(AttackResult.HIT, result);
    }

    @Test
    public void testProcessAttackMiss() {
        AttackResult result = board.processAttack(new Coordinate(0, 0));
        assertEquals(AttackResult.MISS, result);
    }

    @Test
    public void testProcessAttackSunk() {
        Ship ship = new Ship(ShipType.DESTROYER);
        board.placeShip(ship, new Coordinate(0, 0), Orientation.HORIZONTAL);

        board.processAttack(new Coordinate(0, 0));
        AttackResult result = board.processAttack(new Coordinate(1, 0));

        assertEquals(AttackResult.SUNK, result);
    }

    @Test
    public void testProcessAttackAlreadyAttacked() {
        board.processAttack(new Coordinate(5, 5));
        AttackResult result = board.processAttack(new Coordinate(5, 5));

        assertEquals(AttackResult.ALREADY_ATTACKED, result);
    }

    @Test
    public void testProcessAttackInvalidCoordinate() {
        assertThrows(IllegalArgumentException.class, 
            () -> board.processAttack(new Coordinate(10, 10)));
    }

    @Test
    public void testGetCellValid() {
        Cell cell = board.getCell(5, 5);
        assertNotNull(cell);
        assertEquals(new Coordinate(5, 5), cell.getCoordinate());
    }

    @Test
    public void testGetCellWithCoordinate() {
        Coordinate coord = new Coordinate(3, 7);
        Cell cell = board.getCell(coord);
        assertNotNull(cell);
        assertEquals(coord, cell.getCoordinate());
    }

    @Test
    public void testGetCellOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> board.getCell(10, 10));
        assertThrows(IllegalArgumentException.class, () -> board.getCell(-1, 5));
    }

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