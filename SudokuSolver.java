import java.util.Scanner;

/**
 * Sudoku Solver - Console based Java application
 * Solves a 9x9 Sudoku puzzle using backtracking.
 *
 * Input format: Enter the grid row by row, 9 numbers per row, separated by spaces.
 * Use 0 for empty cells.
 *
 * Example row: 5 3 0 0 7 0 0 0 0
 */
public class SudokuSolver {

    static final int SIZE = 9;
    static int[][] board = new int[SIZE][SIZE];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("===== Sudoku Solver =====");
        System.out.println("Choose input method:");
        System.out.println("1. Enter your own puzzle");
        System.out.println("2. Use a sample puzzle");
        System.out.print("Enter choice: ");
        int choice = readInt(sc);

        if (choice == 2) {
            board = getSamplePuzzle();
        } else {
            System.out.println("Enter the Sudoku grid row by row (9 numbers per row, space-separated, 0 for empty cells):");
            for (int i = 0; i < SIZE; i++) {
                System.out.print("Row " + (i + 1) + ": ");
                for (int j = 0; j < SIZE; j++) {
                    board[i][j] = sc.nextInt();
                }
            }
        }

        System.out.println("\nPuzzle to solve:");
        printBoard(board);

        long start = System.currentTimeMillis();
        if (solve(board)) {
            long end = System.currentTimeMillis();
            System.out.println("\nSolved Puzzle:");
            printBoard(board);
            System.out.println("\nSolved in " + (end - start) + " ms.");
        } else {
            System.out.println("\nNo solution exists for this puzzle. Please check your input.");
        }

        sc.close();
    }

    // Backtracking solver
    static boolean solve(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(grid, row, col, num)) {
                            grid[row][col] = num;

                            if (solve(grid)) {
                                return true;
                            }

                            grid[row][col] = 0; // backtrack
                        }
                    }
                    return false; // no valid number found, trigger backtrack
                }
            }
        }
        return true; // all cells filled
    }

    static boolean isValid(int[][] grid, int row, int col, int num) {
        // Check row
        for (int x = 0; x < SIZE; x++) {
            if (grid[row][x] == num) return false;
        }

        // Check column
        for (int x = 0; x < SIZE; x++) {
            if (grid[x][col] == num) return false;
        }

        // Check 3x3 sub-box
        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;
        for (int i = boxRowStart; i < boxRowStart + 3; i++) {
            for (int j = boxColStart; j < boxColStart + 3; j++) {
                if (grid[i][j] == num) return false;
            }
        }

        return true;
    }

    static void printBoard(int[][] grid) {
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print((grid[i][j] == 0 ? "." : grid[i][j]) + " ");
            }
            System.out.println();
        }
    }

    static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    static int[][] getSamplePuzzle() {
        return new int[][]{
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
    }
}
