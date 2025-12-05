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

    @Test
    public void testGameControllerDefaultConstructor() {
        GameView view = mock(GameView.class);
        GameController controller = new GameController(view);
        assertNotNull(controller.getGame());
    }

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

    @Test
    public void testProcessPlayerTurn_SunkShip() {
        Coordinate target = new Coordinate(1, 1);
        
        when(mockView.getCoordinateInput(anyString())).thenReturn(target);
        when(mockGame.processPlayerAttack(target)).thenReturn(AttackResult.SUNK);

        controller.processPlayerTurn();

        verify(mockView).displayAttackResult(AttackResult.SUNK);
        verify(mockView).displayMessage(org.mockito.ArgumentMatchers.contains("sunk an enemy ship"));
    }

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

    @Test
    public void testStartGameDisplaysInstructions() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        verify(mockView).clearScreen();
        verify(mockView).displayInstructions();
    }
    
    @Test
    public void testStartGameCallsBoardControllerSetup() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        verify(mockBoardController).setupPlayerShips(mockPlayerBoard);
    }

    @Test
    public void testStartGameInitializesGame() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        verify(mockGame).placeComputerShipsRandomly();
        verify(mockGame).startGame();
    }


    @Test
    public void testGetGameReturnsInjectedMock() {
        Game retrievedGame = controller.getGame();
        assertEquals(mockGame, retrievedGame);
    }


    @Test
    public void testStartGameCallsDisplayBoard() {
        when(mockGame.isGameOver()).thenReturn(true);
        when(mockGame.getWinner()).thenReturn("Player won!");

        controller.startGame();

        verify(mockView, atLeast(0)).displayBoard(any(Board.class), anyBoolean());
    }

    @Test
    public void testDisplayMessageCalled() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        verify(mockView, atLeastOnce()).displayMessage(anyString());
    }

    @Test
    public void testBoardControllerReceivesCorrectBoard() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        verify(mockBoardController).setupPlayerShips(mockPlayerBoard);
    }


    @Test
    public void testMethodCallOrder() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        var inOrder = inOrder(mockView, mockGame, mockBoardController);

        inOrder.verify(mockView).clearScreen();
        inOrder.verify(mockView).displayInstructions();
    }


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

    @Test
    public void testGameFlowWithMocks() {
        when(mockGame.isGameOver()).thenReturn(false, false, true);
        when(mockGame.getStatus())
            .thenReturn(GameStatus.PLAYER_TURN)
            .thenReturn(GameStatus.COMPUTER_TURN)
            .thenReturn(GameStatus.PLAYER_TURN);

        assertEquals(mockGame, controller.getGame());
    }

    @Test
    public void testStartGameSequence() {
        when(mockGame.isGameOver()).thenReturn(true);

        controller.startGame();

        verify(mockView, atLeastOnce()).displayMessage(anyString());
        verify(mockGame).placeComputerShipsRandomly();
        verify(mockGame).startGame();
    }

    @Test
    public void testControllerHoldsGameReference() {
        assertNotNull(controller.getGame());
        assertEquals(mockGame, controller.getGame());
    }
}
