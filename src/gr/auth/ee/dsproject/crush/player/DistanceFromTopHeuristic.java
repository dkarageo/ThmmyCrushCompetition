package gr.auth.ee.dsproject.crush.player;

import gr.auth.ee.dsproject.crush.heuristics.Heuristic;
import gr.auth.ee.dsproject.crush.player.move.PlayerMove;
import gr.auth.ee.dsproject.crush.board.Board;


/**
 * A heuristic that judges a move based on its distance from top
 * on the board.
 * 
 * evaluate() method returns a score from 0.0 to less than but not
 * 100.0 depending on the distance of the closest move's tile to the 
 * top and is calculated as:
 *  "(board_rows - greater_y - 1) * (100.0 / board_rows)"  
 * It is smaller for moves closest to the top, and greater for moves
 * closest to the bottom of the board.
 * 
 * Public constructors defined in DistanceFromTopHeuristic:
 * -public DistanceFromTopHeuristic()
 * -public DistanceFromTopHeuristic(PlayerMove move, Board board)
 * 
 * Public methods defined in DistanceFromTopHeuristic:
 * -public void setMove(PlayerMove move)
 * -public void setBoard(Board board)
 * -public PlayerMove getMove()
 * -public Board getBoard()
 * -public double evaluate()
 * 
 * @author Dimitrios Karageorgiou
 * @version 0.3
 */
public class DistanceFromTopHeuristic extends Heuristic {
	PlayerMove move;
	Board board;	
	
	
// ==== Public Constructors ====
	
	/**
	 * Create a new DistanceFromTopHeuristic object.
	 */
	public DistanceFromTopHeuristic() {}
	
	/**
	 * Create a new DistanceFromTopHeuristic object by providing a move
	 * and a board object.
	 * 
	 * @param move The move to be evaluated.
	 * @param board The board on which the move is going to be evaluated.
	 */
	public DistanceFromTopHeuristic(PlayerMove move, Board board) {
		this.move = move;
		this.board = board;
	}
	
	
// ==== Public Setters ====
	
	/**
	 * Set move to be evaluated.
	 * 
	 * @param move PlayerMove object which is going to be evaluated.
	 */
	public void setMove(PlayerMove move) { this.move = move; }
	
	/**
	 * Set board on which the move is going to be evaluated.
	 * 
	 * @param board Board object to be used for move's evaluation.
	 */
	public void setBoard(Board board) { this.board = board; }
	

// ==== Public Getters ====
	
	/**
	 * Get the move currently associated with this heuristic.
	 * 
	 * @return PlayerMove object associated with this heuristic.
	 */
	public PlayerMove getMove() { return this.move; }
	
	/**
	 * Get the board currently associated with this heuristic.
	 * 
	 * @return Board object currently associated with this heuristic.
	 */
	public Board getBoard() { return this.board; }
	
	
// ==== Public Methods ====
	
	/**
	 * Evaluate the move currently associated with this heuristic based
	 * on the associated board.
	 * 
	 * The evaluation is done as following:
	 * 	"(board_rows - greater_y - 1) * (100.0 / board_rows)"
	 * where greater_y is the greater distance from bottom out of the 
	 * two tiles PlayerMove object contains and board_rows is the 
	 * number of rows the Board object has.
	 * 
	 * @return A score between 0.0 and less than 100.0.
	 */
	@Override
	public double evaluate() {
		int greaterY;
		
		// Use the greater y out of two tiles to calculate the score.
		if (move.getY1() > move.getY2()) greaterY = move.getY1(); 
		else greaterY = move.getY2();
				
		return (100.0 / (double) board.getPRows()) * 
			   (board.getPRows() - (double) greaterY - 1.0);
	}

}
