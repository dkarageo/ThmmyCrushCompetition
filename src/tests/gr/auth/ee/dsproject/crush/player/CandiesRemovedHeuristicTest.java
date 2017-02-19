package tests.gr.auth.ee.dsproject.crush.player;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gr.auth.ee.dsproject.crush.board.*;
import gr.auth.ee.dsproject.crush.player.move.*;

import gr.auth.ee.dsproject.crush.player.CandiesRemovedHeuristic;


public class CandiesRemovedHeuristicTest {
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
	public void testMoveAndBoardArgumentsConstructor() {
		PlayerMove move = new PlayerMove(
				noMoveBoard.giveTileAt(0, 0), noMoveBoard.giveTileAt(0, 1)
		); 
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(move, noMoveBoard);
		
		assertEquals(move, heur.getPlayerMove());
		assertEquals(noMoveBoard, heur.getBoard());
	}
	
	@Test
	public void testSetBoard() {
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
		heur.setBoard(noMoveBoard);
		
		assertEquals(noMoveBoard, heur.getBoard());
	}
	
	@Test
	public void testSetPlayerMove() {
		PlayerMove move = new PlayerMove(
				noMoveBoard.giveTileAt(0, 0), noMoveBoard.giveTileAt(0, 1)
		); 
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
		heur.setPlayerMove(move);
		
		assertEquals(move, heur.getPlayerMove());
	}
	
	@Test
	public void testInitialCandiesRemovedWithNoPreviousHoles() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 6, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 5, 5, 2, 3, 4 },
				{ 3, 4, 5, 6, 5, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(3, 7), board.giveTileAt(4, 7));
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(move, board);
		Set<Tile> removed = heur.initialCandiesRemoved();
		
		assertEquals(8, removed.size());
		assertTrue(removed.contains(board.giveTileAt(3, 7)));
		assertTrue(removed.contains(board.giveTileAt(5, 7)));
		assertTrue(removed.contains(board.giveTileAt(6, 7)));
		assertTrue(removed.contains(board.giveTileAt(4, 8)));
		assertTrue(removed.contains(board.giveTileAt(4, 6)));
		assertTrue(removed.contains(board.giveTileAt(3, 8)));
		assertTrue(removed.contains(board.giveTileAt(4, 7)));
		assertTrue(removed.contains(board.giveTileAt(3, 6)));
	}
	
	@Test
	public void testInitialCandiesRemovedWithOnlyOneOfMoveTilesCrushWithNoPreviousHoles() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 0, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 5, 5, 2, 3, 4 },
				{ 3, 4, 5, 6, 5, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(3, 7), board.giveTileAt(4, 7));
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(move, board);
		Set<Tile> removed = heur.initialCandiesRemoved();
		
		assertEquals(5, removed.size());
		assertTrue(removed.contains(board.giveTileAt(3, 7)));
		assertTrue(removed.contains(board.giveTileAt(5, 7)));
		assertTrue(removed.contains(board.giveTileAt(6, 7)));
		assertTrue(removed.contains(board.giveTileAt(4, 8)));
		assertTrue(removed.contains(board.giveTileAt(4, 6)));
	}
	
	/**
	 * initialCandiesRemoved() should reject here all tiles having
	 * a negative color. Thus, only the three candies formed by
	 * the move should be considered as crush tiles.
	 */
	@Test
	public void testInitialCandiesRemovedWithPreviousHoles() {
		int[][] boardScheme = {
				{ 0, 1, 2,-1,-1,-1,-1, 0, 1, 2 },
				{ 1, 2, 3, 4,-1,-1,-1, 1, 2, 3 },
				{ 2, 3, 5, 5,-1, 5, 1, 2, 3, 4 },
				{ 3, 4, 5, 6,-1, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(4, 7), board.giveTileAt(5, 7));
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(move, board);
		Set<Tile> removed = heur.initialCandiesRemoved();
		
		assertEquals(3, removed.size());
		assertTrue(removed.contains(board.giveTileAt(2, 7)));
		assertTrue(removed.contains(board.giveTileAt(3, 7)));
		assertTrue(removed.contains(board.giveTileAt(5, 7)));
	}
		
	@Test
	public void testCountChainedCandiesRemovedOneDeep() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 3, 5, 4, 4, 1, 2 },
				{ 1, 3, 3, 4, 4, 6, 4, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(5, 8), board.giveTileAt(6, 8));
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(move, board);
		
		Set<Tile> removed = heur.initialCandiesRemoved(); 
		
		// Test that removed set is the expected one.
		assertEquals(3, removed.size());
		assertTrue(removed.contains(board.giveTileAt(3, 8)));
		assertTrue(removed.contains(board.giveTileAt(4, 8)));
		assertTrue(removed.contains(board.giveTileAt(6, 8)));
		
		removed.remove(board.giveTileAt(6, 8));
		removed.add(board.giveTileAt(5, 8));
		
		// Now calculate the actual chained moves.
		Board afterCrush = CrushUtilities.boardAfterFirstCrush(board, move.toDirArray());
		int chainCount = heur.countChainedCandiesRemoved(afterCrush);
		
		assertEquals(4, chainCount);
	}
	
	@Test
	public void testCountChainedCandiesRemovedTwoDeep() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 4, 6, 4, 1, 2, 3 },
				{ 5, 3, 4, 5, 5, 0, 5, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(5, 7), board.giveTileAt(6, 7));
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(move, board);
		
		Set<Tile> removed = heur.initialCandiesRemoved(); 
		
		// Test that removed set is the expected one.
		assertEquals(3, removed.size());
		assertTrue(removed.contains(board.giveTileAt(3, 7)));
		assertTrue(removed.contains(board.giveTileAt(4, 7)));
		assertTrue(removed.contains(board.giveTileAt(6, 7)));
		
		removed.remove(board.giveTileAt(6, 7));
		removed.add(board.giveTileAt(5, 7));
		
		Board afterCrush = CrushUtilities.boardAfterFirstCrush(board, move.toDirArray());
		
		int chainCount = heur.countChainedCandiesRemoved(afterCrush);
		
		assertEquals(6, chainCount);
	}
	
	@Test
	public void testCountChainedCandiesRemovedThreeDeep() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 4, 6, 4, 1, 2, 3 },
				{ 2, 3, 4, 5, 5, 0, 5, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(5, 7), board.giveTileAt(6, 7));
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(move, board);
		
		Set<Tile> removed = heur.initialCandiesRemoved(); 
		
		// Test that removed set is the expected one.
		assertEquals(3, removed.size());
		assertTrue(removed.contains(board.giveTileAt(3, 7)));
		assertTrue(removed.contains(board.giveTileAt(4, 7)));
		assertTrue(removed.contains(board.giveTileAt(6, 7)));
		
		removed.remove(board.giveTileAt(6, 7));
		removed.add(board.giveTileAt(5, 7));
		
		Board afterCrush = CrushUtilities.boardAfterFirstCrush(board, move.toDirArray());
		
		int chainCount = heur.countChainedCandiesRemoved(afterCrush);
		
		assertEquals(9, chainCount);
	}
	
	@Test
	public void testEvaluate1() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 3, 5, 4, 4, 1, 2 },
				{ 1, 3, 3, 4, 4, 6, 4, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(5, 8), board.giveTileAt(6, 8));
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(move, board);
		
		double score = 6 * 4.0 + 1 * 2.0;
		
		assertEquals(score, heur.evaluate(), 0.1);
	}
	
	@Test
	public void testEvaluate2() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 4, 6, 4, 1, 2, 3 },
				{ 5, 3, 4, 5, 5, 0, 5, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(5, 7), board.giveTileAt(6, 7));
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(move, board);
		
		double score = 6 * 4.0 + 3 * 2.0;
		
		assertEquals(score, heur.evaluate(), 0.1);
	}
	
	@Test
	public void testEvaluate3() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 4, 6, 4, 1, 2, 3 },
				{ 2, 3, 4, 5, 5, 0, 5, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(5, 7), board.giveTileAt(6, 7));
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(move, board);
		
		double score = 6 * 4.0 + 6 * 2.0;
		
		assertEquals(score, heur.evaluate(), 0.1);
	}
	
	
	@Test
	public void testEvaluateOnBoardWithPreviousHolesAndNoChainedMoves() {
		int[][] boardScheme = {
				{ 0, 1,-1,-1,-1,-1,-1, 0, 1, 2 },
				{ 1, 2,-1, 4, 2,-1, 4, 1, 2, 3 },
				{ 5, 3,-1, 5, 5,-1, 5, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(5, 7), board.giveTileAt(6, 7));
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(move, board);
		
		double score = 3 * 4.0;
		
		assertEquals(score, heur.evaluate(), 0.1);
	}
	
	@Test
	public void testEvaluateOnBoardWithPreviousHolesHorizontal() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3,-1, -1,-1, 0, 1, 2 },
				{ 1, 2, 3, 4, 4, 6, 4, 1, 2, 3 },
				{ 5, 3, 4, 5, 5, 0, 5, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(5, 7), board.giveTileAt(6, 7));
		
		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic(move, board);
		
		double score = 6 * 4.0 + 3 * 2.0;
		
		assertEquals(score, heur.evaluate(), 0.1);
	}
	
//==== Tests for legacy code ====
//	
//	@Test 
//	public void testFindAFakeMoveWith3HolesVertical() {
//		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
//		
//		Map<Integer, Integer> holes = new HashMap<>();
//		holes.put(0, 3);
//		
//		PlayerMove move = heur.findAFakeMove(noMoveBoard, holes);
//		
//		assertEquals(0, move.getX1());
//		assertEquals(0, move.getX2());
//		
//		assertEquals(0, move.getY1());
//		assertEquals(1, move.getY2());
//	}
//	
//	@Test 
//	public void testFindAFakeMoveWith3HolesHorizontal() {
//		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
//		
//		Map<Integer, Integer> holes = new HashMap<>();
//		holes.put(0, 1);
//		holes.put(1, 1);
//		holes.put(2, 1);
//		
//		PlayerMove move = heur.findAFakeMove(noMoveBoard, holes);
//		
//		assertEquals(0, move.getX1());
//		assertEquals(1, move.getX2());
//		
//		assertEquals(0, move.getY1());
//		assertEquals(0, move.getY2());
//	}
//	
//	@Test 
//	public void testFindAFakeMoveWith6HolesVertical() {
//		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
//		
//		Map<Integer, Integer> holes = new HashMap<>();
//		holes.put(2, 3);
//		holes.put(4, 3);
//		
//		PlayerMove move = heur.findAFakeMove(noMoveBoard, holes);
//		
//		assertEquals(2, move.getX1());
//		assertEquals(2, move.getX2());
//		
//		assertEquals(0, move.getY1());
//		assertEquals(1, move.getY2());
//	}
//	
//	@Test 
//	public void testFindAFakeMoveWith6HolesHorizontal() {
//		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
//		
//		Map<Integer, Integer> holes = new HashMap<>();
//		holes.put(0, 2);
//		holes.put(1, 2);
//		holes.put(2, 2);		
//		
//		PlayerMove move = heur.findAFakeMove(noMoveBoard, holes);
//		
//		assertEquals(0, move.getX1());
//		assertEquals(1, move.getX2());
//		
//		assertEquals(0, move.getY1());
//		assertEquals(0, move.getY2());
//	}
//	
//	@Test 
//	public void testFindAFakeMoveWith3HolesVerticalAnd3HolesHorizontal() {
//		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
//		
//		Map<Integer, Integer> holes = new HashMap<>();
//		holes.put(2, 3);
//		holes.put(3, 3);
//		holes.put(4, 3);
//		
//		PlayerMove move = heur.findAFakeMove(noMoveBoard, holes);
//		
//		assertEquals(2, move.getX1());
//		assertEquals(3, move.getX2());
//		
//		assertEquals(0, move.getY1());
//		assertEquals(0, move.getY2());
//	}
//	@Test
//	public void testFindVerticalCrushCandiesWithHolesOnNoHoles() {
//		int[][] boardScheme = {
//				{ 1, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
//				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
//				{ 1, 3, 3, 5, 6, 6, 1, 2, 3, 4 },
//				{ 3, 4, 3, 6, 0, 6, 2, 3, 4, 5 },
//				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 5 },
//				{ 1, 6, 0, 1, 2, 3, 4, 5, 6, 5 },
//				{ 1, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
//				{ 1, 2, 3, 4, 5, 6, 0, 1, 1, 3 },
//				{ 2, 3, 4, 5, 6, 0, 1, 2, 1, 4 },
//				{ 3, 4, 5, 6, 0, 1, 2, 3, 1, 5 }, 
//		};
//		
//		Board board = createBoard(boardScheme);
//		
//		Map<Integer, Integer> holes = new HashMap<>();
//		
//		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
//		
//		// Collumn 0
//		Set<Tile> col0 = heur.findVerticalCrushCandiesWithHoles(board, 0, holes);
//		assertEquals(6, col0.size());
//		assertTrue(col0.contains(board.giveTileAt(0, 9)));
//		assertTrue(col0.contains(board.giveTileAt(0, 8)));
//		assertTrue(col0.contains(board.giveTileAt(0, 7)));
//		assertTrue(col0.contains(board.giveTileAt(0, 4)));
//		assertTrue(col0.contains(board.giveTileAt(0, 3)));
//		assertTrue(col0.contains(board.giveTileAt(0, 2)));
//		
//		// Collumn 2
//		Set<Tile> col2 = heur.findVerticalCrushCandiesWithHoles(board, 2, holes);
//		assertEquals(3, col2.size());
//		assertTrue(col2.contains(board.giveTileAt(2, 8)));
//		assertTrue(col2.contains(board.giveTileAt(2, 7)));
//		assertTrue(col2.contains(board.giveTileAt(2, 6)));
//		
//		// Collumn 5
//		Set<Tile> col5 = heur.findVerticalCrushCandiesWithHoles(board, 5, holes);
//		assertEquals(3, col5.size());
//		assertTrue(col5.contains(board.giveTileAt(5, 8)));
//		assertTrue(col5.contains(board.giveTileAt(5, 7)));
//		assertTrue(col5.contains(board.giveTileAt(5, 6)));		
//		
//		// Collumn 8
//		Set<Tile> col8 = heur.findVerticalCrushCandiesWithHoles(board, 8, holes);
//		assertEquals(4, col8.size());
//		assertTrue(col8.contains(board.giveTileAt(8, 3)));
//		assertTrue(col8.contains(board.giveTileAt(8, 2)));
//		assertTrue(col8.contains(board.giveTileAt(8, 1)));
//		assertTrue(col8.contains(board.giveTileAt(8, 0)));
//		
//		// Collumn 0
//		Set<Tile> col9 = heur.findVerticalCrushCandiesWithHoles(board, 9, holes);
//		assertEquals(3, col9.size());
//		assertTrue(col9.contains(board.giveTileAt(9, 6)));
//		assertTrue(col9.contains(board.giveTileAt(9, 5)));
//		assertTrue(col9.contains(board.giveTileAt(9, 4)));
//	}
//	
//	@Test
//	public void testFindVerticalCrushCandiesWithHolesOnHoles() {
//		int[][] boardScheme = {
//				{ 1, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
//				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
//				{ 1, 3, 3, 5, 6, 6, 1, 2, 3, 4 },
//				{ 3, 4, 3, 6, 0, 6, 2, 3, 4, 5 },
//				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 5 },
//				{ 1, 6, 0, 1, 2, 3, 4, 5, 6, 5 },
//				{ 1, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
//				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
//				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
//				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
//		};
//		
//		Board board = createBoard(boardScheme);
//		
//		Map<Integer, Integer> holes = new HashMap<>();
//		holes.put(0, 1);
//		holes.put(5, 2);
//		
//		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
//		
//		// Collumn 0
//		Set<Tile> col0 = heur.findVerticalCrushCandiesWithHoles(board, 0, holes);
//		assertEquals(3, col0.size());
//		assertTrue(col0.contains(board.giveTileAt(0, 4)));
//		assertTrue(col0.contains(board.giveTileAt(0, 3)));
//		assertTrue(col0.contains(board.giveTileAt(0, 2)));
//		
//		// Collumn 2
//		Set<Tile> col2 = heur.findVerticalCrushCandiesWithHoles(board, 2, holes);
//		assertEquals(3, col2.size());
//		assertTrue(col2.contains(board.giveTileAt(2, 8)));
//		assertTrue(col2.contains(board.giveTileAt(2, 7)));
//		assertTrue(col2.contains(board.giveTileAt(2, 6)));
//		
//		// Collumn 5
//		Set<Tile> col5 = heur.findVerticalCrushCandiesWithHoles(board, 5, holes);
//		assertEquals(0, col5.size());
//		
//		// Collumn 9
//		Set<Tile> col9 = heur.findVerticalCrushCandiesWithHoles(board, 9, holes);
//		assertEquals(3, col9.size());
//		assertTrue(col9.contains(board.giveTileAt(9, 6)));
//		assertTrue(col9.contains(board.giveTileAt(9, 5)));
//		assertTrue(col9.contains(board.giveTileAt(9, 4)));
//	}
//	
//	@Test
//	public void testFindHorizontalCrushCandiesWithHolesOnNoHoles() {
//		int[][] boardScheme = {
//				{ 2, 2, 2, 1, 4, 5, 3, 3, 3, 3 },
//				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
//				{ 2, 3, 1, 1, 1, 0, 1, 2, 3, 4 },
//				{ 3, 4, 5, 6, 0, 4, 4, 4, 4, 5 },
//				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
//				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
//				{ 0, 1, 5, 5, 5, 5, 6, 1, 1, 2 },
//				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
//				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
//				{ 3, 4, 5, 6, 6, 6, 2, 3, 3, 3 }, 
//		};
//		
//		Board board = createBoard(boardScheme);
//		
//		Map<Integer, Integer> holes = new HashMap<>();
//		
//		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
//		
//		// Row 9 - 3 Tiles at beginning - 4 Tiles at the end
//		Set<Tile> row9 = heur.findHorizontalCrushCandiesWithHoles(board, 9, holes);
//		assertEquals(7, row9.size());
//		assertTrue(row9.contains(board.giveTileAt(0, 9)));
//		assertTrue(row9.contains(board.giveTileAt(1, 9)));
//		assertTrue(row9.contains(board.giveTileAt(2, 9)));
//		assertTrue(row9.contains(board.giveTileAt(6, 9)));
//		assertTrue(row9.contains(board.giveTileAt(7, 9)));
//		assertTrue(row9.contains(board.giveTileAt(8, 9)));
//		assertTrue(row9.contains(board.giveTileAt(9, 9)));
//		
//		// Row 7 - 3 Tiles at the middle
//		Set<Tile> row7 = heur.findHorizontalCrushCandiesWithHoles(board, 7, holes);
//		assertEquals(3, row7.size());
//		assertTrue(row7.contains(board.giveTileAt(2, 7)));
//		assertTrue(row7.contains(board.giveTileAt(3, 7)));
//		assertTrue(row7.contains(board.giveTileAt(4, 7)));
//		
//		// Row 6 - 4 Tiles at the middle
//		Set<Tile> row6 = heur.findHorizontalCrushCandiesWithHoles(board, 6, holes);
//		assertEquals(4, row6.size());
//		assertTrue(row6.contains(board.giveTileAt(5, 6)));
//		assertTrue(row6.contains(board.giveTileAt(6, 6)));
//		assertTrue(row6.contains(board.giveTileAt(7, 6)));
//		assertTrue(row6.contains(board.giveTileAt(8, 6)));
//		
//		// Row 3 - 4 Tiles at the middle
//		Set<Tile> row3 = heur.findHorizontalCrushCandiesWithHoles(board, 3, holes);
//		assertEquals(4, row3.size());
//		assertTrue(row3.contains(board.giveTileAt(2, 3)));
//		assertTrue(row3.contains(board.giveTileAt(3, 3)));
//		assertTrue(row3.contains(board.giveTileAt(4, 3)));
//		assertTrue(row3.contains(board.giveTileAt(5, 3)));
//		
//		// Row 0 - 3 Tiles at the middle - 3 Tiles at the end
//		Set<Tile> row0 = heur.findHorizontalCrushCandiesWithHoles(board, 0, holes);
//		assertEquals(6, row0.size());
//		assertTrue(row0.contains(board.giveTileAt(3, 0)));
//		assertTrue(row0.contains(board.giveTileAt(4, 0)));
//		assertTrue(row0.contains(board.giveTileAt(5, 0)));
//		assertTrue(row0.contains(board.giveTileAt(7, 0)));
//		assertTrue(row0.contains(board.giveTileAt(8, 0)));
//		assertTrue(row0.contains(board.giveTileAt(9, 0)));
//	}
//	
//	@Test
//	public void testFindHorizontalCrushCandiesWithHolesOnHoles() {
//		int[][] boardScheme = {
//				{ 2, 2, 2, 1, 4, 5, 3, 3, 3, 3 },
//				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
//				{ 2, 3, 1, 1, 1, 0, 1, 2, 3, 4 },
//				{ 3, 4, 5, 6, 0, 4, 4, 4, 4, 5 },
//				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
//				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
//				{ 0, 1, 5, 5, 5, 5, 6, 1, 1, 2 },
//				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
//				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
//				{ 3, 4, 5, 6, 6, 6, 2, 3, 3, 3 }, 
//		};
//		
//		Board board = createBoard(boardScheme);
//		
//		Map<Integer, Integer> holes = new HashMap<>();
//		holes.put(0, 1);
//		holes.put(5, 4);
//		holes.put(9, 2);
//		
//		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
//		
//		// Row 9 - 3 Tiles at beginning with one hole - 4 Tiles at the end with one hole
//		Set<Tile> row9 = heur.findHorizontalCrushCandiesWithHoles(board, 9, holes);
//		assertEquals(3, row9.size());
//		assertTrue(row9.contains(board.giveTileAt(6, 9)));
//		assertTrue(row9.contains(board.giveTileAt(7, 9)));
//		assertTrue(row9.contains(board.giveTileAt(8, 9)));
//		
//		// Row 7 - 3 Tiles at the middle
//		Set<Tile> row7 = heur.findHorizontalCrushCandiesWithHoles(board, 7, holes);
//		assertEquals(3, row7.size());
//		assertTrue(row7.contains(board.giveTileAt(2, 7)));
//		assertTrue(row7.contains(board.giveTileAt(3, 7)));
//		assertTrue(row7.contains(board.giveTileAt(4, 7)));
//		
//		// Row 6 - 4 Tiles at the middle with one hole
//		Set<Tile> row6 = heur.findHorizontalCrushCandiesWithHoles(board, 6, holes);
//		assertEquals(3, row6.size());
//		assertTrue(row6.contains(board.giveTileAt(6, 6)));
//		assertTrue(row6.contains(board.giveTileAt(7, 6)));
//		assertTrue(row6.contains(board.giveTileAt(8, 6)));
//		
//		// Row 3 - 4 Tiles at the middle
//		Set<Tile> row3 = heur.findHorizontalCrushCandiesWithHoles(board, 3, holes);
//		assertEquals(4, row3.size());
//		assertTrue(row3.contains(board.giveTileAt(2, 3)));
//		assertTrue(row3.contains(board.giveTileAt(3, 3)));
//		assertTrue(row3.contains(board.giveTileAt(4, 3)));
//		assertTrue(row3.contains(board.giveTileAt(5, 3)));
//		
//		// Row 0 - 3 Tiles at the middle - 3 Tiles at the end
//		Set<Tile> row0 = heur.findHorizontalCrushCandiesWithHoles(board, 0, holes);
//		assertEquals(6, row0.size());
//		assertTrue(row0.contains(board.giveTileAt(3, 0)));
//		assertTrue(row0.contains(board.giveTileAt(4, 0)));
//		assertTrue(row0.contains(board.giveTileAt(5, 0)));
//		assertTrue(row0.contains(board.giveTileAt(7, 0)));
//		assertTrue(row0.contains(board.giveTileAt(8, 0)));
//		assertTrue(row0.contains(board.giveTileAt(9, 0)));
//	}
//	
//	@Test
//	public void testFindCandiesThatCrushWithHolesOnNoHoles() {
//		int[][] boardScheme = {
//				{ 1, 1, 1, 3, 4, 5, 6, 0, 1, 2 },
//				{ 1, 2, 1, 4, 5, 6, 0, 1, 2, 3 },
//				{ 1, 3, 1, 5, 6, 0, 1, 2, 3, 4 },
//				{ 1, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
//				{ 4, 5, 6, 1, 1, 1, 3, 1, 1, 1 },
//				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
//				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 5 },
//				{ 3, 3, 3, 4, 5, 6, 0, 1, 2, 5 },
//				{ 3, 3, 4, 5, 6, 0, 1, 2, 3, 5 },
//				{ 3, 4, 5, 6, 0, 1, 2, 5, 5, 5 }, 
//		};
//		
//		Board board = createBoard(boardScheme);
//		Map<Integer, Integer> holes = new HashMap<>();
//		
//		CandiesRemovedHeuristic heurs = new CandiesRemovedHeuristic();
//		
//		Set<Tile> tiles = heurs.findCandiesThatCrushWithHoles(board, holes);
//		assertEquals(25, tiles.size());
//		assertTrue(tiles.contains(board.giveTileAt(0, 9)));
//		assertTrue(tiles.contains(board.giveTileAt(1, 9)));
//		assertTrue(tiles.contains(board.giveTileAt(2, 9)));
//		assertTrue(tiles.contains(board.giveTileAt(0, 8)));
//		assertTrue(tiles.contains(board.giveTileAt(0, 7)));
//		assertTrue(tiles.contains(board.giveTileAt(0, 6)));
//		assertTrue(tiles.contains(board.giveTileAt(0, 2)));
//		assertTrue(tiles.contains(board.giveTileAt(0, 1)));
//		assertTrue(tiles.contains(board.giveTileAt(0, 0)));
//		assertTrue(tiles.contains(board.giveTileAt(2, 8)));
//		assertTrue(tiles.contains(board.giveTileAt(2, 7)));
//		assertTrue(tiles.contains(board.giveTileAt(3, 5)));
//		assertTrue(tiles.contains(board.giveTileAt(4, 5)));
//		assertTrue(tiles.contains(board.giveTileAt(5, 5)));
//		assertTrue(tiles.contains(board.giveTileAt(7, 5)));
//		assertTrue(tiles.contains(board.giveTileAt(8, 5)));
//		assertTrue(tiles.contains(board.giveTileAt(9, 5)));
//		assertTrue(tiles.contains(board.giveTileAt(1, 2)));
//		assertTrue(tiles.contains(board.giveTileAt(2, 2)));
//		assertTrue(tiles.contains(board.giveTileAt(9, 3)));
//		assertTrue(tiles.contains(board.giveTileAt(9, 2)));
//		assertTrue(tiles.contains(board.giveTileAt(9, 1)));
//		assertTrue(tiles.contains(board.giveTileAt(9, 0)));
//		assertTrue(tiles.contains(board.giveTileAt(7, 0)));
//		assertTrue(tiles.contains(board.giveTileAt(8, 0)));
//	}
//	
//	@Test
//	public void testFindCandiesThatCrushWithHolesOnHoles() {
//		int[][] boardScheme = {
//				{ 1, 1, 1, 3, 4, 5, 6, 0, 1, 2 },
//				{ 1, 2, 1, 4, 5, 6, 0, 1, 2, 3 },
//				{ 1, 3, 1, 5, 6, 0, 1, 2, 3, 4 },
//				{ 1, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
//				{ 4, 5, 6, 1, 1, 1, 3, 1, 1, 1 },
//				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
//				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 5 },
//				{ 3, 3, 3, 4, 5, 6, 0, 1, 2, 5 },
//				{ 3, 3, 4, 5, 6, 0, 1, 2, 3, 5 },
//				{ 3, 4, 5, 6, 0, 1, 2, 5, 5, 5 }, 
//		};
//		
//		Board board = createBoard(boardScheme);
//		Map<Integer, Integer> holes = new HashMap<>();
//		holes.put(0, 2);
//		holes.put(4, 6);
//		holes.put(9, 7);
//		
//		CandiesRemovedHeuristic heurs = new CandiesRemovedHeuristic();
//		
//		Set<Tile> tiles = heurs.findCandiesThatCrushWithHoles(board, holes);
//		assertEquals(13, tiles.size());
//		assertTrue(tiles.contains(board.giveTileAt(2, 9)));
//		assertTrue(tiles.contains(board.giveTileAt(0, 2)));
//		assertTrue(tiles.contains(board.giveTileAt(0, 1)));
//		assertTrue(tiles.contains(board.giveTileAt(0, 0)));
//		assertTrue(tiles.contains(board.giveTileAt(2, 8)));
//		assertTrue(tiles.contains(board.giveTileAt(2, 7)));
//		assertTrue(tiles.contains(board.giveTileAt(1, 2)));
//		assertTrue(tiles.contains(board.giveTileAt(2, 2)));
//		assertTrue(tiles.contains(board.giveTileAt(9, 2)));
//		assertTrue(tiles.contains(board.giveTileAt(9, 1)));
//		assertTrue(tiles.contains(board.giveTileAt(9, 0)));
//		assertTrue(tiles.contains(board.giveTileAt(7, 0)));
//		assertTrue(tiles.contains(board.giveTileAt(8, 0)));
//	}
//
//	@Test
//	public void testUpdateHolesWithNoPreviousHoles() {
//		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
//		
//		Map<Integer, Integer> holes = new HashMap<>();
//		
//		Set<Tile> removed = new HashSet<>();
//		
//		removed.add(noMoveBoard.giveTileAt(0, 1));
//		removed.add(noMoveBoard.giveTileAt(0, 2));
//		removed.add(noMoveBoard.giveTileAt(0, 3));
//		removed.add(noMoveBoard.giveTileAt(0, 4));
//		removed.add(noMoveBoard.giveTileAt(2, 4));
//		removed.add(noMoveBoard.giveTileAt(2, 5));
//		removed.add(noMoveBoard.giveTileAt(2, 6));
//		removed.add(noMoveBoard.giveTileAt(4, 7));
//		removed.add(noMoveBoard.giveTileAt(4, 8));
//		removed.add(noMoveBoard.giveTileAt(4, 9));
//		removed.add(noMoveBoard.giveTileAt(7, 7));
//		removed.add(noMoveBoard.giveTileAt(7, 8));
//		removed.add(noMoveBoard.giveTileAt(7, 9));
//		removed.add(noMoveBoard.giveTileAt(9, 2));
//		removed.add(noMoveBoard.giveTileAt(9, 3));
//		removed.add(noMoveBoard.giveTileAt(9, 4));
//		removed.add(noMoveBoard.giveTileAt(9, 5));
//		
//		heur.updateHoles(removed, holes);
//		
//		assertEquals(5, holes.keySet().size());
//		assertTrue(holes.keySet().contains(0));
//		assertTrue(holes.keySet().contains(2));
//		assertTrue(holes.keySet().contains(4));
//		assertTrue(holes.keySet().contains(7));
//		assertTrue(holes.keySet().contains(9));
//		
//		assertEquals(4, holes.get(0).intValue());
//		assertEquals(3, holes.get(2).intValue());
//		assertEquals(3, holes.get(4).intValue());
//		assertEquals(3, holes.get(7).intValue());
//		assertEquals(4, holes.get(9).intValue());
//	}
//	
//	@Test
//	public void testUpdateHolesWithPreviousHoles() {
//		CandiesRemovedHeuristic heur = new CandiesRemovedHeuristic();
//		
//		Map<Integer, Integer> holes = new HashMap<>();
//		holes.put(0, 2);
//		holes.put(4, 3);
//		holes.put(5, 4);
//		holes.put(6, 1);
//		
//		Set<Tile> removed = new HashSet<>();
//		
//		removed.add(noMoveBoard.giveTileAt(0, 1));
//		removed.add(noMoveBoard.giveTileAt(0, 2));
//		removed.add(noMoveBoard.giveTileAt(0, 3));
//		removed.add(noMoveBoard.giveTileAt(0, 4));
//		removed.add(noMoveBoard.giveTileAt(2, 4));
//		removed.add(noMoveBoard.giveTileAt(2, 5));
//		removed.add(noMoveBoard.giveTileAt(2, 6));
//		removed.add(noMoveBoard.giveTileAt(4, 7));
//		removed.add(noMoveBoard.giveTileAt(4, 8));
//		removed.add(noMoveBoard.giveTileAt(4, 9));
//		removed.add(noMoveBoard.giveTileAt(7, 7));
//		removed.add(noMoveBoard.giveTileAt(7, 8));
//		removed.add(noMoveBoard.giveTileAt(7, 9));
//		removed.add(noMoveBoard.giveTileAt(9, 2));
//		removed.add(noMoveBoard.giveTileAt(9, 3));
//		removed.add(noMoveBoard.giveTileAt(9, 4));
//		removed.add(noMoveBoard.giveTileAt(9, 5));
//		
//		heur.updateHoles(removed, holes);
//		
//		assertEquals(7, holes.keySet().size());
//		assertTrue(holes.keySet().contains(0));
//		assertTrue(holes.keySet().contains(2));
//		assertTrue(holes.keySet().contains(4));
//		assertTrue(holes.keySet().contains(5));
//		assertTrue(holes.keySet().contains(6));
//		assertTrue(holes.keySet().contains(7));
//		assertTrue(holes.keySet().contains(9));
//		
//		assertEquals(6, holes.get(0).intValue());
//		assertEquals(3, holes.get(2).intValue());
//		assertEquals(6, holes.get(4).intValue());
//		assertEquals(4, holes.get(5).intValue());
//		assertEquals(1, holes.get(6).intValue());
//		assertEquals(3, holes.get(7).intValue());
//		assertEquals(4, holes.get(9).intValue());
//	}
}
