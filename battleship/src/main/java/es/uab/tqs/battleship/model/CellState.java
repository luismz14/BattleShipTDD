package es.uab.tqs.battleship.model;

public enum CellState {
    EMPTY, // rival's cell not yet targeted
    SHIP,    // player's cell that contains a ship
    HIT,     // ship cell that has been hit
    MISS,    // empty cell that has been targeted (water)
}

