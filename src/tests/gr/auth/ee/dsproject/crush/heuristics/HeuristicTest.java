package tests.gr.auth.ee.dsproject.crush.heuristics;

import static org.junit.Assert.*;

import org.junit.Test;

import gr.auth.ee.dsproject.crush.heuristics.Heuristic;


public class HeuristicTest {

	class SimpleHeuristic extends Heuristic {
		public SimpleHeuristic() {
			super();
		}
		
		public SimpleHeuristic(double range) {
			super(range);
		}
		
		public double evaluate() {
			return 50.0;
		}
	}
	
	@Test
	public void testNoArgumentsConstructor() {
		Heuristic h = new SimpleHeuristic();
		assertEquals(h.getRange(), 100.0, 0.1);
	}
	
	@Test
	public void testRangeArgumentConstructor() {
		Heuristic h = new SimpleHeuristic(35.0);
		assertEquals(h.getRange(), 35.0, 0.1);
	}
	
	@Test
	public void testSetRange() {
		Heuristic h = new SimpleHeuristic();
		h.setRange(256.0);
		assertEquals(h.getRange(), 256.0, 0.1);
	}
	
	@Test
	public void testEvaluate() {
		Heuristic h = new SimpleHeuristic();
		assertEquals(h.evaluate(), 50.0, 0.1);
	}
}
