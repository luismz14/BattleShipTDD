package es.uab.tqs.battleship.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
}
