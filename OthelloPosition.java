import java.util.*;
import java.lang.*;

/**
 * This class is used to represent game positions. It uses a 2-dimensional char
 * array for the board and a Boolean to keep track of which player has the move.
 *
 * @author Henrik Bj&ouml;rklund
 */

public class OthelloPosition {
    /**
     * An array used to get an opposite direction, which are row and column differences from the central cell.
     * Used by isAPossibleMove(), used by getMoves()
     */
    protected static final int[] OPPOSITE_DIRECTION_VALUES = {1, 0, -1};

    /**
     * For a normal Othello game, BOARD_SIZE is 8.
     */
    protected static final int BOARD_SIZE = 8;

    /**
     * True if the first player (white) has the move.
     */
    protected boolean playerToMove;

    /**
     * The representation of the board. For convenience, the array actually has
     * two columns and two rows more that the actual game board. The 'middle' is
     * used for the board. The first index is for rows, and the second for
     * columns. This means that for a standard 8x8 game board,
     * <code>board[1][1]</code> represents the upper left corner,
     * <code>board[1][8]</code> the upper right corner, <code>board[8][1]</code>
     * the lower left corner, and <code>board[8][8]</code> the lower left
     * corner. In the array, the charachters 'E', 'W', and 'B' are used to
     * represent empty, white, and black board squares, respectively.
     */
    protected char[][] board;

    /**
     * Creates a new position and sets all squares to empty.
     */
    public OthelloPosition() {
        board = new char[BOARD_SIZE + 2][BOARD_SIZE + 2];
        for (int i = 0; i < BOARD_SIZE + 2; i++)
            for (int j = 0; j < BOARD_SIZE + 2; j++)
                board[i][j] = 'E';

    }

    public OthelloPosition(String s) {
        if (s.length() != 65) {
            board = new char[BOARD_SIZE + 2][BOARD_SIZE + 2];
            for (int i = 0; i < BOARD_SIZE + 2; i++)
                for (int j = 0; j < BOARD_SIZE + 2; j++)
                    board[i][j] = 'E';
        } else {
            board = new char[BOARD_SIZE + 2][BOARD_SIZE + 2];
            if (s.charAt(0) == 'W') {
                playerToMove = true;
            } else {
                playerToMove = false;
            }
            for (int i = 1; i <= 64; i++) {
                char c;
                if (s.charAt(i) == 'E') {
                    c = 'E';
                } else if (s.charAt(i) == 'O') {
                    c = 'W';
                } else {
                    c = 'B';
                }
                int column = ((i - 1) % 8) + 1;
                int row = (i - 1) / 8 + 1;
                board[row][column] = c;
            }
        }

    }

    /**
     * Initializes the position by placing four markers in the middle of the
     * board.
     */
    public void initialize() {
        board[BOARD_SIZE / 2][BOARD_SIZE / 2] = board[BOARD_SIZE / 2 + 1][BOARD_SIZE / 2 + 1] = 'W';
        board[BOARD_SIZE / 2][BOARD_SIZE / 2 + 1] = board[BOARD_SIZE / 2 + 1][BOARD_SIZE / 2] = 'B';
        playerToMove = true;
        //TODO: REMOVE
        /*board[BOARD_SIZE / 2][BOARD_SIZE / 2] = board[BOARD_SIZE / 2 + 1][BOARD_SIZE / 2 + 1] = 'B';
        board[BOARD_SIZE / 2][BOARD_SIZE / 2 + 1] = board[BOARD_SIZE / 2 + 1][BOARD_SIZE / 2] = 'W';
        playerToMove = false;*/
    }

	/* getMoves and helper functions */

    /**
     * Returns a linked list of <code>OthelloAction</code> representing all
     * possible moves in the position. If the list is empty, there are no legal
     * moves for the player who has the move.
     */

    public LinkedList<OthelloAction> getMoves() {

		/*
         * TODO: write the code for this method and whatever helper methods it
		 * needs
		 */

        LinkedList<OthelloAction> moves = new LinkedList<>();

        char playerToken = getPlayerToken(playerToMove);
        char opponentToken = getPlayerToken(!playerToMove);

        //for every cell
        for (int row = 1; row <= BOARD_SIZE; row++) {
            for (int column = 1; column <= BOARD_SIZE; column++) {
                //if the cell contains an opponent token
                if (board[row][column] == opponentToken) {
                    //for every cell adjacent to the opponent token
                    for (int rowDifference = -1; rowDifference <= 1; rowDifference++) {
                        for (int columnDifference = -1; columnDifference <= 1; columnDifference++) {
                            if (!(columnDifference == 0 && rowDifference == 0)) {
                                int testedRow = row + rowDifference;
                                int testedColumn = column + columnDifference;
                                //if an adjacent cell is empty
                                if (isInsideBoard(testedRow, testedColumn) && board[testedRow][testedColumn] == 'E') {
                                    //check if there a possible move in the opposite direction move
                                    if (isAPossibleMove(playerToken, row, column, rowDifference, columnDifference)) {
                                        OthelloAction possibleAction = new OthelloAction(testedRow, testedColumn);

                                        //if, it does not already exist, add it to the possible moves
                                        if (!moves.contains(possibleAction))
                                            moves.add(possibleAction);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return moves;
    }

    char getPlayerToken(boolean playerToMove) {
        if (playerToMove) {
            return 'W';
        } else {
            return 'B';
        }
    }

    private int[] getOppositeDirection(int row_difference, int col_difference) {
        int[] opposite = new int[2];
        opposite[0] = OPPOSITE_DIRECTION_VALUES[row_difference + 1];
        opposite[1] = OPPOSITE_DIRECTION_VALUES[col_difference + 1];
        return opposite;
    }

    /**
     * Method that returns if the parameter index is inside the board. Used when modifying board indexes with both
     * negative or positive values.
     */
    private boolean isInsideBoard(int index) {
        return index <= BOARD_SIZE && index >= 1;
    }

    private boolean isInsideBoard(int row_index, int column_index) {
        return isInsideBoard(row_index) && isInsideBoard(column_index);
    }


    private boolean isAPossibleMove(char playerChar, int row, int column, int rowDifference, int columnDifference) {
        //get the opposite direction to the current position
        int[] oppositeDirections = getOppositeDirection(rowDifference, columnDifference);

        int searchRow = row + oppositeDirections[0];
        int searchColumn = column + oppositeDirections[1];

        while (isInsideBoard(searchRow, searchColumn)) {
            char searchCell = board[searchRow][searchColumn];
            //if we find one of the player token, then it is a possible move
            if (searchCell == playerChar) {
                return true;
            }
            //if we find an empty cell, it is not a possible move
            else if (searchCell == 'E') {
                return false;
            }
            //else it is an opponent token, so we need to check further (unless it is outside the board)
            searchColumn += oppositeDirections[1];
            searchRow += oppositeDirections[0];
        }
        //line full of opponent tokens
        return false;
    }

	/* toMove */

    /**
     * Returns true if the first player (white) has the move, otherwise false.
     */
    public boolean toMove() {
        return playerToMove;
    }

	/* makeMove and helper functions */

    /**
     * Returns the position resulting from making the move <code>action</code>
     * in the current position. Observe that this also changes the player to
     * move next.
     */
    public OthelloPosition makeMove(OthelloAction action)
            throws IllegalMoveException {

		/*
         * TODO: write the code for this method and whatever helper functions it
		 * needs.
		 */

        //check if making a move on the board
        if (!isInsideBoard(action.row) || !isInsideBoard(action.column)) {
            throw new IllegalMoveException(action);
        }

        //check if making a move on an empty cell
        char moveCell = board[action.row][action.column];
        if (moveCell != 'E') {
            throw new IllegalMoveException(action);
        }
        //legal action
        OthelloPosition movedPosition = this.clone();

        char playerToken = getPlayerToken(playerToMove);
        char opponentToken = getPlayerToken(!playerToMove);

        movedPosition.board[action.row][action.column] = playerToken;
        //for every direction, check if one there is one of the playerToMove token
        for (int rowDifference = -1; rowDifference <= 1; rowDifference++) {
            for (int columnDifference = -1; columnDifference <= 1; columnDifference++) {
                if (!(rowDifference == 0 && columnDifference == 0)) {
                    int startRow = action.row + rowDifference;
                    int startColumn = action.column + columnDifference;

                    if (isInsideBoard(startRow, startColumn)) {
                        int currentRow = startRow;
                        int currentColumn = startColumn;

                        char startCell = board[currentRow][currentColumn];
                        //if the cell is empty or if it is the playerToMove token, then there is nothing to do
                        if (startCell != 'E' && startCell != playerToken) {
                            //else loop through all opponent tokens
                            while (isInsideBoard(currentRow, currentColumn) && board[currentRow][currentColumn] == opponentToken) {
                                currentRow += rowDifference;
                                currentColumn += columnDifference;
                            }
                            //if the last cell is the playerToMove token, convert all the tokens in between
                            if (isInsideBoard(currentRow, currentColumn) && board[currentRow][currentColumn] == playerToken) {
                                int i = startRow;
                                int j = startColumn;
                                while (isInsideBoard(i, j) && board[i][j] == opponentToken) {
                                    movedPosition.board[i][j] = playerToken;
                                    i += rowDifference;
                                    j += columnDifference;
                                }
                            }
                            //else it is an empty cell so no token to capture
                        }
                    }
                }
            }
        }

        movedPosition.playerToMove = !playerToMove;
        return movedPosition;
    }

    /**
     * Returns a new <code>OthelloPosition</code>, identical to the current one.
     */
    protected OthelloPosition clone() {
        OthelloPosition newPosition = new OthelloPosition();
        newPosition.playerToMove = playerToMove;
        for (int i = 0; i < BOARD_SIZE + 2; i++)
            for (int j = 0; j < BOARD_SIZE + 2; j++)
                newPosition.board[i][j] = board[i][j];
        return newPosition;
    }

	/* illustrate and other output functions */

    /**
     * Draws an ASCII representation of the position. White squares are marked
     * by '0' while black squares are marked by 'X'.
     */
    public void illustrate() {
        System.out.print("   ");
        for (int i = 1; i <= BOARD_SIZE; i++)
            System.out.print("| " + i + " ");
        System.out.println("|");
        printHorizontalBorder();
        for (int i = 1; i <= BOARD_SIZE; i++) {
            System.out.print(" " + i + " ");
            for (int j = 1; j <= BOARD_SIZE; j++) {
                if (board[i][j] == 'W') {
                    System.out.print("| 0 ");
                } else if (board[i][j] == 'B') {
                    System.out.print("| X ");
                } else {
                    System.out.print("|   ");
                }
            }
            System.out.println("| " + i + " ");
            printHorizontalBorder();
        }
        System.out.print("   ");
        for (int i = 1; i <= BOARD_SIZE; i++)
            System.out.print("| " + i + " ");
        System.out.println("|\n");
    }

    private void printHorizontalBorder() {
        System.out.print("---");
        for (int i = 1; i <= BOARD_SIZE; i++) {
            System.out.print("|---");
        }
        System.out.println("|---");
    }

    public String toString() {
        String s = "";
        char c, d;
        if (playerToMove) {
            s += "W";
        } else {
            s += "B";
        }
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                d = board[i][j];
                if (d == 'W') {
                    c = 'O';
                } else if (d == 'B') {
                    c = 'X';
                } else {
                    c = 'E';
                }
                s += c;
            }
        }
        return s;
    }

}
