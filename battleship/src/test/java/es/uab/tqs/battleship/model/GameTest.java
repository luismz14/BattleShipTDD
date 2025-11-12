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


    @Test
    public void testGameInitialization() {
        assertNotNull(game.getPlayerBoard(), "Player board debe estar inicializado");
        assertNotNull(game.getComputerBoard(), "Computer board debe estar inicializado");
        assertEquals(GameStatus.SETUP, game.getStatus(), "Estado inicial debe ser SETUP");
    }

    @Test
    public void testStartGame() {
        assertEquals(GameStatus.SETUP, game.getStatus());
        game.startGame();
        assertEquals(GameStatus.PLAYER_TURN, game.getStatus(), 
            "Después de startGame(), estado debe ser PLAYER_TURN");
    }

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

    @Test
    public void testIsGameOverGameContinues() {
        Ship playerShip = new Ship(ShipType.DESTROYER);
        Ship enemyShip = new Ship(ShipType.CRUISER);

        game.getPlayerBoard().placeShip(playerShip, new Coordinate(0, 0), Orientation.HORIZONTAL);
        game.getComputerBoard().placeShip(enemyShip, new Coordinate(5, 5), Orientation.VERTICAL);

        assertFalse(game.isGameOver());
    }

    @Test
    public void testIsGameOverNoShips() {
        assertFalse(game.isGameOver());
    }

    @Test
    public void testProcessPlayerAttackValid() {
        Ship enemyShip = new Ship(ShipType.DESTROYER);
        game.getComputerBoard().placeShip(enemyShip, new Coordinate(5, 5), Orientation.HORIZONTAL);

        game.startGame();

        AttackResult result = game.processPlayerAttack(new Coordinate(5, 5));

        assertEquals(AttackResult.HIT, result);
        assertEquals(GameStatus.COMPUTER_TURN, game.getStatus());
    }

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

    @Test
    public void testProcessPlayerAttackWrongTurn() {
        game.startGame();
        game.processPlayerAttack(new Coordinate(0, 0));

        assertThrows(IllegalStateException.class, 
            () -> game.processPlayerAttack(new Coordinate(1, 1)));
    }


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

    @Test
    public void testProcessComputerAttackWrongTurn() {
        game.startGame();

        assertThrows(IllegalStateException.class, 
            () -> game.processComputerAttack());
    }


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
        assertTrue(winner.toLowerCase().contains("Congratulations! You have won the game"));
    }

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

    @Test
    public void testGetWinnerNoWinner() {
        game.startGame();

        String winner = game.getWinner();
        assertNull(winner);
    }

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

    @Test
    public void testPlaceComputerShipsRandomlyMultipleTimes() {
        for (int i = 0; i < 10; i++) {
            Game newGame = new Game();
            newGame.placeComputerShipsRandomly();
            assertEquals(5, newGame.getComputerBoard().getShipCount());
        }
    }
}
