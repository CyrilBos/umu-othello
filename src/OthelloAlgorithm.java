/**
 * This interface defines the mandatory methods for game playing algorithms,
 * i.e., algorithms that take an <code>OthelloAlgorithm</code> and return a
 * suggested move for the player who has the move.
 * 
 * The algorithm only defines the search method. The heuristic evaluation of
 * positions is given by an <code>OthelloEvaluator</code> which is given to the
 * algorithm.
 * 
 * @author Cyril Bos
 */

public interface OthelloAlgorithm {

	/**
	 * Sets the <code>OthelloEvaluator</code> the algorithm is to use for
	 * heuristic evaluation.
	 */
	public void setEvaluator(OthelloEvaluator evaluator);

	/**
	 * Returns the <code>OthelloAction</code> the algorithm considers to be the
	 * best move.
	 */
	public OthelloAction evaluate(OthelloPosition position) throws IllegalMoveException;

	/** Sets the maximum search depth of the algorithm. */
	public void setSearchDepth(int depth);
}