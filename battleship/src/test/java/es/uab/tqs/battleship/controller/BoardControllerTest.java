package es.uab.tqs.battleship.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import es.uab.tqs.battleship.model.Board;
import es.uab.tqs.battleship.model.Cell;
import es.uab.tqs.battleship.model.Coordinate;
import es.uab.tqs.battleship.model.Orientation;
import es.uab.tqs.battleship.model.Ship;
import es.uab.tqs.battleship.model.ShipType;
import es.uab.tqs.battleship.view.GameView;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {

    @Mock
    private GameView mockView;

    @Mock
    private Board mockBoard;

    private BoardController controller;

    @BeforeEach
    public void setUp() {
        controller = new BoardController(mockView);
    }

    /**
     * Test Case: Attempt to place a ship in a valid position.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Valid Class.
     * * Description: Verifies that the attemptPlaceShip method returns true when the Board (Mock)
     * * confirms that the placement is valid and successful.
     * * Mock Interaction: Verifies that the controller calls sequentially:
     * 1. mockBoard.isValidPlacement()
     * 2. mockBoard.placeShip()
     */
    @Test
    public void testAttemptPlaceShipSuccess() {
        Ship ship = new Ship(ShipType.DESTROYER);
        Coordinate start = new Coordinate(0, 0);
        Orientation orientation = Orientation.HORIZONTAL;

        when(mockBoard.isValidPlacement(ship, start, orientation)).thenReturn(true);
        when(mockBoard.placeShip(ship, start, orientation)).thenReturn(true);

        boolean result = controller.attemptPlaceShip(mockBoard, ship, start, orientation);

        assertTrue(result);
        verify(mockBoard).isValidPlacement(ship, start, orientation);
        verify(mockBoard).placeShip(ship, start, orientation);
    }

    /**
     * Test Case: Attempt to place a ship in an invalid position.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Invalid Class.
     * * Description: Verifies that the attemptPlaceShip method returns false when the Board (Mock)
     * indicates the placement is invalid.
     * * Mock Interaction: Crucially verifies that mockBoard.placeShip() is never called.
     */
    @Test
    public void testAttemptPlaceShipInvalidPosition() {
        Ship ship = new Ship(ShipType.CARRIER);
        Coordinate start = new Coordinate(9, 0);
        Orientation orientation = Orientation.HORIZONTAL;

        when(mockBoard.isValidPlacement(ship, start, orientation)).thenReturn(false);

        boolean result = controller.attemptPlaceShip(mockBoard, ship, start, orientation);

        assertFalse(result);
        verify(mockBoard).isValidPlacement(ship, start, orientation);
        verify(mockBoard, never()).placeShip(any(), any(), any());
    }

    /**
     * Test Case: Placement fails during execution despite valid coordinates.
     * * Type: Black Box Testing
     * * Technique: Path Coverage.
     * Description: Verifies that the controller handles the scenario where the board validates
     * the position as correct, but the subsequent placement operation fails.
     * * Mock Interaction: Verifies that the controller attempts the placement
     * after validation, but correctly propagates the failure result.
     */
    @Test
    public void testAttemptPlaceShipFailsAtPlacement() {
        Ship ship = new Ship(ShipType.SUBMARINE);
        Coordinate start = new Coordinate(5, 5);
        Orientation orientation = Orientation.VERTICAL;

        when(mockBoard.isValidPlacement(ship, start, orientation)).thenReturn(true);
        when(mockBoard.placeShip(ship, start, orientation)).thenReturn(false);

        boolean result = controller.attemptPlaceShip(mockBoard, ship, start, orientation);

        assertFalse(result);
        verify(mockBoard).isValidPlacement(ship, start, orientation);
        verify(mockBoard).placeShip(ship, start, orientation);
    }

    /**
     * Test Case: Verify a valid attack on a fresh cell.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Valid Class.
     * * Description: Verifies that isValidAttack returns true when the coordinate is within bounds
     * and the specific cell has not been attacked previously.
     * * Mock Interaction:
     * 1. mockBoard.getSize() -> ensures coordinate is within limits.
     * 2. mockBoard.getCell() -> retrieves the target cell.
     * 3. mockCell.isAlreadyAttacked() -> confirms the cell is available for attack.
     */
    @Test
    public void testIsValidAttackValid() {
        Coordinate coord = new Coordinate(5, 5);
        Cell mockCell = mock(Cell.class);

        when(mockBoard.getSize()).thenReturn(10);
        when(mockBoard.getCell(coord)).thenReturn(mockCell);
        when(mockCell.isAlreadyAttacked()).thenReturn(false);

        boolean result = controller.isValidAttack(mockBoard, coord);

        assertTrue(result);
    }

    /**
     * Test Case: Attempt to attack a coordinate just outside the board limits.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis.
     * * Description: Verifies that isValidAttack returns false when the coordinate is exactly
     * at the boundary limit, which is an invalid position.
     * * Mock Interaction:
     * 1. mockBoard.getSize() -> used to determine validity boundaries.
     * 2. Implicitly verifies that getCell() is NOT called to avoid IndexOutOfBoundsException.
     */
    @Test
    public void testIsValidAttackInvalidCoordinate() {
        Coordinate coord = new Coordinate(10, 10);
        when(mockBoard.getSize()).thenReturn(10);

        boolean result = controller.isValidAttack(mockBoard, coord);

        assertFalse(result);
    }


    /**
     * Test Case: Attempt to attack a cell that has already been hit.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Invalid Class.
     * * Description: Verifies that isValidAttack returns false when the target cell
     * represents a position that has already been attacked previously.
     * * Mock Interaction:
     * 1. mockBoard.getCell() -> retrieves the specific cell object at the coordinates.
     * 2. mockCell.isAlreadyAttacked() -> configured to return true, simulating that the
     * cell was already hit in a previous turn.
     */
    @Test
    public void testIsValidAttackAlreadyAttacked() {
        Coordinate coord = new Coordinate(3, 3);
        Cell mockCell = mock(Cell.class);

        when(mockBoard.getSize()).thenReturn(10);
        when(mockBoard.getCell(coord)).thenReturn(mockCell);
        when(mockCell.isAlreadyAttacked()).thenReturn(true);

        boolean result = controller.isValidAttack(mockBoard, coord);

        assertFalse(result);
    }

    /**
     * Test Case: Count remaining ships when all ships are active.
     * * Type: Black Box Testing
     * * Technique: Loop Testing / Equivalence Partitioning.
     * * Description: Verifies that getRemainingShips correctly iterates through the list of ships
     * and counts all of them since none are sunk.
     * * Mock Interaction:
     * 1. mockBoard.getShips() -> returns a list of 3 mock ships.
     * 2. ship.isSunk() -> called on each element, returning false (active).
     */
    @Test
    public void testGetRemainingShipsAllActive() {
        Ship mockShip1 = mock(Ship.class);
        Ship mockShip2 = mock(Ship.class);
        Ship mockShip3 = mock(Ship.class);

        when(mockShip1.isSunk()).thenReturn(false);
        when(mockShip2.isSunk()).thenReturn(false);
        when(mockShip3.isSunk()).thenReturn(false);

        List<Ship> ships = Arrays.asList(mockShip1, mockShip2, mockShip3);
        when(mockBoard.getShips()).thenReturn(ships);

        int remaining = controller.getRemainingShips(mockBoard);

        assertEquals(3, remaining);
    }

    /**
     * Test Case: Count remaining ships when the list contains mixed states.
     * * Type: Black Box Testing
     * * Technique: Loop Testing  / Equivalence Partitioning.
     * * Description: Verifies that getRemainingShips correctly filters the list of ships,
     * counting only the ones where isSunk() returns false.
     * * Mock Interaction:
     * 1. mockBoard.getShips() -> returns a list of 3 ships.
     * 2. mockShip.isSunk() -> configured to return mixed values (true/false), ensuring
     * the conditional logic inside the loop accurately increments the counter only for active ships.
     */
    @Test
    public void testGetRemainingShipsSomeSunk() {
        Ship mockShip1 = mock(Ship.class);
        Ship mockShip2 = mock(Ship.class);
        Ship mockShip3 = mock(Ship.class);

        when(mockShip1.isSunk()).thenReturn(true);
        when(mockShip2.isSunk()).thenReturn(false);
        when(mockShip3.isSunk()).thenReturn(false);

        List<Ship> ships = Arrays.asList(mockShip1, mockShip2, mockShip3);
        when(mockBoard.getShips()).thenReturn(ships);

        int remaining = controller.getRemainingShips(mockBoard);

        assertEquals(2, remaining);
        verify(mockShip1).isSunk();
        verify(mockShip2).isSunk();
        verify(mockShip3).isSunk();
    }

    /**
     * Test Case: Count remaining ships when all ships have been sunk.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning / Boundary Value.
     * * Description: Verifies that getRemainingShips returns 0 when the isSunk() method
     * returns true for every ship in the list.
     * * Mock Interaction:
     * 1. mockBoard.getShips() -> returns a list of ships.
     * 2. mockShip.isSunk() -> returns true for all elements, ensuring the counter remains at 0.
     */
    @Test
    public void testGetRemainingShipsAllSunk() {
        Ship mockShip1 = mock(Ship.class);
        Ship mockShip2 = mock(Ship.class);

        when(mockShip1.isSunk()).thenReturn(true);
        when(mockShip2.isSunk()).thenReturn(true);

        List<Ship> ships = Arrays.asList(mockShip1, mockShip2);
        when(mockBoard.getShips()).thenReturn(ships);

        int remaining = controller.getRemainingShips(mockBoard);

        assertEquals(0, remaining);
    }

    /**
     * Test Case: Count remaining ships when the board has no ships.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning / Edge Case.
     * * Description: Verifies that getRemainingShips returns 0 when the list of ships
     * provided by the board is empty, ensuring the loop handles empty lists safely.
     * * Mock Interaction:
     * 1. mockBoard.getShips() -> returns an empty list (Collections.emptyList()).
     */
    @Test
    public void testGetRemainingShipsNoShips() {
        when(mockBoard.getShips()).thenReturn(Collections.emptyList());

        int remaining = controller.getRemainingShips(mockBoard);

        assertEquals(0, remaining);
    }

    /**
     * Test Case: Verify that board statistics are correctly displayed.
     * * Type: White Box Testing
     * * Technique: Statement Coverage.
     * * Description: Verifies that displayBoardStats retrieves the ship status from the board
     * and triggers the View to display messages. It ensures the interaction happens the expected
     * number of times.
     * * Mock Interaction:
     * 1. mockBoard.getShips() -> provides the data (3 ships).
     * 2. mockView.displayMessage() -> verifies that the view is updated at least 4 times,
     * confirming that data flows from the Model to the View correctly.
     */
    @Test
    public void testDisplayBoardStats() {
        Ship mockShip1 = mock(Ship.class);
        Ship mockShip2 = mock(Ship.class);
        Ship mockShip3 = mock(Ship.class);

        when(mockShip1.isSunk()).thenReturn(true);
        when(mockShip2.isSunk()).thenReturn(false);
        when(mockShip3.isSunk()).thenReturn(false);

        List<Ship> ships = Arrays.asList(mockShip1, mockShip2, mockShip3);
        when(mockBoard.getShips()).thenReturn(ships);
        when(mockBoard.getShipCount()).thenReturn(3);

        controller.displayBoardStats(mockBoard);

        verify(mockView, atLeast(4)).displayMessage(anyString());
    }

    /**
     * Test Case: Verify the execution order of dependencies during placement.
     * * Type: White Box Testing
     * * Technique: Behavior Verification.
     * * Description: Verifies that the controller enforces the correct logical sequence:
     * validation (isValidPlacement) must happen strictly before the actual placement (placeShip).
     * * Mock Interaction:
     * 1. Uses Mockito.inOrder(mockBoard) to verify the sequence of calls on the mock object.
     * 2. Ensures no logic error exists where placement might be attempted before checking validity.
     */
    @Test
    public void testAttemptPlaceShipCallsInOrder() {
        Ship ship = new Ship(ShipType.BATTLESHIP);
        Coordinate start = new Coordinate(0, 0);
        Orientation orientation = Orientation.VERTICAL;

        when(mockBoard.isValidPlacement(ship, start, orientation)).thenReturn(true);
        when(mockBoard.placeShip(ship, start, orientation)).thenReturn(true);

        controller.attemptPlaceShip(mockBoard, ship, start, orientation);

        var inOrder = inOrder(mockBoard);
        inOrder.verify(mockBoard).isValidPlacement(ship, start, orientation);
        inOrder.verify(mockBoard).placeShip(ship, start, orientation);
    }

    /**
     * Test Case: Verify that placement methods are called strictly once.
     * * Type: White Box Testing
     * * Technique: Behavior Verification.
     * * Description: Verifies that the controller calls isValidPlacement and placeShip exactly
     * once per attempt. This ensures there are no redundant checks or duplicate operations
     * that could lead to performance issues or logical errors.
     * * Mock Interaction:
     * 1. Uses verify(..., times(1)) to strictly enforce the number of invocations.
     */
    @Test
    public void testAttemptPlaceShipCallsExactlyOnce() {
        Ship ship = new Ship(ShipType.CRUISER);
        Coordinate start = new Coordinate(5, 5);
        Orientation orientation = Orientation.HORIZONTAL;

        when(mockBoard.isValidPlacement(ship, start, orientation)).thenReturn(true);
        when(mockBoard.placeShip(ship, start, orientation)).thenReturn(true);

        controller.attemptPlaceShip(mockBoard, ship, start, orientation);

        verify(mockBoard, times(1)).isValidPlacement(ship, start, orientation);
        verify(mockBoard, times(1)).placeShip(ship, start, orientation);
    }

    /**
     * Test Case: Verify the full ship setup process for a player.
     * * Type: White Box Testing
     * * Technique: Loop Testing / Behavior Verification.
     * * Description: Verifies that setupPlayerShips correctly iterates through all required ships,
     *  requesting input from the View and placing them
     * on the Board until the fleet is ready.
     * * Mock Interaction:
     * 1. mockView.getCoordinateInput() & getOrientationInput() -> Simulates user entering valid data 5 times.
     * 2. mockBoard.placeShip() -> Verifies that the controller attempts to place a ship exactly 5 times.
     * 3. verify(... times(5)) -> Explicitly checks the loop count/cardinality.
     */
    @Test
    public void testSetupPlayerShips() {
        Coordinate mockCoord = new Coordinate(0, 0);
        when(mockView.getCoordinateInput(anyString())).thenReturn(mockCoord);
        when(mockView.getOrientationInput()).thenReturn(Orientation.HORIZONTAL);

        when(mockBoard.isValidPlacement(any(Ship.class), any(Coordinate.class), any(Orientation.class)))
                .thenReturn(true);
        when(mockBoard.placeShip(any(Ship.class), any(Coordinate.class), any(Orientation.class)))
                .thenReturn(true);

        controller.setupPlayerShips(mockBoard);

        verify(mockBoard, times(5)).placeShip(any(Ship.class), any(Coordinate.class), any(Orientation.class));
        verify(mockView, times(5)).getOrientationInput();
        verify(mockView).displayMessage(org.mockito.ArgumentMatchers.contains("All your ships are in place"));
    }
}
