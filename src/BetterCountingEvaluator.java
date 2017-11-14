public class BetterCountingEvaluator extends NaiveCountingEvaluator {
    @Override
    public int evaluate(OthelloPosition position) {
        int score = 0;
        char playerToken = position.getPlayerToken(true);

        for(int row = 1; row <= OthelloPosition.BOARD_SIZE; row++) {
            for (int column = 1; column <= OthelloPosition.BOARD_SIZE; column++) {
                char cell = position.board[row][column];
                if(cell == playerToken) {
                    if (row == 1 && (column == 1 || column == OthelloPosition.BOARD_SIZE)
                            || row == OthelloPosition.BOARD_SIZE && (column == 1 || column == OthelloPosition.BOARD_SIZE)) {
                        score += 4;
                    }
                    //Borders are generally harder to capture than other cells, so we favor the moves that help capture them
                    if (row == 1 || row == OthelloPosition.BOARD_SIZE || column == 1 || column == OthelloPosition.BOARD_SIZE) {
                        score += 2;
                    }
                    //Inner borders help the other player capture the border, hence capturing them makes the move generally worse
                    else if (row == 2 || row == OthelloPosition.BOARD_SIZE - 1 || column == 2 || column == OthelloPosition.BOARD_SIZE - 1) {
                        score -= 1;
                    }
                    score += 1;
                }
            }
        }

        return score;
    }
}
