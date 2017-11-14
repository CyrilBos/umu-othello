public class BetterCountingEvaluator extends NaiveCountingEvaluator {
    @Override
    public int evaluate(OthelloPosition position) {
        int whiteScore = 0, blackScore = 0;

        for(int row = 1; row <= OthelloPosition.BOARD_SIZE; row++) {
            for (int column = 1; column <= OthelloPosition.BOARD_SIZE; column++) {
                char cell = position.board[row][column];
                if(cell == 'W') {
                    whiteScore += calcCellScore(row, column);
                }
                else if (cell == 'B') {
                    blackScore += calcCellScore(row, column);
                }
            }
        }

        return whiteScore - blackScore;
    }

    private int calcCellScore(int row, int column) {
        //Tokens in corners are stable, i.e they can not be captured back by the opponent, so they are very valuable
        if (row == 1 && (column == 1 || column == OthelloPosition.BOARD_SIZE)
                || row == OthelloPosition.BOARD_SIZE && (column == 1 || column == OthelloPosition.BOARD_SIZE)) {
            return 4;
        }
        //Borders are generally harder to capture than other cells, so we favor the moves that help capture them
        if (row == 1 || row == OthelloPosition.BOARD_SIZE || column == 1 || column == OthelloPosition.BOARD_SIZE) {
            return 2;
        }
        //Inner borders help the other player capture the border, hence capturing them makes the move generally worse
        else if (row == 2 || row == OthelloPosition.BOARD_SIZE - 1 || column == 2 || column == OthelloPosition.BOARD_SIZE - 1) {
            return -1;
        }
        return 1;
    }
}
