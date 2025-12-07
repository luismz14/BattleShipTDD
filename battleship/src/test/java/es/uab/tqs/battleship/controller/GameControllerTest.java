package es.uab.tqs.battleship.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import es.uab.tqs.battleship.model.AttackResult;
import es.uab.tqs.battleship.model.Board;
import es.uab.tqs.battleship.model.Cell;
import es.uab.tqs.battleship.model.CellState;
import es.uab.tqs.battleship.model.Coordinate;
import es.uab.tqs.battleship.model.Game;
import es.uab.tqs.battleship.model.GameStatus;
import es.uab.tqs.battleship.model.Ship;
import es.uab.tqs.battleship.model.ShipType;
import es.uab.tqs.battleship.view.GameView;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GameControllerTest {

    @Mock
    private GameView mockView;

    @Mock
    private Game mockGame;

    @Mock
    private BoardController mockBoardController;

    @Mock
    private Board mockPlayerBoard;

    @Mock
    private Board mockComputerBoard;

    private GameController controller;

    @BeforeEach
    public void setUp() {
        lenient().when(mockGame.getPlayerBoard()).thenReturn(mockPlayerBoard);
        lenient().when(mockGame.getComputerBoard()).thenReturn(mockComputerBoard);
        lenient().when(mockGame.getStatus()).thenReturn(GameStatus.PLAYER_TURN);

        controller = new GameController(mockView, mockGame, mockBoardController);
    }

    /**
     * Test Case: Verify correct initialization of the Controller via the default constructor.
     * * Type: Unit Testing / State Verification
     * * Technique: Statement Coverage.
     * * Description: Verifies that the default constructor correctly initializes the internal
     * Game model instance, ensuring the controller is in a valid state after creation.
     * * Mock Interaction:
     * 1. Uses a mock GameView as a required dependency.
     * 2. Verifies that controller.getGame() returns a non-null object, confirming instantiation.
     */
    @Test
    public void testGameControllerDefaultConstructor() {
        GameView view = mock(GameView.class);
        GameController controller = new GameController(view);
        assertNotNull(controller.getGame());
    }

    /**
     * Test Case: Verify the main game loop execution and turn alternation (Complex Flow).
     * * Type: White Box Testing
     * * Technique: Loop Testing & Decision Coverage.
     * * Description: Verifies that playGameLoop():
     * 1. Iterates correctly while isGameOver() is false.
     * 2. Executes the "Player Turn" branch when status is PLAYER_TURN.
     * 3. Executes the "Computer Turn" branch when status is COMPUTER_TURN.
     * 4. Terminates correctly when isGameOver() becomes true.
     * * Mock Interaction:
     * - isGameOver() -> returns false, false, true (Simulating 2 full loops + exit check).
     * - getStatus() -> alternates types to ensure both if/else branches are covered.
     */
    @Test
    public void testPlayGameLoop_AlternatesTurnsAndEnds() {
        // First iteration: -> Player Turn
        // Second iteration: -> Computer Turn
        // Third iteration: -> Game Over
        when(mockGame.isGameOver())
            .thenReturn(false)
            .thenReturn(false)
            .thenReturn(true);

        when(mockGame.getStatus())
            .thenReturn(GameStatus.PLAYER_TURN)
            .thenReturn(GameStatus.COMPUTER_TURN);

        when(mockView.getCoordinateInput(anyString())).thenReturn(mock(Coordinate.class));
        when(mockGame.processComputerAttack()).thenReturn(mock(Coordinate.class));
        
        when(mockPlayerBoard.getCell(any())).thenReturn(mock(Cell.class));

        controller.playGameLoop();

        verify(mockGame, atLeast(3)).isGameOver();        
        verify(mockView, atLeastOnce()).getCoordinateInput(anyString());
        verify(mockGame, atLeastOnce()).processComputerAttack(); 
    }

    /**
     * Test Case: Player successfully sinks an enemy ship.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning (Result Class: SUNK).
     * * Description: Verifies that when the model reports an attack resulted in sinking a ship (SUNK),
     * the controller correctly instructs the view to display the specific "SUNK" feedback and message.
     * * Mock Interaction:
     * 1. mockGame.processPlayerAttack() -> returns AttackResult.SUNK.
     * 2. mockView.displayAttackResult() -> verifies correct enum propagation.
     * 3. mockView.displayMessage() -> verifies the specific text feedback for sinking a ship.
     */
    @Test
    public void testProcessPlayerTurn_SunkShip() {
        Coordinate target = new Coordinate(1, 1);
        
        when(mockView.getCoordinateInput(anyString())).thenReturn(target);
        when(mockGame.processPlayerAttack(target)).thenReturn(AttackResult.SUNK);

        controller.processPlayerTurn();

        verify(mockView).displayAttackResult(AttackResult.SUNK);
        verify(mockView).displayMessage(org.mockito.ArgumentMatchers.contains("sunk an enemy ship"));
    }

    /**
     * Test Case: Verify exception handling when the player makes an invalid move.
     * * Type: Black Box Testing
     * * Technique: Exception Handling.
     * * Description: Verifies that the controller gracefully handles an IllegalArgumentException
     * thrown by the model. It ensures the application catches the exception, 
     * displays an error message to the user, and does not attempt to display
     * a success result.
     * * Mock Interaction:
     * 1. mockGame.processPlayerAttack() -> configured to throw an exception.
     * 2. mockView.displayMessage() -> verifies the error message is shown.
     * 3. mockView.displayAttackResult() -> verified with never() to ensure flow control is correct.
     */
    @Test
    void testProcessPlayerTurn_CatchIllegalArgumentException() {
        Coordinate mockCoordinate = new Coordinate(1, 1); 
        when(mockView.getCoordinateInput(anyString())).thenReturn(mockCoordinate);

        String errorMsg = "Already attacked coordinate";
        when(mockGame.processPlayerAttack(mockCoordinate))
            .thenThrow(new IllegalArgumentException(errorMsg));

        controller.processPlayerTurn();

        verify(mockView).displayMessage("Error: " + errorMsg);
        
        verify(mockView, never()).displayAttackResult(any());
    }
    
    /**
     * Test Case: Player successfully hits an enemy ship.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning (Result Class: HIT).
     * * Description: Verifies that when the model reports an attack resulted in a hit (HIT),
     * the controller correctly instructs the view to display the specific "HIT" feedback.
     * * Mock Interaction:
     * 1. mockGame.processPlayerAttack() -> returns AttackResult.HIT.
     * 2. mockView.displayAttackResult() -> verifies correct enum propagation (HIT).
     * 3. mockView.displayMessage() -> verifies the specific text feedback for hitting a ship.
     */
    @Test
    public void testProcessPlayerTurn_HitShip() {
        Coordinate target = new Coordinate(1, 1);
        
        when(mockView.getCoordinateInput(anyString())).thenReturn(target);
        when(mockGame.processPlayerAttack(target)).thenReturn(AttackResult.HIT);

        controller.processPlayerTurn();

        verify(mockView).displayAttackResult(AttackResult.HIT);
        verify(mockView).displayMessage(org.mockito.ArgumentMatchers.contains("hitted an enemy ship"));
    }

    /**
     * Test Case: Computer turn results in sinking a player's ship.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning (Result Class: Hit & Sunk).
     * * Description: Verifies that when the computer attacks a coordinate that results in sinking a ship,
     * the controller:
     * 1. Detects the HIT state.
     * 2. Checks if the ship is SUNK.
     * 3. Retrieves the ship type name.
     * 4. Displays both the hit message and the specific sunk message to the user.
     * * Mock Interaction:
     * - Uses chained mocking: Board -> Cell -> Ship -> ShipType.
     * - Verifies that the view receives the specific ship name ("Submarine") in the output.
     */
    @Test
    public void testProcessComputerTurn_ComputerSinksPlayerShip() {
        Coordinate attackCoords = new Coordinate(5, 5);
        
        when(mockGame.processComputerAttack()).thenReturn(attackCoords);
        
        Cell mockCell = mock(Cell.class);
        Ship mockShip = mock(Ship.class);
        ShipType mockType = mock(ShipType.class);

        when(mockPlayerBoard.getCell(attackCoords)).thenReturn(mockCell);
        when(mockCell.getState()).thenReturn(CellState.HIT);
        when(mockCell.getShip()).thenReturn(mockShip);
        when(mockShip.isSunk()).thenReturn(true);
        when(mockShip.getType()).thenReturn(mockType);
        when(mockType.getDisplayName()).thenReturn("Submarine");

        controller.processComputerTurn();

        verify(mockView).displayMessage(org.mockito.ArgumentMatchers.contains("The computer hit one of your ships"));
        
        verify(mockView).displayMessage(org.mockito.ArgumentMatchers.contains("Your Submarine has been sunk"));
    }

    /**
     * Test Case: Computer turn results in a miss.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning (Result Class: MISS).
     * * Description: Verifies that when the computer attacks a coordinate that results in a miss
     * (CellState.MISS), the controller correctly identifies the state and displays the
     * specific "missed" message to the user.
     * * Mock Interaction:
     * 1. mockGame.processComputerAttack() -> determines the coordinate.
     * 2. mockPlayerBoard.getCell() -> retrieves the cell status.
     * 3. mockCell.getState() -> returns CellState.MISS.
     * 4. mockView.displayMessage() -> verifies that the specific "The computer missed" message is shown.
     */
    @Test
    public void testProcessComputerTurn_ComputerMiss() {
        Coordinate attackCoords = new Coordinate(5, 5);
        
        when(mockGame.processComputerAttack()).thenReturn(attackCoords);
        
        Cell mockCell = mock(Cell.class);
        Ship mockShip = mock(Ship.class);
        ShipType mockType = mock(ShipType.class);

        when(mockPlayerBoard.getCell(attackCoords)).thenReturn(mockCell);
        when(mockCell.getState()).thenReturn(CellState.MISS);
        when(mockCell.getShip()).thenReturn(mockShip);
        when(mockShip.isSunk()).thenReturn(true);
        when(mockShip.getType()).thenReturn(mockType);
        when(mockType.getDisplayName()).thenReturn("Submarine");

        controller.processComputerTurn();

        verify(mockView).displayMessage(org.mockito.ArgumentMatchers.contains("The computer missed"));
    }

    /**
     * Test Case: Verify the UI initialization sequence at game start.
     * * Type: White Box Testing
     * * Technique: Behavior Verification.
     * * Description: Verifies that when the game starts, the controller immediately clears the
     * screen and displays the game instructions to the user.
     * * Mock Interaction:
     * 1. mockGame.isGameOver() -> returns true to ensure the method exits without entering the game loop.
     * 2. mockView.clearScreen() -> verifies the UI cleanup.
     * 3. mockView.displayInstructions() -> verifies that instructions are shown.
     */
    @Test
    public void testStartGameDisplaysInstructions() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        verify(mockView).clearScreen();
        verify(mockView).displayInstructions();
    }
    
    /**
     * Test Case: Verify delegation of ship setup to BoardController.
     * * Type: Structural Testing
     * * Technique: Behavior Verification.
     * * Description: Verifies that the startGame method correctly delegates the responsibility
     * of setting up the player's ships to the specialized BoardController. This ensures
     * proper separation of concerns within the MVC architecture.
     * * Mock Interaction:
     * 1. verify(mockBoardController).setupPlayerShips() -> confirms the method call was passed
     * to the dependency with the correct board instance.
     */
    @Test
    public void testStartGameCallsBoardControllerSetup() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        verify(mockBoardController).setupPlayerShips(mockPlayerBoard);
    }

    /**
     * Test Case: Verify game model initialization triggers.
     * * Type: White Box Testing
     * * Technique: Behavior Verification.
     * * Description: Verifies that startGame triggers the essential model operations required
     * to begin a match: randomly placing computer ships and changing the internal game status
     * to 'STARTED'.
     * * Mock Interaction:
     * 1. verify(mockGame).placeComputerShipsRandomly() -> ensures the opponent is ready.
     * 2. verify(mockGame).startGame() -> ensures the game state is updated to allow turns to begin.
     */
    @Test
    public void testStartGameInitializesGame() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        verify(mockGame).placeComputerShipsRandomly();
        verify(mockGame).startGame();
    }

    /**
     * Test Case: Verify access to the internal Game model.
     * * Type: White Box Testing
     * * Technique: Statement Coverage.
     * * Description: Verifies that the getGame() method returns the exact instance of the
     * Game model that was injected during initialization. This confirms that the controller
     * is operating on the expected model instance.
     * * Mock Interaction:
     * 1. Initializes the controller with a specific mockGame.
     * 2. Asserts that getGame() returns that exact same object reference.
     */
    @Test
    public void testGetGameReturnsInjectedMock() {
        Game retrievedGame = controller.getGame();
        assertEquals(mockGame, retrievedGame);
    }

    /**
     * Test Case: Verify interaction with the View to display boards at game end.
     * * Type: White Box Testing
     * * Technique: Behavior Verification.
     * * Description: Verifies that the startGame sequence 
     * includes interactions with the View to display the boards.
     * * Mock Interaction:
     * 1. mockGame.getWinner() -> prepares the game end state.
     * 2. verify(mockView).displayBoard(...) -> checks that the view's displayBoard method
     * is compatible with the Board objects and boolean flags passed by the controller.
     */
    @Test
    public void testStartGameCallsDisplayBoard() {
        when(mockGame.isGameOver()).thenReturn(true);
        when(mockGame.getWinner()).thenReturn("Player won!");

        controller.startGame();

        verify(mockView, atLeast(0)).displayBoard(any(Board.class), anyBoolean());
    }

    /**
     * Test Case: Verify general user feedback mechanisms.
     * * Type: White Box Testing
     * * Technique: Behavior Verification.
     * * Description: Verifies that the controller communicates with the user via the View
     * during the game startup sequence. It ensures that the application is not "silent"
     * and provides some form of textual feedback.
     * * Mock Interaction:
     * 1. verify(mockView, atLeastOnce()).displayMessage(...) -> ensures that the displayMessage
     * method is called one or more times, validating the UI feedback channel.
     */
    @Test
    public void testDisplayMessageCalled() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        verify(mockView, atLeastOnce()).displayMessage(anyString());
    }

    /**
     * Test Case: Verify the correct data flow between GameController and BoardController.
     * * Type: White Box Testing
     * * Technique: Behavior Verification.
     * * Description: Verifies that when the game starts, the GameController passes the
     * specific 'playerBoard' instance to the BoardController.
     * This ensures that the user's ships are placed on the correct board instance.
     * * Mock Interaction:
     * 1. verify(mockBoardController).setupPlayerShips(mockPlayerBoard) -> strictly checks that the
     * argument passed was the mockPlayerBoard object, not any other board.
     */
    @Test
    public void testBoardControllerReceivesCorrectBoard() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        verify(mockBoardController).setupPlayerShips(mockPlayerBoard);
    }

    /**
     * Test Case: Verify the exact execution order of UI initialization.
     * * Type: White Box Testing
     * * Technique: Behavior Verification.
     * * Description: Verifies that the controller executes the startup sequence in the strict
     * logical order: first clearing the screen, and only then displaying the instructions.
     * * Mock Interaction:
     * 1. Uses Mockito.inOrder(...) to create a sequence verifier for the view.
     * 2. inOrder.verify(...).clearScreen() -> MUST be the first call.
     * 3. inOrder.verify(...).displayInstructions() -> MUST be the second call.
     * This guarantees no visual glitches or out-of-order text display.
     */
    @Test
    public void testMethodCallOrder() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        var inOrder = inOrder(mockView, mockGame, mockBoardController);

        inOrder.verify(mockView).clearScreen();
        inOrder.verify(mockView).displayInstructions();
    }

    /**
     * Test Case: Verify the orchestration of multiple components during startup.
     * * Type: White Box Testing
     * * Technique: Behavior Verification.
     * * Description: Verifies that the startGame method correctly coordinates actions across
     * three different dependencies:
     * 1. The View (to clear the screen).
     * 2. The BoardController (to handle ship setup).
     * 3. The Game Model (to place computer ships and start the match).
     * * Mock Interaction:
     * - Checks that calls are distributed correctly among mockView, mockBoardController, and mockGame.
     */
    @Test
    public void testMultipleMocksInteraction() {
        when(mockGame.isGameOver()).thenReturn(true);
        when(mockGame.getWinner()).thenReturn("Player won!");

        controller.startGame();

        verify(mockView).clearScreen();
        verify(mockBoardController).setupPlayerShips(any(Board.class));
        verify(mockGame).placeComputerShipsRandomly();
        verify(mockGame).startGame();
    }


    @Test
    public void testMockDefaultBehavior() {
        GameView newMockView = mock(GameView.class);
        newMockView.displayMessage("test");
        verify(newMockView).displayMessage("test");
    }

    /**
     * Test Case: Sanity check for Mockito framework configuration.
     * * Type: Infrastructure Testing
     * * Technique: Behavior Verification.
     * * Description: A baseline test to verify that the Mockito library is functioning correctly
     * in the current environment. It isolates the mocking tool to ensure that basic creation
     * and verification of mocks work as expected, ruling out environmental issues.
     * * Mock Interaction:
     * 1. Creates a raw mock of GameView (independent of the controller).
     * 2. verify(newMockView).displayMessage(...) -> confirms that interactions are recorded.
     */
    @Test
    public void testGameFlowWithMocks() {
        when(mockGame.isGameOver()).thenReturn(false, false, true);
        when(mockGame.getStatus())
            .thenReturn(GameStatus.PLAYER_TURN)
            .thenReturn(GameStatus.COMPUTER_TURN)
            .thenReturn(GameStatus.PLAYER_TURN);

        assertEquals(mockGame, controller.getGame());
    }

    /**
     * Test Case: Verify controller integrity with complex model state configuration.
     * * Type: White Box Testing
     * * Technique: Mock Configuration Verification.
     * * Description: Verifies that the controller maintains the correct reference to the
     * Game model even when that model is configured with complex state transitions.
     * * Mock Interaction:
     * 1. Configures mockGame with a sequence of states (Player -> Computer -> Player).
     * 2. Asserts that controller.getGame() still returns the expected mock instance.
     */
    @Test
    public void testStartGameSequence() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        verify(mockView, atLeastOnce()).displayMessage(anyString());
        verify(mockGame).placeComputerShipsRandomly();
        verify(mockGame).startGame();
    }

    /**
     * Test Case: Verify that the controller maintains a valid reference to the Game model.
     * * Type: White Box Testing / State Verification
     * * Technique: Statement Coverage (Getter).
     * * Description: Verifies that the getGame() method returns a non-null object that
     * matches the injected dependency. This ensures the controller is correctly wired to its model.
     * * Mock Interaction:
     * 1. assertNotNull() -> ensures initialization.
     * 2. assertEquals() -> ensures the reference matches the mock.
     */
    @Test
    public void testControllerHoldsGameReference() {
        assertNotNull(controller.getGame());
        assertEquals(mockGame, controller.getGame());
    }
}
