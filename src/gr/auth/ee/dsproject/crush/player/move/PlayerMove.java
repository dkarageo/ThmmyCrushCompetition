package gr.auth.ee.dsproject.crush.player.move;

import gr.auth.ee.dsproject.crush.board.CrushUtilities;
import gr.auth.ee.dsproject.crush.board.Tile;


/**
 * PlayerMove class provides a way to elegantly pack a player's move. I.e.
 * the two tiles to switch positions.
 * 
 * PlayerMove won't allow null references to be provided for tiles. In case
 * of null references in either constructors or methods that set tiles, a
 * NullTileRuntimeException will be thrown.
 * 
 * On the solely case that no argument constructor is used and user
 * attempts to retrieve move's values, PlayerMove object is considered
 * non-ready and an EmptyMoveRuntimeException is thrown.  
 *   
 * Public constructors defined in PlayerMove:
 *  -public PlayerMove()
 *  -public PlayerMove(Tile[] tiles) throws NullTileRuntimeException, InvalidMoveRuntimeException
 *  -public PlayerMove(Tile tile1, Tile tile2) throws NullTileRuntimeException
 *   
 * Public methods defined in PlayerMove
 *  -public void setTiles(Tile[] tiles) throws NullTileRuntimeException
 *  -public void setTiles(Tile tile1, Tile tile2) throws NullTileRuntimeException, InvalidMoveRuntimeException
 *  -public Tile[] getTiles() throws EmptyMoveRuntimeException
 *  -public int getX1() throws EmptyMoveRuntimeException
 *  -public int getX2() throws EmptyMoveRuntimeException
 *  -public int getY1() throws EmptyMoveRuntimeException
 *  -public int getY2() throws EmptyMoveRuntimeException
 *  -public int[] toCordsArray() throws EmptyMoveRuntimeException
 *  -public int[] toDirArray() throws SameTilesRuntimeException, InvalidDirectionRuntimeException
 *  
 * @author Dimitrios Karageorgiou
 * @version 1.1
 * 
 */
public class PlayerMove {
	
	// Instance Variables
	
	/**
	 * An array containing the two tiles to switch positions.
	 */
	private Tile[] tiles;
	
	
	// Public Constructors
	
	/**
	 * Construct a new PlayerMove.
	 * 
	 * A new PlayerMove object constructed with no argument constructor
	 * should be filled with valid Tiles before used.
	 */
	public PlayerMove() {
		tiles = new Tile[2];
	}
	
	/**
	 * Construct a new PlayerMove by providing an array of exactly two
	 * Tiles in the form of [Tile1, Tile2].
	 * 
	 * Null array reference or null tile references inside array will
	 * cause a NullTileRuntimeException to be thrown. 
	 * 
	 * Providing an array without exactly two objects will cause an
	 * InvalidMoveRuntimeException. 
	 * 
	 * @param tiles An array of Tile in the form of [Tile1, Tile2]
	 * @throws NullTileRuntimeException 
	 * @throws InvalidMoveRuntimeException
	 */
	public PlayerMove(Tile[] tiles) 
	    throws NullTileRuntimeException,
		       InvalidMoveRuntimeException
	{
		if (tiles == null) throw new NullTileRuntimeException();
		if (tiles.length != 2 ) throw new InvalidMoveRuntimeException();
		if (tiles[0] == null || tiles[1] == null) throw new NullTileRuntimeException();
		
		this.tiles = tiles;
	}
	
	/**
	 * Construct a new PlayerMove object by providing the two tiles to be
	 * switched when the move takes place.
	 * 
	 * Null tile references will cause a NullTileRuntimeException to be thrown.
	 * 
	 * @param tile1 The first Tile of a move.
	 * @param tile2 The second Tile of a move.
	 * @throws NullTileRuntimeException
	 */
	public PlayerMove(Tile tile1, Tile tile2) throws NullTileRuntimeException {
		if (tile1 == null || tile2 == null) throw new NullTileRuntimeException(); 
		
		tiles = new Tile[2];
		tiles[0] = tile1;
		tiles[1] = tile2;
	}
	
	
	// Public Setters
	
	/**
	 * Set move's tiles to the given Tile array. Tile array should contain
	 * exactly two Tile references in the form of [Tile1, Tile2].
	 * 
	 * Null array reference or null tile references inside array will
	 * cause a NullTileRuntimeException to be thrown. 
	 * 
	 * Providing an array with more than two objects will cause an
	 * InvalidMoveRuntimeException. 
	 * 
	 * @param tiles An array of Tile in the form of [Tile1, Tile2]
	 * @throws NullTileRuntimeException
	 * @throws InvalidMoveRuntimeException
	 */
	public void setTiles(Tile[] tiles) 
		throws NullTileRuntimeException,
		       InvalidMoveRuntimeException
	{
		if (tiles == null) throw new NullTileRuntimeException();
		if (tiles.length != 2 ) throw new InvalidMoveRuntimeException();
		if (tiles[0] == null || tiles[1] == null) throw new NullTileRuntimeException();
		this.tiles = tiles;
	}
	
	/**
	 * Set move's tiles to the given two Tile objects.
	 * 
	 * Null Tile references will cause a NullTileRuntimeException to
	 * be thrown.
	 * 
	 * @param tile1 First tile of the move.
	 * @param tile2 Second tile of the move.
	 * @throws NullTileRuntimeException
	 */
	public void setTiles(Tile tile1, Tile tile2) throws NullTileRuntimeException {
		if (tile1 == null || tile2 == null) throw new NullTileRuntimeException();
		tiles[0] = tile1;
		tiles[1] = tile2;
	}
	
	
	// Public Getters
	
	/**
	 * Returns an array containing the two Tile objects a move consists of.
	 * 
	 * If move never got valid Tiles, e.g for a move created with no
	 * argument constructor and proper Tile objects never given,
	 * an EmptyMoveRuntimeException is thrown.
	 * 
	 * @return Tile array containing Tile objects of the move.
	 * @throws EmptyMoveRuntimeException
	 */
	public Tile[] getTiles() throws EmptyMoveRuntimeException { 
		if (tiles[0] == null || tiles[1] == null) throw new EmptyMoveRuntimeException();
		
		return tiles; 
	}
	
	
	// Public Methods
	
	/**
	 * A shortcut to get x position of first tile. 
	 * 
	 * In case first tile has not been provided to PlayerMove object, 
	 * an EmptyMoveRuntimeException is thrown.
	 * 
	 * @return x position of first tile.
	 * @throws EmptyMoveRuntimeException
	 */
	public int getX1() throws EmptyMoveRuntimeException { 
		if (tiles[0] == null) throw new EmptyMoveRuntimeException();
		return tiles[0].getX(); 
	}
	
	/**
	 * A shortcut to get y position of first tile. 
	 * 
	 * In case first tile has not been provided to PlayerMove object, 
	 * an EmptyMoveRuntimeException is thrown.
	 * 
	 * @return y position of first tile.
	 * @throws EmptyMoveRuntimeException
	 */
	public int getY1() throws EmptyMoveRuntimeException { 
		if (tiles[0] == null) throw new EmptyMoveRuntimeException();
		return tiles[0].getY(); 
	}
	
	/**
	 * A shortcut to get x position of second tile. 
	 * 
	 * In case second tile has not been provided to PlayerMove object, 
	 * an EmptyMoveRuntimeException is thrown.
	 * 
	 * @return x position of second tile.
	 * @throws EmptyMoveRuntimeException
	 */
	public int getX2() throws EmptyMoveRuntimeException { 
		if (tiles[1] == null) throw new EmptyMoveRuntimeException();
		return tiles[1].getX(); 
	}
	
	/**
	 * A shortcut to get y position of second tile. 
	 * 
	 * In case second tile has not been provided to PlayerMove object, 
	 * an EmptyMoveRuntimeException is thrown.
	 * 
	 * @return y position of second tile.
	 * @throws EmptyMoveRuntimeException
	 */
	public int getY2() throws EmptyMoveRuntimeException { 
		if (tiles[1] == null) throw new EmptyMoveRuntimeException();
		return tiles[1].getY(); 
	}
	
	/**
	 * A shortcut to get cords of tiles to switch positions in the
	 * form of [x1, y1, x2, y2] int array.
	 * 
	 * In case PlayerMove has not been initialized properly and
	 * provided both tiles that a move requires, an
	 * EmptyMoveRuntimeException is thrown.
	 * 
	 * @return Tile cords in [x1, y1, x2, y2] int array format.
	 * @throws EmptyMoveRuntimeException
	 */
	public int[] toCordsArray() throws EmptyMoveRuntimeException {
		if (tiles == null || tiles[0] == null || tiles[1] == null) {
			throw new EmptyMoveRuntimeException();
		}
		
		int[] arrayMove = { tiles[0].getX(), tiles[0].getY(),
							tiles[1].getX(), tiles[1].getY() };
		
		return arrayMove;
	}
	
	/**
	 * A shortcut to get the move in the form of [x1, y1, direction] int
	 * array. 
	 * 
	 * Possible directions are defined by CrushUtilities.
	 * 
	 * In case the move consists of tiles of the same cords, a
	 * SameTileRuntimeException is thrown.
	 * 
	 * In case the move consists of tiles that are not adjacent to a
	 * direction defined by CrushUtilities, InvalidDirectionRuntimeException
	 * is thrown.
	 * 
	 * @return The move in the form of [x1, y1, direction] int array.
	 * @throws SameTilesRuntimeException
	 * @throws InvalidDirectionRuntimeException
	 */
	public int[] toDirArray() 
			throws SameTilesRuntimeException, InvalidDirectionRuntimeException 
	{
		int direction;
		
		int x1 = getX1();
		int x2 = getX2();
		int y1 = getY1();
		int y2 = getY2();
		
		if (x1 > x2 && y1 == y2) {
			direction = CrushUtilities.LEFT;
		} else if (x1 < x2 && y1 == y2) {
			direction = CrushUtilities.RIGHT;
		} else if (y1 > y2 && x1 == x2) {
			direction = CrushUtilities.DOWN;
		} else if (y1 < y2 && x1 == x2) {
			direction = CrushUtilities.UP;
		} else if (x1 == x2 && y1 == y2) {
			throw new SameTilesRuntimeException();
		} else {
			throw new InvalidDirectionRuntimeException();
		}
		
		int[] move = { x1, y1, direction };
		
		return move;
	}
	
	
// Exceptions defined in PlayerMove
	
	/**
	 * An exception that may be raised when someone tries to pass
	 * a null tile reference to a PlayerMove object. 
	 */
	public static class NullTileRuntimeException extends RuntimeException {
		static final long serialVersionUID = 1;
	}
	
	/**
	 * An exception that may be raised when someone tries to use
	 * a PlayerMove object whose two tiles slot are not filled
	 * with valid Tile objects.
	 */
	public static class EmptyMoveRuntimeException extends RuntimeException {
		static final long serialVersionUID = 1;
	}
	
	/**
	 * An exception that may be raised when someone tries to pass,
	 * an other than null, invalid move to PlayerMove object.
	 */
	public static class InvalidMoveRuntimeException extends RuntimeException {
		static final long serialVersionUID = 1;
	}
	
	/**
	 * An exception that may be thrown when move is found to contain
	 * two tiles with the same cords.
	 */
	public static class SameTilesRuntimeException extends RuntimeException {
		static final long serialVersionUID = 1;
	}
	
	/**
	 * An exception that may be thrown when a move is consisted of tiles
	 * not adjacent in directions defined in CrushUtilities.
	 */
	public static class InvalidDirectionRuntimeException extends RuntimeException {
		static final long serialVersionUID = 1;
	}
}
