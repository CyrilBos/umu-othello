import java.util.LinkedList;

public class Othello {
    public static void main(String[] args) throws IllegalMoveException {
        long startTime = System.currentTimeMillis();

        String positionString = args[0];
        OthelloPosition position = new OthelloPosition(positionString);

        long remainingTime = (long) Integer.parseInt(args[1]);

        OthelloAlgorithm moveChooser = new AlphaBeta();
        //OthelloEvaluator evaluator = new NaiveCountingEvaluator();
        OthelloEvaluator evaluator = new BetterCountingEvaluator();
        moveChooser.setEvaluator(evaluator);

        int depth = 7;
        moveChooser.setSearchDepth(depth);

        OthelloAction move = moveChooser.evaluate(position);
        long execTime = System.currentTimeMillis() - startTime;
        remainingTime = remainingTime - execTime;

        //Stop the search if the remaining time
        while (remainingTime > execTime) {
            startTime = System.currentTimeMillis();

            //search again with an incremented depth to find a supposedly better move
            moveChooser.setSearchDepth(depth++);
            move = moveChooser.evaluate(position);

            execTime = System.currentTimeMillis() - startTime;
            remainingTime = remainingTime - execTime;
        }

        move.print();

    }
}
