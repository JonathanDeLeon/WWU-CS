
/**
 * 
 * @author Jonathan De Leon
 * @category CS 241 Final Project
 * @version 1.00
 * @content
 * 		MoveValidator validates the moves made by piece. It calls the appropriate
 * 		move checker method that is in the piece's class
 *
 */

public class MoveValidator {
	private ChessLogic chessLogic;		//Logic of the game
	private Piece sourcePiece;			//Piece to move
	private Piece targetPiece;			//Piece that will be the target from sourcePiece

	/**
	 * Constructor
	 */
	public MoveValidator(ChessLogic chessLogic) {
		this.chessLogic = chessLogic;
	}
	
	/**
	 * Checks if a move made is valid by calling the appropriate piece's rules in their class 
	 * This is using inheritance and polymorphism. There are additional checks in the method for certain
	 * piece classes 
	 * @param sourceRow
	 * @param sourceColumn
	 * @param targetRow
	 * @param targetColumn
	 * @return
	 */
	public boolean isMoveValid(int sourceRow, int sourceColumn, int targetRow, int targetColumn){
		
		sourcePiece = chessLogic.getNonCapturedPieceAtLocation(sourceRow, sourceColumn);		//gets sourcePiece
		targetPiece = chessLogic.getNonCapturedPieceAtLocation(targetRow, targetColumn);		//gets targetPiece if any

		//No source piece
		if(sourcePiece == null)
			return false;
		
		//Checks for whose turn it is
		if(sourcePiece.getColor() != chessLogic.getGameState())
			throw new IllegalStateException("It's not your turn.");
		
		//If new location is not available and not captureable
		//Checks if new location contains a piece of same color
		if(!(isTargetLocationAvailable() || isTargetLocationCaptureable()))
			return false;
		
		//Calls appropriate piece's rules that are overwritten in the classes
		boolean validPieceMove = sourcePiece.isValidMove(targetRow, targetColumn);
		
		if(sourcePiece instanceof Pawn && isTargetLocationCaptureable()){
			//Additional checks for pawns
			Pawn testPawn = (Pawn)sourcePiece;
			if(validPieceMove){
				//Pawns cannot capture piece forward
				testPawn.setFirstMove(false);
				validPieceMove = false;
			}else if(!validPieceMove){	
				//pawns can move diagonally to capture piece
				validPieceMove = testPawn.isDiagonalMoveValid(targetRow, targetColumn);
			}
		}else if(validPieceMove && (sourcePiece instanceof Bishop || sourcePiece instanceof Rook || sourcePiece instanceof Queen)){
			//Making sure there are not pieces in the way
			validPieceMove = !arePiecesBetweenSourceAndTarget(targetRow, targetColumn);
		}
		
		return validPieceMove;
	}
	
	/**
	 * Method checks if the new location contains a piece of opposite color
	 * @return true if new location has an opponent's piece
	 */
	public boolean isTargetLocationCaptureable(){
		if(targetPiece == null)
			return false;
		else if(targetPiece.getColor() != sourcePiece.getColor())
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if the new location has no piece
	 * @return true if there is no piece in the new location
	 */
	public boolean isTargetLocationAvailable(){
		return targetPiece == null;
	}

	/**
	 * Function checks if there are pieces in the sourcePiece's new path 
	 * Used only for Rooks, Queens, and Bishops
	 * @param targetRow New target row
	 * @param targetColumn New target column
	 * @return true if there are pieces
	 */
	private boolean arePiecesBetweenSourceAndTarget(int targetRow, int targetColumn) {
		int sourceRow = sourcePiece.getRow();
		int sourceColumn = sourcePiece.getColumn();
		int currentRow, currentColumn;
		if(sourceRow == targetRow){
			//Queen and Rook pieces moving right or left
			currentRow = 0;
			currentColumn = targetColumn > sourceColumn ? 1 : -1;
			for(int i = 1; i <= Math.abs(targetColumn - sourceColumn)-1;++i){
				if(chessLogic.isNonCapturedPieceAtLocation(sourceRow, sourceColumn + i*currentColumn)){
					return true;
				}
			}
		}else if(sourceColumn == targetColumn){
			//Queen and Rook pieces moving up or down
			currentRow = targetRow > sourceRow ? 1 : -1;
			currentColumn = 0;
			for(int i = 1; i <= Math.abs(targetRow - sourceRow)-1;++i){
				if(chessLogic.isNonCapturedPieceAtLocation(sourceRow + i*currentRow, sourceColumn)){
					return true;
				}
			}
		}else if(Math.abs(sourceRow - targetRow) == Math.abs(sourceColumn - targetColumn)){
			//Queen and Bishop pieces
			currentRow = targetRow > sourceRow ? 1 : -1;
			currentColumn = targetColumn > sourceColumn ? 1 : -1;
			//Check diagonal squares in path for piece
			for(int i = 1; i <= Math.abs(targetColumn - sourceColumn)-1;++i){
				if(chessLogic.isNonCapturedPieceAtLocation(sourceRow + i*currentRow, sourceColumn + i*currentColumn)){
					return true;
				}
			}
		}
		return false;
	}
}
