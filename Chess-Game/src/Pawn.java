
/**
 * 
 * @author Jonathan De Leon
 * @category CS 241 Final Project
 * @version 1.00
 * @content
 *		Pawn subclass with appropriate valid move method checker
 */

public class Pawn extends Piece {

	private boolean hasFirstMove;		//firstMove has not been made flag

	/**
	 * Constructor 
	 */
	public Pawn(int row, int column, int color) {
		super(row, column, color);
		this.hasFirstMove = true;
	}
	/**
	 * Getters and Setters 
	 */
	public boolean getHasFirstMove(){
		return hasFirstMove;
	}
	public void setFirstMove(boolean firstMove){
		this.hasFirstMove = firstMove;
	}
	public void madeFirstMove(){
		this.hasFirstMove = false;
	}
	
	/**
	 * Pawn can move forward a square. Or on its first move it can move two squares
	 */
	public boolean isValidMove(int targetRow, int targetColumn){
		System.out.println(this.getClass() + "'s isValidMove has been called");
		if(!super.isValidMove(targetRow, targetColumn))
			return false;
		boolean isValid = false;
		if(this.getColumn() == targetColumn){
			//Pawn can only move forward
			if(this.getColor() == ChessLogic.COLOR_WHITE){
				if(this.hasFirstMove && this.getRow() + 2 == targetRow){
					//move two up
					isValid = true;
				}
				else if(this.getRow() + 1 == targetRow){
					//move one up
					isValid = true;
				}else{
					isValid = false;
				}
			}else{
				//black pawn
				if(this.hasFirstMove && this.getRow() - 2 == targetRow){
					//move two down
					isValid = true;
				}
				else if(this.getRow() - 1 == targetRow){
					//move one down
					isValid = true;
				}else{
					isValid = false;
				}
			}
		}else{
			isValid = false;
		}
		//Pawn successfully made a move
		if(isValid && this.hasFirstMove)
			madeFirstMove();
		return isValid;
	}

	/**
	 *	Pawns can move diagonal only to capture a piece 
	 * @param targetRow
	 * @param targetColumn
	 * @return
	 */
	public boolean isDiagonalMoveValid(int targetRow, int targetColumn){
		boolean isValid = false;
		if(this.getColumn()+1 == targetColumn || this.getColumn()-1 == targetColumn){
			// one column to the right or left
			if(this.getColor() == ChessLogic.COLOR_WHITE ){
				// white
				if(this.getRow()+1 == targetRow){
					// move one up
					isValid = true;
				}else{
					isValid = false;
				}
			}else{
				// black
				if( this.getRow()-1 == targetRow ){
					// move one down
					isValid = true;
				}else{
					isValid = false;
				}
			}
		}else{
			isValid = false;
		}
		if(isValid && this.hasFirstMove)
			madeFirstMove();
		return isValid;
	}
}
