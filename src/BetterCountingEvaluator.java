/**
 * Class that implements a board evaluator. Its heuristic is mainly based on crucial cells:
 * - the corners, that are the most valuable
 * - the
 */
public class BetterCountingEvaluator implements OthelloEvaluator {
    private static final int BAD_C_SCORE = -12;
    private static final int BAD_X_SCORE = -22;
    private static final int STABLE_SCORE = 12;
    private static final int CORNER_SCORE = 22;

    @Override
    public int evaluate(OthelloPosition position) {
        int boardScore = boardScore(position);
        int playerMovesNumberScore = position.getMoves().size();

        OthelloPosition opponentPosition = position.clone();
        opponentPosition.playerToMove = false;
        int opponentMovesNumberScore = position.getMoves().size();
        //boardScore is white score - black score
        // so add playerMovesNumberScore if the white player is playing, else subtract it
        if (position.toMove())
            return boardScore + playerMovesNumberScore - opponentMovesNumberScore;
        else
            return boardScore - playerMovesNumberScore + opponentMovesNumberScore;
    }

    /**
     * returns the board score, being the difference white player score - black player score
     */
    private int boardScore(OthelloPosition position) {
        int whiteScore = 0;
        int blackScore = 0;
        for (int row = 1; row <= OthelloPosition.BOARD_SIZE; row++) {
            for (int column = 1; column <= OthelloPosition.BOARD_SIZE; column++) {
                char cell = position.board[row][column];
                if (cell == 'W') {
                    whiteScore += calcCellScore(position, row, column);
                } else if (cell == 'B') {
                    blackScore += calcCellScore(position, row, column);
                }
            }
        }

        return whiteScore - blackScore;
    }

    /**
     * returns the score of a disc depending on its properties (corner, stable, C disc, X disc)
     */
    private int calcCellScore(OthelloPosition position, int row, int column) {

        //Tokens in corners are the most valuable as they can not be captured back by the opponent
        if (row == 1 && (column == 1 || column == OthelloPosition.BOARD_SIZE)
                || row == OthelloPosition.BOARD_SIZE && (column == 1 || column == OthelloPosition.BOARD_SIZE)) {
            return CORNER_SCORE;
        }
        //Checks if a token is stable, i.e it can not be captured back. This is worth a lot but not as much as a corner
        else if (isTokenStable(position, row, column)) {
            return STABLE_SCORE;
        }
        else if (isABadCDisc(position, row, column)) {
            return BAD_C_SCORE;
        }
        else if (isABadXDisc(position, row, column)) {
            return BAD_X_SCORE;
        }
        return 1;
    }


    /**
     * Checks if the disc at [row][column] is stable, i.e in all directions opposite to adjacent empty cells
     * there are only player discs until the end of the board.
     */
    private boolean isTokenStable(OthelloPosition position, int row, int column) {
        char playerDisc = position.board[row][column];
        char opponentDisc = 'W';

        if (playerDisc == 'W')
            opponentDisc = 'B';

        for (int rowDifference = -1; rowDifference <= 1; rowDifference++) {
            for (int columnDifference = -1; columnDifference <= 1; columnDifference++) {
                if (!(columnDifference == 0 && rowDifference == 0)) {
                    int searchRow = row + rowDifference;
                    int searchColumn = column + columnDifference;
                    //look for a direction with an empty cell
                    if (position.isInsideBoard(searchRow, searchColumn) && position.board[searchRow][searchColumn] == 'E') {
                        int[] oppositeDirection = position.getOppositeDirection(rowDifference, columnDifference);
                        //loop through
                        while (position.isInsideBoard(searchRow, searchColumn) && position.board[searchRow][searchColumn] == playerDisc) {
                            searchRow += oppositeDirection[0];
                            searchColumn += oppositeDirection[1];
                        }
                        //in the opposite direction, if there is an opponent disc or an empty cell which might later on
                        // be captured by the opponent, then this is not a stable token.
                        if (position.isInsideBoard(searchRow, searchColumn) && position.board[searchRow][searchColumn] != playerDisc)
                            return false;

                    }
                }
            }
        }
        //the token is surrounded by player discs so it is stable
        return true;
    }


    /**
     * returns if a disc is at one of the C positions
     */
    private boolean isABadCDisc(OthelloPosition position, int row, int column) {
        //top left corner
        if (row == 1 && column == 2 || row == 2 && column == 1) {
            return position.board[1][1] == 'E';
            // bottom right corner
        } else if (row == OthelloPosition.BOARD_SIZE && column == OthelloPosition.BOARD_SIZE - 1
                || row == OthelloPosition.BOARD_SIZE - 1 && column == OthelloPosition.BOARD_SIZE) {
            return position.board[OthelloPosition.BOARD_SIZE][OthelloPosition.BOARD_SIZE] == 'E';
            //bottom left corner
        } else if (row == OthelloPosition.BOARD_SIZE - 1 && column == 1
                || row == OthelloPosition.BOARD_SIZE && column == 2 ) {
            return position.board[OthelloPosition.BOARD_SIZE][1] == 'E';
            //top right corner
        } else if (row == 1 && column == OthelloPosition.BOARD_SIZE - 1
                || row == 2 && column == OthelloPosition.BOARD_SIZE) {
            return position.board[1][OthelloPosition.BOARD_SIZE] == 'E';
        }
        return false;
    }

    /**
     * returns if a disc is at one of the X positions
     */
    private boolean isABadXDisc(OthelloPosition position, int row, int column) {
        //from top left to bottom right diagonal, next to corners
        return (row == column && (row == 2 || row == OthelloPosition.BOARD_SIZE - 1))
                //from top right to bottom left diagonal, next to corners
                || ((row == OthelloPosition.BOARD_SIZE - column + 1) && (row == 2 || row == OthelloPosition.BOARD_SIZE - 1));
    }




}
