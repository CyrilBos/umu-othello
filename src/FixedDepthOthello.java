public class FixedDepthOthello {
    public static void main(String[] args) throws IllegalMoveException {
        long timeLimitStamp = Long.MAX_VALUE;
        long start = System.currentTimeMillis() / 1000;
        String positionString = args[0];
        OthelloPosition position = new OthelloPosition(positionString);
        position.illustrate();
        OthelloAlgorithm moveChooser = new AlphaBeta(timeLimitStamp);
        OthelloEvaluator evaluator = new BoardEvaluator();
        moveChooser.setEvaluator(evaluator);
        OthelloAction chosenMove = new OthelloAction(0, 0);

        try {
            for (int depth = 1; depth < 10 && !position.isGameEnded(chosenMove); depth++) {
                moveChooser.setSearchDepth(depth);

                chosenMove = moveChooser.evaluate(position);
                System.out.println("depth = " + depth + " time = " + (System.currentTimeMillis() / 1000 - start));
                chosenMove.pprint();
            }
        } catch (OutOfTimeException e) {
            e.printStackTrace();
        }
        chosenMove.pprint();
        //position.makeMove(chosenMove).illustrate();
    }
}
