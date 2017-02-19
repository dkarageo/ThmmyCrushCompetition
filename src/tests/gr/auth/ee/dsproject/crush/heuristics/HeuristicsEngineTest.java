/**
 * 
 */
package tests.gr.auth.ee.dsproject.crush.heuristics;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import gr.auth.ee.dsproject.crush.heuristics.Heuristic;
import gr.auth.ee.dsproject.crush.heuristics.HeuristicsEngine;
import gr.auth.ee.dsproject.crush.heuristics.HeuristicsMathModel;


public class HeuristicsEngineTest {
	
	class SimpleHeuristic extends Heuristic {
		public SimpleHeuristic() {
			super();
		}
		
		public SimpleHeuristic(double range) {
			super(range);
		}
		
		public double evaluate() {
			return 100.0;
		}
	}
	
	/**
	 * A simple math model with three scale constants that gives equal
	 * weights to every scale.
	 * 
	 * Used just for testing the structure of the abstract class.
	 */
	class SimpleHeuristicsMathModel extends HeuristicsMathModel {
		public static final int SCALE1 = 0;
		public static final int SCALE2 = 1;
		public static final int SCALE3 = 2;
		
		public Integer[] getWeightScales() {
			Integer[] scales = {SCALE1, SCALE2, SCALE3};
			return scales;
		}
		
		public Map<Integer, Double> processModelScales(Set<Integer> scales) {
			HashMap<Integer, Double> weights = new HashMap<Integer, Double>();
			
			double average = 1.0 / scales.size();
			
			for (Integer scale : scales) {
				weights.put(scale, average);
			}
			
			return weights;
		}
	}
	
	@Test
	public void testNoArgumentsConstructor() {
		HeuristicsEngine engine = new HeuristicsEngine();
		assertNotNull(engine.getWeightedHeuristics());
	}
	
	@Test
	public void testModelArgumentConstructor() {
		SimpleHeuristicsMathModel model = new SimpleHeuristicsMathModel();
				
		HeuristicsEngine engine = new HeuristicsEngine(model);
		
		assertTrue(engine.getModel().equals(model));
	}
	
	@Test
	public void testModelAndHeuristicsArgumentConstructor() {
		// Create a new simple model.
		SimpleHeuristicsMathModel model = new SimpleHeuristicsMathModel();
		
		// Create a map of weighted heuristics.
		HashMap<Integer, ArrayList<Heuristic>> heurs = new HashMap<Integer, ArrayList<Heuristic>>();
		
		ArrayList<Heuristic> group1 = new ArrayList<Heuristic>();
		group1.add(new SimpleHeuristic());
		group1.add(new SimpleHeuristic());
		heurs.put(SimpleHeuristicsMathModel.SCALE1, group1);
		
		ArrayList<Heuristic> group2 = new ArrayList<Heuristic>();
		group2.add(new SimpleHeuristic());
		group2.add(new SimpleHeuristic());
		heurs.put(SimpleHeuristicsMathModel.SCALE2, group1);
		
		ArrayList<Heuristic> group3 = new ArrayList<Heuristic>();
		group3.add(new SimpleHeuristic());
		group3.add(new SimpleHeuristic());
		heurs.put(SimpleHeuristicsMathModel.SCALE3, group1);
		
		HeuristicsEngine engine = new HeuristicsEngine(model, heurs);
		
		assertTrue(engine.getModel().equals(model));
		assertTrue(engine.getWeightedHeuristics().equals(heurs));
	}
	
	@Test
	public void testSetModel() {
		SimpleHeuristicsMathModel model = new SimpleHeuristicsMathModel();
				
		HeuristicsEngine engine = new HeuristicsEngine();
		engine.setModel(model);
		
		assertTrue(engine.getModel().equals(model));
	}
	
	@Test
	public void testSetWeighterHeuristics() {		
		// Create a map of weighted heuristics.
		HashMap<Integer, ArrayList<Heuristic>> heurs = new HashMap<Integer, ArrayList<Heuristic>>();
		
		ArrayList<Heuristic> group1 = new ArrayList<Heuristic>();
		group1.add(new SimpleHeuristic());
		group1.add(new SimpleHeuristic());
		heurs.put(SimpleHeuristicsMathModel.SCALE1, group1);
		
		ArrayList<Heuristic> group2 = new ArrayList<Heuristic>();
		group2.add(new SimpleHeuristic());
		group2.add(new SimpleHeuristic());
		heurs.put(SimpleHeuristicsMathModel.SCALE2, group1);
		
		ArrayList<Heuristic> group3 = new ArrayList<Heuristic>();
		group3.add(new SimpleHeuristic());
		group3.add(new SimpleHeuristic());
		heurs.put(SimpleHeuristicsMathModel.SCALE3, group1);
		
		HeuristicsEngine engine = new HeuristicsEngine();
		engine.setWeightedHeuristics(heurs);
		
		assertTrue(engine.getWeightedHeuristics().equals(heurs));
	}
	
	@Test
	public void testAdd() {
		HeuristicsEngine engine = new HeuristicsEngine();
		
		SimpleHeuristic h1 = new SimpleHeuristic();
		SimpleHeuristic h2 = new SimpleHeuristic();
		SimpleHeuristic h3 = new SimpleHeuristic();
		SimpleHeuristic h4 = new SimpleHeuristic();
		SimpleHeuristic h5 = new SimpleHeuristic();
		
		engine.add(h1, SimpleHeuristicsMathModel.SCALE1);
		engine.add(h2, SimpleHeuristicsMathModel.SCALE1);
		engine.add(h3, SimpleHeuristicsMathModel.SCALE2);
		engine.add(h4, SimpleHeuristicsMathModel.SCALE3);
		engine.add(h5, SimpleHeuristicsMathModel.SCALE3);
		
		Map<Integer, ArrayList<Heuristic>> heurs = engine.getWeightedHeuristics();
		
		assertTrue(heurs.get(SimpleHeuristicsMathModel.SCALE1).contains(h1));
		assertTrue(heurs.get(SimpleHeuristicsMathModel.SCALE1).contains(h2));
		assertTrue(heurs.get(SimpleHeuristicsMathModel.SCALE2).contains(h3));
		assertTrue(heurs.get(SimpleHeuristicsMathModel.SCALE3).contains(h4));
		assertTrue(heurs.get(SimpleHeuristicsMathModel.SCALE3).contains(h5));
		
		assertEquals(heurs.get(SimpleHeuristicsMathModel.SCALE1).size(), 2);
		assertEquals(heurs.get(SimpleHeuristicsMathModel.SCALE2).size(), 1);
		assertEquals(heurs.get(SimpleHeuristicsMathModel.SCALE3).size(), 2);
	}
	
	@Test
	public void testEvaluateWithSimpleModelAndSimpleHeuristics() {
		HeuristicsEngine engine = new HeuristicsEngine(new SimpleHeuristicsMathModel());
		
		engine.add(new SimpleHeuristic(), SimpleHeuristicsMathModel.SCALE1);
		engine.add(new SimpleHeuristic(), SimpleHeuristicsMathModel.SCALE1);
		engine.add(new SimpleHeuristic(), SimpleHeuristicsMathModel.SCALE2);
		engine.add(new SimpleHeuristic(), SimpleHeuristicsMathModel.SCALE3);
		engine.add(new SimpleHeuristic(), SimpleHeuristicsMathModel.SCALE3);
		
		assertEquals(100.0, engine.evaluate(), 0.1);
	}
}
