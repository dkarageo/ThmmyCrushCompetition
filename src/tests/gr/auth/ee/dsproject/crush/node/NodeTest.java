package tests.gr.auth.ee.dsproject.crush.node;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import gr.auth.ee.dsproject.crush.board.*;
import gr.auth.ee.dsproject.crush.node84208535.*;
import gr.auth.ee.dsproject.crush.player.move.*;


public class NodeTest {

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
}