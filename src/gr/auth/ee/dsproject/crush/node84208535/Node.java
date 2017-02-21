package gr.auth.ee.dsproject.crush.node84208535;

import java.util.ArrayList;

import gr.auth.ee.dsproject.crush.board.Board;
import gr.auth.ee.dsproject.crush.board.CrushUtilities;
import gr.auth.ee.dsproject.crush.heuristics.HeuristicsEngine;
import gr.auth.ee.dsproject.crush.heuristics.SliderMathModel;
import gr.auth.ee.dsproject.crush.player.CandiesRemovedHeuristic;
import gr.auth.ee.dsproject.crush.player.DistanceFromTopHeuristic;
import gr.auth.ee.dsproject.crush.player.move.PlayerMove;


/**
 * Node class provides a way to model the nodes
 * needed in an a tree representing the state of 
 * a ThmmyCrush Board.
 * 
 * 
 * Public constructors defined in Node:
 * -public Node()
 * -public Node(Node parent)
 * -public Node(Node parent, Board board)
 * -public Node(Node parent, Board board, PlayerMove move)
 * 
 * Public methods defined in Node:
 * -public Node getParent()
 * -public ArrayList<Node> getChildren()
 * -public Board getNodeBoard()
 * -public int getNodeDepth()
 * -public PlayerMove getNodeMove()
 * -public double getNodeEvaluation()
 * -public void setParent(Node parent)
 * -public void setChildren(ArrayList<Node> children)
 * -public void setNodeBoard(Board nodeBoard)
 * -public void setNodeDepth(int nodeDepth)
 * -public void setNodeMove(PlayerMove nodeMove)
 * -public void setNodeEvaluation(double nodeEvaluation)
 * -public void addChild(Node child) throws NullNodeRuntimeException
 * -public void createChildren()
 * -public double evaluate(boolean negative)
 * -public boolean leadsToExtraTurn() throws NonEvaluatedNodeException
 * 
 * Private methods defined in Node:
 * -private double doHeuristicEvaluation(Board board, PlayerMove move)
 * 
 * Exceptions defined in Node:
 * -public static class NullNodeRuntimeException extends RuntimeException
 * -public static class NonEvaluatedNodeException extends RuntimeException
 * 
 * @author Dimitrios Karageorgiou
 * @version 0.4
 */
public class Node {
	
//==== Private instance variables ====
	
	/**
	 * The parent node of this node.
	 */
	private Node parent;
	
	/**
	 * An ArrayList containing all the children nodes of this 
	 * node.
	 */
	private ArrayList<Node> children;
	
	/**
	 * The depth current node has in the Tree.
	 */
	private int nodeDepth;
	
	/**
	 * The PlayerMove object associated with this node.
	 */
	private PlayerMove nodeMove;
	
	/**
	 * The Board object associated with this node.
	 */
	private Board nodeBoard;
	
	/**
	 * The evaluation score this node's move has.
	 */
	private double nodeEvaluation;

	/**
	 * Indicates whether the move evaluated on this node, initiated
	 * an extra turn for this player or not.
	 */
	private boolean extraTurn;
	
	/**
	 * Indicates whether this node has been evaluated by calling evaluate()
	 * method or not.
	 */
	private boolean hasEvaluated;
	
	
//==== Public Constructors ====
	
	/**
	 * Create a Node object without any arguments.
	 */
	public Node() {
		children = new ArrayList<>();
		extraTurn = false;
		hasEvaluated = false;
	}
	
	/**
	 * Create a Node object by providing the parent Node object.
	 * 
	 * @param parent The Node to be considered the parent one in
	 * 				 the tree.
	 */
	public Node(Node parent) {
		this();
		this.parent = parent;
	}
	
	/**
	 * Create a Node object by providing the parent Node object
	 * and the Board object associated with this node. 
	 * 
	 * @param parent The Node to be considered the parent one in
	 * 				 the tree.
	 * @param board The board associated with this node.
	 */
	public Node(Node parent, Board board) {
		this();
		this.parent = parent;
		this.nodeBoard = board;
	}
	
	/**
	 * Create a new node by providing the parent node, the board
	 * representing the current state and the move that lead to this
	 * state.
	 *  
	 * @param parent A Node object representing the previous state. For
	 * 				 root nodes it may be null.
	 * @param board A Board object representing the current state of
	 * 				the game.
	 * @param move A PlayerMove object representing the move which lead
	 * 			   to current state. For root nodes it may be null.
	 */
	public Node(Node parent, Board board, PlayerMove move) {
		this();
		this.parent = parent;
		this.nodeBoard = board;
		this.nodeMove = move;
	}
	
	
//==== Public Getters ====
	
	/**
	 * Get the parent node of this node.
	 * 
	 * @return The parent Node object associated with
	 * 		   this node.
	 */
	public Node getParent() { return this.parent; }
	
	/**
	 * Get the children of this node.
	 * 
	 * @return An ArrayList<Node> object containing all the
	 * 		   children nodes of this node.
	 */
	public ArrayList<Node> getChildren() { return this.children; }
	
	/**
	 * Get the board of this node.
	 * 
	 * @return The Board object associated with this node.
	 */
	public Board getNodeBoard() { return this.nodeBoard; }
	
	/**
	 * Get the depth of this node in the tree.
	 * 
	 * @return An int representing the depth of this node in
	 * 		   its tree.
	 */
	public int getNodeDepth() { return this.nodeDepth; }
	
	/**
	 * Get the move associated with this node.
	 * 
	 * @return The PlayerMove object associated with this node.
	 */
	public PlayerMove getNodeMove() { return this.nodeMove; }
	
	/**
	 * Get the evaluation of this node.
	 * 
	 * @return A double representing the evaluation of this node,
	 * 		   provided by user.
	 */
	public double getNodeEvaluation() { return this.nodeEvaluation; }
	
	
//==== Public Setters ====
	
	/**
	 * Set the parent node of this node.
	 * 
	 * @param parent A Node object that will be set as parent of
	 * 				 the current one.
	 */
	public void setParent(Node parent) { this.parent = parent; }
	
	/**
	 * Set the children nodes of this node.
	 * 
	 * @param children An ArrayList<Node> object containing all
	 * 				   children nodes of the current one.
	 */
	public void setChildren(ArrayList<Node> children) { this.children = children; }
	
	/**
	 * Sets given board as the board of this node.
	 * 
	 * @param nodeBoard The Board object to be set as the board of
	 * 					this node. 
	 */
	public void setNodeBoard(Board nodeBoard) { this.nodeBoard = nodeBoard; }
	
	/**
	 * Sets given depth as the depth of this node.
	 * 
	 * @param nodeDepth An int representing the depth of the node
	 * 					in the tree.
	 */
	public void setNodeDepth(int nodeDepth) { this.nodeDepth = nodeDepth; }
	
	/**
	 * Sets the move associated with this node.
	 * 
	 * @param nodeMove A PlayerMove object to be set as the node's move.
	 */
	public void setNodeMove(PlayerMove nodeMove) { this.nodeMove = nodeMove; }
	
	/**
	 * Sets the evaluation score of this node.
	 * 
	 * @param nodeEvaluation A double to be set as the evaluation score
	 * 						 of this node.
	 */
	public void setNodeEvaluation(double nodeEvaluation) { this.nodeEvaluation = nodeEvaluation; }
	

//==== Public Methods ====
	
	/**
	 * Add given node as a child node to the current one.
	 * 
	 * If given node is null, a NullNodeRuntimeException is thrown.
	 * 
	 * @param child A Node object to be added as child node of this
	 * 				node.
	 * @throws NullNodeRuntimeException
	 */
	public void addChild(Node child) throws NullNodeRuntimeException {
		
		if(child == null) throw new NullNodeRuntimeException();
		
		if (children == null) children = new ArrayList<>();
		
		children.add(child);
	}
		
	/**
     * Creates all the children of the current node.
     * 
     * It creates all the children, i.e. the next states,
     * that are possible based on the board of the current node.
     * 
     * It finds out all possible moves on the board of current node
     * and for every single one of these moves creates a child node
     * with its move set to the move lead there, its board set to
     * the board this move caused to be created and parent set to
     * given node.
     */
    public void createChildren() {
    	
    	for (int[] dirMove : CrushUtilities.getAvailableMoves(nodeBoard)) {
    		
    		// Convert old style move of [x, y, direction] to PlayerMove object.
    		int[] cordsMove = CrushUtilities.calculateNextMove(dirMove);
    		PlayerMove move = new PlayerMove(
    				nodeBoard.giveTileAt(cordsMove[0], cordsMove[1]),
    				nodeBoard.giveTileAt(cordsMove[2], cordsMove[3])
    		);
    		
    		Board afterMoveBoard = CrushUtilities.boardAfterFullMove(nodeBoard, dirMove);
    		
    		addChild(new Node(this, afterMoveBoard, move));
    	}
    }
	
	/**
	 * Evaluates the move associated with this node and sets its evaluation
	 * to it.
	 * 
	 * Move is evaluated using a heuristic evaluation upon it.
	 * 
	 * Evaluation is done using the move of this node, i.e. the move which
	 * caused the current node to be created and the board of the previous
	 * state, i.e. the parent's board. Evaluation on root nodes, i.e. nodes
	 * that do not have parent, is always 0.
	 * 
	 * @param negative Defines whether this node should be evaluated positively
	 * 				   or negatively.
	 * @return The evaluation in the form of a double.
	 */
	public double evaluate(boolean negative) {	
		
		if (parent == null) {
    		// If parent is null, then no move lead to current state so
    		// evaluation of current state is 0.
    		setNodeEvaluation(0);
    	
    	} else if (negative) {
    		setNodeEvaluation(
    				-doHeuristicEvaluation(parent.getNodeBoard(), nodeMove)
    		);
    	
    	} else {
    		setNodeEvaluation(
    				doHeuristicEvaluation(parent.getNodeBoard(), nodeMove)
    		);
    	}
		
		// Set this node as evaluated.
		hasEvaluated = true;
		
		return nodeEvaluation;
	}

	/**
	 * Returns true if player is granted an extra turn after doing
	 * the move associated with this node. 
	 * 
	 * If node has not been evaluated yet, a NonEvaluatedNodeException
	 * is thrown.
	 * 
	 * @return True if player is granted an extra turn after doing
	 * 		   the associated move, else false.
	 * @throws NonEvaluatedNodeException
	 */
	public boolean leadsToExtraTurn() throws NonEvaluatedNodeException {
		
		if (!hasEvaluated) throw new NonEvaluatedNodeException();
		
		return extraTurn;
	}
	
	
//==== Private methods ====
	
	/**
     * Does a heuristic evaluation of the given move, based on the
     * given board.
     * 
     * Evaluation is done using the following heuristics:
     *   -CandiesRemovedHeuristic
     *   -DistanceFromTopHeuristic
     * 
     * @param board A board on which the move will be evaluated.
     * @param move The move to be evaluated.
     * @return A double representing how good the move is.
     */
    private double doHeuristicEvaluation(Board board, PlayerMove move) {
    	
    	HeuristicsEngine engine = new HeuristicsEngine(new SliderMathModel(1.7));
    	
    	CandiesRemovedHeuristic candyHeur = new CandiesRemovedHeuristic(move, board);
    	
    	engine.add(candyHeur, SliderMathModel.VERY_HIGH);
    	engine.add(new DistanceFromTopHeuristic(move, board), SliderMathModel.VERY_LOW);
    	
    	double score = engine.evaluate(); 
    	
    	// Check whether this move initiates an extra turn.
    	// Ugly, ugly, ugly... really ugly, but no time to
    	// implement proper actions and listeners...
    	extraTurn = candyHeur.causedAnExtraTurn();
 	
    	return score;
    }
    
	
//==== Exceptions defined in Node ====
	
	/**
	 * An exception to be thrown when a Node reference is null where it
	 * shouldn't.
	 */
	public static class NullNodeRuntimeException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
	
	/**
	 * An exception to be thrown when access to data that are valid only
	 * after node evaluation has taken place is asked, though evaluation
	 * has not happened yet.
	 */
	public static class NonEvaluatedNodeException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
}
