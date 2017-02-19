package tests.gr.auth.ee.dsproject.crush.heuristics;

import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test;

import gr.auth.ee.dsproject.crush.heuristics.HeuristicsMathModel;


public class HeuristicsMathModelTest {
	
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
			
			double average = 100.0 / scales.size();
			
			for (Integer scale : scales) {
				weights.put(scale, average);
			}
			
			return weights;
		}
	}	
	
	@Test
	public void testGetWeightScales() {
		HeuristicsMathModel model = new SimpleHeuristicsMathModel();
		Integer[] expected = {new Integer(0), new Integer(1), new Integer(2)};
		assertArrayEquals(model.getWeightScales(), expected);
	}
	
	@Test
	public void testProcessModelScalesWithOneScale() {
		HeuristicsMathModel model = new SimpleHeuristicsMathModel();
		
		HashSet<Integer> scales = new HashSet<Integer>();
		scales.add(SimpleHeuristicsMathModel.SCALE1);
		
		assertEquals(
				model.calculateWeights(scales).get(
						SimpleHeuristicsMathModel.SCALE1), 100.0, 0.1);
	}
	
	@Test
	public void testProcessModelScalesWithTwoScales() {
		HeuristicsMathModel model = new SimpleHeuristicsMathModel();
		
		HashSet<Integer> scales = new HashSet<Integer>();
		scales.add(SimpleHeuristicsMathModel.SCALE1);
		scales.add(SimpleHeuristicsMathModel.SCALE2);
		
		assertEquals(
				model.calculateWeights(scales).get(
						SimpleHeuristicsMathModel.SCALE1), 50.0, 0.1);
		
		assertEquals(
				model.calculateWeights(scales).get(
						SimpleHeuristicsMathModel.SCALE2), 50.0, 0.1);
	}
	
	@Test
	public void testProcessModelScalesWithThreeScales() {
		HeuristicsMathModel model = new SimpleHeuristicsMathModel();
		
		HashSet<Integer> scales = new HashSet<Integer>();
		scales.add(SimpleHeuristicsMathModel.SCALE1);
		scales.add(SimpleHeuristicsMathModel.SCALE2);
		scales.add(SimpleHeuristicsMathModel.SCALE3);
		
		assertEquals(
				model.calculateWeights(scales).get(
						SimpleHeuristicsMathModel.SCALE1), 33.3, 0.1);
		
		assertEquals(
				model.calculateWeights(scales).get(
						SimpleHeuristicsMathModel.SCALE2), 33.3, 0.1);
		
		assertEquals(
				model.calculateWeights(scales).get(
						SimpleHeuristicsMathModel.SCALE3), 33.3, 0.1);
	}
	
	@Test
	public void testProcessModelScalesException() {
		HeuristicsMathModel model = new SimpleHeuristicsMathModel();
		
		HashSet<Integer> scales = new HashSet<Integer>();
		scales.add(122); // invalid constant
		
		boolean threwException = false;
		
		try {
			model.calculateWeights(scales);
		} catch (HeuristicsMathModel.IllegalWeightRuntimeException e) {
			threwException = true;
		}
		
		assertTrue(threwException);
	}
	
	@Test 
	public void testIsValidWeightWithValidWeights() {
		HeuristicsMathModel model = new SimpleHeuristicsMathModel();
		
		assertTrue(model.isValidWeight(SimpleHeuristicsMathModel.SCALE1));
		assertTrue(model.isValidWeight(SimpleHeuristicsMathModel.SCALE2));
		assertTrue(model.isValidWeight(SimpleHeuristicsMathModel.SCALE3));
	}
	
	@Test 
	public void testIsValidWeightWithInvalidWeights() {
		HeuristicsMathModel model = new SimpleHeuristicsMathModel();
		
		assertFalse(model.isValidWeight(30));
		assertFalse(model.isValidWeight(-1));
		assertFalse(model.isValidWeight(125));		
	}
}
