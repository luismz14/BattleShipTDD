package es.uab.tqs.battleship.model;

/**
 * Represents the various states the game can be in during its lifecycle.
 * These states control the flow from setup to gameplay turns and finally to the
 * end result.
 */
public enum GameStatus {
    SETUP,
    PLAYER_TURN,
    COMPUTER_TURN,
    PLAYER_WON,
    COMPUTER_WON,
    DRAW
}