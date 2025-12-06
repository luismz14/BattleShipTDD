package es.uab.tqs.battleship.controller;

import es.uab.tqs.battleship.model.AttackResult;
import es.uab.tqs.battleship.model.Cell;
import es.uab.tqs.battleship.model.CellState;
import es.uab.tqs.battleship.model.Coordinate;
import es.uab.tqs.battleship.model.Game;
import es.uab.tqs.battleship.model.GameStatus;
import es.uab.tqs.battleship.view.GameView;

/**
 * The main controller for the Battleship game application.
 * Controls the overall game flow, including game setup, the main game loop
 * (turns),
 * and processing player/computer actions. It acts as the bridge between the
 * Model and the View.
 */
public class GameController {

    private final Game game;
    private final GameView view;
    private final BoardController boardController;

    /**
     * Constructs a new GameController with the specified view.
     * Initializes a new Game model and a BoardController.
     *
     * @param view The game view interface implementation.
     */
    public GameController(GameView view) {
        this.game = new Game();
        this.view = view;
        this.boardController = new BoardController(view);
    }

    /**
     * Constructor for testing purposes or dependency injection.
     * Allows injecting specific (or mocked) instances of Game and BoardController.
     *
     * @param view            The game view.
     * @param game            The game model.
     * @param boardController The board controller.
     */
    public GameController(GameView view, Game game, BoardController boardController) {
        this.game = game;
        this.view = view;
        this.boardController = boardController;
    }

    /**
     * Starts the application and manages the high-level game sequence.
     * 1. Initializes the UI.
     * 2. Orchestrates the ship placement phase for both player and computer.
     * 3. Transitions the game to the playing phase.
     * 4. Enters the main game loop.
     */
    public void startGame() {
        view.clearScreen();
        view.displayInstructions();

        view.displayMessage("\n--- PLACE YOUR SHIPS ---\n");
        // Delegate ship placement to the specialized BoardController
        boardController.setupPlayerShips(game.getPlayerBoard());

        view.displayMessage("\nPlacing enemy ships...\n");
        // The computer places ships instantly using random logic
        game.placeComputerShipsRandomly();

        // Signal the model that setup is complete and the game is starting
        game.startGame();
        view.displayMessage("\n¡The game has started!\n");

        playGameLoop();
    }

    /**
     * The main loop of the game.
     * Continues to alternate turns between the player and the computer until a
     * game-over condition is met.
     */
    public void playGameLoop() {
        while (!game.isGameOver()) {
            if (game.getStatus() == GameStatus.PLAYER_TURN) {
                processPlayerTurn();
            } else if (game.getStatus() == GameStatus.COMPUTER_TURN) {
                processComputerTurn();
            }
        }

        // Game loop exited, meaning someone won
        displayFinalResult();
    }

    /**
     * Handles the logic for a single turn of the human player.
     * 1. Displays the current state of boards.
     * 2. Prompts user for attack coordinates.
     * 3. Executes the attack in the model.
     * 4. Provides feedback on the attack result.
     */
    public void processPlayerTurn() {
        view.displayMessage("\n=== YOUR TURN ===\n");
        view.displayMessage("Enemy board:");
        // Show enemy board with ships hidden (fog of war)
        view.displayBoard(game.getComputerBoard(), true);

        view.displayMessage("\nYour board:");
        // Show player board fully visible
        view.displayBoard(game.getPlayerBoard(), false);

        Coordinate target = view.getCoordinateInput("Enter coordinates to attack");

        try {
            // Process the attack through the model
            AttackResult result = game.processPlayerAttack(target);
            view.displayAttackResult(result);

            if (result == AttackResult.SUNK) {
                view.displayMessage("You have sunk an enemy ship!\n");
            }

            if (result == AttackResult.HIT) {
                view.displayMessage("You hitted an enemy ship!\n");
            }

        } catch (IllegalArgumentException e) {
            // Handle invalid coordinates or logic errors
            view.displayMessage("Error: " + e.getMessage());
        }
    }

    /**
     * Handles the logic for a single turn of the computer opponent.
     * 1. Adds a delay for better user experience (simulating "thinking").
     * 2. Executes the computer's attack via the model.
     * 3. Checks if the attack hit/sunk a player's ship and notifies the user.
     */
    public void processComputerTurn() {
        view.displayMessage("\n=== COMPUTER'S TURN ===\n");
        view.displayMessage("The computer is attacking...\n");

        try {
            Thread.sleep(1000); // Dramatic pause for UX
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // The model determines where the computer attacks
        Coordinate attacked = game.processComputerAttack();
        view.displayMessage("Computer attacked: " + attacked);

        // Retrieve the result of the attack to inform the player
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

    /**
     * Displays the final game results after the game loop ends.
     * Shows the final state of both boards (revealing enemy ships) and the winner
     * message.
     */
    public void displayFinalResult() {
        view.displayMessage("\nFinal enemy board:");
        // Reveal enemy ships at the end
        view.displayBoard(game.getComputerBoard(), false);

        view.displayMessage("\nYour final board:");
        view.displayBoard(game.getPlayerBoard(), false);

        String winner = game.getWinner();
        view.displayGameResult(winner);
    }

    /**
     * Retrieves the game model instance.
     * Primarily used for testing to verify internal state changes.
     *
     * @return The Game model.
     */
    public Game getGame() {
        return game;
    }
}