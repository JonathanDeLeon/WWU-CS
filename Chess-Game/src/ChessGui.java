import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import javax.swing.*;

/**
 * 
 * @author Jonathan De Leon
 * @category CS 241 Final Project
 * @version 1.00
 * @content
 * 		ChessGui contains the GUI for the chess game that has chessLogic. The pieces are 
 * 		painted onto the board which can then be dragged. 
 *
 */

public class ChessGui extends JPanel {
	private JLabel gameStateLabel;		//Game State Label	
	private JLabel blackPlayerLabel;		//Black Player Label	
	private JLabel whitePlayerLabel;		//White Player Label	
	private JPanel buttonPanel;  				// button panel
	private JButton resetButton;               // reset button
	private JButton instrucButton;               // instruction button
	private JButton exitButton;               // exit button
	JFrame f;				//GUI Frame

	private static final int BOARD_START_X = 301;		//Board starting x coordinate
	private static final int BOARD_START_Y = 51;		//Board starting y coordinate
	private static final int SQUARE_WIDTH = 50;			//Tile square width
	private static final int SQUARE_HEIGHT = 50;		//Tile square height
	private static final int DRAG_TARGET_SQUARE_X = BOARD_START_X - (int)(SQUARE_WIDTH/2.0);	//Drag target x coordinate
	private static final int DRAG_TARGET_SQUARE_Y = BOARD_START_Y - (int)(SQUARE_HEIGHT/2.0);	//Drag target y coordinate
	
	private Image imgBackground;		//Background image
	
	private ChessLogic chessLogic; 		//Logic of the game

	//ArrayList to hold pieces
	private ListType<Piece> pieces = new ListType<Piece>();

	public ChessGui() {
		//Dimension boardSize = new Dimension(BOARD_START_X * 5, BOARD_START_Y*20);
		URL urlBackgroundImg = getClass().getResource("/img/board.png");
		this.imgBackground = new ImageIcon(urlBackgroundImg).getImage();
		
		//buildPieces();
		this.chessLogic = new ChessLogic();
		this.pieces = chessLogic.getPieces();
 
        // add mouse listeners to enable drag and drop
        PiecesListener listener = new PiecesListener(this.pieces, this);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        
		this.setLayout(null);
		// label to display game state
		String labelText = this.getGameStateAsText();
		this.gameStateLabel = new JLabel(labelText);
		gameStateLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		gameStateLabel.setBounds(440, 10, 150, 30);
		gameStateLabel.setForeground(Color.WHITE);
		this.add(gameStateLabel);
		
		//player piece labels
		this.blackPlayerLabel = new JLabel("Black Prisoners");
		blackPlayerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		blackPlayerLabel.setForeground(Color.BLACK);
		blackPlayerLabel.setBounds(convertColumnToX(-4), convertRowToY(6), 150, 30);
		this.add(blackPlayerLabel);
		this.whitePlayerLabel = new JLabel("White Prisoners");
		whitePlayerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		whitePlayerLabel.setForeground(Color.WHITE);
		whitePlayerLabel.setBounds(ChessGui.convertColumnToX(10), ChessGui.convertRowToY(6), 150, 30);
		this.add(whitePlayerLabel);

		//build buttons
		buildButtonPanel();
		buttonPanel.setBounds(350, ChessGui.convertRowToY(-2), 300, 35);
		this.add(buttonPanel);
        

        // create JFrame
        f = new JFrame();
        f.setVisible(true);
        f.setTitle("Chess Game");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
		f.setResizable(false);
		f.setSize(this.imgBackground.getWidth(null), this.imgBackground.getHeight(null));
		
		
		//Click instruction button
		instrucButton.doClick();
	}

	/**
	 * Paint component to draw the images of the pieces and the background.
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(this.imgBackground, 0, 0, null);
		for(Piece piece : this.pieces){
			//if(!piece.isCaptured())
				g.drawImage(piece.getImage(), piece.getX(), piece.getY(), null);
		}
		this.gameStateLabel.setText(getGameStateAsText()); 
		if(this.chessLogic.getGameState() == ChessLogic.GAME_STATE_WHITE)
			gameStateLabel.setForeground(Color.WHITE);
		else
			gameStateLabel.setForeground(Color.BLACK);
	}
	
	/**
	 * @return Description of game state
	 */
	private String getGameStateAsText() {
		String state = "unknown";
		switch (this.chessLogic.getGameState()) {
			case ChessLogic.GAME_STATE_BLACK: 
				state = "Black Turn";
				break;
			case ChessLogic.GAME_STATE_WHITE: 
				state = "White Turn";
				break;
			case ChessLogic.GAME_STATE_END: 
				state = "GAME OVER";
				break;
		}
		return state;
	}
	
	/**
	 * Function that builds panels containing buttons
	 */
	private void buildButtonPanel(){
		//Builds buttons and adds Action Listeners for them
		instrucButton = new JButton("Instructions...");
		instrucButton.addActionListener(new ButtonActionListener());
		resetButton = new JButton("Reset Game");
		resetButton.addActionListener(new ButtonActionListener());
		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ButtonActionListener());
		
		buttonPanel = new JPanel();
		
		//Adds the buttons to the panel
		buttonPanel.add(instrucButton);
		buttonPanel.add(resetButton);
		buttonPanel.add(exitButton);
	}
	
	
	/**
	 * convert column into x coordinate
	 * @param column
	 * @return x coordinate for column
	 */
	public static int convertColumnToX(int column){
		return BOARD_START_X + SQUARE_WIDTH * column;
	}
	
	/**
	 * convert row into y coordinate
	 * @param row
	 * @return y coordinate for row
	 */
	public static int convertRowToY(int row){
		return BOARD_START_Y + SQUARE_HEIGHT * (ChessLogic.ROW_8 - row);
	}
	
	/**
	 * convert x coordinate into column
	 * @param x
	 * @return logical column for x coordinate
	*/
	public static int convertXToColumn(int x){
		return (x - DRAG_TARGET_SQUARE_X)/SQUARE_WIDTH;
	}
	
	/**
	 * convert y coordinate into row
	 * @param y
	 * @return logical row for y coordinate
	 */
	public static int convertYToRow(int y){
		return ChessLogic.ROW_8 - (y - DRAG_TARGET_SQUARE_Y)/SQUARE_HEIGHT;
	}
	
	/**
	 * change location of given piece if valid
	 * If the location is not valid, the piece is moved to original position
	 * @param dragPiece	Piece that is being dragged
	 * @param x	X Coordinate of mouse	
	 * @param y Y Coordinate of mouse
	 */
	public void setNewPieceLocation(Piece dragPiece, int x, int y) {
		int targetRow = ChessGui.convertYToRow(y);
		int targetColumn = ChessGui.convertXToColumn(x);
		dragPiece = (Piece)dragPiece;
		if(!chessLogic.isGameEndConditionReached()){
			try{
				if(Piece.isValidMove(dragPiece.getRow(), dragPiece.getColumn(), targetRow, targetColumn)){
					//change model and update gui piece afterwards
					System.out.println("Moving piece from row: "+dragPiece.getRow()+" and column: "+dragPiece.getColumn()+" to row: "+targetRow+" and column: "+targetColumn);
					chessLogic.movePiece(dragPiece.getRow(), dragPiece.getColumn(), targetRow, targetColumn);
				}
			}catch(IllegalArgumentException e){
				// reset piece position if move is not valid
				System.out.println(e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage());
			}catch(IllegalStateException e){
				System.out.println(e.getMessage());
				JOptionPane.showMessageDialog(null, e.getMessage());
			}finally{
				//Sets piece's position whether to reset it or give it a new position
				dragPiece.setPiecePosition();
				System.out.println(dragPiece);
			}
		}
		else{
			dragPiece.setPiecePosition();
			JOptionPane.showMessageDialog(null, "Game is over. Click reset to start a new game.");
		}
	}
	
	/**
	 *	private inner class for buttons 
	 */
	private class ButtonActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == instrucButton){
				// The name of the file to open.
		        String fileName = "instructions.txt";

		        // This will reference one line at a time
		        String line = null;
		        String str = "";

		        try {
		            FileReader fileReader = new FileReader(fileName);
		            BufferedReader bufferedReader = new BufferedReader(fileReader);

		            while((line = bufferedReader.readLine()) != null) {
		            	str += line +"\n";
		                System.out.println(line);
		            }   

					JTextArea ta = new JTextArea(10, 10);
					ta.setText(str);
					ta.setWrapStyleWord(true);
					ta.setLineWrap(true);
					ta.setCaretPosition(0);
					ta.setEditable(false);

		            JOptionPane.showMessageDialog(null, new JScrollPane(ta), "Instructions", JOptionPane.INFORMATION_MESSAGE);
		            // Always close files.
		            bufferedReader.close();         
		        }
		        catch(FileNotFoundException ex) {
		            System.out.println(
		                "Unable to open file '" + 
		                fileName + "'");                
		        }
		        catch(IOException ex) {
		            System.out.println(
		                "Error reading file '" 
		                + fileName + "'");                  
		        }
			}else if(e.getSource() == resetButton){
				f.setVisible(false);
				f.dispose();
				new ChessGui();
			}else{
				System.exit(0);
			}
		}
	}
	
	/**
	 * Main method	 
	 */
	public static void main(String[] args) {
		new ChessGui();
	}
}