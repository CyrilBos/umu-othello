import java.util.LinkedList;

public class AlphaBeta implements OthelloAlgorithm {
    private OthelloEvaluator evaluator, min_evaluator, max_evaluator;
    private int maxDepth;
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
        this.maxDepth = depth;
    }

    @Override
    public OthelloAction evaluate(OthelloPosition position) throws IllegalMoveException {
        int depth = 1;
        int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
        if (position.playerToMove)
            return this.MaxValue(null, position, alpha, beta, depth);
        else
            return this.MinValue(null, position, alpha, beta, depth);
    }

    private void treatLeafPosition(OthelloAction callingMove, OthelloPosition position) {
        if (callingMove != null) {
            OthelloPosition evalPosition = position.clone();
            evalPosition.playerToMove = !position.playerToMove;
            //Check if the position is an ending move
            if (position.getMoves().isEmpty() && evalPosition.getMoves().isEmpty()) {
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
                    callingMove.setValue(Integer.MAX_VALUE);//White player wins. The white score is maximal
                } else {
                    callingMove.setValue(Integer.MIN_VALUE);//Black player wins. The white score is minimal
                }
            } else {
                callingMove.setValue(evaluator.evaluate(position));
            }
        }
    }

    private OthelloAction MaxValue(OthelloAction callingMove, OthelloPosition position, int alpha, int beta, int depth) throws IllegalMoveException {
        LinkedList<OthelloAction> moves = position.getMoves();
        //if a leaf position is reached
        if (depth > this.maxDepth) {
            treatLeafPosition(callingMove, position);
            System.out.println("MaxCallingMove");
            if (callingMove != null)
                callingMove.print();
            return callingMove;
        }
        else if (moves.isEmpty()) {

        }

        int value = Integer.MIN_VALUE;
        OthelloAction bestMove = null;
        for (OthelloAction move : moves) {
            if (System.currentTimeMillis() > timeLimitStamp)
                return bestMove;

            OthelloPosition next_position = position.makeMove(move);
            //if moveResult is null, there was a timeout
            OthelloAction moveResult = MinValue(move, next_position, alpha, beta, depth + 1);
            if (moveResult != null) {
                if (moveResult.getValue() > value) {
                    value = moveResult.getValue();
                    bestMove = move;
                }
                if (value >= beta) {
                    System.out.println("BETA CUT");
                    if (move != null)
                        move.print();
                    return move;
                }

                alpha = Integer.max(alpha, value);
            }
        }
        System.out.println("MAXendBestMove");
        if (bestMove != null)
            bestMove.print();
        return bestMove;
    }

    private OthelloAction MinValue(OthelloAction callingMove, OthelloPosition position, int alpha, int beta, int depth) throws IllegalMoveException {
        LinkedList<OthelloAction> moves = position.getMoves();
        if (moves.isEmpty() || depth > this.maxDepth) {
            treatLeafPosition(callingMove, position);
            System.out.println("MINCallingMove");
            if (callingMove != null)
                callingMove.print();
            return callingMove;
        }

        int value = Integer.MAX_VALUE;
        OthelloAction bestMove = null;
        for (
                OthelloAction move : moves)

        {
            if (System.currentTimeMillis() > timeLimitStamp)
                return bestMove;

            OthelloPosition nextPosition = position.makeMove(move);
            OthelloAction moveResult = MaxValue(move, nextPosition, alpha, beta, depth + 1);
            //if moveResult is null, there was a timeout
            if (moveResult != null) {
                if (moveResult.getValue() < value) {
                    value = moveResult.getValue();
                    bestMove = move;
                }

                if (value <= alpha) {
                    System.out.println("ALPHA CUT");
                    if (move != null)
                        move.print();
                    return move;
                }

                beta = Integer.min(beta, value);
            }
        }
        System.out.println("MINendBestMove");
        if (bestMove != null)
            bestMove.print();
        return bestMove;
    }
}