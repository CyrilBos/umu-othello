import java.util.LinkedList;

public class Othello {
    public static void main(String[] args) throws IllegalMoveException {
        OthelloAlgorithm moveChooser = new AlphaBeta();

        OthelloEvaluator evaluator = new NaiveCountingEvaluator();

        moveChooser.setSearchDepth(7);
        moveChooser.setEvaluator(evaluator);

        //String position = args[1];
        //int timeLimit = Integer.parseInt(args[2]);
        //OthelloPosition position = new OthelloPosition(position);
        //

        OthelloPosition position = new OthelloPosition();
        LinkedList<OthelloAction> moves;

        position.initialize();
        do {
            System.out.println("Current player: " + position.getPlayerToken(position.playerToMove));
            position.illustrate();
            position = position.makeMove(moveChooser.evaluate(position));
            moves = position.getMoves();
        } while (!moves.isEmpty());

            //moves is empty: the game is finished

        int white_tokens = 0;
        int black_tokens = 0;

        for (char[] row : position.board) {
            for (char column : row) {
                if(column == 'W')
                    white_tokens++;
                else if (column == 'B')
                    black_tokens++;
            }
        }

        System.out.println("White tokens: " + white_tokens);
        System.out.println("Black tokens: " + black_tokens);
    }
}
