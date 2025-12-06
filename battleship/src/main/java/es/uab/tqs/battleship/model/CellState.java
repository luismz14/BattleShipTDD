package es.uab.tqs.battleship.model;

/**
 * Represents the possible states of a cell on the game board.
 * These states help track the progress of the game, including ship positions
 * and attack results.
 */
public enum CellState {
    EMPTY, // rival's cell not yet targeted
    SHIP, // player's cell that contains a ship
    HIT, // ship cell that has been hit
    MISS, // empty cell that has been targeted (water)
}
