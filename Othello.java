import java.util.LinkedList;

public class Othello {
    public static void main(String[] args) throws IllegalMoveException {
        //String position = args[1];
        //int timeLimit = Integer.parseInt(args[2]);

        OthelloPosition position = new OthelloPosition();
        position.initialize();
        position.illustrate();
        LinkedList<OthelloAction> moves = position.getMoves();
        while (!moves.isEmpty()) {
            position = position.makeMove(moves.getFirst());//TODO: change by OthelloAlgorithm
            position.illustrate();
            moves = position.getMoves();
        }

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
