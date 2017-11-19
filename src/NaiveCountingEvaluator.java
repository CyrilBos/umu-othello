import java.util.LinkedList;

public class NaiveCountingEvaluator implements OthelloEvaluator {
    @Override
    public int evaluate(OthelloPosition position) {
        int score = 0;
        char playerToken = position.getPlayerDisc(true);

        for(char[] row: position.board) {
            for(char cell: row) {
                if(cell == playerToken) {
                    score += 1;
                }
            }
        }

        return score;
    }
}
