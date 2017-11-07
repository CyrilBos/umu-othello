
/**
 * This class represents a 'move' in a game. The move is simply represented by
 * two integers: the row and the column where the player puts the marker. In
 * addition, the <code>OthelloAction</code> has a field where the estimated
 * value of the move can be stored during computations.
 * 
 * @author Henrik Bj&ouml;rklund
 */

public class OthelloAction {

	/** The row where the marker is placed. */
	protected int row = -1;

	/** The column where the marker is placed. */
	protected int column = -1;

	/** The estimated value of the move. */
	protected int value = 0;

	/** True if the player has to pass, i.e., if there is no legal move. */
	protected boolean pass = false;

	/**
	 * Creates a new <code>OthelloAction</code> with row <code>r</code>, column
	 * <code>c</code>, and value 0.
	 */
	public OthelloAction(int r, int c) {
		row = r;
		column = c;
		value = 0;
	}

	public OthelloAction(int r, int c, boolean p) {
		row = r;
		column = c;
		value = 0;
		pass = p;
	}

	public OthelloAction(String s) {
		if (s.equals("pass")) {
			row = 0;
			column = 0;
			value = 0;
			pass = true;
		} else {
			row = (new Integer((new Character(s.charAt(1))).toString()))
					.intValue();
			column = (new Integer((new Character(s.charAt(3))).toString()))
					.intValue();
			value = 0;
		}
	}

	/** Sets the estimated value of the move. */
	public void setValue(int v) {
		value = v;
	}

	/** Returns the estimated value of the move. */
	public int getValue() {
		return value;
	}

	/** Sets the column where the marker is to be placed. */
	public void setColumn(int c) {
		column = c;
	}

	/** Returns the column where the marker is to be placed. */
	public int getColumn() {
		return column;
	}

	/** Sets the row where the marker is to be placed. */
	public void setRow(int r) {
		row = r;
	}

	/** Returns the row where the marker is to be placed. */
	public int getRow() {
		return row;
	}

	/**
	 * Sets the boolean that indicates whether this is a pass move. This should
	 * only be true if there are no legal moves.
	 */
	public void setPassMove(boolean b) {
		pass = b;
	}

	/**
	 * Returns true if this is a pass move, indicating that the player has no
	 * legal moves. Otherwise returns false.
	 */
	public boolean isPassMove() {
		return pass;
	}

	public void print() {
		if (pass) {
			System.out.println("pass");
		} else {
			System.out.println("(" + row + "," + column + ")");
		}
	}

}