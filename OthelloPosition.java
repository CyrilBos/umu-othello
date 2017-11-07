import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

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
     * An array to get an opposite direction, which are row and column differences from the central cell.
     * Used by addPossibleMoves(), used by getMoves()
     */
    protected static final int opposite_direction_value[] = {1, 0, -1};


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
        LinkedList<OthelloAction> moves = new LinkedList<OthelloAction>();
        char player_char = 'W';
        char other_char = 'B';
        if (!playerToMove) {
            player_char = 'B'; //if player is black, we want to focus on white tokens
            other_char = 'W';
        }
        //for every other player token
        for (int row = 1; row < BOARD_SIZE; row++) {
            for (int col = 1; col < BOARD_SIZE; col++) {
                if (board[row][col] == other_char) {
                    //for every cell adjacent to the other player token
                    for (int row_difference = -1; row_difference <= 1; row_difference++) {
                        for (int col_difference = -1; col_difference <= 1; col_difference++) {
                            //if a cell is empty, check if there is a possible move
                            if (board[row - row_difference][col - col_difference] == 'E') {
                                addPossibleMoves(moves, player_char, row, col);
                            }
                        }
                    }
                }
            }
        }

        return moves;
    }

    private int[] getOppositeDirection(int row_difference, int col_difference) {
        int[] opposite = new int[];
        opposite[0] = opposite_direction_value[row_difference + 1];
        opposite[1] = opposite_direction_value[col_difference + 1];
        return opposite;
    }

    private boolean getSearchCondition(int increment, int current) throws IllegalArgumentException {
        switch (increment) {
            case 1:
                return current < BOARD_SIZE;
            case 0:
                return true;
            case -1:
                return current > 1;
        }

        throw new IllegalArgumentException("getSearchCondition() called with wrong increment");
    }


    private void addPossibleMoves(LinkedList<OthelloAction> moves, char player_char, int row, int col) {
        boolean stop_condition = false;

        int[] opposite_direction = getOppositeDirection(row, col);
        int search_row = row + opposite_direction[0];
        int search_col = col + opposite_direction[1];
        for (; getSearchCondition(search_row, opposite_direction[0]) && !stop_condition; search_row += opposite_direction[0]) {
            for (; getSearchCondition(search_col, opposite_direction[1]) && !stop_condition; search_col += opposite_direction[1]) {
                if (board[search_row][search_col] == 'E') {
                    stop_condition = true;
                } else if (board[search_row][search_col] == player_char) {
                    stop_condition = true;
                    OthelloAction action = new OthelloAction(row, col);
                    if (!moves.contains(action)) {
                        moves.add(action);
                    }
                }
            }
        }
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
        OthelloPosition movedPosition = new OthelloPosition();


        playerToMove = !playerToMove;
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
