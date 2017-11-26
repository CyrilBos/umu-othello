import java.util.LinkedList;

public class SelfPlayingOthello {
    public static void main(String[] args) throws IllegalMoveException {
        OthelloPosition position = new OthelloPosition();
        LinkedList<OthelloAction> moves;
        OthelloAlgorithm moveChooser = new AlphaBeta(1);
        //OthelloEvaluator evaluator = new NaiveCountingEvaluator();
        OthelloEvaluator evaluator = new BoardEvaluator();
        moveChooser.setEvaluator(evaluator);

        moveChooser.setSearchDepth(7);

        position.initialize();
        do {
            System.out.println("Current player: " + position.getPlayerDisc(position.playerToMove));
            position.illustrate();
            try {
                position = position.makeMove(moveChooser.evaluate(position));
            } catch (OutOfTimeException e) {
                e.printStackTrace();
            }
            moves = position.getMoves();
        } while (!moves.isEmpty());

        //moves is empty: the game is finished

        int white_tokens = 0;
        int black_tokens = 0;

        for (char[] row : position.board) {
            for (char column : row) {
                if (column == 'W')
                    white_tokens++;
                else if (column == 'B')
                    black_tokens++;
            }
        }

        System.out.println("White tokens: " + white_tokens);
        System.out.println("Black tokens: " + black_tokens);
    }
}
