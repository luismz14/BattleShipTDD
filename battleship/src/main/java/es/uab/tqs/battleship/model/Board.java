package es.uab.tqs.battleship.model;

public class Board {
    private final int  size;
    private final CellState[][] grid;

    //CAMBIAR ESTO, EN EL VIEW LEER EL PLAYERID, ATRIBUTO PLAYERID Y CONSTRUIR TABLERO CON WATER
    public Board(int size) {
        this.size = size;
        this.grid = new CellState[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                    grid[i][j] = CellState.WATER;
            }
        }
    }
}
