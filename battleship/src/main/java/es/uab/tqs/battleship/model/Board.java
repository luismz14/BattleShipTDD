package es.uab.tqs.battleship.model;

public class Board {
    private final int  size;
    private final CellState[][] playerGrid;
    private final CellState[][] rivalGrid;

    public Board(int size) {
        if (size <= 4) {
            throw new IllegalArgumentException("Size must be 5 or greater");
        }
        this.size = size;
        this.rivalGrid = new CellState[size][size];
        this.playerGrid = new CellState[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                    playerGrid[i][j] = CellState.WATER;
                    rivalGrid[i][j] = CellState.UNKNOWN;
            }
        }
    }
    public boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }
    public CellState getCellState(int row, int col, boolean isPlayer) {
        CellState[][] grid = isPlayer ? playerGrid : rivalGrid;
        if (!isValidCoordinate(row, col)) {
            throw new IndexOutOfBoundsException("Invalid coordinates");
        }
        return grid[row][col];
    }
    
    public void setCellState(int row, int col, CellState state, boolean isPlayer) {
        CellState[][] grid = isPlayer ? playerGrid : rivalGrid;
        if (!isValidCoordinate(row, col)) {
            throw new IndexOutOfBoundsException("Invalid coordinates");
        }
        grid[row][col] = state;
    }
}
