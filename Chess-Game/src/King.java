
/**
 * 
 * @author Jonathan De Leon
 * @category CS 241 Final Project
 * @version 1.00
 * @content
 *		King subclass with appropriate valid move method checker
 */

public class King extends Piece {

	/**
	 * Constructor
	 */
	public King(int row, int column, int color) {
		super(row, column, color);
	}
	
	/**
	 * King moves one square in any direction
	 */
	public boolean isValidMove(int targetRow, int targetColumn){
		System.out.println(this.getClass() + "'s isValidMove has been called");
		if(!super.isValidMove(targetRow, targetColumn))
			return false;
		// The king moves one square in any direction
		if( this.getRow()+1 == targetRow && this.getColumn() == targetColumn){
			//up
			return true;
		}else if( this.getRow()+1 == targetRow && this.getColumn()+1 == targetColumn){
			//up right
			return true;
		}else if( this.getRow() == targetRow && this.getColumn()+1 == targetColumn){
			//right
			return true;
		}else if( this.getRow()-1 == targetRow && this.getColumn()+1 == targetColumn){
			//down right
			return true;
		}else if( this.getRow()-1 == targetRow && this.getColumn() == targetColumn){
			//down
			return true;
		}else if( this.getRow()-1 == targetRow && this.getColumn()-1 == targetColumn){
			//down left
			return true;
		}else if( this.getRow() == targetRow && this.getColumn()-1 == targetColumn){
			//left
			return true;
		}else if( this.getRow()+1 == targetRow && this.getColumn()-1 == targetColumn){
			//up left
			return true;
		}else{
			return false;
		}
	}
	
	

}
