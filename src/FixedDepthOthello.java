/**
 * Created by cyrbos on 11/19/17.
 */
public class FixedDepthOthello {
    public static void main(String[] args) throws IllegalMoveException {
        long timeLimitStamp = Long.MAX_VALUE;
        String positionString = args[0];
        OthelloPosition position = new OthelloPosition(positionString);

        OthelloAlgorithm moveChooser = new AlphaBeta(timeLimitStamp);
        //OthelloEvaluator evaluator = new NaiveCountingEvaluator();
        OthelloEvaluator evaluator = new BetterCountingEvaluator();
        moveChooser.setEvaluator(evaluator);

        int depth = 2;
        moveChooser.setSearchDepth(depth);
        OthelloAction chosenMove = moveChooser.evaluate(position);
        if (chosenMove == null)
            chosenMove = new OthelloAction(0, 0, true);

        chosenMove.print();
    }
}
