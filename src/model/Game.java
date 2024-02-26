package model;

import exceptions.WinnerException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    List<Character> b; // the board

    // EFFECTS: Creates the Tic Tac Toe grid with nine spots
    public Game() {
        b = new ArrayList<>();
        setUpGridWithNulls();
    }

    // Helper function to set up the grid with 9 null values
    private void setUpGridWithNulls() {
        for (int i = 0; i < 9; i++) {
            b.add(null);
        }
    }

    // MODIFIES: this
    // EFFECTS: places an 'X' at specified position and outputs true
    // If position is already occupied by a non-null value, returns false
    public boolean placeX(int pos) {
        if (b.get(pos-1) == null) {
            b.set(pos - 1, 'X');
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: places an 'O' at specified position and outputs true
    // If position is already occupied by a non-null value, returns false
    public boolean placeO(int pos) {
        if (b.get(pos-1) == null) {
            b.set(pos - 1, 'O');
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: Throws WinnerException with "X" or "O" depending on who won.
    // Else, outputs true if the game is not in stalemate, false otherwise.
    public boolean checkForWin() throws WinnerException {
        for (int i = 0; i < 3; i++) {
            if (b.get(0) != null && b.get(3) != null && b.get(6) != null) {
                boolean col = b.get(i) == b.get(i + 3) && b.get(i) == b.get(i + 6);
                if (col) {
                    throw new WinnerException(b.get(i));
                }
            }
        }
        for (int i = 0; i < 7; i=i+3) {
            if (b.get(i) != null) {
                boolean row = b.get(i) == (b.get(i+1)) && b.get(i) == (b.get(i+2));
                if (row) {
                    throw new WinnerException(b.get(i));
                }
            }
        }
        if (b.get(4) != null) { //Checks for diagonals
            if (b.get(4) == (b.get(0)) && b.get(4) == (b.get(8))) {
                throw new WinnerException(b.get(4));
            }
            if (b.get(4) == (b.get(2)) && b.get(4) == (b.get(6))) {
                throw new WinnerException(b.get(4));
            }
        }
        // Checks for stalemate
        for (int i = 0; i < 9; i++) {
            if (b.get(i) == null) {
                return true; // not a draw... yet
            }
        }
        return false; // stalemate
    }

    // Methods for game AI

    // MODIFIES: this
    // EFFECTS: adds an X at a good tactical position automatically
    public void computersTurn() {
        int max = -1;
        int bestPos = 0;
        // Check for good position in the rows
        for (int i = 0; i < 7; i = i+3) {
            if (getNumTicksInRow('X', i) > max) {
                if (getNumTicksInRow('X', i) + getNumTicksInRow('O', i) != 3) {
                    max = getNumTicksInRow('X', i);
                    bestPos = findNonOccupiedPosInRow(i); // already inc by 1
                }
            }
        }
        // Check for good position in the columns
        for (int i = 0; i < 3; i++) {
            if (getNumTicksInCol('X', i) + getNumTicksInCol('O', i) != 3) {
                if (getNumTicksInCol('X', i) > max) {
                    max = getNumTicksInCol('X', i);
                    bestPos = findNonOccupiedPosInCol(i);
                }
            }
        }
        // Check for good position in the diagonal from top left to bottom right
        if (getNumTicksInDiagTopLeft('X') + getNumTicksInDiagTopLeft('O') != 3) {
            if (getNumTicksInDiagTopLeft('X') > max) {
                max = getNumTicksInDiagTopLeft('X');
                bestPos = findNonOccupiedPosInDiagTopLeft();
            }
        }
        // Check for good position in the diagonal from top right to bottom left
        if (getNumTicksInDiagTopRight('X') + getNumTicksInDiagTopRight('O') != 3) {
            if (getNumTicksInDiagTopRight('X') > max) {
                max = getNumTicksInDiagTopRight('X');
                bestPos = findNonOccupiedPosInDiagTopRight();
            }
        }
        // If empty board, places an X at a random spot
        if (numberOfNulls() == 9) {
            bestPos = ThreadLocalRandom.current().nextInt(1, 9);
        }
        placeX(bestPos);
    }

    // EFFECTS: Outputs the number of empty positions on the board
    public int numberOfNulls() {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            if (b.get(i) == null)
                count++;
        }
        return count;
    }

    // REQUIRES: i = 0, 3, or 6, c is 'X' or 'O'
    // EFFECTS: finds the number of given tick in row i
    private int getNumTicksInRow(Character c, int i) {
        int num = 0;
        for (int j = i; j < i+3; j++) {
            if (b.get(j) == c) {
                num++;
            }
        }
        return num;
    }

    // REQUIRES: i = 0, 1, or 2, c is 'X' or 'O'
    // EFFECTS: finds the number of given tick in column i
    private int getNumTicksInCol(Character c, int i) {
        int num = 0;
        for (int j = i; j < i+7; j=j+3) {
            if (b.get(j) == c) {
                num++;
            }
        }
        return num;
    }

    // REQUIRES: c = 0, 1, 2
    // EFFECTS: returns an empty position in the specified column
    // Returns -1 otherwise
    private int findNonOccupiedPosInCol(int c) {
        for (int i = c; i < 9; i=i+3) {
            if (b.get(i) == null) {
                return i+1;
            }
        }
        return -1;
    }

    // REQUIRES: c = 0, 3, 6
    // EFFECTS: returns an empty position in the specified row
    // Returns -1 otherwise
    private int findNonOccupiedPosInRow(int r) {
        for (int i = r; i < r+3; i++) {
            if (b.get(i) == null) {
                return i+1;
            }
        }
        return -1; // unable to find non-occupied position
    }

    // EFFECTS: returns an empty position in the diagonal from top left
    // to bottom right. Returns -1 otherwise
    private int findNonOccupiedPosInDiagTopLeft() {
        if (b.get(0) != null)
            return 1;
        if (b.get(4) != null)
            return 5;
        if (b.get(8) != null)
            return 9;
        return -1;
    }

    // EFFECTS: returns an empty position in the diagonal from top right
    // to bottom left. Returns -1 otherwise
    private int findNonOccupiedPosInDiagTopRight() {
        if (b.get(2) != null)
            return 3;
        if (b.get(4) != null)
            return 5;
        if (b.get(6) != null)
            return 7;
        return -1;
    }

    // REQUIRES: c = 'X' or 'O'
    // EFFECTS: returns number of specified tick in the top left diagonal
    private int getNumTicksInDiagTopLeft(Character c) {
        int num = 0;
        if (b.get(0) == c)
            num++;
        if (b.get(4) == c)
            num++;
        if (b.get(8) == c)
            num++;
        return num;
    }

    // REQUIRES: c = 'X' or 'O'
    // EFFECTS: returns number of specified tick in the top right diagonal
    private int getNumTicksInDiagTopRight(Character c) {
        int num = 0;
        if (b.get(2) == c)
            num++;
        if (b.get(4) == c)
            num++;
        if (b.get(6) == c)
            num++;
        return num;
    }

    // printers

    public void printGridCoords() {
        System.out.println("\n" +
                "   1   |   2   |   3   \n" +
                "-----------------------\n" +
                "   4   |   5   |   6   \n" +
                "-----------------------\n" +
                "   7   |   8   |   9   \n");
    }

    public void printGridStatus() {
        System.out.println("\n" +
                "   " + b.get(0) + "   |   " + b.get(1) + "   |   " + b.get(2) + "   \n" +
                "-----------------------\n" +
                "   " + b.get(3) + "   |   " + b.get(4) + "   |   " + b.get(5) + "   \n" +
                "-----------------------\n" +
                "   " + b.get(6) + "   |   " + b.get(7) + "   |   " + b.get(8) + "   \n");
    }
}
