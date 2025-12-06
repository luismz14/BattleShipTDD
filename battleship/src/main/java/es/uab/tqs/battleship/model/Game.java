package es.uab.tqs.battleship.model;

import java.util.Random;

/**
 * Manages the core game logic, state, and flow of a Battleship game.
 * This class coordinates the player's and computer's boards, tracks the current
 * game status,
 * and handles the turn-based mechanics including AI behavior.
 */
public class Game {

    private final Board playerBoard;
    private final Board computerBoard;
    private GameStatus status;
    private final Random random;

    /**
     * Constructs a new Game instance.
     * Initializes two 10x10 boards and sets the initial state to SETUP.
     */
    public Game() {
        // Standard Battleship board size is 10x10
        this.playerBoard = new Board(10);
        this.computerBoard = new Board(10);
        this.status = GameStatus.SETUP;
        this.random = new Random();
    }

    /**
     * Retrieves the player's board.
     *
     * @return The Board object for the human player.
     */
    public Board getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Retrieves the computer's board.
     *
     * @return The Board object for the computer.
     */
    public Board getComputerBoard() {
        return computerBoard;
    }

    /**
     * Gets the current status of the game.
     *
     * @return The current GameStatus.
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Transitions the game from the setup phase to the main gameplay phase.
     * This method should be called after all ships have been placed.
     * It sets the status to PLAYER_TURN, allowing the game loop to begin.
     */
    public void startGame() {
        if (status == GameStatus.SETUP) {
            status = GameStatus.PLAYER_TURN;
        }
    }

    /**
     * Processes an attack action initiated by the human player.
     * Validates that it is currently the player's turn before proceeding.
     * If the attack results in a win condition, the game status is updated to
     * PLAYER_WON.
     * Otherwise, the turn is passed to the computer.
     *
     * @param coordinate The coordinate on the computer's board to attack.
     * @return The result of the attack (HIT, MISS, SUNK, ALREADY_ATTACKED).
     * @throws IllegalStateException if called when it is not the player's turn.
     */
    public AttackResult processPlayerAttack(Coordinate coordinate) {
        if (status != GameStatus.PLAYER_TURN) {
            throw new IllegalStateException("No es el turno del jugador");
        }

        // Execute attack on the computer's board
        AttackResult result = computerBoard.processAttack(coordinate);

        // Check for victory condition immediately after the attack
        if (isGameOver()) {
            status = GameStatus.PLAYER_WON;
        } else {
            // Pass turn to the computer if the game isn't over
            status = GameStatus.COMPUTER_TURN;
        }

        return result;
    }

    /**
     * Processes the computer's turn to attack the player.
     * Generates a random valid attack coordinate and executes it against the
     * player's board.
     * Updates the game status to COMPUTER_WON if the player loses all ships,
     * otherwise returns control to the player.
     *
     * @return The coordinate where the computer decided to attack.
     * @throws IllegalStateException if called when it is not the computer's turn.
     */
    public Coordinate processComputerAttack() {
        if (status != GameStatus.COMPUTER_TURN) {
            throw new IllegalStateException("No es el turno de la computadora");
        }

        // AI Logic: Generate a random coordinate that hasn't been attacked yet
        Coordinate attackCoord = generateRandomAttack();

        playerBoard.processAttack(attackCoord);

        if (playerBoard.allShipsSunk()) {
            status = GameStatus.COMPUTER_WON;
        } else {
            status = GameStatus.PLAYER_TURN;
        }

        return attackCoord;
    }

    /**
     * Helper method for the AI to find a valid attack target.
     * It loops until it finds a coordinate that has not been attacked previously
     * (state is EMPTY or SHIP).
     *
     * @return A valid Coordinate for the attack.
     */
    private Coordinate generateRandomAttack() {
        Coordinate coord;
        Cell cell;

        do {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            coord = new Coordinate(x, y);
            cell = playerBoard.getCell(coord);
            // Keep trying if we accidentally picked a cell that was already HIT or MISS
        } while (cell.isAlreadyAttacked());

        return coord;
    }

    /**
     * Checks if the game has reached a terminal state (win/loss).
     * A game is over if all ships on either board are sunk.
     *
     * @return true if the game is over; false otherwise.
     */
    public boolean isGameOver() {
        if (computerBoard.allShipsSunk()) {
            return true;
        }

        if (playerBoard.allShipsSunk()) {
            return true;
        }

        return false;
    }

    /**
     * Returns a message declaring the winner of the game.
     *
     * @return A congratulatory or defeat message depending on the winner, or null
     *         if the game is ongoing.
     */
    public String getWinner() {
        if (status == GameStatus.PLAYER_WON) {
            return "Congratulations! You have won the game.";
        } else if (status == GameStatus.COMPUTER_WON) {
            return "The computer has won the game. Better luck next time!";
        }
        return null;
    }

    /**
     * Automates the placement of ships for the computer player.
     * Iterates through all available ship types and attempts to place them at
     * random coordinates
     * and orientations until a valid placement is found for each.
     */
    public void placeComputerShipsRandomly() {
        ShipType[] types = ShipType.values();

        for (ShipType type : types) {
            Ship ship = new Ship(type);
            boolean placed = false;

            // Keep trying random positions until the ship fits
            while (!placed) {
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                Orientation orientation = random.nextBoolean()
                        ? Orientation.HORIZONTAL
                        : Orientation.VERTICAL;

                Coordinate start = new Coordinate(x, y);
                // The board handles collision detection and boundaries
                placed = computerBoard.placeShip(ship, start, orientation);
            }
        }
    }
}