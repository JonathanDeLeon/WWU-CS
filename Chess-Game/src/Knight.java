
/**
 * 
 * @author Jonathan De Leon
 * @category CS 241 Final Project
 * @version 1.00
 * @content
 *		Knight subclass with appropriate valid move method checker
 */

public class Knight extends Piece {

	/**
	 * Constructor  
	 */
	public Knight(int row, int column, int color) {
		super(row, column, color);
	}

	/**
	 * Knights move in an "L" shape which is two squares long and one square wide. 
	 * It can leap over pieces 
	 */
	public boolean isValidMove(int targetRow, int targetColumn){
		System.out.println(this.getClass() + "'s isValidMove has been called");
		if(!super.isValidMove(targetRow, targetColumn))
			return false;
		if( this.getRow()+2 == targetRow && this.getColumn()+1 == targetColumn){
			// move up up right
			return true;
		}else if( this.getRow()+1 == targetRow && this.getColumn()+2 == targetColumn){
			// move up right right
			return true;
		}else if( this.getRow()-1 == targetRow && this.getColumn()+2 == targetColumn){
			// move down right right
			return true;
		}else if( this.getRow()-2 == targetRow && this.getColumn()+1 == targetColumn){
			// move down down right
			return true;
		}else if( this.getRow()-2 == targetRow && this.getColumn()-1 == targetColumn){
			// move down down left
			return true;
		}else if( this.getRow()-1 == targetRow && this.getColumn()-2 == targetColumn){
			// move down left left
			return true;
		}else if( this.getRow()+1 == targetRow && this.getColumn()-2 == targetColumn){
			// move up left left
			return true;
		}else if( this.getRow()+2 == targetRow && this.getColumn()-1 == targetColumn){
			// move up up left
			return true;
		}else{
			return false;
		}
	}
}
