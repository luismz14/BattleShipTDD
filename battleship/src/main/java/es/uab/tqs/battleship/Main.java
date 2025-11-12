package es.uab.tqs.battleship;

import es.uab.tqs.battleship.controller.GameController;
import es.uab.tqs.battleship.view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        GameController controller = new GameController(view);

        controller.startGame();

        view.close();
    }
}
