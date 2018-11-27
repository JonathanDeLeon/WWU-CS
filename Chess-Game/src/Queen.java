
/**
 * 
 * @author Jonathan De Leon
 * @category CS 241 Final Project
 * @version 1.00
 * @content
 *		Queen subclass with appropriate valid move method checker
 */

public class Queen extends Piece {

	/**
	 * Constructor 
	 */
	public Queen(int row, int column, int color) {
		super(row, column, color);
	}
	
	/**
	 * Queen combines the rules of the rook and bishop by moving diagonally,
	 * up, or down
	 */
	public boolean isValidMove(int targetRow, int targetColumn){
		System.out.println(this.getClass() + "'s isValidMove has been called");
		if(!super.isValidMove(targetRow, targetColumn))
			return false;
        if(Math.abs(this.getRow() - targetRow) == Math.abs(this.getColumn() - targetColumn))
        	//move diagonally
            return true;
        if(this.getRow() == targetRow)
        	//move left or right
            return true;
        if(this.getColumn() == targetColumn)
        	//move up or down
            return true;

        return false;
	}
}
