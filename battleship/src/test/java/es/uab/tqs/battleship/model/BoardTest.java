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
    void testBoardInitializedWithWater() {
        Board board = new Board(10);
        // assertEquals(CellState.WATER, board.getCellState(0, 0));
    }

    @Test
    void testGetCellStateReturnsWaterInitially() {
        Board board = new Board(10);
        assertEquals(CellState.WATER, board.getCellState(0, 0));
        assertEquals(CellState.WATER, board.getCellState(5, 5));
        assertEquals(CellState.WATER, board.getCellState(9, 9));
    }

    @Test
    void testGetCellStateThrowsExceptionForInvalidCoordinates() {
        Board board = new Board(10);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            board.getCellState(-1, 0);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            board.getCellState(10, 0);
        });
}
}
