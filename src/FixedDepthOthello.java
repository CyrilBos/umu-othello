/**
 * Created by cyrbos on 11/19/17.
 */
public class FixedDepthOthello {
    public static void main(String[] args) throws IllegalMoveException {
        long timeLimitStamp = Long.MAX_VALUE;
        long start = System.currentTimeMillis() / 1000;
        String positionString = args[0];
        OthelloPosition position = new OthelloPosition(positionString);
        position.illustrate();
        OthelloAlgorithm moveChooser = new AlphaBeta(timeLimitStamp);
        OthelloEvaluator evaluator = new BetterCountingEvaluator();
        moveChooser.setEvaluator(evaluator);
        OthelloAction chosenMove = new OthelloAction(0,0);

        for (int depth = 1; depth < 10 && !position.isGameEnded(chosenMove); depth++){
            moveChooser.setSearchDepth(depth);
            chosenMove = moveChooser.evaluate(position);
            System.out.println("depth = " + depth + " time = " + (System.currentTimeMillis() / 1000 - start));
            chosenMove.pprint();
        }
        chosenMove.pprint();
        //position.makeMove(chosenMove).illustrate();
    }
}
