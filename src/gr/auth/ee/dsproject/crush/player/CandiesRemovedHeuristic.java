package gr.auth.ee.dsproject.crush.player;

import java.util.HashSet;
import java.util.Set;

import gr.auth.ee.dsproject.crush.heuristics.Heuristic;
import gr.auth.ee.dsproject.crush.board.Board;
import gr.auth.ee.dsproject.crush.board.CrushUtilities;
import gr.auth.ee.dsproject.crush.board.Tile;
import gr.auth.ee.dsproject.crush.player.move.PlayerMove;
import gr.auth.ee.dsproject.crush.util.BoardUtils;


/**
 * CandiesRemovedHeuristic evaluates a player's move on a
 * game board, according to how many tiles it is going to
 * remove, immediately and by chained moves.
 * 
 * A common way to create an instance of it is the following:
 * "CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(aMove, aBoard)"
 * 
 * It can be either passed to a HeuristicsEngine object or used
 * independently like following:
 * "double score = heur.evaluate();"
 * 
 * Score of evaluate() method is defined in the following way:
 * -4.0 points for the first 6 candies
 * -2.0 points for the following 8 candies
 * -1.0 points for the following 10 candies
 * -50.0/remaining_candies for the following candies remaining.   
 * To a total of a 100.0 score. 
 * 
 * Public constructors defined in CandiesRemovedHeuristic:
 * -public CandiesRemovedHeuristic()
 * -public CandiesRemovedHeuristic(PlayerMove move, Board board)
 * 
 * Public methods defined in CandiesRemovedHeuristic:
 * -public void setBoard(Board board)
 * -public void setPlayerMove(PlayerMove move)
 * -public Board getBoard()
 * -public PlayerMove getPlayerMove()
 * -public double evaluate()
 * -public Set<Tile> initialCandiesRemoved()
 * -public int countChainedCandiesRemoved(Board currentBoard)																			   
 * 
 * @author Dimitrios Karageorgiou
 * @version 0.3
 */
public class CandiesRemovedHeuristic extends Heuristic {
	
// ==== Instance Variables ====
	
	/**
	 * The player's move the heuristic is going to check.
	 */
	private PlayerMove move;
	
	/**
	 * The board where provided player's move should take effect.
	 */
	private Board board;
	
	
// ==== Public Constructors ====
	
	/**
	 * Create a new empty CandiesRemovedHeuristic.
	 */
	public CandiesRemovedHeuristic() {}
	
	/**
	 * Create a new CandiesRemovedHeuristic with a player's move
	 * and the board this move is going to have an effect.
	 * 
	 * @param move The move the heuristic will evaluate. 
	 * @param board The board on which this move is going to be
	 * 				evaluated.
	 */
	public CandiesRemovedHeuristic(PlayerMove move, Board board) {
		this.move = move;
		this.board = board;
	}
	
	
// ==== Public Setters ====
	
	/**
	 * Set the board of the CandiesRemovedHeuristic, where the move is
	 * going to be evaluated.
	 * 
	 * @param board The board where the move will be evaluated.
	 */
	public void setBoard(Board board) { this.board = board; }
	
	/**
	 * Set the move which is going to be evaluated.
	 * 
	 * @param move The move to be evaluated.
	 */
	public void setPlayerMove(PlayerMove move) { this.move = move; }
		
	
// ==== Public Getters ====
	
	/**
	 * Get the board currently associated with this CandiesRemovedHeuristic
	 * object. 
	 * 
	 * @return Board associated with this object.
	 */
	public Board getBoard() { return board; }
	
	/**
	 * Get the move currently associated with this CandiesRemovedHeuristic
	 * object. 
	 * 
	 * @return PlayerMove associated with this object.
	 */
	public PlayerMove getPlayerMove() { return move; }
		
	
// ==== Public Methods ====
	
	@Override
	/**
	 * Calculates a score based on the number of candies the player's
	 * move specified by move instance variable will cause to be removed
	 * from the board. It counts  both candies to be removed immediately 
	 * after player's move and candies to be removed by the following 
	 * chained moves.
	 * 
	 * The score is formed in the following way:
	 * -4.0 points for the first 6 candies
	 * -2.0 points for the following 8 candies
	 * -1.0 points for the following 10 candies
	 * -50.0/remaining_candies for the following candies remaining.   
	 * To a total of a 100.0 score.
	 * 
	 * @return A score between 0 and 100.0 based on the removed tiles.  
	 */
	public double evaluate() {
		double score = 0;
		int overallCandiesRemoved = 0;
		
		// Calculate the tiles removed directly by player's move.
		overallCandiesRemoved += initialCandiesRemoved().size();
		
		// Calculate the tiles removed by chained moves.
		overallCandiesRemoved += countChainedCandiesRemoved(
				CrushUtilities.boardAfterFirstCrush(board, move.toDirArray())
		);
		
		if (overallCandiesRemoved > 6) {
			score += 6.0 * 4.0;
		} else if (overallCandiesRemoved > 0) {
			score += ((double) overallCandiesRemoved) * 4.0;
		}
		overallCandiesRemoved -= 6;
		
		if (overallCandiesRemoved > 8) {
			score += 8.0 * 2.0;
		} else if (overallCandiesRemoved > 0) {
			score += ((double) overallCandiesRemoved) * 2.0;
		}
		overallCandiesRemoved -= 8;
		
		if (overallCandiesRemoved > 10) {
			score += 10.0 * 1.0;
		} else if (overallCandiesRemoved > 0) {
			score += ((double) overallCandiesRemoved) * 1.0;
		}
		overallCandiesRemoved -= 10;
		
		if (overallCandiesRemoved > 0) {
			score += ((double) overallCandiesRemoved) * (50.0 / ((double) board.getRows() * board.getCols()));
		}
		
		return score;
	}
		
	/**
	 * Returns a set of candies to be removed upon playing the move
	 * specified by instance variable move. Only candies that being
	 * removed directly when the move is done are contained. Candies 
	 * removed upon chain moves are not contained. 
	 *  
	 * @return Set of candies on board object defined by instance
	 * variable board to be removed after move. The two tiles that
	 * switch position during a move, are contained unchanged and
	 * with their original cords they have before move.
	 */
	public Set<Tile> initialCandiesRemoved() {
		// Get a copy of the real board object, and play the move on it,
		// but without any crushes.
		Board boardJustBeforeCrush = CrushUtilities.cloneBoard(board);
		
		boardJustBeforeCrush = CrushUtilities.boardAfterFirstMove(
				boardJustBeforeCrush, move.toDirArray()
		);
		
		// Place here all tiles possible to be removed from copied board.
		Set<Tile> tilesForRemoval = new HashSet<>();
				
		// Get the tiles that exist at cords specified by move on copied board, 
		// after the move has taken place.
		Tile[] afterMoveTiles = {
				boardJustBeforeCrush.giveTileAt(move.getX1(), move.getY1()),
				boardJustBeforeCrush.giveTileAt(move.getX2(), move.getY2())
		};
		
		for (Tile t : afterMoveTiles) {
			// Find all the same color tiles around the current one.
			Set<Tile> curTiles = BoardUtils.findAdjacentSameColorTiles(boardJustBeforeCrush, t, 2);
			
			tilesForRemoval.addAll(curTiles);
			tilesForRemoval.add(t);  // The beginning tile is also an one possible for removal.
		}
		
		// Finally find which of the adjacent tiles on copied board form 
		// at least a 3-in-a-row to crush.
		tilesForRemoval = BoardUtils.findTilesThatCrush(tilesForRemoval);
		
		// And now find the actual tile objects of the real board,
		// that corresponds to the ones in the copy. This is the set
		// to be returned.
		Set<Tile> removedOnActualBoard = new HashSet<>();
		
		// If the first tile after move doesn't have adjacent tiles,
		// remove the second tile, in order to use the remaining cords
		// to match the removed tiles on the real board. Do the opposite
		// for the second tile.
		if(tilesForRemoval.contains(afterMoveTiles[0]) && 
		   !tilesForRemoval.contains(afterMoveTiles[1])) 
		{		
			tilesForRemoval.add(afterMoveTiles[1]);
			tilesForRemoval.remove(afterMoveTiles[0]);
		} 
		else if (tilesForRemoval.contains(afterMoveTiles[1]) &&
				 !tilesForRemoval.contains(afterMoveTiles[0])) 
		{
			tilesForRemoval.add(afterMoveTiles[0]);
			tilesForRemoval.remove(afterMoveTiles[1]);
		}
		
		// Match the tiles to the ones on the real board.
		for (Tile t : tilesForRemoval) {
			removedOnActualBoard.add(board.giveTileAt(t.getX(), t.getY()));
		}
		
		return removedOnActualBoard;
	}
	
	/**
	 * Recursively count the number of candies removed from the board
	 * by chained moves.
	 * 
	 * Normally, countChainedCandiesRemoved method should be called with
	 * the state of the board just after the initial removal of candies 
	 * caused by the actual player's move. 
	 * 
	 * @param currentBoard A board object representing the state just after
	 * 					   the actual player's move has been done, meaning
	 * 					   that no candies based on chained moves should have
	 * 					   been removed.
	 * @return The overall number of candies removed by chained moves. Ranges
	 * 		   from 0 to "currentBoard.getCols() * currentBoard.getRows() - 
	 * 		   candiesRemovedByInitialMove". 
	 */
	public int countChainedCandiesRemoved(Board board) 
	{
		// Find the tiles that can be removed from the board, i.e. they crush. 
	    Set<Tile> tilesForRemoval = BoardUtils.findAllNPles(board);
		 
		board = CrushUtilities.boardAfterDeletingNples(board, board.getPRows());
		
		// If at least a 3-in-a-row existed, there is a possibility that new
		// chained moves have become available. Upon no removal, no chained
		// moves are possible since board hasn't changed at all.
		if (tilesForRemoval.size() > 2) {
			return tilesForRemoval.size() + countChainedCandiesRemoved(board);
		} else {
			return 0;
		}
	}
	
// ==== Unused Code ====
//	
//	/**
//	 * Finds a move on the given board that is purely based on holes, meaning
//	 * that it will return a move which causes two holes to switch.
//	 * 
//	 * @param currentBoard The board which will be searched for a fake move.
//	 * @param holes A map that uses as keys the x position, i.e. the column
//	 * 				index. As values it takes the number of holes, i.e. the
//	 * 				number of newly introduced and of unknown color candies 
//	 * 				to the top of every column.
//	 * @return A move based on tiles defined as holes.
//	 */
//	public PlayerMove findAFakeMove(Board currentBoard, Map<Integer, Integer> holes) {
//		PlayerMove move = new PlayerMove();
//		
//		for(Integer i : holes.keySet()) {
//			// Search for a horizontal move
//			if (holes.containsKey(i + 1)) {
//				move.setTiles(currentBoard.giveTileAt(i, 0), currentBoard.giveTileAt(i + 1, 0));
//				break;
//			}
//			
//			// Search for a vertical move
//			if (holes.get(i) >= 2) {
//				move.setTiles(currentBoard.giveTileAt(i, 0), currentBoard.giveTileAt(i, 1));
//				break;
//			}
//		}
//		
//		return move;
//	}
//	
//	
//	/*
//	 * Scan the defined line of candies, for candies that participate in
//	 * 3-or-more-in-the-row, but ignoring candies specified as holes. 
//	 * 
//	 * When vertical is set to false, a line of candies is considered a
//	 * row of the board. In this case, line parameter defines the index
//	 * of the row, i.e. the y cord of every row.
//	 * 
//	 * When vertical is set to true, a line of candies is considered a
//	 * column of the board. In this case, line parameter defines the index
//	 * of the column, i.e. the x cord of every column.
//	 * 
//	 * @param board The board the line of candies is taken from.
//	 * @param line The index of the line. Row index when vertical set to false,
//	 * 			   column index when vertical set to true.
//	 * @param vertical Defines whether the line of candies is a row or a column
//	 * 				   of the given board. For row set to false, for column set
//	 * 				   to true.
//	 * @param holes A map that uses as keys the x position, i.e. the column
//	 * 				index. As values it takes the number of holes, i.e. the
//	 * 				number of newly introduced and of unknown color candies 
//	 * 				to the top of every column.
//	 * @return : A set containing the tiles of the line 
//	 
//	private Set<Tile> findOrientedInLineCrushCandiesWithHoles(
//			Board board, int line, boolean vertical, Map<Integer, Integer> holes)
//	{
//		Set<Tile> crushCandies = new HashSet<>();
//						
//		if (vertical) {}
//		else { xIncr = 1; }
//	}
//	*/
//
//	
//	/**
//	 * Recursively count the number of candies removed from the board
//	 * by chained moves.
//	 * 
//	 * Normally, countChainedCandiesRemoved method should be called with
//	 * the state of the board just after the initial removal of candies 
//	 * caused by the actual player's move. 
//	 * 
//	 * Also the tiles the player's move caused to be removed from the board
//	 * should be provided as a set, in order to calculate the holes they
//	 * left to the top of every column. As a hole is considered a tile
//	 * which fills the board after another one has crushed and thus it's
//	 * color cannot be known beforehand.
//	 * 
//	 * Previous holes when calling this method just after the player's move
//	 * and with the board's state being the one just after the above move
//	 * (thus no chained moves evaluated), can be either null or just an empty
//	 * Map<Integer, Integer>. Specifying an empty map is useful when an
//	 * implementation other than the default HashMap should be used.
//	 *  
//	 * @param currentBoard A board object representing the state just after
//	 * 					   the actual player's move has been done, meaning
//	 * 					   that no candies based on chained moves should have
//	 * 					   been removed.
//	 * @param removed The tiles of the board, the actual player's move caused
//	 * 				  to be removed immediately. Candies by chained moves are
//	 * 				  excluded.
//	 * @param previousHoles A map containing any unknown tiles at the top of
//	 * 						columns. The index of the column, i.e. its x value,
//	 * 						is used as a key to the map. The value matched to
//	 * 						the key is the number of unknown color tiles to
//	 * 						the top of the corresponding column. It contains
//	 * 						only holes created before the move which caused
//	 * 						tiles in removed set to crush.
//	 * @return The overall number of candies removed by chained moves. Ranges
//	 * 		   from 0 to currentBoard.getCols() * currentBoard.getRows() - removed.size(). 
//	 */
//	public int countChainedCandiesRemoved(Board currentBoard, Set<Tile> removed, 
//										   Map<Integer, Integer> previousHoles) 
//	{		
//		// If not at least 3 previously removed, i.e. not even a single crush, 
//		// no chain moves can exist.
//		if (removed.size() < 3) return 0;
//				
//		// In first run it can be run with no holes and a new map will be created.
//		if (previousHoles == null) previousHoles = new HashMap<>();  
//		
//		// Integrate the given removed tiles to previousHoles.
//		updateHoles(removed, previousHoles);
//		
//		// Find the tiles that can be removed from the board, i.e. they crush. 
//	    Set<Tile> tilesForRemoval = findCandiesThatCrushWithHoles(currentBoard, previousHoles);
//		 
//		currentBoard = CrushUtilities.boardAfterDeletingNples(currentBoard, currentBoard.getPRows());
//		
//		return tilesForRemoval.size() + countChainedCandiesRemoved(
//											currentBoard, tilesForRemoval, previousHoles);
//	}
//		
//	/**
//	 * Updates the holes map given, by incrementing the holes number on
//	 * every column for every removed tile on this column.
//	 * 
//	 * @param removed Tiles removed from the board for which the number of
//	 * 				  holes left back should be counted. 
//	 * @param holes A map that uses as keys the x position, i.e. the column
//	 * 				index. As values it takes the number of holes, i.e. the
//	 * 				number of newly introduced and of unknown color candies 
//	 * 				to the top of every column.
//	 */
//	public void updateHoles(Set<Tile> removed, Map<Integer, Integer> holes) {
//		for (Tile t : removed) {
//			Integer x = t.getX();
//			
//			if (holes.containsKey(x)) {
//				holes.put(x, holes.get(x) + 1);
//			} else {
//				holes.put(x, 1);
//			}
//		}
//	}
//	
//	/**
//	 * Search a single column of a board object for 3-or-more-in-a-row same
//	 * color tiles, excluding the ones defined as holes by the holes map.
//	 * 
//	 * Tiles contained in the hole area of the column defined by holes map,
//	 * are considered as unknown tiles and therefore doesn't match the color
//	 * of any other tile.
//	 * 
//	 * @param board The board the column of candies is taken from.
//	 * @param column The index of the column to search, i.e. the x value of
//	 * 				 this column.
//	 * @param holes A map that uses as keys the x position, i.e. the column
//	 * 				index. As values it takes the number of holes, i.e. the
//	 * 				number of newly introduced and of unknown color candies 
//	 * 				to the top of every column.
//	 * @return A set of tiles containing all the tiles in this column that
//	 *         crush.
//	 */
//	public Set<Tile> findVerticalCrushCandiesWithHoles(Board board, int column, 
//														Map<Integer, Integer> holes)
//	{
//		Set<Tile> crushCandies = new HashSet<>();
//		
//		int max_y;
//		
//		// On vertical search, max vertical index is the one before holes.
//		if (holes.containsKey(column)) {
//			max_y = board.getPRows() - 1 - holes.get(column);
//		} else {
//			max_y = board.getPRows() - 1;  // No holes means go till the end.
//		}
//		
//		Tile previous = null;  // The previous tile at every loop.
//		Set<Tile> tempTiles = new HashSet<>();  // Temporarily keep the equal 
//												// asserted row here.
//		
//		for(int y = 0; y <= max_y; y++) {
//			Tile current = board.giveTileAt(column, y);
//			
//			if (previous != null) { // null means current is the first tile.
//				
//				// If current tile has different color than the previous one,
//				// then clear the temp array of same color tiles, but first
//				// save the ones found if they are at least 3.
//				if (current.getColor() != previous.getColor()) {
//					if (tempTiles.size() > 2) crushCandies.addAll(tempTiles);
//					tempTiles.clear();
//				}	
//			}
//			
//			tempTiles.add(current);
//			previous = current;
//			
//			// On last y, tiles won't be saved automatically because
//			// saving is done on the next loop. So save them manually.
//			if ((y == max_y) && tempTiles.size() > 2) {
//				crushCandies.addAll(tempTiles);
//			}
//		}
//		
//		return crushCandies;
//	}
//	
//	/**
//	 * Search a single row of a board object for 3-or-more-in-a-row same
//	 * color tiles, excluding the ones defined as holes by the holes map.
//	 * 
//	 * Tiles contained in the hole area of the column defined by holes map,
//	 * are considered as unknown tiles and therefore doesn't match the color
//	 * of any other tile.
//	 * 
//	 * @param board The board the row of candies is taken from.
//	 * @param row The index of the row to search, i.e. the y value of
//	 * 			  this row.
//	 * @param holes A map that uses as keys the x position, i.e. the column
//	 * 				index. As values it takes the number of holes, i.e. the
//	 * 				number of newly introduced and of unknown color candies 
//	 * 				to the top of every column.
//	 * @return A set of tiles containing all the tiles in this row that crush.
//	 */
//	public Set<Tile> findHorizontalCrushCandiesWithHoles(Board board, int row,
//														  Map<Integer, Integer> holes)
//	{
//		Set<Tile> crushCandies = new HashSet<>();
//		
//		Set<Tile> tempTiles = new HashSet<>();
//		Tile previous = null;
//		
//		for(int x = 0; x < board.getCols(); x++) {
//			Tile current = board.giveTileAt(x, row);
//			
//			// Get the number of holes at the top of current column.
//			int currentHoles = 0;
//			if (holes.containsKey(x)) currentHoles = holes.get(x);
//			
//			// If the color of the current tile doesn't match the one of the previous
//			// tile or current tile belongs to the holes area of the column, then
//			// clear the temp tiles, but firstly save these tiles if they form at least
//			// a three row.
//			if(previous != null && 
//			   ((current.getColor() != previous.getColor()) || 
//			    row > board.getPRows() - currentHoles - 1)) 
//			{
//				if (tempTiles.size() > 2) crushCandies.addAll(tempTiles);
//				tempTiles.clear();
//			}
//			
//			// Though the set has been cleared, if current tile marked as
//			// a hole is added to tempTiles, it will not be checked again
//			// and will be considered a valid tile.
//			if (row <= board.getPRows() - currentHoles - 1) tempTiles.add(current);
//			
//			previous = current;
//			
//			// On last x, it won't loop again so tiles to be saved automatically.
//			// Save them manually here.
//			if ((x == board.getCols() - 1) && tempTiles.size() > 2) {
//				crushCandies.addAll(tempTiles);
//			}
//		}
//		
//		return crushCandies;
//	}
//	
//	/**
//	 * Scan board for existing 3-or-more-in-a-row same color candies
//	 * horizontally or vertically, but ignoring the top N candies of
//	 * every column, if N is defined in holes for that column.
//	 * 
//	 * @param board The board object to be searched for adjacent same
//	 * 				color candies.
//	 * @param holes A map that uses as keys the x position, i.e. the column
//	 * 				index. As values it takes the number of holes, i.e. the
//	 * 				number of newly introduced and of unknown color candies 
//	 * 				to the top of every column.
//	 * @return A set containing the tiles of the board that participate in
//	 * 		   a 3-or-more-in-a-row. 
//	 */
//	public Set<Tile> findCandiesThatCrushWithHoles(Board board, Map<Integer, Integer> holes) {
//		Set<Tile> crushTiles = new HashSet<>();
//				
//		// Search for matching tiles vertically, i.e. every column.
//		for (int x = 0; x < board.getCols(); x++) {
//			crushTiles.addAll(findVerticalCrushCandiesWithHoles(board, x, holes));
//		}
//		
//		// Search for matching tiles horizontally, i.e. every row.
//		for (int y = 0; y < board.getPRows(); y++) {
//			crushTiles.addAll(findHorizontalCrushCandiesWithHoles(board, y, holes));
//		}
//		
//		return crushTiles;
//	}	
}
