import java.util.LinkedList;

/**
 * Class that implements the Alpha Beta algorithm in a recursive way. It stops by throwing a OutOfTimeException when
 * reaching timeLimitStamp.
 */
public class AlphaBeta implements OthelloAlgorithm {
    private OthelloEvaluator evaluator;
    private int searchDepth;
    private long timeLimitStamp;


    public AlphaBeta(long timeLimitStamp) {
        this.timeLimitStamp = timeLimitStamp;
    }

    @Override
    public void setEvaluator(OthelloEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public void setSearchDepth(int depth) {
        this.searchDepth = depth;
    }

    /**
     * Implements AlphaBeta, calling MaxValue() or MinValue() depending on which player the AI is playing.
     * @param position Starting position
     * @return Best OthelloAction possible found during the search
     * @throws IllegalMoveException Thrown if an illegal move is made during the search
     * @throws OutOfTimeException Thrown if the search can not continue under the time limit
     */
    @Override
    public OthelloAction evaluate(OthelloPosition position) throws IllegalMoveException, OutOfTimeException {
        int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
        if (position.playerToMove) {
            return this.maxValue(position, alpha, beta, this.searchDepth);
        } else {
            return this.minValue(position, alpha, beta, this.searchDepth);
        }
    }



    /**
     * Implements Max side of AlphaBeta search.
     *
     */
    private OthelloAction maxValue(OthelloPosition position, int alpha, int beta, int depth) throws IllegalMoveException, OutOfTimeException {
        LinkedList<OthelloAction> moves = position.getMoves();
        //reached max depth
        if (depth == 0) {
            return maxDepthScore(position);
        }
        //reached leaf position
        else if (moves.isEmpty()) {
            return leafScore(position);
        } else {
            int value = Integer.MIN_VALUE;

            OthelloAction bestMove = new OthelloAction(0, 0);
            for (OthelloAction move : moves) {
                //check time every move made
                if (System.currentTimeMillis() > timeLimitStamp)
                    throw new OutOfTimeException();

                OthelloPosition next_position = position.makeMove(move);
                OthelloAction moveResult = minValue(next_position, alpha, beta, depth - 1);
                //replace the bestMove if this one is better
                if (moveResult.getValue() > value) {
                    value = moveResult.getValue();
                    move.value = value;
                    bestMove = move;
                }
                //the new move is not as good as the best of this depth: cut the search
                if (value >= beta) {
                    return move;
                }

                alpha = Integer.max(alpha, value);
            }
            return bestMove;
        }
    }


    /**
     * Implements Min side of AlphaBeta search.
     */
    private OthelloAction minValue(OthelloPosition position, int alpha, int beta, int depth) throws IllegalMoveException, OutOfTimeException {
        LinkedList<OthelloAction> moves = position.getMoves();
        if (depth == 0) {
            return maxDepthScore(position);
        }
        else if (moves.isEmpty()) {
            return leafScore(position);
        } else {

            int value = Integer.MAX_VALUE;

            OthelloAction bestMove = new OthelloAction(0, 0);
            for (OthelloAction move : moves) {
                if (System.currentTimeMillis() > timeLimitStamp)
                    throw new OutOfTimeException();

                OthelloPosition nextPosition = position.makeMove(move);
                OthelloAction moveResult = maxValue(nextPosition, alpha, beta, depth - 1);
                if (moveResult.getValue() < value) {
                    value = moveResult.getValue();
                    move.value = value;
                    bestMove = move;
                }

                if (value <= alpha) {
                    move.value = value;
                    return move;
                }

                beta = Integer.min(beta, value);
            }
            return bestMove;
        }
    }

    /**
     * factorizes the code when Min or Max goes at maximum depth.
     * @param position current position at max depth
     * @return pass OthelloAction which value is infinity or the position score
     */
    private OthelloAction maxDepthScore(OthelloPosition position) {
        OthelloAction maxDepthMove = new OthelloAction(0, 0);
        maxDepthMove.value = this.evaluator.evaluate(position);
        return maxDepthMove;
    }

    /**
     * Checks a leaf position to return an infinity value if the game is won. Else it evaluates normally the position.
     * @param position leaf position without being at max depth
     * @return pass OthelloAction which value is infinity or the position score
     * @throws IllegalMoveException to satisfy makeMove() prototype, making a pass move just to switch player
     */
    private OthelloAction leafScore(OthelloPosition position) throws IllegalMoveException {
        OthelloAction leafScore = new OthelloAction(0,0, true);
        //switch to the opponent turn
        OthelloPosition evalPosition = position.makeMove(leafScore);
        //Check if the position is an ending move
        if (evalPosition.getMoves().isEmpty()) {
            int whiteNumber = 0;
            int blackNumber = 0;
            //get the total count of each player tokens
            for (int row = 1; row <= OthelloPosition.BOARD_SIZE; row++) {
                for (int column = 1; column <= OthelloPosition.BOARD_SIZE; column++) {
                    char cell = position.board[row][column];
                    if (cell == 'W') {
                        whiteNumber += 1;
                    } else if (cell == 'B') {
                        blackNumber += 1;
                    }
                }
            }
            if (whiteNumber > blackNumber) {
                leafScore.setValue(Integer.MAX_VALUE);//White player wins. The white score is maximal
            } else {
                leafScore.setValue(Integer.MIN_VALUE);//Black player wins. The black score is minimal
            }
        } else {
            leafScore.setValue(evaluator.evaluate(position));
        }
        return leafScore;
    }
}