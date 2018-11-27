import java.awt.event.*;

/**
 * 
 * @author Jonathan De Leon
 * @category CS 241 Final Project
 * @version 1.00
 * @content
 * 		These are the mouse listener classes that are used on the ChessGui and for the
 * 		pieces.
 *
 */

public class PiecesListener implements MouseListener, MouseMotionListener{
		private ListType<Piece> pieces;		//list of pieces
		private ChessGui chessGui;			//ChessGui object
		
		private Piece dragPiece;			//piece being dragged
		private int dragOffsetX;			//drag offset x coordinate
		private int dragOffsetY;			//drag offset y coordinate

		/**
		 * Constructor
		 */
		public PiecesListener(ListType<Piece> pieces, ChessGui chessGui) {
	        this.pieces = pieces;
	        this.chessGui = chessGui;
	    }

		public void mousePressed(MouseEvent e) {
	        int x = e.getPoint().x;
	        int y = e.getPoint().y;
	 
	        // find out which piece to move
	        // iterating in reverse order
	        for (int i = pieces.size()-1; i >= 0; i--) {
	            Piece piece = pieces.get(i);
	            if(piece.isCaptured()) 
	            	continue;
	 
	            if(mouseOverPiece(piece,x,y)){
	                // calculate offset
	                dragOffsetX = x - piece.getX();
	                dragOffsetY = y - piece.getY();
	                dragPiece = piece;
	                break;
	            }
	        }
	 
	        // move drag piece to the top of the list
	        if(dragPiece != null){
	            pieces.remove(dragPiece);
	            pieces.add(dragPiece);
	        }
	    }
	 
	    /**
	     * check whether the mouse is currently over this piece
	     * @param piece the playing piece
	     * @param x x coordinate of mouse
	     * @param y y coordinate of mouse
	     * @return true if mouse is over the piece
	     */
	    private boolean mouseOverPiece(Piece piece, int x, int y) {
	        return piece.getX() <= x
	            && piece.getX()+piece.getWidth() >= x
	            && piece.getY() <= y
	            && piece.getY()+piece.getHeight() >= y;
	    }
	 
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if(dragPiece != null){
	    		int x = e.getPoint().x - this.dragOffsetX;
	    		int y = e.getPoint().y - this.dragOffsetY;
	    		
	    		//set the piece to a new location
	    		chessGui.setNewPieceLocation(dragPiece, x, y);
	    		chessGui.repaint();
	    		dragPiece = null;
	    	}
	    }
	 
	    @Override
	    public void mouseDragged(MouseEvent e) {
	        if(dragPiece != null){
	    		int x = e.getPoint().x - this.dragOffsetX;
	    		int y = e.getPoint().y - this.dragOffsetY;

	    		System.out.println(
						"row:"+ChessGui.convertYToRow(y)
						+" column:"+ChessGui.convertXToColumn(x));

	            dragPiece.setX(x);
	            dragPiece.setY(y);
	            chessGui.repaint();
	        }
	 
	    }
		@Override
		public void mouseMoved(MouseEvent e) {
		}
		@Override
		public void mouseClicked(MouseEvent e) {
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
	}