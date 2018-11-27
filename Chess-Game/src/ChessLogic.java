import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * 
 * @author Jonathan De Leon
 * @category CS 241 Final Project
 * @version 1.00
 * @content
 * 		ChessLogic contains the logic behind the chess game making sure the rules of the game are taken
 * 		into effect as the pieces move around.
 *
 */

public class ChessLogic {
	
	//State of the game
	private int gameState = GAME_STATE_WHITE;
	public static final int GAME_STATE_WHITE = 0;
	public static final int GAME_STATE_BLACK = 1;
	public static final int GAME_STATE_END = 2;

	//piece colors
	public static final int COLOR_WHITE = 0;
	public static final int COLOR_BLACK = 1;
	
	//Row numbers
	public static final int ROW_1 = 0;
	public static final int ROW_2 = 1;
	public static final int ROW_3 = 2;
	public static final int ROW_4 = 3;
	public static final int ROW_5 = 4;
	public static final int ROW_6 = 5;
	public static final int ROW_7 = 6;
	public static final int ROW_8 = 7;
	
	//Column numbers
	public static final int COLUMN_A = 0;
	public static final int COLUMN_B = 1;
	public static final int COLUMN_C = 2;
	public static final int COLUMN_D = 3;
	public static final int COLUMN_E = 4;
	public static final int COLUMN_F = 5;
	public static final int COLUMN_G = 6;
	public static final int COLUMN_H = 7;
	public static final int MIN_COLUMN = -5;
	public static final int MAX_COLUMN = 12;

	//piece numbers
	private static final int PAWN = 1;
	private static final int ROOK = 2;
	private static final int KNIGHT = 3;
	private static final int BISHOP = 4;
	private static final int QUEEN = 5;
	private static final int KING = 6;

	private ListType<Piece> pieces = new ListType<Piece>();
	
	private MoveValidator moveValidator;

	/**
	 * initialize game
	 */
	public ChessLogic(){
		this.moveValidator = new MoveValidator(this);
		// create and place pieces
		// rook, knight, bishop, queen, king, bishop, knight, and rook
		createPiece(COLOR_WHITE, ROOK, ROW_1, COLUMN_A);
		createPiece(COLOR_WHITE, KNIGHT, ROW_1, COLUMN_B);
		createPiece(COLOR_WHITE, BISHOP, ROW_1, COLUMN_C);
		createPiece(COLOR_WHITE, QUEEN, ROW_1, COLUMN_D);
		createPiece(COLOR_WHITE, KING, ROW_1, COLUMN_E);
		createPiece(COLOR_WHITE, BISHOP, ROW_1, COLUMN_F);
		createPiece(COLOR_WHITE, KNIGHT, ROW_1, COLUMN_G);
		createPiece(COLOR_WHITE, ROOK, ROW_1, COLUMN_H);
		
		// pawns
		int currentColumn = COLUMN_A;
		for (int i = 0; i < 8; i++) {
			createPiece(COLOR_WHITE, PAWN, ROW_2, currentColumn);
			currentColumn++;
		}

		createPiece(COLOR_BLACK, ROOK, ROW_8, COLUMN_A);
		createPiece(COLOR_BLACK, KNIGHT, ROW_8, COLUMN_B);
		createPiece(COLOR_BLACK, BISHOP, ROW_8, COLUMN_C);
		createPiece(COLOR_BLACK, QUEEN, ROW_8, COLUMN_D);
		createPiece(COLOR_BLACK, KING, ROW_8, COLUMN_E);
		createPiece(COLOR_BLACK, BISHOP, ROW_8, COLUMN_F);
		createPiece(COLOR_BLACK, KNIGHT, ROW_8, COLUMN_G);
		createPiece(COLOR_BLACK, ROOK, ROW_8, COLUMN_H);
		
		// pawns
		currentColumn = COLUMN_A;
		for (int i = 0; i < 8; i++) {
			createPiece(COLOR_BLACK, PAWN, ROW_7, currentColumn);
			currentColumn++;
		}
	}

	/**
	 * create piece instance and add it to the list of pieces
	 * 
	 * @param color on of Pieces.COLOR_..
	 * @param type on of Pieces.TYPE_..
	 * @param row on of Pieces.ROW_..
	 * @param column on of Pieces.COLUMN_..
	 */
	private void createPiece(int color, int type, int row, int column) {
		Image img = this.getImageForPiece(color, type);
		Piece piece = null;
		//Instantiates piece based on type
		switch (type) {
			case BISHOP:
				piece = new Bishop(row, column, color);
				break;
			case KING:
				piece = new King(row, column, color);
				break;
			case KNIGHT:
				piece = new Knight(row, column, color);
				break;
			case PAWN:
				piece = new Pawn(row, column, color);
				break;
			case QUEEN:
				piece = new Queen(row, column, color);
				break;
			case ROOK:
				piece = new Rook(row, column, color);
				break;
		}
		if(piece != null){
			piece.setImage(img);
			this.pieces.add(piece);
		}
	}
	/**
	 * load image for given color and type. This method translates the color and
	 * type information into a filename and loads that particular file.
	 *
	 * @param color color constant
	 * @param type type constant
	 * @return image
	 */
	private Image getImageForPiece(int color, int type) {
	 
		String filename = "";
	 
		filename += (color == COLOR_WHITE ? "w" : "b");
		switch (type) {
			case BISHOP:
				filename += "b";
				break;
			case KING:
				filename += "k";
				break;
			case KNIGHT:
				filename += "n";
				break;
			case PAWN:
				filename += "p";
				break;
			case QUEEN:
				filename += "q";
				break;
			case ROOK:
				filename += "r";
				break;
		}
		filename += ".png";
	 
		URL urlPieceImg = getClass().getResource("/img/"+filename);
		return new ImageIcon(urlPieceImg).getImage();
	}
	
	/**
	 * @return list of pieces
	 */
	public ListType<Piece> getPieces(){
		return pieces;
	}
	
	/**
	 * @return current game state
	 */
	public int getGameState() {
		return this.gameState;
	}
	
	/**
	 * Move piece to the specified location. if new location contains an opponent's
	 * piece, then that piece will be captured. Uses MoveValidator class to check
	 * if move can be made. 
	 * @param sourceRow Row of the piece to move
	 * @param sourceColumn Column of the piece to move
	 * @param targetRow Row where piece is going to move
	 * @param targetColumn Column where piece is going to move
	 * @return true If piece has been moved successfully.
	 * @exception throws IllegalArgumentException if move cannot be made
	 */
	public boolean movePiece(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		if(!moveValidator.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn))
			throw new IllegalArgumentException("Invalid Move");

		Piece dragPiece = getNonCapturedPieceAtLocation(sourceRow, sourceColumn);
		
		//check if the move is capturing an opponent piece
		int opponentColor = (dragPiece.getColor()==ChessLogic.COLOR_BLACK ? ChessLogic.COLOR_WHITE : ChessLogic.COLOR_BLACK);
		if(isNonCapturedPieceAtLocation(opponentColor, targetRow, targetColumn)){
			Piece opponentPiece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
			opponentPiece.isCaptured(true);
			//Sets opponentPiece to the side showing it is captured
			if(opponentColor == ChessLogic.COLOR_WHITE){
				outerLoop:
					for(int r = 5; r > 0; r--){
						for(int c = 9; c <= ChessLogic.MAX_COLUMN; c++){
							if(!isPieceAtLocation(r, c)){
								opponentPiece.setRow(r);
								opponentPiece.setColumn(c);
								break outerLoop;
							}
						}
					}
			}else{
				outerLoop:
					for(int r = 5; r > 0; r--){
						for(int c = -2; c >= ChessLogic.MIN_COLUMN; c--){
							if(!isPieceAtLocation(r, c)){
								opponentPiece.setRow(r);
								opponentPiece.setColumn(c);
								break outerLoop;
							}
						}
					}
			}
			opponentPiece.setPiecePosition();
		}
		
		dragPiece.setRow(targetRow);
		dragPiece.setColumn(targetColumn);
		
		if (isGameEndConditionReached()) {
			this.gameState = GAME_STATE_END;
		} else {
			this.changeGameState();
		}

		return true;
	}

	/**
	 * check if the games end condition is met. King has been captured
	 * @return true if the game end condition is met
	 */
	public boolean isGameEndConditionReached() {
		if(this.gameState == ChessLogic.GAME_STATE_END)
			return true;
		for (Piece piece : this.pieces) {
			if (piece instanceof King && piece.isCaptured()) {
				if (this.gameState == ChessLogic.GAME_STATE_BLACK) {
					JOptionPane.showMessageDialog(null, "Game Over! Black won!");
				} else {
					JOptionPane.showMessageDialog(null, "Game Over! White won!");
				}
				return true;
			} 	
		}
		return false;
	}
	
	/**
	 * switches the game state based on conditions
	 * @exception IllegalStateException if game is an unknown state
	 */
	public void changeGameState() {

		// check if game end condition has been reached
		if (isGameEndConditionReached()) {
			this.gameState = ChessLogic.GAME_STATE_END;
			return;
		}

		switch (this.gameState) {
			case GAME_STATE_BLACK:
				this.gameState = GAME_STATE_WHITE;
				break;
			case GAME_STATE_WHITE:
				this.gameState = GAME_STATE_BLACK;
				break;
			case GAME_STATE_END:
				//Do nothing
				break;
			default:
				throw new IllegalStateException("Unknown game state:" + this.gameState);
		}
	}
	
	/**
	 * returns the first piece at the specified location that is not captured
	 * @param row Row to search for
	 * @param column Column to search for
	 * @return the first non-captured piece at specified location
	 */
	public Piece getNonCapturedPieceAtLocation(int row, int column) {
		for (Piece piece : this.pieces) {
			if(piece.getRow() == row
					&& piece.getColumn() == column
					&& piece.isCaptured() == false ){
				return piece;
			}
		}
		return null;
	}
	
	/**
	 * returns true if a piece is found at the specified location regardless if captured or not
	 * @param row Row to search for
	 * @param column Column to search for
	 * @return true, if location contains a piece
	 */
	public boolean isPieceAtLocation(int row, int column) {
		for (Piece piece : this.pieces) {
			if(piece.getRow() == row
					&& piece.getColumn() == column){
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether there is a piece at the specified location that is not
	 * captured and has the specified color.
	 * @param color Color to search for
	 * @param row Row to search for
	 * @param column Column to search for
	 * @return true, if the location contains an opponent
	 */
	private boolean isNonCapturedPieceAtLocation(int color, int row, int column) {
		for (Piece piece : this.pieces) {
			if( piece.getRow() == row
					&& piece.getColumn() == column
					&& piece.isCaptured() == false
					&& piece.getColor() == color){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether there is a piece at the specified location
	 * @param row Row to search for
	 * @param column Column to search for
	 * @return true, if the location contains a piece
	 */
	public boolean isNonCapturedPieceAtLocation(int row, int column) {
		for (Piece piece : this.pieces) {
			if( piece.getRow() == row
					&& piece.getColumn() == column
					&& piece.isCaptured() == false){
				return true;
			}
		}
		return false;
	}
}
