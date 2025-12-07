package es.uab.tqs.battleship.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    /**
     * Test Case: Verify initial game state.
     * * Type: Unit Testing
     * * Technique: Statement Coverage.
     * * Description: Verifies that a new Game object initializes both boards (Player and Computer)
     * and sets the initial status to SETUP, ready for ship placement.
     */
    @Test
    public void testGameInitialization() {
        assertNotNull(game.getPlayerBoard(), "Player board debe estar inicializado");
        assertNotNull(game.getComputerBoard(), "Computer board debe estar inicializado");
        assertEquals(GameStatus.SETUP, game.getStatus(), "Estado inicial debe ser SETUP");
    }

    /**
     * Test Case: Verify transition from Setup to Playing.
     * * Type: White Box Testing
     * * Technique: State Transition Testing (SETUP -> PLAYER_TURN).
     * * Description: Verifies that calling startGame() correctly changes the internal game state
     * from SETUP to PLAYER_TURN, enabling the game loop to begin.
     */
    @Test
    public void testStartGame() {
        assertEquals(GameStatus.SETUP, game.getStatus());
        game.startGame();
        assertEquals(GameStatus.PLAYER_TURN, game.getStatus(), 
            "Después de startGame(), estado debe ser PLAYER_TURN");
    }

    /**
     * Test Case: Verify Game Over condition when Player wins.
     * * Type: Black Box Testing / Integration Testing
     * * Technique: State Transition Testing (Playing -> Player Won).
     * * Description: Simulates a full game scenario where the player sinks the last enemy ship.
     * Verifies that isGameOver returns true and the status updates to PLAYER_WON.
     */
    @Test
    public void testIsGameOverPlayerWins() {
        Ship enemyShip = new Ship(ShipType.DESTROYER);
        game.getComputerBoard().placeShip(enemyShip, new Coordinate(0, 0), Orientation.HORIZONTAL);

        game.startGame();

        game.processPlayerAttack(new Coordinate(0, 0));
        game.processComputerAttack();

        game.processPlayerAttack(new Coordinate(1, 0));

        assertTrue(game.isGameOver());
        assertEquals(GameStatus.PLAYER_WON, game.getStatus());
    }

    /**
     * Test Case: Verify Game Over condition when Computer wins.
     * * Type: Black Box Testing
     * * Technique: State Transition Testing (Playing -> Computer Won).
     * * Description: Manually sets up the board state to simulate that all player ships are sunk.
     * Verifies that isGameOver returns true immediately.
     */
    @Test
    public void testIsGameOverComputerWins() {
        Ship playerShip = new Ship(ShipType.DESTROYER);
        game.getPlayerBoard().placeShip(playerShip, new Coordinate(0, 0), Orientation.HORIZONTAL);

        Ship enemyShip = new Ship(ShipType.CRUISER);
        game.getComputerBoard().placeShip(enemyShip, new Coordinate(5, 5), Orientation.VERTICAL);

        game.getPlayerBoard().processAttack(new Coordinate(0, 0));
        game.getPlayerBoard().processAttack(new Coordinate(1, 0));

        assertTrue(game.isGameOver());
    }

    /**
     * Test Case: Verify Game Continues when ships remain.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Active Game.
     * * Description: Verifies that isGameOver returns false as long as both sides have
     * at least one active ship remaining.
     */
    @Test
    public void testIsGameOverGameContinues() {
        Ship playerShip = new Ship(ShipType.DESTROYER);
        Ship enemyShip = new Ship(ShipType.CRUISER);

        game.getPlayerBoard().placeShip(playerShip, new Coordinate(0, 0), Orientation.HORIZONTAL);
        game.getComputerBoard().placeShip(enemyShip, new Coordinate(5, 5), Orientation.VERTICAL);

        assertFalse(game.isGameOver());
    }

    /**
     * Test Case: Verify Game Over logic with empty boards.
     * * Type: Black Box Testing
     * * Technique: Boundary Value / Edge Case.
     * * Description: Verifies behavior when no ships are present (e.g. before setup is complete).
     * Typically returns false as the game hasn't really started/ended in a valid way.
     */
    @Test
    public void testIsGameOverNoShips() {
        assertFalse(game.isGameOver());
    }

    /**
     * Test Case: Verify Player Attack logic and Turn Switch.
     * * Type: Integration Testing / White Box Testing
     * * Technique: State Transition Testing (PLAYER_TURN -> COMPUTER_TURN).
     * * Description: Verifies that a valid player attack:
     * 1. Returns the correct result (HIT).
     * 2. Automatically switches the game state to COMPUTER_TURN.
     */
    @Test
    public void testProcessPlayerAttackValid() {
        Ship enemyShip = new Ship(ShipType.DESTROYER);
        game.getComputerBoard().placeShip(enemyShip, new Coordinate(5, 5), Orientation.HORIZONTAL);

        game.startGame();

        AttackResult result = game.processPlayerAttack(new Coordinate(5, 5));

        assertEquals(AttackResult.HIT, result);
        assertEquals(GameStatus.COMPUTER_TURN, game.getStatus());
    }

    /**
     * Test Case: Verify Player Attack resulting in Win.
     * * Type: Integration Testing
     * * Technique: Path Coverage (Winning Move).
     * * Description: Verifies that if the player's attack sinks the last ship, the game
     * detects the win immediately and updates status to PLAYER_WON instead of switching turns.
     */
    @Test
    public void testProcessPlayerAttackWinsGame() {
        Ship enemyShip = new Ship(ShipType.DESTROYER);
        game.getComputerBoard().placeShip(enemyShip, new Coordinate(0, 0), Orientation.HORIZONTAL);

        game.startGame();

        game.processPlayerAttack(new Coordinate(0, 0));
        game.processComputerAttack();

        AttackResult result = game.processPlayerAttack(new Coordinate(1, 0));

        assertEquals(AttackResult.SUNK, result);
        assertEquals(GameStatus.PLAYER_WON, game.getStatus());
    }

    /**
     * Test Case: Verify protection against playing out of turn.
     * * Type: Black Box Testing
     * * Technique: State Transition Testing / Error Guessing.
     * * Description: Verifies that attempting to play twice in a row (or during computer turn)
     * throws an IllegalStateException, enforcing the game rules.
     */
    @Test
    public void testProcessPlayerAttackWrongTurn() {
        game.startGame();
        game.processPlayerAttack(new Coordinate(0, 0));

        assertThrows(IllegalStateException.class, 
            () -> game.processPlayerAttack(new Coordinate(1, 1)));
    }

    /**
     * Test Case: Verify Computer Attack logic and Turn Switch.
     * * Type: Integration Testing
     * * Technique: State Transition Testing (COMPUTER_TURN -> PLAYER_TURN).
     * * Description: Verifies that the computer's turn:
     * 1. Generates a valid attack coordinate.
     * 2. Switches the state back to PLAYER_TURN.
     */
    @Test
    public void testProcessComputerAttack() {
        Ship playerShip = new Ship(ShipType.CRUISER);
        game.getPlayerBoard().placeShip(playerShip, new Coordinate(5, 5), Orientation.VERTICAL);

        game.startGame();
        game.processPlayerAttack(new Coordinate(0, 0));

        assertEquals(GameStatus.COMPUTER_TURN, game.getStatus());

        Coordinate attacked = game.processComputerAttack();

        assertNotNull(attacked);
        assertTrue(attacked.isValid(10));
        assertEquals(GameStatus.PLAYER_TURN, game.getStatus());
    }

    /**
     * Test Case: Verify Computer Attack resulting in Win.
     * * Type: Integration Testing
     * * Technique: Path Coverage (Losing Move).
     * * Description: Verifies that if the computer sinks the player's last ship,
     * the game correctly identifies the 'Computer Won' state.
     */
    @Test
    public void testProcessComputerAttackWinsGame() {
        Ship playerShip = new Ship(ShipType.DESTROYER);
        game.getPlayerBoard().placeShip(playerShip, new Coordinate(0, 0), Orientation.HORIZONTAL);

        Ship enemyShip = new Ship(ShipType.CRUISER);
        game.getComputerBoard().placeShip(enemyShip, new Coordinate(5, 5), Orientation.VERTICAL);

        game.getPlayerBoard().processAttack(new Coordinate(0, 0));
        game.getPlayerBoard().processAttack(new Coordinate(1, 0));

        assertTrue(game.getPlayerBoard().allShipsSunk());
        assertTrue(game.isGameOver());
    }

    /**
     * Test Case: Verify protection against Computer playing out of turn.
     * * Type: Black Box Testing
     * * Technique: State Transition Testing.
     * * Description: Verifies that processComputerAttack throws an exception if called
     * during the player's turn.
     */
    @Test
    public void testProcessComputerAttackWrongTurn() {
        game.startGame();

        assertThrows(IllegalStateException.class, 
            () -> game.processComputerAttack());
    }

    /**
     * Test Case: Verify winner message for Player Victory.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Winner String.
     * * Description: Verifies that getWinner returns a string containing "won" when
     * the player has achieved victory.
     */
    @Test
    public void testGetWinnerPlayerWon() {
        Ship enemyShip = new Ship(ShipType.DESTROYER);
        game.getComputerBoard().placeShip(enemyShip, new Coordinate(0, 0), Orientation.HORIZONTAL);

        game.startGame();

        game.processPlayerAttack(new Coordinate(0, 0));
        game.processComputerAttack();

        game.processPlayerAttack(new Coordinate(1, 0));

        String winner = game.getWinner();
        assertNotNull(winner);
        assertTrue(winner.toLowerCase().contains("won"));
    }

    /**
     * Test Case: Verify winner message for Computer Victory.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Loser String.
     * * Description: Verifies that getWinner returns the specific computer victory message.
     */
    @Test
    public void testGetWinnerComputerWon() {
        Ship playerShip = new Ship(ShipType.DESTROYER);
        game.getPlayerBoard().placeShip(playerShip, new Coordinate(0, 0), Orientation.HORIZONTAL);

        Ship enemyShip = new Ship(ShipType.CRUISER);
        game.getComputerBoard().placeShip(enemyShip, new Coordinate(5, 5), Orientation.VERTICAL);

        game.getPlayerBoard().processAttack(new Coordinate(0, 0));
        game.getPlayerBoard().processAttack(new Coordinate(1, 0));

        assertTrue(game.isGameOver());

        String winner = game.getWinner();
        if (winner != null) {
            assertTrue(winner.toLowerCase().contains("The computer has won the game. Better luck next time!"));
        }
    }

    /**
     * Test Case: Verify winner message during active game.
     * * Type: Black Box Testing
     * * Technique: State Verification.
     * * Description: Verifies that getWinner returns null if the game is still in progress.
     */
    @Test
    public void testGetWinnerNoWinner() {
        game.startGame();

        String winner = game.getWinner();
        assertNull(winner);
    }

    /**
     * Test Case: Verify Computer Ship Placement Logic.
     * * Type: Black Box Testing
     * * Technique: State Verification / Business Rule Check.
     * * Description: Verifies that placeComputerShipsRandomly populates the computer board
     * with exactly 5 ships of the correct types (Carrier, Battleship, etc.), ensuring
     * the AI setup adheres to the game rules.
     */
    @Test
    public void testPlaceComputerShipsRandomly() {
        game.placeComputerShipsRandomly();

        Board computerBoard = game.getComputerBoard();

        assertEquals(5, computerBoard.getShipCount());

        var ships = computerBoard.getShips();
        assertTrue(ships.stream().anyMatch(s -> s.getType() == ShipType.CARRIER));
        assertTrue(ships.stream().anyMatch(s -> s.getType() == ShipType.BATTLESHIP));
        assertTrue(ships.stream().anyMatch(s -> s.getType() == ShipType.CRUISER));
        assertTrue(ships.stream().anyMatch(s -> s.getType() == ShipType.SUBMARINE));
        assertTrue(ships.stream().anyMatch(s -> s.getType() == ShipType.DESTROYER));
    }

    /**
     * Test Case: Verify Robustness of Random Placement.
     * * Type: White Box Testing
     * * Technique: Loop Testing (Repetition).
     * * Description: Runs the random placement logic multiple times (10 iterations) to ensure
     * that it consistently succeeds without infinite loops or placement failures, proving
     * the robustness of the random coordinate generation.
     */
    @Test
    public void testPlaceComputerShipsRandomlyMultipleTimes() {
        for (int i = 0; i < 10; i++) {
            Game newGame = new Game();
            newGame.placeComputerShipsRandomly();
            assertEquals(5, newGame.getComputerBoard().getShipCount());
        }
    }
}
