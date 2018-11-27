/**
 * 
 * @author Jonathan De Leon
 * @category CS 241 Final Project
 * @version 1.00
 * @content
 *		Rook subclass with appropriate valid move method checker
 */

public class Rook extends Piece {

	/**
	 * Constructor 
	 */
	public Rook(int row, int column, int color) {
		super(row, column, color);
	}
	
	/**
	 * Rook can move any number of squares up or down.
	 */
	public boolean isValidMove(int targetRow, int targetColumn){
		System.out.println(this.getClass() + "'s isValidMove has been called");
		if(!super.isValidMove(targetRow, targetColumn))
			return false;
		if(this.getRow() == targetRow)
			//move left or right
            return true;
        if(this.getColumn() == targetColumn)
        	//move up or down
            return true;
        return false;
	}
}
