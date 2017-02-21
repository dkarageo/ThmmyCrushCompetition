package gr.auth.ee.dsproject.crush.player;

import java.util.ArrayList;

import gr.auth.ee.dsproject.crush.board.Board;
import gr.auth.ee.dsproject.crush.board.CrushUtilities;
import gr.auth.ee.dsproject.crush.defplayers.AbstractPlayer;
import gr.auth.ee.dsproject.crush.node.Node;
import gr.auth.ee.dsproject.crush.player.move.PlayerMove;


/**
 * A player that uses minimax algorithm with AB pruning on
 * a heuristics based evaluate function.
 * 
 * Depth of minimax algorithm is defined in MINIMAX_DEPTH
 * constant of this class.
 * 
 * Constants defined in MinMaxPlayer:
 * -public static final int MINIMAX_DEPTH = 4
 * 
 * Public constructors defined in MinMaxPlayer:
 * -public MinMaxPlayer(Integer pid)
 * 
 * Public methods defined in MinMaxPlayer:
 * -public String getName()
 * -public int getId()
 * -public int getScore()
 * -public void setScore(int score)
 * -public void setId(int id)
 * -public void setName(String name)
 * -public int[] getNextMove(ArrayList<int[]> availableMoves, Board board)
 * 
 * @author Dimitrios Karageorgiou
 * @version 0.3
 */
public class HeuristicPlayer implements AbstractPlayer {

//==== Public Constants ====
	
	/**
	 * Defines the depth of minimax algorithm.
	 */
	public static final int MINIMAX_DEPTH = 3;
	
	
//==== Instance Variables ====
	
    int score;
    int id;
    String name;

    
//==== Public Constructors ====
    
    public HeuristicPlayer(Integer pid) {
    	
    	name = "MinimaxOnHeuristic";
    	id = pid;
        score = 0;
    }

    
//==== Public Getters ====    
    
    public String getName() { return name; }

    public int getId() { return id; }

    public int getScore() { return score; }
    
    
//==== Public Setters ====
    
    public void setScore(int score) { this.score = score; }
    
    public void setId(int id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    
//==== Public Methods ====
    
    /**
     * Finds and returns the best move out of provided available
     * moves that is going to be played on the given board, using
     * a minimax algorithm based on a heuristic evaluation function.
     * 
     * Depth of minimax algoritmh is defined by MINIMAX_DEPTH
     * constant in MinMaxPlayer class.
     * 
     * @param availableMoves The currently available moves for the
     * 						 player to play on the given board, in
     * 						 the form of [x, y, direction] int array.
     * 						 All these moves are packed into an
     * 						 ArrayList object. direction value as defined
     * 						 in board.CrushUtilities
     * @param board The board on which the move is going to be played.
     * @return The move that player should play, if the form of
     * 		   [x1, y1, x2, y2] int array.
     */
    public int[] getNextMove(ArrayList<int[]> availableMoves, Board board) {
    	
    	long startTime = System.currentTimeMillis();
    	
    	// Create the root node representing current state of board.
    	// This is the initial state for minimax.
    	Node root = new Node(null, CrushUtilities.cloneBoard(board, 45), null);
    	
    	// Create the minimax tree to the depth defined by MINIMAX_DEPTH constant.
    	createMinimaxTree(root, MINIMAX_DEPTH, -Double.MAX_VALUE, Double.MAX_VALUE, true);
    	
    	int[] cordsArray = findBestMove(root).toCordsArray(); 
    	
    	long endTime = System.currentTimeMillis();
    	
    	System.out.println("Time to find a move: " + (endTime - startTime) + "ms");
    	
    	// Return the highest scoring move out of minimax tree.
    	return cordsArray;
    }   

   
//==== Private Methods ====
        
    /**
     * Creates a memory optimized A-B pruned minimax tree under 
     * the root node, which should only be used for accessing root
     * node's evaluation, its children evaluations and its children moves.
     * It returns the evaluation of the current state for specified 
     * player, maximizing or minimizing
     * 
     * The evaluations are as expected formed by searching until the given
     * depth. Though all nodes after first level are discarded. Thus,
     * memory consumption scales linear with depth instead of
     * exponentially.
     * 
     * Root node should contain at least the board of the initial state.
     * 
     * It should normally be called with min and max arguments of
     * -Double.MAX_VALUE and Double.MAX_VALUE accordingly. Though
     * they can be set to any best matching value and are the 
     * initial values used for A-B pruning.
     * 
     * Maximizing defines if the current state should be checked from
     * player's perspective, i.e. maximizing, or enemy's perspective,
     * i.e. minimizing. Maximizing player considers greater values better,
     * when minimizing player considers lower values better.
     * 
     * @param n The root node for the tree to be created.
     * @param depth The depth to which the tree will be created.
     * @param min The minimum evaluation value that is considered valid.
     * @param max The maximum evaluation value that is considered valid.
     * @param maximizing True for getting evaluation for player's perspective,
     * 					 false for getting evaluation for enemy's perspective.	
     * @return The evaluation of the current state for given player.
     */
    private double createMinimaxTree(Node n, int depth, double min, 
    								 double max, boolean maximizing) 
    {    	
    	// DEBUG
    	//System.out.println("Maximizing: " + maximizing);
    	
    	// Find the evaluation of current state. Since this method is going
    	// to run one more time than depth, the evaluation here is the
    	// opposite than maximizing. This happens because the first call
    	// is the root node, and for every move it's effect is calculated
    	// on the next run. 
    	n.evaluate(maximizing);
    	    	
    	// If current node is not a leaf, then evaluate its children,
    	// and form the overall evaluation by using the ones from deeper
    	// levels.
    	if (depth != 0) {
    		n.createChildren();
    		
    		// Checks whether the move associated to current node leads to an
    		// extra turn for current player.
    		boolean extraTurn = n.leadsToExtraTurn();
    		if (!extraTurn) depth--; // On extra turn, add 1 more level to minimax
    								 // because current one is going 
    		
    		if (n.getChildren().size() == 0 ) {
    			// If known available moves on the board have been 
    			// depleted and no further search can be done, then
    			// do a fixed evaluation for the future moves that
    			// may exist on the actual board.

//    			System.out.println("No moves found");
    			
    			if (maximizing)	n.setNodeEvaluation(n.getNodeEvaluation() + doFixedEvaluation(depth));
        		else n.setNodeEvaluation(n.getNodeEvaluation() - doFixedEvaluation(depth));
    			
    			return n.getNodeEvaluation();
    		}
    		
    		if (maximizing) {
    			double cMax = -Double.MAX_VALUE;
    			
//    			// DEBUG
//				if(extraTurn) {
//					System.out.println("Current Maximizing: " + maximizing + " and Next Maximizing: " + extraTurn);
//				}
    			
    			for (Node child : n.getChildren()) {    				    				
    				double eval = createMinimaxTree(child, depth, cMax, max, extraTurn);
    				
    				cMax = Math.max(cMax, eval);
    				
    				if ((n.getNodeEvaluation() + eval) >= max) {
    					cMax = eval;
    					break;
    				}
        		}
    			
    			n.setNodeEvaluation(n.getNodeEvaluation() + cMax);
    			
    		} else {
    			double cMin = Double.MAX_VALUE;

//				// DEBUG
//    			if(extraTurn) {
//					System.out.println("Current Maximizing: " + maximizing + " and Next Maximizing: " + !extraTurn);
//				}
				
    			for (Node child : n.getChildren()) {    				
    				double eval = createMinimaxTree(child, depth, cMin, max, !extraTurn);
    				
    				cMin = Math.min(cMin, eval);
    				
    				if ((n.getNodeEvaluation() + eval) <= min) {
    					cMin = eval;
    					break;
    				}	
        		}
    			
    			n.setNodeEvaluation(n.getNodeEvaluation() + cMin);    			
    		}
    	}
    	
    	// Release from memory all the node that will never used again.
    	// It essentially leaves the tree only with root node and its
    	// children, and they also got their board states removed, but
    	// it makes ram consumption to scale linear with depth instead
    	// of exponentially.
    	n.setNodeBoard(null);
    	if (depth < MINIMAX_DEPTH - 1) n.setChildren(null);
    	
    	return n.getNodeEvaluation();
    }
       
    /**
     * Find the move that lead to the highest evaluated branch
     * on a minimax tree.
     * 
     * @param root The root node of the minimax tree.
     * @return The move lead to highest scoring branch of
     * 		   the tree.
     */
    private PlayerMove findBestMove(Node root) {
    	
    	PlayerMove bestMove = null;
    	double max = -Double.MAX_VALUE;
    	    	
    	for (Node child : root.getChildren()) {
    		double curEval = child.getNodeEvaluation();
    		
    		if (max < curEval) {
    			max = curEval;
    			bestMove = child.getNodeMove();
    		}
    	}
    	
    	return bestMove;
    }    
    
    /**
     * Does a fixed evaluation and returns a fixed evaluation value.
     * 
     * Used when no known data are available in order to do a real
     * heuristic evaluation.
     * 
     * @param remainingDepth The remaining levels the minimax cannot
     * 						 evaluate due to absence of data.
     * @return A double of value 3.0 for remaining depths greater or equal 
     * 		   to 2, and of value 4.0 for remaining depths lesser or equal
     * 		   to 1.  
     */
    private double doFixedEvaluation(int remainingDepth) {
    	if (remainingDepth <= 1) return 0.0;
    	else return 0.0;
    }    
}

//package gr.auth.ee.dsproject.crush.player;
//
//import java.util.ArrayList;
//
//import gr.auth.ee.dsproject.crush.defplayers.AbstractPlayer;
//import gr.auth.ee.dsproject.crush.board.CrushUtilities;
//import gr.auth.ee.dsproject.crush.board.Board;
//import gr.auth.ee.dsproject.crush.heuristics.*;
//import gr.auth.ee.dsproject.crush.player.move.PlayerMove;
//import gr.auth.ee.dsproject.crush.board.Tile;
//
//
//public class HeuristicPlayer implements AbstractPlayer
//{
//    // TODO Fill the class code.
//
//    int score;
//    int id;
//    String name;
//
//    public HeuristicPlayer (Integer pid)
//    {
//        id = pid;
//        score = 0;
//    }
//
//    @Override
//    public String getName ()
//    {
//        return "evaluation";
//    }
//
//    @Override
//    public int getId ()
//    {
//        return id;
//    }
//
//    @Override
//    public void setScore (int score)
//    {
//        this.score = score;
//    }
//
//    @Override
//    public int getScore ()
//    {
//        return score;
//    }
//
//    @Override
//    public void setId (int id)
//    {
//        this.id = id;
//    }
//
//    @Override
//    public void setName (String name)
//    {
//        this.name = name;
//    }
//
//    @Override
//    public int[] getNextMove(ArrayList<int[]> availableMoves, Board board)
//    {
//        int[] move = availableMoves.get(findBestMoveIndex(availableMoves, board));
//
//        return CrushUtilities.calculateNextMove(move);
//    }
//
//    int findBestMoveIndex (ArrayList<int[]> availableMoves, Board board)
//    {	
//	    double max = 0.0;
//	    int max_i = 0;
//	  
//	    for (int i = 0; i < availableMoves.size(); i++) {
//		   	double moveScore = moveEvaluation(availableMoves.get(i), board);
//		  		  	
//		  	if (moveScore > max) {
//		  		max = moveScore;
//		  	    max_i = i;
//		  	}
//        }
//	  
//	    return max_i;	  
//    }
//  
//    /**
//     * Evaluates the given move based on heuristics run on a HeuristicsEngine.
//     * 
//     * The math model in use is the SliderMathModel with a scale factor of 2.0
//     * 
//     * Heuristics used for evaluation:
//     * -CandiesRemovedHeuristic on VERY_HIGH
//     * -DistanceFromTopHeuristic on VERY_LOW
//     *
//     * @param move A move to be evaluated in the form of {x, y, direction}
//     * 			 array.
//     * @param board The board on which the move will be evaluated.
//     * @return A value between 0.0 and less than 100.0 indicating how good a move is.
//     * 		 Greater values mean better moves.	
//     */
//    double moveEvaluation (int[] move, Board board)
//    {
//	    HeuristicsEngine engine = new HeuristicsEngine(new SliderMathModel(2.0));
//	  	  
//	    int[] dirMove = CrushUtilities.calculateNextMove(move);
//	    
//	    Tile tile1 = board.giveTileAt(dirMove[0], dirMove[1]);
//	    Tile tile2 = board.giveTileAt(dirMove[2], dirMove[3]);
//	    PlayerMove pMove = new PlayerMove(tile1, tile2);
//	  
//	    engine.add(new CandiesRemovedHeuristic(pMove, board), SliderMathModel.VERY_HIGH);
//	    engine.add(new DistanceFromTopHeuristic(pMove, board), SliderMathModel.VERY_LOW);
//	  
//	    return engine.evaluate();
//    }
//
//  
////==== Legacy code ====
////  
////    /** 
////     * getMoveDestination() calculates the cords of the block next to the
////     * given one, on the given direction.
////     * 
////     * directedMove argument is in the form of { x, y, CrushUtilities.[DIRECTION] }
////     * 
////     * No checking is done for negative origin or destination cords.
////     * 
////     * @param directedMove A move in the form of {x, y, direction} int array.
////     * @return The block's cord where move's direction points in the form of
////     * 		{x, y} int array.
////     */ 
////    private static int[] getMoveDestination (int[] directedMove) 
////    {
////	    // Assume that destination cords is the same as origin cords.
////        int[] dest = { directedMove[0], directedMove[1] };
////  
////        int direction = directedMove[2];
////  
////        // Correct dest cords according to the move direction. 
////	    if (direction == CrushUtilities.LEFT) { 
////		    dest[0] -= 1;
////	    } 
////	    else if (direction == CrushUtilities.RIGHT) {
////		    dest[0] += 1;
////	    } 
////	    else if (direction == CrushUtilities.UP) {
////		    dest[1] += 1;
////	    } 
////	    else if (direction == CrushUtilities.DOWN) {
////		    dest[1] -= 1;
////	    }
////	  
////	    return dest;	  
////    }
//}
