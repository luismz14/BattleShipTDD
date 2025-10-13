package es.uab.tqs.battleship.model;

public enum CellState {
    UNKNOWN, // rival's cell not yet targeted
    WATER,   // targeted rival's cell with no ship or player's empty cell 
    HIT,     // ship cell that has been hit
    SUNK,    // ship cell that has been sunk
    SHIP,    // player's cell that contains a ship
}
