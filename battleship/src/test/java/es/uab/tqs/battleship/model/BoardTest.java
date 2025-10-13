package es.uab.tqs.battleship.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class BoardTest {
    @Test
    void testCreateBoard() {
        Board board = new Board(10);
        assertNotNull(board);
    }

    @Test
    void testCreateBoardWithInvalidSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Board(4);
        });
    }

    @Test
    void testBoardInitialized() {
        Board board = new Board(10);
        assertEquals(CellState.WATER, board.getCellState(0, 0, true));
        assertEquals(CellState.UNKNOWN, board.getCellState(0, 0, false));
    }

    @Test
    void testGetCellStateReturnsUnknownInitially() {
        Board board = new Board(10);
        assertEquals(CellState.WATER, board.getCellState(0, 0, true));
        assertEquals(CellState.UNKNOWN, board.getCellState(0, 0, false));
        assertEquals(CellState.WATER, board.getCellState(5, 5, true));
        assertEquals(CellState.UNKNOWN, board.getCellState(5, 5, false));
        assertEquals(CellState.WATER, board.getCellState(9, 9, true));
        assertEquals(CellState.UNKNOWN, board.getCellState(9, 9, false));
    }

    @Test
    void testGetCellStateThrowsExceptionForInvalidCoordinates() {
        Board board = new Board(10);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            board.getCellState(-1, 0, true);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            board.getCellState(-1, 0, false);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            board.getCellState(10, 0, true);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            board.getCellState(10, 0, false);
        });
    }

    @Test
    void testSetCellState() {
        Board board = new Board(10);
        board.setCellState(0, 0, CellState.SHIP, true);
        assertEquals(CellState.SHIP, board.getCellState(0, 0, true));
        board.setCellState(0, 0, CellState.HIT, false);
        assertEquals(CellState.HIT, board.getCellState(0, 0, false));
    }
}
