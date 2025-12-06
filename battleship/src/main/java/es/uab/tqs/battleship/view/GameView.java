package es.uab.tqs.battleship.view;

import es.uab.tqs.battleship.model.AttackResult;
import es.uab.tqs.battleship.model.Board;
import es.uab.tqs.battleship.model.Coordinate;
import es.uab.tqs.battleship.model.Orientation;

public interface GameView {

    void displayBoard(Board board, boolean hideShips);

    void displayMessage(String message);

    void displayAttackResult(AttackResult result);

    Coordinate getCoordinateInput(String prompt);

    Orientation getOrientationInput();

    void displayGameResult(String winner);

    void displayInstructions();

    void clearScreen();

    boolean getConfirmation(String prompt);
}

