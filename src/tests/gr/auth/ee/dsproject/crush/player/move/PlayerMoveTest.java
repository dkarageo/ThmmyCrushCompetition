package tests.gr.auth.ee.dsproject.crush.player.move;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gr.auth.ee.dsproject.crush.board.CrushUtilities;
import gr.auth.ee.dsproject.crush.board.Tile;
import gr.auth.ee.dsproject.crush.player.move.PlayerMove;


public class PlayerMoveTest {
	
	private Tile tile1;
	private Tile tile2;
	
	@Before
	public void setUp() throws Exception {
		tile1 = new Tile();
		tile1.setX(1);
		tile1.setY(1);
		
		tile2 = new Tile();
		tile2.setX(2);
		tile2.setY(2);
	}

	@Test
	public void testGetTilesExceptionAndNoArgConstructor() {
		PlayerMove move = new PlayerMove();
		boolean ex = false;
		
		try {
			move.getTiles();
		} catch (PlayerMove.EmptyMoveRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testGetTilesAndTileArrayArgumentConstructor() {
		Tile[] tiles = { tile1, tile2 };
		PlayerMove move = new PlayerMove(tiles);
		boolean ex = false;
				
		try {
			assertTrue(move.getTiles()[0].equals(tile1));
			assertTrue(move.getTiles()[1].equals(tile2));
		} catch (PlayerMove.EmptyMoveRuntimeException e) {
			ex = true;
		}
		
		assertFalse(ex);
	}
	
	@Test
	public void testTileArrayArgumentConstructorForNullTileException() {
		// First null, second good
		Tile[] tiles = { null, tile2 };
		
		boolean ex = false;
				
		try {
			PlayerMove move = new PlayerMove(tiles);
		} catch (PlayerMove.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
		
		// First good, second null
		tiles[0] = tile1;
		tiles[1] = null;
		
		ex = false;
				
		try {
			PlayerMove move = new PlayerMove(tiles);
		} catch (PlayerMove.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
		
		// Both null
		tiles[1] = null;
		
		ex = false;
		
		try {
			PlayerMove move = new PlayerMove(tiles);
		} catch (PlayerMove.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testTileArrayArgumentConstructorForInvalidMoveException() {
		// Test less than two tiles.
		Tile[] tiles = { tile1 };
		
		boolean ex = false;
		
		try {
			PlayerMove move = new PlayerMove(tiles);
		} catch (PlayerMove.InvalidMoveRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
		
		
		// Test more than two tiles.
		tiles = new Tile[3];
		tiles[0] = tile1;
		tiles[1] = tile2;
		tiles[2] = tile1; // the same again is just fine
		
		ex = false;
		
		try {
			PlayerMove move = new PlayerMove(tiles);
		} catch (PlayerMove.InvalidMoveRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testTile1AndTile2ArgumentsConstructor() {
		PlayerMove move = new PlayerMove(tile1, tile2);
		
		assertTrue(move.getTiles()[0].equals(tile1));
		assertTrue(move.getTiles()[1].equals(tile2));
	}
	
	@Test
	public void testTile1AndTile2ArgumentConstructorNullTileException() {
		// First null, second good		
		boolean ex = false;
				
		try {
			PlayerMove move = new PlayerMove(null, tile2);
		} catch (PlayerMove.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
		
		// First good, second null		
		ex = false;
				
		try {
			PlayerMove move = new PlayerMove(tile1, null);
		} catch (PlayerMove.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
		
		// Both null		
		ex = false;
		
		try {
			PlayerMove move = new PlayerMove(null, null);
		} catch (PlayerMove.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testSetTilesWithTileArray() {
		PlayerMove move = new PlayerMove();
		Tile[] tiles = { tile1, tile2 }; 
		
		move.setTiles(tiles);
		
		assertTrue(move.getTiles()[0].equals(tile1));
		assertTrue(move.getTiles()[1].equals(tile2));
	}
	
	@Test
	public void testSetTilesWithTileArrayForNullTileException() {
		PlayerMove move = new PlayerMove();
		
		// First null, second good
		Tile[] tiles = { null, tile2 };
		
		boolean ex = false;
				
		try {
			move.setTiles(tiles);
		} catch (PlayerMove.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
		
		// First good, second null
		tiles[0] = tile1;
		tiles[1] = null;
		
		ex = false;
				
		try {
			move.setTiles(tiles);
		} catch (PlayerMove.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
		
		// Both null
		tiles[1] = null;
		
		ex = false;
		
		try {
			move.setTiles(tiles);
		} catch (PlayerMove.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testSetTilesWithTileArrayForInvalidMoveException() {
		PlayerMove move = new PlayerMove();
		
		// Test less than two tiles.
		Tile[] tiles = { tile1 };
		
		boolean ex = false;
		
		try {
			move.setTiles(tiles);
		} catch (PlayerMove.InvalidMoveRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
		
		
		// Test more than two tiles.
		tiles = new Tile[3];
		tiles[0] = tile1;
		tiles[1] = tile2;
		tiles[2] = tile1; // the same again is just fine
		
		ex = false;
		
		try {
			move.setTiles(tiles);
		} catch (PlayerMove.InvalidMoveRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	
	@Test
	public void testSetTilesWithSeparateTiles() {
		PlayerMove move = new PlayerMove();
		
		move.setTiles(tile1, tile2);
		
		assertTrue(move.getTiles()[0].equals(tile1));
		assertTrue(move.getTiles()[1].equals(tile2));
	}
	
	@Test
	public void testtestSetTilesWithSeparateTilesForNullTileException() {
		PlayerMove move = new PlayerMove();
		
		// First null, second good		
		boolean ex = false;
				
		try {
			move.setTiles(null, tile2);
		} catch (PlayerMove.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
		
		// First good, second null		
		ex = false;
				
		try {
			move.setTiles(null, tile2);
		} catch (PlayerMove.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
		
		// Both null		
		ex = false;
		
		try {
			move.setTiles(null, tile2);
		} catch (PlayerMove.NullTileRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testGetX1OnReadyMove() {
		PlayerMove move = new PlayerMove(tile1, tile2);
		
		assertEquals(tile1.getX(), move.getX1());
	}
	
	@Test
	public void testGetX2OnReadyMove() {
		PlayerMove move = new PlayerMove(tile1, tile2);
		
		assertEquals(tile2.getX(), move.getX2());
	}
	
	@Test
	public void testGetY1OnReadyMove() {
		PlayerMove move = new PlayerMove(tile1, tile2);
		
		assertEquals(tile1.getY(), move.getY1());
	}
	
	@Test
	public void testGetY2OnReadyMove() {
		PlayerMove move = new PlayerMove(tile1, tile2);
		
		assertEquals(tile2.getY(), move.getY2());
	}
	
	@Test
	public void testGetX1OnEmptyMove() {
		PlayerMove move = new PlayerMove();
		boolean ex = false;
		
		try {
			move.getX1();
		} catch (PlayerMove.EmptyMoveRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testGetX2OnEmptyMove() {
		PlayerMove move = new PlayerMove();
		boolean ex = false;
		
		try {
			move.getX2();
		} catch (PlayerMove.EmptyMoveRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testGetY1OnEmptyMove() {
		PlayerMove move = new PlayerMove();
		boolean ex = false;
		
		try {
			move.getY1();
		} catch (PlayerMove.EmptyMoveRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testGetY2OnEmptyMove() {
		PlayerMove move = new PlayerMove();
		boolean ex = false;
		
		try {
			move.getY2();
		} catch (PlayerMove.EmptyMoveRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testToCordsArray() {
		PlayerMove move = new PlayerMove(tile1, tile2);
		
		int[] expected = { tile1.getX(), tile1.getY(), tile2.getX(), tile2.getY() };
		
		assertArrayEquals(expected, move.toCordsArray());
	}
	
	@Test
	public void toCordsArrayOnEmptyMove() {
		PlayerMove move = new PlayerMove();
		
		boolean ex = false;
		
		try {
			move.toCordsArray();
		} catch (PlayerMove.EmptyMoveRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testToDirArraySameTilesException() {
		PlayerMove move = new PlayerMove(new Tile(0, 1, 1, 1, false), new Tile(1, 1, 1, 2, false));
		
		boolean ex = false;
		
		try {			
			move.toDirArray();
		} catch (PlayerMove.SameTilesRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testToDirArrayInvalidDirectionException() {
		PlayerMove move = new PlayerMove(new Tile(0, 1, 1, 1, false), new Tile(1, 2, 2, 2, false));
		
		boolean ex = false;
		
		try {			
			move.toDirArray();
		} catch (PlayerMove.InvalidDirectionRuntimeException e) {
			ex = true;
		}
		
		assertTrue(ex);
	}
	
	@Test
	public void testToDirArrayUpDirection() {
		PlayerMove move = new PlayerMove(new Tile(0, 1, 1, 1, false), new Tile(1, 1, 2, 2, false));
		
		int[] expected = {1, 1, CrushUtilities.UP};
		
		assertArrayEquals(expected, move.toDirArray());
	}
	
	@Test
	public void testToDirArrayDownDirection() {
		PlayerMove move = new PlayerMove(new Tile(0, 1, 1, 1, false), new Tile(1, 1, 0, 2, false));
		
		int[] expected = {1, 1, CrushUtilities.DOWN};
		
		assertArrayEquals(expected, move.toDirArray());
	}
	
	@Test
	public void testToDirArrayLeftDirection() {
		PlayerMove move = new PlayerMove(new Tile(0, 1, 1, 1, false), new Tile(1, 0, 1, 2, false));
		
		int[] expected = {1, 1, CrushUtilities.LEFT};
		
		assertArrayEquals(expected, move.toDirArray());
	}
	
	@Test
	public void testToDirArrayRightDirection() {
		PlayerMove move = new PlayerMove(new Tile(0, 1, 1, 1, false), new Tile(1, 2, 1, 2, false));
		
		int[] expected = {1, 1, CrushUtilities.RIGHT};
		
		assertArrayEquals(expected, move.toDirArray());
	}
}
