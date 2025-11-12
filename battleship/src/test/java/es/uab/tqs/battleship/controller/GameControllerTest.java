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

import es.uab.tqs.battleship.model.Board;
import es.uab.tqs.battleship.model.Game;
import es.uab.tqs.battleship.model.GameStatus;
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
