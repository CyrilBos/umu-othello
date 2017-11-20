public class BetterCountingEvaluator implements OthelloEvaluator {
    private static final int BAD_C_SCORE = -50;
    private static final int BAD_X_SCORE = -100;
    private static final int STABLE_SCORE = 100;
    public static final int CORNER_SCORE = 1000;

    @Override
    public int evaluate(OthelloPosition position) {
        int boardScore = boardScore(position);
        int playerMovesNumberScore = position.getMoves().size();

        OthelloPosition opponentPosition = position.clone();
        opponentPosition.playerToMove = false;
        int opponentMovesNumberScore = position.getMoves().size();
        if (position.toMove())
            return boardScore + playerMovesNumberScore - opponentMovesNumberScore;
        else
            return boardScore - playerMovesNumberScore + opponentMovesNumberScore;
    }

    //TODO: pattern heuristic

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

    private boolean isABadXDisc(OthelloPosition position, int row, int column) {
        //from top left to bottom right diagonal, next to corners
        return (row == column && (row == 2 || row == OthelloPosition.BOARD_SIZE - 1))
                //from top right to bottom left diagonal, next to corners
                || ((row == OthelloPosition.BOARD_SIZE - column + 1) && (row == 2 || row == OthelloPosition.BOARD_SIZE - 1));
    }

    private int calcCellScore(OthelloPosition position, int row, int column) {

        //Tokens in corners are the most valuable as they can not be captured back by the opponent
        if (row == 1 && (column == 1 || column == OthelloPosition.BOARD_SIZE)
                || row == OthelloPosition.BOARD_SIZE && (column == 1 || column == OthelloPosition.BOARD_SIZE)) {
            return CORNER_SCORE;
            //TODO: see if other aligned corners are already captured, if so, very good move.
        }
        //Checks if a token is stable, i.e it can not be captured back. This is worth a lot but not as much as a corner
        //TODO: debug is Token Stable. need to check triangles instead of every direction is not a possible move
        else if (isTokenStable(position, row, column)) {
            return STABLE_SCORE / 2;
        }
        else if (isABadCDisc(position, row, column)) {
            return BAD_C_SCORE;
        }
        else if (isABadXDisc(position, row, column)) {
            return BAD_X_SCORE;
        }
        return 0;
        /*
        //else if it is the top left to bottom right diagonal
        else if (row == column) {
            //the score is multiplied by the distance to the center
            return DIAGONAL_SCORE * getDistanceToCenter(row, OthelloPosition.BOARD_SIZE / 2);

        }
        // else if it is the top right to bottom left diagonal
        else if (row == OthelloPosition.BOARD_SIZE - column + 1) { // +1 because first board index is 1
            return DIAGONAL_SCORE * getDistanceToCenter(row, OthelloPosition.BOARD_SIZE / 2);
        } else {
            return 0;
        }
        */
    }

    private int getDistanceToCenter(int row, int column, int boardLength) {
        return getDistanceToCenter(row, boardLength) + getDistanceToCenter(column, boardLength);
    }

    private int getDistanceToCenter(int index, int boardLength) {
        if (index <= boardLength / 2)
            return boardLength / 2 - index;
        else
            return index - (boardLength / 2 + 1);
    }

    /**
     * TODO: move to OthelloPosition?
     * Checks if position.board[row][column] is stable, i.e there is no possible moves for the other player around it in
     * every direction
     *
     * @param position
     * @param row
     * @param column
     * @return
     */
    private boolean isTokenStable(OthelloPosition position, int row, int column) {
        char opponentChar = 'W';

        if (position.board[row][column] == 'W')
            opponentChar = 'B';

        for (int rowDifference = -1; rowDifference <= 1; rowDifference++) {
            for (int columnDifference = -1; columnDifference <= 1; columnDifference++) {
                if (!(columnDifference == 0 && rowDifference == 0)) {
                    int searchRow = row + rowDifference;
                    int searchColumn = column + columnDifference;
                    if (position.isInsideBoard(searchRow, searchColumn) && position.board[searchRow][searchColumn] == 'E') {
                    }
                }
            }
        }
        return true;
    }

    private int stabilityHeuristic() {
        return 0;
    }

    private int parityHeuristic() {
        return 0;
    }
}
