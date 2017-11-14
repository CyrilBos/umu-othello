import java.util.LinkedList;

public class AlphaBeta implements OthelloAlgorithm {
    private OthelloEvaluator evaluator;
    private int max_depth;

    @Override
    public void setEvaluator(OthelloEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public void setSearchDepth(int depth) {
        this.max_depth = depth;
    }

    @Override
    public OthelloAction evaluate(OthelloPosition position) throws IllegalMoveException {
        int depth = 1;
        int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
        return MaxValue(null, position, alpha, beta, depth);
    }

    private OthelloAction MaxValue(OthelloAction callingMove, OthelloPosition position, int alpha, int beta, int depth) throws IllegalMoveException {
        LinkedList<OthelloAction> moves = position.getMoves();
        if (moves.isEmpty() || depth > max_depth) {
            if(callingMove != null)
                callingMove.setValue(evaluator.evaluate(position));
            return callingMove;
        }
        int value = Integer.MIN_VALUE;
        OthelloAction bestMove = null;
        for(OthelloAction move: moves) {
            OthelloPosition next_position = position.makeMove(move);
            int next_value = MinValue(move, next_position, alpha, beta, depth+1).getValue();
            if (next_value > value) {
                value = next_value;
                bestMove = move;
            }
            if (value >= beta)
                return move;
            alpha = Integer.max(alpha, value);
        }
        return bestMove;
    }

    private OthelloAction MinValue(OthelloAction callingMove, OthelloPosition position, int alpha, int beta, int depth) throws IllegalMoveException {
        LinkedList<OthelloAction> moves = position.getMoves();
        if (moves.isEmpty() || depth > max_depth) {
            callingMove.setValue(evaluator.evaluate(position));
            return callingMove;
        }
        int value = Integer.MAX_VALUE;
        OthelloAction bestMove = null;
        for(OthelloAction move: moves) {
            OthelloPosition next_position = position.makeMove(move);
            int next_value = MaxValue(move, next_position, alpha, beta, depth+1).getValue();
            if (next_value < value) {
                value = next_value;
                bestMove = move;
            }

            if (value <= alpha)
                return move;

            beta = Integer.min(beta, value);
        }
        return bestMove;

    }
}