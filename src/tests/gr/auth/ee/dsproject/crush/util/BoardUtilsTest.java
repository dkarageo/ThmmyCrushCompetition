package tests.gr.auth.ee.dsproject.crush.util;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

import gr.auth.ee.dsproject.crush.board.Board;
import gr.auth.ee.dsproject.crush.util.BoardUtils;
import gr.auth.ee.dsproject.crush.board.Tile;
import gr.auth.ee.dsproject.crush.board.CrushUtilities;


public class BoardUtilsTest {
	Board noMoveBoard;
	
	private Board createBoard(int[][] boardScheme) {		
		int rows = boardScheme.length;
		int cols = boardScheme[0].length;
		
		Board board = new Board(rows);
		Method setTile = null;
				
		try {
			setTile = board.getClass().getDeclaredMethod(
					"setTile", int.class, int.class, int.class, int.class, boolean.class
			);
			setTile.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				try {
					setTile.invoke(board, y * 10 + x, x, y, boardScheme[rows - y - 1][x], false);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		return board;
	}
	
	@Before
	public void setUp() throws Exception {
		// Create a new 10 x 10 no move board.
		int[][] noMoveBoardScheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		noMoveBoard = createBoard(noMoveBoardScheme);		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFindInDirectionSameColorTilesNullBoardException() {
		boolean ex = false;
		
		try {
			BoardUtils.findInDirectionSameColorTiles(null, noMoveBoard.giveTileAt(0, 0), CrushUtilities.UP, 3);
		} catch (BoardUtils.NullBoardRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testFindInDirectionSameColorTilesNullTileException() {
		boolean ex = false;
		
		try {
			BoardUtils.findInDirectionSameColorTiles(noMoveBoard, null, CrushUtilities.UP, 3);
		} catch (BoardUtils.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testFindInDirectionSameColorTilesInvalidDirectionException() {
		boolean ex = false;
		
		try {
			BoardUtils.findInDirectionSameColorTiles(noMoveBoard, noMoveBoard.giveTileAt(0, 0), 1821, 3);
		} catch (BoardUtils.InvalidDirectionsRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testFindInDirectionSameColorTiles() {
		int[][] scheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 6, 6, 0, 1, 2, 3 },
				{ 2, 3, 6, 6, 6, 6, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 6, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 6, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(scheme);
				
		Set<Tile> toLeft = BoardUtils.findInDirectionSameColorTiles(board, board.giveTileAt(4, 7), CrushUtilities.LEFT, 3);
		assertEquals(2, toLeft.size());
		assertTrue(toLeft.contains(board.giveTileAt(3, 7)));
		assertTrue(toLeft.contains(board.giveTileAt(2, 7)));
		
		Set<Tile> toRight = BoardUtils.findInDirectionSameColorTiles(board, board.giveTileAt(4, 7), CrushUtilities.RIGHT, 3);
		assertEquals(1, toRight.size());
		assertTrue(toRight.contains(board.giveTileAt(5, 7)));
		
		Set<Tile> toDown = BoardUtils.findInDirectionSameColorTiles(board, board.giveTileAt(4, 7), CrushUtilities.UP, 3);
		
		assertEquals(1, toDown.size());
		assertTrue(toDown.contains(board.giveTileAt(4, 8)));
		
		Set<Tile> toUp = BoardUtils.findInDirectionSameColorTiles(board, board.giveTileAt(4, 7), CrushUtilities.DOWN, 3);
		assertEquals(2, toUp.size());
		assertTrue(toUp.contains(board.giveTileAt(4, 6)));
		assertTrue(toUp.contains(board.giveTileAt(4, 5)));
	}
	
	@Test
	public void testFindAdjacentSameColorTilesNullBoardException() {
		boolean ex = false;
		
		try {
			BoardUtils.findAdjacentSameColorTiles(null, noMoveBoard.giveTileAt(0, 0), 10);
		} catch (BoardUtils.NullBoardRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testFindAdjacentSameColorTilesNullTileException() {
		boolean ex = false;
		
		try {
			BoardUtils.findAdjacentSameColorTiles(noMoveBoard, null, 10);
		} catch (BoardUtils.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testFindAdjacentSameColorTiles() {
		int[][] scheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 6, 6, 0, 1, 2, 3 },
				{ 2, 3, 6, 6, 6, 6, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 6, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(scheme);
		
		Set<Tile> tiles = BoardUtils.findAdjacentSameColorTiles(board, board.giveTileAt(4, 7), 5);
		
		assertEquals(5, tiles.size());
		assertTrue(tiles.contains(board.giveTileAt(2, 7)));
		assertTrue(tiles.contains(board.giveTileAt(3, 7)));
		assertTrue(tiles.contains(board.giveTileAt(5, 7)));
		assertTrue(tiles.contains(board.giveTileAt(4, 8)));
		assertTrue(tiles.contains(board.giveTileAt(4, 6)));
	}
	
	@Test
	public void testFindAdjacentSameColorTiles2() {
		int[][] scheme = {
				{ 0, 1, 2, 3, 6, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 6, 6, 0, 1, 2, 3 },
				{ 2, 3, 3, 2, 6, 6, 6, 2, 3, 4 },
				{ 3, 4, 5, 6, 6, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 6, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(scheme);
		
		Set<Tile> tiles = BoardUtils.findAdjacentSameColorTiles(board, board.giveTileAt(4, 7), 5);
		
		assertEquals(6, tiles.size());
		assertTrue(tiles.contains(board.giveTileAt(4, 8)));
		assertTrue(tiles.contains(board.giveTileAt(4, 9)));
		assertTrue(tiles.contains(board.giveTileAt(4, 6)));
		assertTrue(tiles.contains(board.giveTileAt(4, 5)));
		assertTrue(tiles.contains(board.giveTileAt(5, 7)));
		assertTrue(tiles.contains(board.giveTileAt(6, 7)));
	}
	
	@Test
	public void testFindTilesThatCrush() {
		int[][] scheme = {
				{ 0, 1, 2, 3, 6, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 2, 6, 6, 0, 1, 2, 3 },
				{ 1, 2, 2, 2, 6, 6, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 6, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 6, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(scheme);
		
		Set<Tile> allAdjacent = new HashSet<>(); 
		
		allAdjacent.addAll(BoardUtils.findAdjacentSameColorTiles(board, board.giveTileAt(3, 7), 2));
		allAdjacent.addAll(BoardUtils.findAdjacentSameColorTiles(board, board.giveTileAt(4, 7), 2));
		allAdjacent.add(board.giveTileAt(3, 7));
		allAdjacent.add(board.giveTileAt(4, 7));
		
		// Assertions to test that the initial set is correct.
		assertEquals(10, allAdjacent.size());
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 9)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 8)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 6)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 5)));
		assertTrue(allAdjacent.contains(board.giveTileAt(5, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(3, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(2, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(1, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(3, 8)));
		
		// Now test the findTIlesThatCrushMethod.
		Set<Tile> crushTiles = BoardUtils.findTilesThatCrush(allAdjacent);
		
		assertEquals(8, crushTiles.size());
		assertTrue(crushTiles.contains(board.giveTileAt(4, 9)));
		assertTrue(crushTiles.contains(board.giveTileAt(4, 8)));
		assertTrue(crushTiles.contains(board.giveTileAt(4, 7)));
		assertTrue(crushTiles.contains(board.giveTileAt(4, 6)));
		assertTrue(crushTiles.contains(board.giveTileAt(4, 5)));
		assertTrue(crushTiles.contains(board.giveTileAt(3, 7)));
		assertTrue(crushTiles.contains(board.giveTileAt(2, 7)));
		assertTrue(crushTiles.contains(board.giveTileAt(1, 7)));
	}
	
	@Test
	public void testFindTilesThatCrushWithNoTilesToCrush() {
		int[][] scheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 2, 5, 6, 0, 1, 2, 3 },
				{ 1, 1, 2, 2, 6, 6, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 6, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(scheme);
		
		Set<Tile> allAdjacent = new HashSet<>(); 
		
		allAdjacent.addAll(BoardUtils.findAdjacentSameColorTiles(board, board.giveTileAt(3, 7), 2));
		allAdjacent.addAll(BoardUtils.findAdjacentSameColorTiles(board, board.giveTileAt(4, 7), 2));
		allAdjacent.add(board.giveTileAt(3, 7));
		allAdjacent.add(board.giveTileAt(4, 7));
		
		// Assertions to test that the initial set is correct.
		assertEquals(6, allAdjacent.size());
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 6)));
		assertTrue(allAdjacent.contains(board.giveTileAt(5, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(3, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(2, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(3, 8)));
		
		// Now test the findTIlesThatCrushMethod.
		Set<Tile> crushTiles = BoardUtils.findTilesThatCrush(allAdjacent);
		
		assertEquals(0, crushTiles.size());
	}
	
	@Test
	public void findTilesThatCrushWithHoleTiles() {
		int[][] scheme = {
				{ 0, 1, 2,-1, 6, 5, 6, 0, 1, 2 },
				{ 1, 2, 3,-1, 6, 6, 0, 1, 2, 3 },
				{ 1, 2, 2,-1, 6, 6, 6, 2, 3, 4 },
				{ 3, 4, 5,-1, 6, 1, 2, 3, 4, 5 },
				{ 4, 5, 6,-1, 6, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(scheme);
		
		Set<Tile> allAdjacent = new HashSet<>(); 
		
		allAdjacent.addAll(BoardUtils.findAdjacentSameColorTiles(board, board.giveTileAt(3, 7), 2));
		allAdjacent.addAll(BoardUtils.findAdjacentSameColorTiles(board, board.giveTileAt(4, 7), 2));
		allAdjacent.add(board.giveTileAt(3, 7));
		allAdjacent.add(board.giveTileAt(4, 7));
		
		// Assertions to test that the initial set is correct.
		assertEquals(12, allAdjacent.size());
		assertTrue(allAdjacent.contains(board.giveTileAt(3, 9)));
		assertTrue(allAdjacent.contains(board.giveTileAt(3, 8)));
		assertTrue(allAdjacent.contains(board.giveTileAt(3, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(3, 6)));
		assertTrue(allAdjacent.contains(board.giveTileAt(3, 5)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 9)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 8)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 6)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 5)));
		assertTrue(allAdjacent.contains(board.giveTileAt(5, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(6, 7)));
		
		// Now test the findTIlesThatCrushMethod.
		Set<Tile> crushTiles = BoardUtils.findTilesThatCrush(allAdjacent);
		
		assertEquals(7, crushTiles.size());
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 9)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 8)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 6)));
		assertTrue(allAdjacent.contains(board.giveTileAt(4, 5)));
		assertTrue(allAdjacent.contains(board.giveTileAt(5, 7)));
		assertTrue(allAdjacent.contains(board.giveTileAt(6, 7)));
	}
	
	@Test
	public void testFindAllNPlesException() {
		boolean ex = false;
		
		try {
			BoardUtils.findAllNPles(null);
		} catch (BoardUtils.NullBoardRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testFindAllNPlesWithoutUnknownCandies() {
		int[][] boardScheme = {
				{ 1, 1, 1, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 1, 4, 5, 6, 0, 1, 2, 3 },
				{ 1, 3, 1, 5, 6, 0, 1, 2, 3, 4 },
				{ 1, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 1, 1, 1, 3, 1, 1, 1 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 5 },
				{ 3, 3, 3, 4, 5, 6, 0, 1, 2, 5 },
				{ 3, 3, 4, 5, 6, 0, 1, 2, 3, 5 },
				{ 3, 4, 5, 6, 0, 1, 2, 5, 5, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		
		Set<Tile> tiles = BoardUtils.findAllNPles(board);
		
		assertEquals(25, tiles.size());
		assertTrue(tiles.contains(board.giveTileAt(0, 9)));
		assertTrue(tiles.contains(board.giveTileAt(1, 9)));
		assertTrue(tiles.contains(board.giveTileAt(2, 9)));
		assertTrue(tiles.contains(board.giveTileAt(0, 8)));
		assertTrue(tiles.contains(board.giveTileAt(0, 7)));
		assertTrue(tiles.contains(board.giveTileAt(0, 6)));
		assertTrue(tiles.contains(board.giveTileAt(0, 2)));
		assertTrue(tiles.contains(board.giveTileAt(0, 1)));
		assertTrue(tiles.contains(board.giveTileAt(0, 0)));
		assertTrue(tiles.contains(board.giveTileAt(2, 8)));
		assertTrue(tiles.contains(board.giveTileAt(2, 7)));
		assertTrue(tiles.contains(board.giveTileAt(3, 5)));
		assertTrue(tiles.contains(board.giveTileAt(4, 5)));
		assertTrue(tiles.contains(board.giveTileAt(5, 5)));
		assertTrue(tiles.contains(board.giveTileAt(7, 5)));
		assertTrue(tiles.contains(board.giveTileAt(8, 5)));
		assertTrue(tiles.contains(board.giveTileAt(9, 5)));
		assertTrue(tiles.contains(board.giveTileAt(1, 2)));
		assertTrue(tiles.contains(board.giveTileAt(2, 2)));
		assertTrue(tiles.contains(board.giveTileAt(9, 3)));
		assertTrue(tiles.contains(board.giveTileAt(9, 2)));
		assertTrue(tiles.contains(board.giveTileAt(9, 1)));
		assertTrue(tiles.contains(board.giveTileAt(9, 0)));
		assertTrue(tiles.contains(board.giveTileAt(7, 0)));
		assertTrue(tiles.contains(board.giveTileAt(8, 0)));
	}
	
	@Test
	public void testFindAllNPlesWithUnknownCandies() {
		int[][] boardScheme = {
				{-1, 1, 1, 3,-1, 5, 6, 0, 1,-1 },
				{-1, 2, 1, 4,-1, 6, 0, 1, 2,-1 },
				{ 1, 3, 1, 5,-1, 0, 1, 2, 3,-1 },
				{ 1, 4, 5, 6,-1, 1, 2, 3, 4,-1 },
				{ 4, 5, 6, 1,-1, 1, 3, 1, 1,-1 },
				{ 5, 6, 0, 1,-1, 3, 4, 5, 6,-1 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1,-1 },
				{ 3, 3, 3, 4, 5, 6, 0, 1, 2, 5 },
				{ 3, 3, 4, 5, 6, 0, 1, 2, 3, 5 },
				{ 3, 4, 5, 6, 0, 1, 2, 5, 5, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		
		Set<Tile> tiles = BoardUtils.findAllNPles(board);
		
		assertEquals(13, tiles.size());
		assertTrue(tiles.contains(board.giveTileAt(2, 9)));
		assertTrue(tiles.contains(board.giveTileAt(0, 2)));
		assertTrue(tiles.contains(board.giveTileAt(0, 1)));
		assertTrue(tiles.contains(board.giveTileAt(0, 0)));
		assertTrue(tiles.contains(board.giveTileAt(2, 8)));
		assertTrue(tiles.contains(board.giveTileAt(2, 7)));
		assertTrue(tiles.contains(board.giveTileAt(1, 2)));
		assertTrue(tiles.contains(board.giveTileAt(2, 2)));
		assertTrue(tiles.contains(board.giveTileAt(9, 2)));
		assertTrue(tiles.contains(board.giveTileAt(9, 1)));
		assertTrue(tiles.contains(board.giveTileAt(9, 0)));
		assertTrue(tiles.contains(board.giveTileAt(7, 0)));
		assertTrue(tiles.contains(board.giveTileAt(8, 0)));
	}
		
	@Test
	public void testIsValidCordsOnActualBoard() {
		for (int y = 0; y < noMoveBoard.getPRows(); y++) {
			for (int x = 0; x < noMoveBoard.getCols(); x++) {
				assertTrue(BoardUtils.isValidCords(noMoveBoard, x, y));
			}
		}
		
		assertFalse(BoardUtils.isValidCords(noMoveBoard, -1, 0));
		assertFalse(BoardUtils.isValidCords(noMoveBoard, 0, -1));
		assertFalse(BoardUtils.isValidCords(noMoveBoard, noMoveBoard.getCols(), 0));
		assertFalse(BoardUtils.isValidCords(noMoveBoard, 0, noMoveBoard.getPRows()));
	}
	
	@Test
	public void testIsValidCordsByCrushUtilitiesValues() {
		for (int y = 0; y < CrushUtilities.NUMBER_OF_PLAYABLE_ROWS; y++) {
			for (int x = 0; x < CrushUtilities.NUMBER_OF_COLUMNS; x++) {
				assertTrue(BoardUtils.isValidCords(x, y));
			}
		}
		
		assertFalse(BoardUtils.isValidCords(-1, 0));
		assertFalse(BoardUtils.isValidCords(0, -1));
		assertFalse(BoardUtils.isValidCords(CrushUtilities.NUMBER_OF_COLUMNS, 0));
		assertFalse(BoardUtils.isValidCords(0, CrushUtilities.NUMBER_OF_PLAYABLE_ROWS));
	}
	

// ==== Tests for legacy code ====
//	
//	@Test
//	public void testBoardCopyException() {
//		boolean ex = false;
//		
//		try {
//			BoardUtils.boardCopy(null);
//		} catch (BoardUtils.NullBoardRuntimeException e) {
//			ex = true;
//		}
//		
//		assertTrue(ex);
//	}
//	
//	@Test
//	public void testBoardCopyWithNoMoveBoard() {
//		Board noMoveBoardCopy = BoardUtils.boardCopy(noMoveBoard);
//		
//		for(int y = 0; y < noMoveBoard.getCols(); y++){
//			for(int x = 0; x < noMoveBoard.getWidth(); x++) {
//				assertEquals(noMoveBoard.giveTileAt(x, y).getX(), noMoveBoardCopy.giveTileAt(x, y).getX());
//				assertEquals(noMoveBoard.giveTileAt(x, y).getY(), noMoveBoardCopy.giveTileAt(x, y).getY());
//				assertTrue(noMoveBoard.giveTileAt(x, y).equals(noMoveBoardCopy.giveTileAt(x, y)));
//			}
//		}
//	}
//	
//	@Test
//	public void testDoMoveNullMoveException() {
//		boolean ex = false;
//		
//		try {
//			BoardUtils.doMove(noMoveBoard, null);
//		} catch (BoardUtils.NullMoveRuntimeException e) {
//			ex = true;
//		}
//		
//		assertTrue(ex);
//	}
//	
//	@Test
//	public void testDoMoveNullBoardException() {
//		PlayerMove move = new PlayerMove(noMoveBoard.giveTileAt(0, 0), 
//										 noMoveBoard.giveTileAt(1, 0));
//		
//		boolean ex = false;
//		
//		try {
//			BoardUtils.doMove(null, move);
//		} catch (BoardUtils.NullBoardRuntimeException e) {
//			ex = true;
//		}
//		
//		assertTrue(ex);
//	}
//	
//	@Test
//	public void testDoMoveVertically() {
//		Board noMoveBoardCopy = BoardUtils.boardCopy(noMoveBoard);
//		
//		PlayerMove move = new PlayerMove(noMoveBoardCopy.giveTileAt(2, 2), 
//				 						 noMoveBoardCopy.giveTileAt(2, 3));
//		
//		noMoveBoardCopy = BoardUtils.doMove(noMoveBoardCopy, move);
//		
//		assertEquals(2, noMoveBoardCopy.giveTileAt(2, 2).getX());
//		assertEquals(2, noMoveBoardCopy.giveTileAt(2, 2).getY());
//		
//		assertEquals(2, noMoveBoardCopy.giveTileAt(2, 3).getX());
//		assertEquals(3, noMoveBoardCopy.giveTileAt(2, 3).getY());
//		
//		assertEquals(noMoveBoard.giveTileAt(2, 2).getColor(), noMoveBoardCopy.giveTileAt(2, 3).getColor());
//		assertEquals(noMoveBoard.giveTileAt(2, 3).getColor(), noMoveBoardCopy.giveTileAt(2, 2).getColor());
//	}
//	
//	@Test
//	public void testGetBoardAfterMoveAndCrush() {
//		int[][] boardScheme = {
//				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
//				{ 1, 2, 3, 6, 5, 6, 6, 1, 2, 3 },
//				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
//				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
//				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
//				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
//				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
//				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
//				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
//				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
//		};
//		
//		Board board = createBoard(boardScheme);
//		
//		Board afterMoveBoard = BoardUtils.getBoardAfterMoveAndCrush(
//				board, new PlayerMove(board.giveTileAt(3, 1), board.giveTileAt(4, 1)));
//		
//		/*
//		for (int y = 0; y < afterMoveBoard.getRows(); y++) {
//			for (int x = 0; x < afterMoveBoard.getCols(); x++) {
//				System.out.print(afterMoveBoard.giveTileAt(x, y).getColor() + " ");
//			}
//			System.out.print("\n");
//		}
//		*/
//		
//		assertEquals(4, afterMoveBoard.giveTileAt(4, 1).getColor());
//		assertEquals(5, afterMoveBoard.giveTileAt(5, 1).getColor());
//		assertEquals(6, afterMoveBoard.giveTileAt(6, 1).getColor());
//	}
//	
//	@Test
//	public void testGetBoardAfterMoveAndCrush2() {
//		int[][] boardScheme = {
//				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
//				{ 1, 2, 3, 6, 5, 6, 6, 1, 2, 3 },
//				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
//				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
//				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
//				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
//				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
//				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
//				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
//				{ 3, 4, 5, 6, 0, 1, 3, 3, 3, 5 }, 
//		};
//		
//		Board board = createBoard(boardScheme);
//		
//		Board afterMoveBoard = BoardUtils.getBoardAfterMoveAndCrush(
//				board, new PlayerMove(board.giveTileAt(3, 1), board.giveTileAt(4, 1)));
//		
//		/*
//		for (int y = 0; y < afterMoveBoard.getRows(); y++) {
//			for (int x = 0; x < afterMoveBoard.getCols(); x++) {
//				System.out.print(afterMoveBoard.giveTileAt(x, y).getColor() + " ");
//			}
//			System.out.print("\n");
//		}
//		*/
//				
//		assertEquals(4, afterMoveBoard.giveTileAt(4, 1).getColor());
//		assertEquals(5, afterMoveBoard.giveTileAt(5, 1).getColor());
//		assertEquals(6, afterMoveBoard.giveTileAt(6, 2).getColor());
//		assertEquals(6, afterMoveBoard.giveTileAt(6, 7).getColor());
//		assertEquals(0, afterMoveBoard.giveTileAt(7, 7).getColor());
//		assertEquals(1, afterMoveBoard.giveTileAt(8, 7).getColor());
//		assertEquals(0, afterMoveBoard.giveTileAt(6, 8).getColor());
//		assertEquals(1, afterMoveBoard.giveTileAt(7, 8).getColor());
//		assertEquals(2, afterMoveBoard.giveTileAt(8, 8).getColor());
//		assertEquals(1, afterMoveBoard.giveTileAt(6, 9).getColor());
//		assertEquals(2, afterMoveBoard.giveTileAt(7, 9).getColor());
//		assertEquals(3, afterMoveBoard.giveTileAt(8, 9).getColor());
//	}
//	
//	@Test
//	public void testGetBoardAfterMoveAndCrush3() {
//		int[][] boardScheme = {
//				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
//				{ 1, 2, 3, 6, 5, 6, 6, 1, 2, 3 },
//				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
//				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
//				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
//				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
//				{ 0, 3, 2, 3, 4, 5, 6, 0, 1, 2 },
//				{ 1, 3, 3, 4, 5, 6, 0, 1, 2, 3 },
//				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
//				{ 3, 3, 5, 6, 0, 1, 3, 3, 3, 5 }, 
//		};
//		
//		Board board = createBoard(boardScheme);
//		
//		Board afterMoveBoard = BoardUtils.getBoardAfterMoveAndCrush(
//				board, new PlayerMove(board.giveTileAt(3, 1), board.giveTileAt(4, 1)));
//		
//		/*
//		for (int y = 0; y < afterMoveBoard.getRows(); y++) {
//			for (int x = 0; x < afterMoveBoard.getCols(); x++) {
//				System.out.print(afterMoveBoard.giveTileAt(x, y).getColor() + " ");
//			}
//			System.out.print("\n");
//		}
//		*/
//				
//		assertEquals(4, afterMoveBoard.giveTileAt(4, 1).getColor());
//		assertEquals(5, afterMoveBoard.giveTileAt(5, 1).getColor());
//		assertEquals(6, afterMoveBoard.giveTileAt(6, 2).getColor());
//		assertEquals(6, afterMoveBoard.giveTileAt(6, 7).getColor());
//		assertEquals(0, afterMoveBoard.giveTileAt(7, 7).getColor());
//		assertEquals(1, afterMoveBoard.giveTileAt(8, 7).getColor());
//		assertEquals(0, afterMoveBoard.giveTileAt(6, 8).getColor());
//		assertEquals(1, afterMoveBoard.giveTileAt(7, 8).getColor());
//		assertEquals(2, afterMoveBoard.giveTileAt(8, 8).getColor());
//		assertEquals(1, afterMoveBoard.giveTileAt(6, 9).getColor());
//		assertEquals(2, afterMoveBoard.giveTileAt(7, 9).getColor());
//		assertEquals(3, afterMoveBoard.giveTileAt(8, 9).getColor());
//		assertEquals(4, afterMoveBoard.giveTileAt(1, 7).getColor());
//		assertEquals(5, afterMoveBoard.giveTileAt(1, 8).getColor());
//		assertEquals(6, afterMoveBoard.giveTileAt(1, 9).getColor());		
//	}
}
