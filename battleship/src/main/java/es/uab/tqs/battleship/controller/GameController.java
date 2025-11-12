package es.uab.tqs.battleship.controller;

import es.uab.tqs.battleship.model.AttackResult;
import es.uab.tqs.battleship.model.Cell;
import es.uab.tqs.battleship.model.CellState;
import es.uab.tqs.battleship.model.Coordinate;
import es.uab.tqs.battleship.model.Game;
import es.uab.tqs.battleship.model.GameStatus;
import es.uab.tqs.battleship.view.GameView;

public class GameController {
    private final Game game;
    private final GameView view;
    private final BoardController boardController;

    public GameController(GameView view) {
        this.game = new Game();
        this.view = view;
        this.boardController = new BoardController(view);
    }

    public GameController(GameView view, Game game, BoardController boardController) {
        this.game = game;
        this.view = view;
        this.boardController = boardController;
    }

    public void startGame() {
        view.clearScreen();
        view.displayInstructions();

        view.displayMessage("\n--- PLACE YOUR SHIPS ---\n");
        boardController.setupPlayerShips(game.getPlayerBoard());

        view.displayMessage("\nPlacing enemy ships...\n");
        game.placeComputerShipsRandomly();

        game.startGame();
        view.displayMessage("\n¡The game has started!\n");

        playGameLoop();
    }

    private void playGameLoop() {
        while (!game.isGameOver()) {
            if (game.getStatus() == GameStatus.PLAYER_TURN) {
                processPlayerTurn();
            } else if (game.getStatus() == GameStatus.COMPUTER_TURN) {
                processComputerTurn();
            }
        }

        displayFinalResult();
    }

    private void processPlayerTurn() {
        view.displayMessage("\n=== YOUR TURN ===\n");
        view.displayMessage("Enemy board:");
        view.displayBoard(game.getComputerBoard(), true);

        view.displayMessage("\nYour board:");
        view.displayBoard(game.getPlayerBoard(), false);

        Coordinate target = view.getCoordinateInput("Enter coordinates to attack");

        try {
            AttackResult result = game.processPlayerAttack(target);
            view.displayAttackResult(result);

            if (result == AttackResult.SUNK) {
                view.displayMessage("You have sunk an enemy ship!\n");
            }

        } catch (IllegalArgumentException e) {
            view.displayMessage("Error: " + e.getMessage());
        }
    }

    private void processComputerTurn() {
        view.displayMessage("\n=== COMPUTER'S TURN ===\n");
        view.displayMessage("The computer is attacking...\n");

        try {
            Thread.sleep(1000);  // Dramatic pause
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Coordinate attacked = game.processComputerAttack();
        view.displayMessage("Computer attacked: " + attacked);

        Cell cell = game.getPlayerBoard().getCell(attacked);
        if (cell.getState() == CellState.HIT) {
            view.displayMessage(">>> The computer hit one of your ships! <<<");

            if (cell.getShip().isSunk()) {
                view.displayMessage(">>> Your " + cell.getShip().getType().getDisplayName() 
                    + " has been sunk! <<<\n");
            }
        } else {
            view.displayMessage(">>> The computer missed! <<<\n");
        }
    }

    private void displayFinalResult() {
        view.displayMessage("\nFinal enemy board:");
        view.displayBoard(game.getComputerBoard(), false);

        view.displayMessage("\nYour final board:");
        view.displayBoard(game.getPlayerBoard(), false);

        String winner = game.getWinner();
        view.displayGameResult(winner);
    }

    //test purpose
    public Game getGame() {
        return game;
    }
}
