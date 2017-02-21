package tests.gr.auth.ee.dsproject.crush.node;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.junit.Test;

import gr.auth.ee.dsproject.crush.board.*;
import gr.auth.ee.dsproject.crush.node84208535.*;
import gr.auth.ee.dsproject.crush.player.move.*;


public class NodeTest {
	
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
	
	@Test
	public void testNoArgumentsConstructor() {
		Node n = new Node();
		
		assertEquals(null, n.getParent());
		assertNotEquals(null, n.getChildren());
		assertEquals(0, n.getChildren().size());
		assertEquals(0, n.getNodeDepth());
		assertEquals(null, n.getNodeMove());
		assertEquals(0.0, n.getNodeEvaluation(), 0.001);
		assertEquals(null, n.getNodeBoard());
	}
	
	
	@Test
	public void testParentArgumentConstructor() {
		Node parent = new Node();
		
		Node n = new Node(parent);
		
		assertNotEquals(null, n.getChildren());
		assertEquals(0, n.getChildren().size());
		assertEquals(0, n.getNodeDepth());
		assertEquals(null, n.getNodeMove());
		assertEquals(0.0, n.getNodeEvaluation(), 0.001);
		assertEquals(null, n.getNodeBoard());

		assertEquals(parent, n.getParent());
	}
	
	@Test
	public void testParentAndBoardArgumentsConstructor() {
		Node parent = new Node();
		Board board = new Board(10);
		
		Node n = new Node(parent, board);
		
		assertNotEquals(null, n.getChildren());
		assertEquals(0, n.getChildren().size());
		assertEquals(0, n.getNodeDepth());
		assertEquals(null, n.getNodeMove());
		assertEquals(0.0, n.getNodeEvaluation(), 0.001);
		
		assertEquals(parent, n.getParent());
		assertEquals(board, n.getNodeBoard());
	}
	
	@Test
	public void testParentBoardAndMoveArgumentsConstructor() {
		Node parent = new Node();
		Board board = new Board(10);
		PlayerMove move = new PlayerMove();
		
		Node n = new Node(parent, board, move);
		
		assertNotEquals(null, n.getChildren());
		assertEquals(0, n.getChildren().size());
		assertEquals(0, n.getNodeDepth());
		assertEquals(0.0, n.getNodeEvaluation(), 0.001);
		
		assertEquals(parent, n.getParent());
		assertEquals(board, n.getNodeBoard());
		assertEquals(move, n.getNodeMove());
	}
	
	@Test
	public void testSetParent() {
		Node parent = new Node();
		Node n = new Node();
		n.setParent(parent);
		
		assertEquals(parent, n.getParent());
	}
	
	@Test
	public void testSetNodeBoard() {
		Board board = new Board(10);
		
		Node n = new Node();
		n.setNodeBoard(board);
		
		assertEquals(board, n.getNodeBoard());
	}
	
	@Test
	public void testSetChildren() {
		ArrayList<Node> children = new ArrayList<>();
		Node n = new Node();
		n.setChildren(children);
		
		assertEquals(children, n.getChildren());
	}
	
	@Test
	public void testSetNodeDepth() {
		Node n = new Node();
		n.setNodeDepth(2);
		
		assertEquals(2, n.getNodeDepth());
	}
	
	@Test
	public void testSetNodeMove() {
		PlayerMove move = new PlayerMove(new Tile(), new Tile());
		
		Node n = new Node();
		n.setNodeMove(move);
		
		assertEquals(move, n.getNodeMove());
	}
	
	@Test
	public void testSetNodeEvaluation() {
		Node n = new Node();
		n.setNodeEvaluation(38.265);
		
		assertEquals(38.265, n.getNodeEvaluation(), 0.001);
	}
	
	@Test
	public void testAddChildException() {
		Node n = new Node();
		boolean ex = false;
		
		try {
			n.addChild(null);
		} catch (Node.NullNodeRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testAddChildOnPreviouslyEmptyChildren() {
		Node n = new Node();
		
		Node child = new Node();
		n.addChild(child);
		
		assertNotEquals(null, n.getChildren());
		
		assertEquals(1, n.getChildren().size());
		assertTrue(n.getChildren().contains(child));
	}
	
	@Test
	public void testAddChildOnPreviouslyOneChild() {
		Node n = new Node();
		n.addChild(new Node());
		
		Node child = new Node();
		n.addChild(child);
		
		assertNotEquals(null, n.getChildren());
		
		assertEquals(2, n.getChildren().size());
		assertTrue(n.getChildren().contains(child));
	}
	
	@Test
	public void testLeadsToExtraTurnException() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 3, 5, 3, 5, 5, 2 },
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
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(5, 9), board.giveTileAt(6, 9));
		
		Node parent = new Node(null, board);
		
		Node n = new Node(parent, board); // Board object not to be used, so provide anything.
		n.setNodeMove(move);
		
		boolean ex = false;
		
		try {		
			n.leadsToExtraTurn();
		} catch (Node.NonEvaluatedNodeException e) {
			ex = true;
		}		
		
		assertTrue(ex);
	}
	
	@Test
	public void testLeadsToExtraTurn1() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 3, 5, 3, 5, 5, 2 },
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
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(5, 9), board.giveTileAt(6, 9));
		
		Node parent = new Node(null, board);
		
		Node n = new Node(parent, board); // Board object not to be used, so provide anything.
		n.setNodeMove(move);
		
		n.evaluate(false);
		
		assertTrue(n.leadsToExtraTurn());
	}
	
	@Test
	public void testLeadsToExtraTurn2() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 4, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 4, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 6, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 6, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(3, 5), board.giveTileAt(3, 6));
		
		Node parent = new Node(null, board);
		
		Node n = new Node(parent, board); // Board object not to be used, so provide anything.
		n.setNodeMove(move);
		
		n.evaluate(false);
		
		assertTrue(n.leadsToExtraTurn());
	}
	
	@Test
	public void testLeadsToExtraTurn3() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 3, 3, 4, 3 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 3, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 3, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(8, 6), board.giveTileAt(9, 6));
		
		Node parent = new Node(null, board);
		
		Node n = new Node(parent, board); // Board object not to be used, so provide anything.
		n.setNodeMove(move);
		
		n.evaluate(false);
		
		assertTrue(n.leadsToExtraTurn());
	}
	
	@Test
	public void testLeadsToExtraTurn4() {
		int[][] boardScheme = {
				{ 0, 1, 3, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 3, 6, 0, 1, 2, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(2, 7), board.giveTileAt(2, 6));
		
		Node parent = new Node(null, board);
		
		Node n = new Node(parent, board); // Board object not to be used, so provide anything.
		n.setNodeMove(move);
		
		n.evaluate(false);
		
		assertFalse(n.leadsToExtraTurn());
	}
	
	@Test
	public void testLeadsToExtraTurn5() {
		int[][] boardScheme = {
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 4, 4, 1, 4, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 4, 3, 4, 5 },
				{ 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 },
				{ 5, 6, 0, 1, 2, 3, 4, 5, 6, 0 },
				{ 0, 1, 2, 3, 4, 5, 6, 0, 1, 2 },
				{ 1, 2, 3, 4, 5, 6, 0, 1, 2, 3 },
				{ 2, 3, 4, 5, 6, 0, 1, 2, 3, 4 },
				{ 3, 4, 5, 6, 0, 1, 2, 3, 4, 5 }, 
		};
		
		Board board = createBoard(boardScheme);
		PlayerMove move = new PlayerMove(board.giveTileAt(6, 7), board.giveTileAt(7, 7));
		
		Node parent = new Node(null, board);
		
		Node n = new Node(parent, board); // Board object not to be used, so provide anything.
		n.setNodeMove(move);
		
		n.evaluate(false);
		
		assertFalse(n.leadsToExtraTurn());
	}
}