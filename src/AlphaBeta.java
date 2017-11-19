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
            return this.MaxValue(position, alpha, beta, depth);
        else
            return this.MinValue(position, alpha, beta, depth);
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

    private OthelloAction MaxValue(OthelloPosition position, int alpha, int beta, int depth) throws IllegalMoveException {
        LinkedList<OthelloAction> moves = position.getMoves();

        if (depth > this.maxDepth) {
            OthelloAction maxDepthMove = new OthelloAction(0, 0);
            this.evaluator.evaluate(position);
            //treatLeafPosition(callingMove, position);
            System.out.println("MaxCallingMove");
            maxDepthMove.pprint();
            return maxDepthMove;
        }
        //if a leaf position is reached
        else if (moves.isEmpty()) {
            OthelloAction leafMove = new OthelloAction(0, 0, true);
            this.evaluator.evaluate(position);
            //treatLeafPosition(callingMove, position);
            System.out.println("LeafCallingMove");
            leafMove.pprint();
            return leafMove;
        }
        else {
            int value = Integer.MIN_VALUE;
            OthelloAction bestMove = new OthelloAction(0,0);
            for (OthelloAction move : moves) {
                if (System.currentTimeMillis() > timeLimitStamp)
                    return bestMove;

                OthelloPosition next_position = position.makeMove(move);
                //if moveResult is null, there was a timeout
                OthelloAction moveResult = MinValue(next_position, alpha, beta, depth + 1);
                if (moveResult != null) {
                    if (moveResult.getValue() > value) {
                        value = moveResult.getValue();
                        move.value = value;
                        bestMove = move;
                    }
                    if (value >= beta) {
                        System.out.println("BETA CUT");
                        if (move != null)
                            move.pprint();
                        return move;
                    }

                    alpha = Integer.max(alpha, value);
                }
            }
            System.out.println("MAXendBestMove");
            bestMove.pprint();
            return bestMove;
        }
    }

    private OthelloAction MinValue(OthelloPosition position, int alpha, int beta, int depth) throws IllegalMoveException {
        LinkedList<OthelloAction> moves = position.getMoves();
        if (depth > this.maxDepth) {
            OthelloAction maxDepthMove = new OthelloAction(0, 0);
            maxDepthMove.value = this.evaluator.evaluate(position);
            //treatLeafPosition(callingMove, position);
            System.out.println("MaxCallingMove");
            maxDepthMove.pprint();
            return maxDepthMove;
        }
        //if a leaf position is reached
        else if (moves.isEmpty()) {
            OthelloAction leafMove = new OthelloAction(0, 0, true);
            leafMove.value = this.evaluator.evaluate(position);
            //treatLeafPosition(callingMove, position);
            System.out.println("LeafCallingMove");
            leafMove.pprint();
            return leafMove;
        }
        else {

            int value = Integer.MAX_VALUE;
            OthelloAction bestMove = new OthelloAction(0, 0);
            for (OthelloAction move : moves) {
                if (System.currentTimeMillis() > timeLimitStamp)
                    return bestMove;

                OthelloPosition nextPosition = position.makeMove(move);
                OthelloAction moveResult = MaxValue(nextPosition, alpha, beta, depth + 1);
                //if moveResult is null, there was a timeout
                if (moveResult.getValue() < value) {
                    value = moveResult.getValue();
                    move.value = value;
                    bestMove = move;
                }

                if (value <= alpha) {
                    move.value = value;
                    System.out.println("ALPHA CUT");
                    move.pprint();
                    return move;
                }

                beta = Integer.min(beta, value);
            }
            System.out.println("MINendBestMove");
            bestMove.pprint();
            return bestMove;
        }
    }
}