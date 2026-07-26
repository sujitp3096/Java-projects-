import java.util.Scanner;

public class TicTacToe {

    static char[][] board = {
            {'1', '2', '3'},
            {'4', '5', '6'},
            {'7', '8', '9'}
    };

    static char currentPlayer = 'X';

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            printBoard();

            System.out.print("Player " + currentPlayer + ", Enter Position (1-9): ");
            int position = sc.nextInt();

            if (!placeMark(position)) {
                System.out.println("Invalid Move! Try Again.");
                continue;
            }

            if (checkWinner()) {
                printBoard();
                System.out.println("🎉 Player " + currentPlayer + " Wins!");
                break;
            }

            if (isBoardFull()) {
                printBoard();
                System.out.println("🤝 Match Draw!");
                break;
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        sc.close();
    }

    static void printBoard() {

        System.out.println();

        for (int i = 0; i < 3; i++) {

            System.out.println(" " + board[i][0] + " | " + board[i][1] + " | " + board[i][2]);

            if (i < 2)
                System.out.println("---|---|---");
        }

        System.out.println();
    }

    static boolean placeMark(int pos) {

        int row = (pos - 1) / 3;
        int col = (pos - 1) % 3;

        if (pos < 1 || pos > 9)
            return false;

        if (board[row][col] == 'X' || board[row][col] == 'O')
            return false;

        board[row][col] = currentPlayer;
        return true;
    }

    static boolean checkWinner() {

        // Rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer &&
                board[i][1] == currentPlayer &&
                board[i][2] == currentPlayer)
                return true;
        }

        // Columns
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == currentPlayer &&
                board[1][i] == currentPlayer &&
                board[2][i] == currentPlayer)
                return true;
        }

        // Diagonals
        if (board[0][0] == currentPlayer &&
            board[1][1] == currentPlayer &&
            board[2][2] == currentPlayer)
            return true;

        if (board[0][2] == currentPlayer &&
            board[1][1] == currentPlayer &&
            board[2][0] == currentPlayer)
            return true;

        return false;
    }

    static boolean isBoardFull() {

        for (char[] row : board) {
            for (char cell : row) {
                if (cell != 'X' && cell != 'O')
                    return false;
            }
        }

        return true;
    }
}
