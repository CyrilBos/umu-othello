import java.util.LinkedList;

public class Othello {
    public static void main(String[] args) throws IllegalMoveException {
        //Counting the initialization time in the move time, so cannot be replaced by a clean do-while
        long startTime = System.currentTimeMillis();

        String positionString = args[0];
        OthelloPosition position = new OthelloPosition(positionString);

        //Time parameter converted into milliseconds
        long remainingTime = (long) Integer.parseInt(args[1]) * 1000;

        OthelloAlgorithm moveChooser = new AlphaBeta();
        //OthelloEvaluator evaluator = new NaiveCountingEvaluator();
        OthelloEvaluator evaluator = new BetterCountingEvaluator();
        moveChooser.setEvaluator(evaluator);

        //minimal depth set to the same as the naive heuristic
        int depth = 7;
        moveChooser.setSearchDepth(depth);

        OthelloAction move = moveChooser.evaluate(position);
        long executionTime = System.currentTimeMillis() - startTime;
        remainingTime = remainingTime - executionTime;

        //System.out.println("Remaining time: ");
        //System.out.println(remainingTime);

        //Stop the search if the remaining time is inferior to the last search time
        while (remainingTime > executionTime) {
            startTime = System.currentTimeMillis();
            //System.out.println("Starting time: ");
            //System.out.println(startTime);

            //search again with an incremented depth to find a supposedly better move
            moveChooser.setSearchDepth(depth++);
            move = moveChooser.evaluate(position);

            executionTime = System.currentTimeMillis() - startTime;
            //System.out.println("Execution time: ");
            //System.out.println(executionTime);
            remainingTime = remainingTime - executionTime;
            //System.out.println("Remaining time: ");
            //System.out.println(remainingTime);
        }
        if (move == null)
            move = new OthelloAction(0,0,true);
        move.print();

    }
}
