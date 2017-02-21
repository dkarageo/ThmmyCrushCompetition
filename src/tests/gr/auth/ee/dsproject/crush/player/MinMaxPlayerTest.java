package tests.gr.auth.ee.dsproject.crush.player;

import static org.junit.Assert.*;

import java.lang.reflect.*;

import org.junit.Before;
import org.junit.Test;

import gr.auth.ee.dsproject.crush.player.MinMaxPlayer;


public class MinMaxPlayerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDoFixedEvaluation() {
		MinMaxPlayer mPlayer = new MinMaxPlayer(0);
		
		Method doFixedEvaluation = null;
		
		try {
			doFixedEvaluation = mPlayer.getClass().getDeclaredMethod("doFixedEvaluation", int.class);
			doFixedEvaluation.setAccessible(true);
			
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		try {
			// Test 0 remaining depth.
			assertEquals(20.0, doFixedEvaluation.invoke(mPlayer, 0));
			
			// Test 1 remaining depth.
			assertEquals(20.0, doFixedEvaluation.invoke(mPlayer, 1));
			
			// Test 2 remaining depth.
			assertEquals(12.0, doFixedEvaluation.invoke(mPlayer, 2));
			
			// Test 3 remaining depth.
			assertEquals(12.0, doFixedEvaluation.invoke(mPlayer, 3));
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
