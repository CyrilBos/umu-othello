/**
 * This interface defines the mandatory methods for an evaluator, i.e., a class
 * that can take a position and return an integer value that represents a
 * heuristic evaluation of the position (positive numbers if the position is
 * better for the first player, white). Notice that an evaluator is not supposed
 * to make moves in the position to 'see into the future', but only evaluate the
 * static features of the postion.
 * 
 * @author Henrik Bj&ouml;rklund
 */

public interface OthelloEvaluator {

	/** Reterns an integer, representing a heuristic evaluation of the postion. */
	public int evaluate(OthelloPosition position);

}