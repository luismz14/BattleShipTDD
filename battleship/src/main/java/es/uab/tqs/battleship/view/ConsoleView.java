package es.uab.tqs.battleship.view;

import java.util.Scanner;

import es.uab.tqs.battleship.model.AttackResult;
import es.uab.tqs.battleship.model.Board;
import es.uab.tqs.battleship.model.Cell;
import es.uab.tqs.battleship.model.CellState;
import es.uab.tqs.battleship.model.Coordinate;

public class ConsoleView implements GameView {
    private final Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void displayBoard(Board board, boolean hideShips) {
        System.out.println("\n  0 1 2 3 4 5 6 7 8 9");
        System.out.println("  -------------------");

        for (int y = 0; y < board.getSize(); y++) {
            System.out.print(y + "|");

            for (int x = 0; x < board.getSize(); x++) {
                Cell cell = board.getCell(x, y);
                System.out.print(getCellDisplay(cell, hideShips) + " ");
            }

            System.out.println("|");
        }

        System.out.println("  -------------------\n");
    }

    private String getCellDisplay(Cell cell, boolean hideShips) {
        CellState state = cell.getState();

        switch (state) {
            case HIT:
                return "X";  // Hit
            case MISS:
                return "O";  // Water
            case SHIP:
                return hideShips ? "~" : "S";  // Ship (hidden if hideShips is true)
            case EMPTY:
            default:
                return "~";  // Water not attacked
        }
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayAttackResult(AttackResult result) {
        System.out.println("\n>>> " + result.getMessage() + " <<<\n");
    }

    @Override
    public Coordinate getCoordinateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (format: x y): ");
                String input = scanner.nextLine().trim();
                String[] parts = input.split("\\s+");

                if (parts.length != 2) {
                    displayMessage("Error: Enter two numbers separated by a space");
                    continue;
                }

                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);

                Coordinate coord = new Coordinate(x, y);

                if (!coord.isValid(10)) {
                    displayMessage("Error: Coordinates off board (0-9)");
                    continue;
                }

                return coord;

            } catch (NumberFormatException e) {
                displayMessage("Error: Enter valid numbers");
            }
        }
    }

    @Override
    public void displayGameResult(String winner) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("       END OF THE GAME");
        System.out.println("=".repeat(40));
        System.out.println(winner);
        System.out.println("=".repeat(40) + "\n");
    }

    @Override
    public void displayInstructions() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          BATTLESHIP");
        System.out.println("=".repeat(50));
        System.out.println("\nObjective: Sink all enemy ships\n");
        System.out.println("Available boats:");
        System.out.println("  - Carrier (5 cells)");
        System.out.println("  - Battleship (4 cells)");
        System.out.println("  - Cruiser (3 cells)");
        System.out.println("  - Submarine (3 cells)");
        System.out.println("  - Destroyer (2 cells)");
        System.out.println("\nSymbols:");
        System.out.println("  ~ = Water not attacked");
        System.out.println("  O = Attacked water (miss)");
        System.out.println("  X = Impact on boat");
        System.out.println("  S = Your ship (only visible on your board)");
        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    @Override
    public void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    @Override
    public boolean getConfirmation(String prompt) {
        System.out.print(prompt + " (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("s") || response.equals("si") || 
                response.equals("y") || response.equals("yes");
    }

    public void close() {
        scanner.close();
    }
}
