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

    @Test
    public void testIsValidAttackInvalidCoordinate() {
        Coordinate coord = new Coordinate(10, 10);
        when(mockBoard.getSize()).thenReturn(10);

        boolean result = controller.isValidAttack(mockBoard, coord);

        assertFalse(result);
    }

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

    @Test
    public void testGetRemainingShipsNoShips() {
        when(mockBoard.getShips()).thenReturn(Collections.emptyList());

        int remaining = controller.getRemainingShips(mockBoard);

        assertEquals(0, remaining);
    }

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
