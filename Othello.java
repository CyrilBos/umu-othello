import java.util.LinkedList;

public class Othello {
    public static void main(String[] args) throws IllegalMoveException {

        OthelloPosition startingPosition = new OthelloPosition();
        startingPosition.initialize();
        startingPosition.illustrate();
        LinkedList<OthelloAction> moves = startingPosition.getMoves();
        while (!moves.isEmpty()) {
            startingPosition.makeMove(moves.getFirst());
            startingPosition.illustrate();
            moves = startingPosition.getMoves();
        };
    }
}
