import java.util.LinkedList;

public class AlphaBeta implements OthelloAlgorithm {
    private OthelloEvaluator evaluator;
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
            return MaxValue(null, position, alpha, beta, depth);
        else
            return MinValue(null, position, alpha, beta, depth);
    }

    private OthelloAction MaxValue(OthelloAction callingMove, OthelloPosition position, int alpha, int beta, int depth) throws IllegalMoveException {
        LinkedList<OthelloAction> moves = position.getMoves();
        //if a leaf is reached
        if (moves.isEmpty() || depth > this.maxDepth) {
            if(callingMove != null)
                callingMove.setValue(evaluator.evaluate(position));
            return callingMove;
        }

        int value = Integer.MIN_VALUE;
        OthelloAction bestMove = null;
        for(OthelloAction move: moves) {
            if (System.currentTimeMillis() > timeLimitStamp)
                return bestMove;

            OthelloPosition next_position = position.makeMove(move);
            //if moveResult is null, there was a timeout
            OthelloAction moveResult = MinValue(move, next_position, alpha, beta, depth+1);
            if (moveResult != null) {
                if (moveResult.getValue() > value) {
                    value = moveResult.getValue();
                    bestMove = move;
                }
                if (value >= beta)
                    return move;

                alpha = Integer.max(alpha, value);
            }
        }
        return bestMove;
    }

    private OthelloAction MinValue(OthelloAction callingMove, OthelloPosition position, int alpha, int beta, int depth) throws IllegalMoveException {
        LinkedList<OthelloAction> moves = position.getMoves();
        if (moves.isEmpty() || depth > this.maxDepth) {
            if (callingMove != null)
                callingMove.setValue(evaluator.evaluate(position));
            return callingMove;
        }

        int value = Integer.MAX_VALUE;
        OthelloAction bestMove = null;
        for(OthelloAction move: moves) {
            if (System.currentTimeMillis() > timeLimitStamp)
                return bestMove;

            OthelloPosition next_position = position.makeMove(move);
            OthelloAction moveResult = MaxValue(move, next_position, alpha, beta, depth + 1);
            //if moveResult is null, there was a timeout
            if (moveResult != null) {
                if (moveResult.getValue() < value) {
                    value = moveResult.getValue();
                    bestMove = move;
                }

                if (value <= alpha)
                    return move;

                beta = Integer.min(beta, value);
            }
        }
        return bestMove;
    }
}