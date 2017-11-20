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

        char playerDisc = getPlayerDisc(playerToMove);
        char opponentDisc = getPlayerDisc(!playerToMove);

        //for every cell
        for (int row = 1; row <= BOARD_SIZE; row++) {
            for (int column = 1; column <= BOARD_SIZE; column++) {
                //if the cell contains an opponent Disc
                if (board[row][column] == opponentDisc) {
                    //for every cell adjacent to the opponent disc
                    for (int rowDifference = -1; rowDifference <= 1; rowDifference++) {
                        for (int columnDifference = -1; columnDifference <= 1; columnDifference++) {
                            if (!(columnDifference == 0 && rowDifference == 0)) {
                                int testedRow = row + rowDifference;
                                int testedColumn = column + columnDifference;
                                //if an adjacent cell is empty
                                if (isInsideBoard(testedRow, testedColumn) && board[testedRow][testedColumn] == 'E') {
                                    //check if there a possible move in the opposite direction move
                                    if (isAPossibleMove(playerDisc, row, column, rowDifference, columnDifference)) {
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

    /**
     * Method that returns the char corresponding to the playerToMove value.
     */
    char getPlayerDisc(boolean playerToMove) {
        if (playerToMove) {
            return 'W';
        } else {
            return 'B';
        }
    }

    /**
     * Method that returns an array corresponding to the opposite direction, given in and given by a difference in row
     * index and a difference in column index.
     */
    private int[] getOppositeDirection(int rowDifference, int columnDifference) {
        int[] opposite = new int[2];
        opposite[0] = OPPOSITE_DIRECTION_VALUES[rowDifference + 1];
        opposite[1] = OPPOSITE_DIRECTION_VALUES[columnDifference + 1];
        return opposite;
    }

    /**
     * Method that returns if the parameter index is inside the board. Used when modifying board indexes with both
     * negative or positive values.
     */
    private boolean isInsideBoard(int index) {
        return index <= BOARD_SIZE && index >= 1;
    }

    /**
     * Method that returns if the indexes given as parameters are inside the board. Used when modifying board
     * indexes with both negative or positive values.
     */
    boolean isInsideBoard(int rowIndex, int columnIndex) {
        return isInsideBoard(rowIndex) && isInsideBoard(columnIndex);
    }

    /**
     * Method that returns if a move is possible. It is called by getMoves() when it finds an opponent in the cell
     * [row,column]. This method calculates the opposite direction and finds if there is a player token in that direction,
     * making it a valid move.
     */
    boolean isAPossibleMove(char playerChar, int row, int column, int rowDifference, int columnDifference) {
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
        //line full of opponent Discs
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
        OthelloPosition movedPosition = this.clone();

        if (!action.isPassMove()) {
            //check if making a move on the board
            if (!isInsideBoard(action.row) || !isInsideBoard(action.column)) {
                throw new IllegalMoveException(action);
            }

            //check if making a move on an empty cell
            char moveCell = board[action.row][action.column];
            if (moveCell != 'E') {
                throw new IllegalMoveException(action);
            }


            char playerDisc = getPlayerDisc(playerToMove);
            char opponentDisc = getPlayerDisc(!playerToMove);

            movedPosition.board[action.row][action.column] = playerDisc;

            boolean discsWereConverted = false;

            //for every direction, check if one there is one of the playerToMove Disc
            for (int rowDifference = -1; rowDifference <= 1; rowDifference++) {
                for (int columnDifference = -1; columnDifference <= 1; columnDifference++) {
                    if (!(rowDifference == 0 && columnDifference == 0)) {
                        int startRow = action.row + rowDifference;
                        int startColumn = action.column + columnDifference;

                        if (isInsideBoard(startRow, startColumn)) {
                            //save indexes values for later
                            int currentRow = startRow;
                            int currentColumn = startColumn;

                            char startCell = board[currentRow][currentColumn];
                            //if the cell is empty or if it is the playerToMove Disc, then there is nothing to do
                            if (startCell != 'E' && startCell != playerDisc) {
                                //else loop through all opponent Discs
                                while (isInsideBoard(currentRow, currentColumn) && board[currentRow][currentColumn] == opponentDisc) {
                                    currentRow += rowDifference;
                                    currentColumn += columnDifference;
                                }
                                //if the last cell is the playerToMove Disc, convert all the Discs in between
                                if (isInsideBoard(currentRow, currentColumn) && board[currentRow][currentColumn] == playerDisc) {
                                    int i = startRow;
                                    int j = startColumn;
                                    while (isInsideBoard(i, j) && board[i][j] == opponentDisc) {
                                        discsWereConverted = true;
                                        movedPosition.board[i][j] = playerDisc;
                                        i += rowDifference;
                                        j += columnDifference;
                                    }
                                }
                                //else it is an empty cell, so not a possible move

                            }
                        }
                    }
                }
            }
            //Illegal move if nothing was converted
            if (!discsWereConverted) {
                throw new IllegalMoveException(action);
            }
        }
        movedPosition.playerToMove = !playerToMove;
        return movedPosition;
    }

    boolean isGameEnded(OthelloAction action) throws IllegalMoveException {
        if (!action.equals(new OthelloAction(0,0))) {
            OthelloPosition newPosition = this.clone();
            newPosition.makeMove(action);
            if (newPosition.getMoves().isEmpty()) {
                if (newPosition.makeMove(new OthelloAction(0, 0, true)).getMoves().isEmpty()) {
                    return true;
                }
            }
        }
        return false;

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
