package es.uab.tqs.battleship.model;

import java.util.Random;

public class Game {
    private final Board playerBoard;
    private final Board computerBoard;
    private GameStatus status;
    private final Random random;

    public Game() {
        this.playerBoard = new Board(10);
        this.computerBoard = new Board(10);
        this.status = GameStatus.SETUP;
        this.random = new Random();
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public Board getComputerBoard() {
        return computerBoard;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void startGame() {
        if (status == GameStatus.SETUP) {
            status = GameStatus.PLAYER_TURN;
        }
    }

    public AttackResult processPlayerAttack(Coordinate coordinate) {
        if (status != GameStatus.PLAYER_TURN) {
            throw new IllegalStateException("No es el turno del jugador");
        }

        AttackResult result = computerBoard.processAttack(coordinate);

        if (isGameOver()) {
            status = GameStatus.PLAYER_WON;
        } else {
            status = GameStatus.COMPUTER_TURN;
        }

        return result;
    }

    public Coordinate processComputerAttack() {
        if (status != GameStatus.COMPUTER_TURN) {
            throw new IllegalStateException("No es el turno de la computadora");
        }

        Coordinate attackCoord = generateRandomAttack();
        AttackResult result = playerBoard.processAttack(attackCoord);

        if (playerBoard.allShipsSunk()) {
            status = GameStatus.COMPUTER_WON;
        } else {
            status = GameStatus.PLAYER_TURN;
        }

        return attackCoord;
    }

    private Coordinate generateRandomAttack() {
        Coordinate coord;
        Cell cell;

        do {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            coord = new Coordinate(x, y);
            cell = playerBoard.getCell(coord);
        } while (cell.isAlreadyAttacked());

        return coord;
    }

    public boolean isGameOver() {
        if (computerBoard.allShipsSunk()) {
            return true;
        }

        if (playerBoard.allShipsSunk()) {
            return true;
        }

        return false;
    }

    public String getWinner() {
        if (status == GameStatus.PLAYER_WON) {
            return "Congratulations! You have won the game.";
        } else if (status == GameStatus.COMPUTER_WON) {
            return "The computer has won the game. Better luck next time!";
        }
        return null;
    }

    public void placeComputerShipsRandomly() {
        ShipType[] types = ShipType.values();

        for (ShipType type : types) {
            Ship ship = new Ship(type);
            boolean placed = false;

            while (!placed) {
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                Orientation orientation = random.nextBoolean() 
                    ? Orientation.HORIZONTAL 
                    : Orientation.VERTICAL;

                Coordinate start = new Coordinate(x, y);
                placed = computerBoard.placeShip(ship, start, orientation);
            }
        }
    }
}
