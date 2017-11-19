public class BetterCountingEvaluator implements OthelloEvaluator {
    private static final int DIAGONAL_BASE_SCORE = -100;
    private static final int BORDER_BASE_SCORE = 10;
    private static final int INNER_BORDER_BASE_SCORE = -10;
    public static final int CORNER_SCORE = 1000;

    @Override
    public int evaluate(OthelloPosition position) {
        int score = tokenHeuristic(position);
        return score;
    }

    //TODO: pattern heuristic

    private int tokenHeuristic(OthelloPosition position) {
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

    private int calcCellScore(OthelloPosition position, int row, int column) {
        //Tokens in corners are stable, i.e they can not be captured back by the opponent, so they are very valuable
        if (row == 1 && (column == 1 || column == OthelloPosition.BOARD_SIZE)
                || row == OthelloPosition.BOARD_SIZE && (column == 1 || column == OthelloPosition.BOARD_SIZE)) {
            return CORNER_SCORE;
            //TODO: see if other aligned corners are already captured, if so, very good move.
        }
        else if (isTokenStable(position, row, column)) {
            return CORNER_SCORE;
        }
        //else if it is the top left to bottom right diagonal
        else if (row == column) {
            //the score is multiplied by the distance to the center
            return DIAGONAL_BASE_SCORE * getDistanceToCenter(row, OthelloPosition.BOARD_SIZE / 2);

        }
        // else if it is the top right to bottom left diagonal
        else if (row == OthelloPosition.BOARD_SIZE - column + 1) { // +1 because first board index is 1
            return DIAGONAL_BASE_SCORE * getDistanceToCenter(row, OthelloPosition.BOARD_SIZE / 2);
        }
        //Borders are generally harder to capture than other cells, so we favor the moves that help capture them
        else if (row == 1 || row == OthelloPosition.BOARD_SIZE || column == 1 || column == OthelloPosition.BOARD_SIZE) {
            return BORDER_BASE_SCORE * getDistanceToCenter(row, column);
        }
        //Inner borders help the other player capture the border and corners, hence capturing them makes the move generally worse
        else if (row == 2 || row == OthelloPosition.BOARD_SIZE - 1 || column == 2 || column == OthelloPosition.BOARD_SIZE - 1) {
            return -INNER_BORDER_BASE_SCORE * getDistanceToCenter(row, column);
        }
        else {
            return 0;
        }
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
     * @param position
     * @param row
     * @param column
     * @return
     */
    private boolean isTokenStable(OthelloPosition position, int row, int column) {
        char opponentChar = position.getPlayerDisc(!position.toMove());

        for (int rowDifference = -1; rowDifference <= 1; rowDifference++) {
            for (int columnDifference = -1; columnDifference <= 1; columnDifference++) {
                if (!(columnDifference == 0 && rowDifference == 0)) {
                    int searchRow = row + rowDifference;
                    int searchColumn = column + columnDifference;
                    if (position.isInsideBoard(searchRow, searchColumn) && position.board[searchRow][searchColumn] == 'E') {
                        if (position.isAPossibleMove(opponentChar, row, column, rowDifference, columnDifference)) {
                            return false;
                        }
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
