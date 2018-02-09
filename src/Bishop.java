
/**
 * 
 * @author Jonathan De Leon
 * @category CS 241 Final Project
 * @version 1.00
 * @content
 *		Bishop subclass with appropriate valid move method checker
 */


public class Bishop extends Piece {

	/**
	 * Constructor
	 */
	public Bishop(int row, int column, int color) {
		super(row, column, color);
	}
	
	/**
	 * Bishops can move any numnber of squares diagonally
	 */
	public boolean isValidMove(int targetRow, int targetColumn){
		System.out.println(this.getClass() + "'s isValidMove has been called");
		if(!super.isValidMove(targetRow, targetColumn))
			return false;
        if(Math.abs(this.getRow() - targetRow) == Math.abs(this.getColumn() - targetColumn))
        	//move diagonally
            return true;
        return false;
	}
}
