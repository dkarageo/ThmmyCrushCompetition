package gr.auth.ee.dsproject.crush.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Comparator;

import gr.auth.ee.dsproject.crush.board.CrushUtilities;
import gr.auth.ee.dsproject.crush.board.Board;
import gr.auth.ee.dsproject.crush.board.Tile;


/**
 * BoardUtils contains utilities methods useful when working with a board.
 * 
 * Static public methods defined in BoardUtils:
 * 
 * -public Set<Tile> findAdjacentSameColorTiles(Board board, Tile currentTile, int dirMax)
 *		throws NullBoardRuntimeException, NullTileRuntimeException
 * -public static Set<Tile> findAllNPles(Board board) 
			throws NullBoardRuntimeException
 * -public Set<Tile> findInDirectionSameColorTiles(Board board, Tile currentTile, int direction, int max)
 *		throws NullBoardRuntimeException, NullTileRuntimeException, InvalidDirectionRuntimeException
 * -public Set<Tile> findTilesThatCrush(Set<Tile> possibleAdjacentTiles)
 * 		throws NullBoardRuntimeException
 * -public static boolean isValidCords(Board board, int x, int y)
 * 		throws NullBoardRuntimeException
 * -public static boolean isValidCords(int x, int y)
 * 
 * Comparators defined in BoardUtils:
 * -public class TileFirstByYThenByX implements Comparator<Tile> 
 *  
 * Exceptions defined in BoardUtils:
 * -public static class NullBoardRuntimeException extends RuntimeException
 * -public static class NullTileRuntimeException extends RuntimeException
 * -public static class InvalidDirectionsRuntimeException extends RuntimeException
 * -public static class NullMoveRuntimeException extends RuntimeException
 * 
 * @author Dimitrios Karageorgiou
 * @version 0.3
 */
public class BoardUtils {
	
// ==== Private Constants ====
	
	/**
	 * An array to pack all four direction constants defined in CrushUtilities
	 * for iteration or other validation purposes.
	 */
	private static final int[] DIRECTIONS = { CrushUtilities.UP, CrushUtilities.DOWN,
			  								  CrushUtilities.LEFT, CrushUtilities.RIGHT };
	
	
// ==== Public Methods ====
	
	/**
	 * Searches the given board for tiles adjacent to the one given one, 
	 * whose color match the color of the given tile.
	 * 
	 * Searching is done in adjacent tiles to all four directions.
	 * 
	 * Max number of matching tiles a direction should be searched for,
	 * is defined by dirMax.
	 * 
	 * In case a null reference for board or currentTile is passed,
	 * NullBoardRuntimeException or NullTileRuntimeException is
	 * thrown accordingly.
	 * 
	 * @param board The board to be searched for same color tiles.
	 * @param currentTile The tile to be used as the central one.
	 * @param dirMax The max number of matching tiles a direction to be
	 * 				 searched for.
	 * @return Set of matching tiles in all four directions, excluding the
	 * 		   initial one.
	 * @throws NullBoardRuntimeException Thrown when a null board reference 
	 * 									 is passed.
	 * @throws NullTileRuntimeException Thrown when a null currentTile reference 
	 * 									is passed.
	 */
	public static Set<Tile> findAdjacentSameColorTiles(Board board, Tile currentTile, int dirMax)
		throws NullBoardRuntimeException, NullTileRuntimeException 
	{
		if (board == null) throw new NullBoardRuntimeException();
		if (currentTile == null) throw new NullTileRuntimeException();
		
		Set<Tile> sameColorTiles = new HashSet<>();
		
		// Add same color tiles found in four directions to tiles set. Searching in 
		for (int dir : DIRECTIONS) {
			sameColorTiles.addAll(findInDirectionSameColorTiles(board, currentTile, dir, dirMax));
		}
		
		return sameColorTiles;
	}
		
	/**
	 * Scan board for existing 3-or-more-in-a-row same color candies
	 * horizontally or vertically. Tiles that have a color value of -1,
	 * i.e. they are marked as unknown tiles, are excluded.
	 * 
	 * If given board is a null reference, NullBoardRuntimeException is
	 * thrown.
	 * 
	 * @param board The board object to be searched for adjacent same
	 * 				color candies.
	 * @return A set containing the tiles of the board that participate in
	 * 		   a 3-or-more-in-a-row.
	 * @throws NullBoardRuntimeException 
	 */
	public static Set<Tile> findAllNPles(Board board) 
			throws NullBoardRuntimeException 
	{
		if (board == null) throw new NullBoardRuntimeException();
		
		Set<Tile> crushTiles = new HashSet<>();
		
		for (int y = 0; y < board.getPRows(); y++) {
			for (int x = 0; x < board.getCols(); x++) {				
				Tile t = board.giveTileAt(x, y);
				
				if (t.getColor() != -1) {
					Set<Tile> adjacentTiles = BoardUtils.findAdjacentSameColorTiles(
							board, t, Integer.MAX_VALUE
					);
					adjacentTiles.add(t);
					
					crushTiles.addAll(BoardUtils.findTilesThatCrush(adjacentTiles));
				}
			}
		}
		
		return crushTiles;
	}
	
	/**
	 * Looks for adjacent tiles of same color in the given direction. 
	 * 
	 * It stops searching in one of the following conditions: 
	 * -max tiles are found,
	 * -boundaries of board have been reached,
	 * -a tile of non-matching color to the given one is found.
	 * 
	 * In case board or currentTile is a null reference, NullBoardRuntimeException
	 * or NullTileRuntimeException are thrown accordingly. If direction is not
	 * a valid direction as defined in CrushUtilities, an
	 * InvalidDirectionRuntimeException is thrown.
	 * 
	 * @param board The board to be searched for same color tiles.
	 * @param currentTile The tile to be used as the starting one.
	 * @param direction The direction which will be searched for matching tiles. 
	 * @param max The maximum number of matching tiles to be searched for. 0 or 
	 * 			  negative values will result in no search to happen. 
	 * @return Matching tiles found returned as a set of Tile references. Given
	 * 		   tile is excluded from the returned set.
	 * @throws NullBoardRuntimeException Thrown when a null board reference is passed.
	 * @throws NullTileRuntimeException Thrown when a null tile reference is passed.
	 * @throws InvalidDirectionsRuntimeException Thrown when a direction not specified
	 * 							    			 in CrushUtilities is passed.
	 */
	public static Set<Tile> findInDirectionSameColorTiles(Board board, Tile currentTile, 
														  int direction, int max)
		throws NullBoardRuntimeException, NullTileRuntimeException , 
			   InvalidDirectionsRuntimeException
	{
		if (board == null) throw new NullBoardRuntimeException();
		if (currentTile == null) throw new NullTileRuntimeException();
				
		Set<Tile> sameColorTiles = new HashSet<>();
		
		// Create the current cord increment values according to the direction
		// given.
		int xIncr = 0;
		int yIncr = 0;
		
		switch(direction) {
		case CrushUtilities.UP:
			yIncr = 1;
			break;
			
		case CrushUtilities.DOWN:
			yIncr = -1;
			break;
		
		case CrushUtilities.LEFT:
			xIncr = -1;
			break;
		
		case CrushUtilities.RIGHT:
			xIncr = 1;
			break;
			
		default:
			throw new InvalidDirectionsRuntimeException();
		}
		
		Tile nextTile;
		
		// Search at most max tiles.
		for (int i = 0; i < max; i++) {
			int nextX = currentTile.getX() + xIncr;
			int nextY = currentTile.getY() + yIncr;
			
			if (!isValidCords(board, nextX, nextY)) break; // board's boundaries have been met
			
			nextTile = board.giveTileAt(nextX, nextY);
			
			// If a matching tile is found add it to the set, 
			// else quit searching.
			if (currentTile.getColor() == nextTile.getColor()) {
				sameColorTiles.add(nextTile);
			} else {
				break;
			}
			
			currentTile = nextTile;
		}
		
		return sameColorTiles;
	}
	
	/**
	 * Find all the tiles that actually crush, given a set of the 
	 * adjacent same color tiles to the move's ones.
	 * 
	 * A tile crush, means this tile forms an at least 3-in-the-row
	 * horizontally or vertically. Tiles that got a color value of -1,
	 * are considered as tiles of unknown color, and are automatically
	 * excluded from tiles that crush.
	 * 
	 * The tile set given should only contain the same color tiles
	 * adjacent to the two tiles a move consists of. That means
	 * the set should contain at most two different color tiles.
	 * If tiles that don't fit these criteria are given, the
	 * return set may contain more tiles than the ones which
	 * actually crush. 
	 * 
	 * If a null argument is provided for possibleAdjacentTiles parameter,
	 * a NullTileRuntimeException is thrown. 
	 *  
	 * @param possibleAdjacentTiles A set of adjacent to the move's ones,
	 * 		  						same color tiles.
	 * @return A set of tiles that actually crush.
	 * @throws NullTileRuntimeException
	 */
	public static Set<Tile> findTilesThatCrush(Set<Tile> possibleAdjacentTiles)
		throws NullTileRuntimeException
	{
		if (possibleAdjacentTiles == null) throw new NullTileRuntimeException();
		
		// NO TIME AND BORED = VERY BAD CODE! SHOULD BE SOMETIME FIXED!
		
		Set<Tile> crushTiles = new HashSet<>();
		
		// Firstly, color value of tiles is used as key for outer map, thus categorized by color.
		// Secondly, an ArrayList of maps is used as a value. This arrayList categorizes the
		// tiles whether they form an at least 3-in-the-row horizontally or vertically.
		// Thirdly, the maps inside the above array, uses sameX or sameY value, thus
		// categorizes the tiles by the row/column in which they are.
		// Finally, all these tiles all contained inside ArrayList objects.
		// A tile may contained in more than one ArrayLists if it is used both in a
		// vertical and in a horizontal row. Though final set container will
		// take care of these duplicate entries.
		Map<Integer, ArrayList<Map<Integer, ArrayList<Tile>>>> categorizedTiles = new HashMap<>();		
		
		for(Tile t : possibleAdjacentTiles) {
			// Firstly get all the tiles of the same color.
			ArrayList<Map<Integer, ArrayList<Tile>>> byColor;
			
			if (categorizedTiles.containsKey(t.getColor())) {
				byColor = categorizedTiles.get(t.getColor());
			} else {
				byColor = new ArrayList<>();
				byColor.add(null);
				byColor.add(null);
				categorizedTiles.put(t.getColor(), byColor);
			}
			
			// Get all same color tiles that crush vertically (same Xs).
			Map<Integer, ArrayList<Tile>> byVerticalCrush;
			
			if (byColor.get(0) != null) {
				byVerticalCrush = byColor.get(0);
			} else {
				byVerticalCrush = new HashMap<>();
				byColor.set(0, byVerticalCrush);
			}
			
			// Get all same color tiles that crush horizontally (same Ys).
			Map<Integer, ArrayList<Tile>> byHorizontalCrush;
			
			if (byColor.get(1) != null) {
				byHorizontalCrush = byColor.get(1);
			} else {
				byHorizontalCrush = new HashMap<>();
				byColor.set(1, byHorizontalCrush);
			}
			
			// Get all tiles of same X.
			ArrayList<Tile> byX;
			
			if (byVerticalCrush.containsKey(t.getX())) {
				byX = byVerticalCrush.get(t.getX());
			} else {
				byX = new ArrayList<>();
				byVerticalCrush.put(t.getX(), byX);
			}
			
			// Get all tiles of same Y.
			ArrayList<Tile> byY;
			
			if (byHorizontalCrush.containsKey(t.getY())) {
				byY = byHorizontalCrush.get(t.getY());
			} else {
				byY = new ArrayList<>();
				byHorizontalCrush.put(t.getY(), byY);
			}
			
			// Finally, put every tile in its right group of X and Y.
			byX.add(t);
			byY.add(t);
		}
		
		// Remove tiles of unknown color.
		if (categorizedTiles.containsKey(-1)) categorizedTiles.remove(-1);
		
		// Finally iterate over the categories created before and if
		// a category contains at least 3 tiles, then these tiles crush.
		// So, add these tiles to crushTiles set.
		for(Map.Entry<Integer, ArrayList<Map<Integer, ArrayList<Tile>>>> byColor : categorizedTiles.entrySet()) {			
			for(Map<Integer, ArrayList<Tile>> byCrushAxis : byColor.getValue()) {
				for(Map.Entry<Integer, ArrayList<Tile>> byAxisValue : byCrushAxis.entrySet()) {
					if(byAxisValue.getValue().size() > 2) {
						crushTiles.addAll(byAxisValue.getValue());
					}
				}
			}
		}
		
		return crushTiles;
	}

	/**
	 * Checks whether the given cords are between boundaries of
	 * the board, as returned by board.getCols() and 
	 * board.getPRows().
	 * 
	 * @param board The board the cords will be checked on.
	 * @param x x cord that is valid when is lower than board.getCols()
	 * 			  and non negative.
	 * @param y y cord that is valid when is lower than board.getPRows()
	 * 			  and non negative.
	 * @return True for valid cords, false for invalid.
	 */
	public static boolean isValidCords(Board board, int x, int y) {
		if (board == null) throw new NullBoardRuntimeException();
		
		if (x >= 0 && x < board.getCols() && 
			y >= 0 && y < board.getPRows()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether the given cords are between boundaries as
	 * defined in CrushUtilities.
	 * 
	 * @param x x cord that is valid when is lower than 
	 * 			CrushUtilities.NUMBER_OF_PLAYABLE_COLUMNS
	 * 			and non negative.
	 * @param y y cord that is valid when is lower than 
	 * 			CrushUtilities.NUMBER_OF_ROWS and non negative.
	 * @return True for valid cords, false for invalid.
	 */
	public static boolean isValidCords(int x, int y) {
		if (x >= 0 && x < CrushUtilities.NUMBER_OF_COLUMNS && 
			y >= 0 && y < CrushUtilities.NUMBER_OF_PLAYABLE_ROWS) {
			return true;
		}
		return false;
	}
	
	
// ==== Comparators defined in BoardUtils ====

	/**
	 * Comparator that compares two tile object based on their
	 * y and x cords.
	 * 
	 * It first compares by y and returns:
	 * -1 if y1 < y2
	 *  1 if y1 > y2
	 *  
	 * If y1 == y1, then it compares by x and returns:
	 * -1 if x1 < x2
	 *  1 if x1 > x2
	 *  
	 * If x1 == x2 && y1 == y2 it returns 0.
	 */
	public static class TileFirstByYThenByX implements Comparator<Tile> {
		public int compare(Tile t1, Tile t2) {
			int x1 = t1.getX();
			int x2 = t2.getX();
			int y1 = t1.getY();
			int y2 = t2.getY();
			
			if (y1 > y2) return 1;
			else if (y1 < y2) return -1;
			else {
				if (x1 > x2) return 1;
				else if (x1 < x2) return -1;
				else return 0;
			}
		}
	}
	

// ==== Exceptions defined in BoardUtils ====
	
	/**
	 * An exception to be thrown when a null board reference is encountered
	 * where it normally shouldn't.
	 */
	public static class NullBoardRuntimeException extends RuntimeException {
		private static final long serialVersionUID = 1L;		
	}
	
	/**
	 * An exception to be thrown when a null move reference is encountered
	 * where it normally shouldn't.
	 */
	public static class NullMoveRuntimeException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
	
	/**
	 * An exception to thrown when a null tile reference is encountered where
	 * it normally shouldn't.
	 */
	public static class NullTileRuntimeException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
		
	/**
	 * An exception to be thrown when invalid directions are encountered where
	 * it normally shouldn't.
	 */
	public static class InvalidDirectionsRuntimeException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
	
	
// ==== Legacy Code ====
	
//	/**
//	 * Returns a shallow copy of a Board object. Tile structure is completely
//	 * copied. Though tiles object themselves are NOT copied.
//	 *  
//	 * In case a null board reference is provided, it throws a
//	 * NullBoardRuntimeException.
//	 * 
//	 * @param board Board object to be copied.
//	 * @return A shallow copy of the given board object.
//	 * @throws NullBoardRuntimeException 
//	 */
//	public Board boardCopy(Board board) throws NullBoardRuntimeException {
//		if (board == null) throw new NullBoardRuntimeException();
//		
//		Board newBoard = new Board(board.getCols(), board.getRows(), 
//								   board.getWidth(), board.getHeight());
//		
//		for (int y = 0; y < board.getRows(); ++y) {
//			for (int x = 0; x < board.getCols(); ++x) {
//				newBoard.setTile(board.giveTileAt(x, y));
//			}
//		}
//		
//		return newBoard;
//	}
//	
//	/**
//	 * Return a copy of board given, where given move has been played
//	 * and tiles it caused to crush have been removed and new tiles
//	 * have filled the top of the board. 
//	 * 
//	 * It doesn't only search for tiles next to the ones consisting the
//	 * move, but searches the whole board for 3-or-more-in-a-row same
//	 * color tiles.
//	 * 
//	 * New tiles added to the top, do not get marked in any way so
//	 * if needed to be known how many and on which columns these tiles
//	 * added, it should be calculated externally.
//	 * 
//	 * In case a null board reference is provided, it throws a
//	 * NullBoardRuntimeException.
//	 * 
//	 * In case a null move reference is provided, it throws a
//	 * NullMoveRuntimeException.
//	 *  
//	 * @param board A board object which will be searched for same color
//	 * 				tile rows created after the move has been played. 
//	 * @param move A move to play on the board.
//	 * @return A board object from which every existing row of same color
//	 * 		   tiles after move has been played, is removed.
//	 * @throws NullBoardRuntimeException
//	 * @throws NullMoveRuntimeException
//	 */
//	public Board getBoardAfterMoveAndCrush(Board board, PlayerMove move)
//		throws NullBoardRuntimeException, NullMoveRuntimeException
//	{
//		if (board == null) throw new NullBoardRuntimeException();
//		if (move == null) throw new NullMoveRuntimeException();
//		
//		// Get a copy to work on and leave original one intact.
//		Board copiedBoard = boardCopy(board);
//		doMove(copiedBoard, move);
//		
//		// Tiles to be removed, board wide, sorted first by y, then by x
//		// in ascending order.
//		Set<Tile> removed = new TreeSet<>(new TileFirstByYThenByX());
//		
//		// Find every tile that participates in an uncrushed 
//		// 3-or-more-in-a-row on the board.
//		for (int y = 0; y < copiedBoard.getRows(); y++) {
//			for (int x = 0; x < copiedBoard.getCols(); x++) {
//				Set<Tile> adjacent = findAdjacentSameColorTiles(
//						copiedBoard, copiedBoard.giveTileAt(x, y), 999);
//				adjacent.add(copiedBoard.giveTileAt(x, y));  // current tile is also an adjacent one
//				
//				removed.addAll(findTilesThatCrush(adjacent));
//			}
//		}
//		
//		// For every tile that crushes, move that tile to the top of its
//		// column.
//		for (Tile t : removed) {			
//			for (int y = t.getY(); y > 0; y--) {
//				doMove(copiedBoard, new PlayerMove(
//							copiedBoard.giveTileAt(t.getX(), y), 
//							copiedBoard.giveTileAt(t.getX(), y - 1)
//				));
//			}
//		}
//		
//		return copiedBoard;
//	}
//	
//	/**
//	 * Switches the position of the two tiles defined by a PlayerMove
//	 * object.
//	 * 
//	 * In case a null board reference is provided, it throws a
//	 * NullBoardRuntimeException.
//	 * 
//	 * In case a null move reference is provided, it throws a
//	 * NullMoveRuntimeException.
//	 * 
//	 * @param board The board on which to switch position of tiles
//	 * 				defined by move.
//	 * @param move The move that defines the tiles to be switched.
//	 * @return Board reference it called with.
//	 * @throws NullBoardRuntimeException
//	 * @throws NullMoveRuntimeException
//	 */
//	public Board doMove(Board board, PlayerMove move) 
//			throws NullBoardRuntimeException,
//				   NullMoveRuntimeException
//	{
//		if (board == null) throw new NullBoardRuntimeException();
//		if (move == null) throw new NullMoveRuntimeException();
//		
//		board.moveTile(move.getX1(), move.getY1(), move.getX2(), move.getY2());
//		
//		return board;
//	}
}