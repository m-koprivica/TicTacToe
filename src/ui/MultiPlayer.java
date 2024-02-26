package ui;

import exceptions.WinnerException;
import model.Game;

import java.util.Scanner;

public class MultiPlayer {
    public static void main(String[] args) {
        Game g = new Game();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Tic-Tac-Toe!");
        System.out.println("Here are the coordinates for each of the grid's positions...\n");
        g.printGridCoords();
        System.out.println("Lets play!\n");
        while (true) {
            while(true) {
                System.out.println("Where do you want to place an X?");
                int coordinate = scanner.nextInt();
                if (g.placeX(coordinate)) {
                    g.printGridStatus();
                    break;
                } else {
                    System.out.println("Invalid position, please try again\n");
                    g.printGridStatus();
                }
            }
            checkForEndConditions(g);
            while (true) {
                System.out.println("Where do you want to place an O?");
                int coordinate = scanner.nextInt();
                if (g.placeO(coordinate)) {
                    g.printGridStatus();
                    break;
                } else {
                    System.out.println("Invalid position, please try again\n");
                    g.printGridStatus();
                }
            }
            checkForEndConditions(g);
        }
    }

    // Checks to see if game is won by someone, in stalemate, or still able to be played
    private static void checkForEndConditions(Game g) {
        try {
            if (!g.checkForWin()) {
                System.out.println("Stalemate!");
                System.exit(0);
            }
        } catch (WinnerException e) {
            System.out.println("Congratulations! Winner: " + e.getMessage() + " !");
            System.exit(0);
        }
    }
}
