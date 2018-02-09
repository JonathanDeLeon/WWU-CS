import java.awt.*;

/**
 * 
 * @author Jonathan De Leon
 * @category CS 241 Final Project
 * @version 1.00
 * @content
 *		Piece class is the super class of all pieces where all pieces in a chess game
 *		will contain the following methods and fields.
 */

public class Piece {
	private Image img;
	private int x;
	private int y;
	private int row;
	private int column;
	private int width;
	private int height;
	
	private int color;
	private boolean captured = false;

	/**
	 * Constructor
	 */
	public Piece() {
		this.row = -1;
		this.column = -1;
	}
	
	public Piece(int row, int column, int color) {
		this.row = row;
		this.column = column;
		this.color = color;
		this.setPiecePosition();
	}
	/*
	 * Getters and Setters
	 */
	public Image getImage() {
		return img;
	}
	public void setImage(Image img) {
		this.img = img;
		this.width = img.getWidth(null);
		this.height = img.getHeight(null);
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	/**
	 * toString method
	 */
	public String toString(){
		String str = "{Type: "+this.getClass()+", Color: "; 
		str += this.getColor() == ChessLogic.COLOR_WHITE ? "White" : "Black";
		str += ", Row: "+row+", Column: "+column+", isCaptured: "+captured+"}";
		return str;
	}
	/**
	 * Sets if piece has been captured
	 * @param isCaptured
	 */
	public void isCaptured(boolean isCaptured){
		this.captured = isCaptured;
	}
	public boolean isCaptured(){
		return this.captured;
	}
	
	/**
	 * isValidMove default method to check if any piece made a valid move. This method is
	 * overridden in subclasses
	 * @param targetRow
	 * @param targetColumn
	 * @return
	 */
	public boolean isValidMove(int targetRow, int targetColumn){
		// check if target location within boundaries
		if( targetRow < ChessLogic.ROW_1 || targetRow > ChessLogic.ROW_8
				|| targetColumn < ChessLogic.COLUMN_A || targetColumn > ChessLogic.COLUMN_H){
			throw new IllegalArgumentException("Target's row or column out of scope");
		}
		//location is the same
		if(this.row == targetRow && this.column == targetColumn)
			return false;
		return true;
	}
	
	/**
	 * Static default validMove checker. Making sure the piece has made a move and is not out of range
	 * @param sourceRow
	 * @param sourceColumn
	 * @param targetRow
	 * @param targetColumn
	 * @return
	 */
	public static boolean isValidMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn){
		// check if target location within boundaries
		if( targetRow < ChessLogic.ROW_1 || targetRow > ChessLogic.ROW_8
				|| targetColumn < ChessLogic.COLUMN_A || targetColumn > ChessLogic.COLUMN_H){
			throw new IllegalArgumentException("Target's row or column out of scope");
		}
		//location is the same
		if(sourceRow == targetRow && sourceColumn == targetColumn)
			return false;
		return true;
	}
	
	/**
	 * Sets piece coordinates that are equal to the piece's row and column
	 */
	public void setPiecePosition(){
		this.x = ChessGui.convertColumnToX(this.column);
		this.y = ChessGui.convertRowToY(this.row);
	}
}
