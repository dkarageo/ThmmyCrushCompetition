package tests.gr.auth.ee.dsproject.crush.heuristics;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

import gr.auth.ee.dsproject.crush.heuristics.SliderMathModel;


public class SliderMathModelTest {
		
	@Test
	public void testNoArgumentsConstructorForSettingFactorProperly() {
		SliderMathModel m = new SliderMathModel();
		assertEquals(2.0, m.getFactor(), 0.1);
	}
	
	@Test
	public void testFactorArgumentConstructorForSettingFactorProperly() {
		SliderMathModel m = new SliderMathModel(12.25);
		assertEquals(12.25, m.getFactor(), 0.01);
	}
	
	@Test
	public void testSetFactorForSettingFactorProperly() {
		SliderMathModel m = new SliderMathModel();
		m.setFactor(7.25);
		assertEquals(7.25, m.getFactor(), 0.01);
	}
	
	@Test
	public void testGetWeightScalesForReturningAllValidScales() {
		SliderMathModel m = new SliderMathModel();
		Integer[] returnedScales = m.getWeightScales();
		
		Integer[] actualScales = {
			SliderMathModel.VERY_LOW,
			SliderMathModel.LOW,
			SliderMathModel.MID,
			SliderMathModel.HIGH,
			SliderMathModel.VERY_HIGH,
		};
		
		boolean allFound = true;
		
		// Look for every actual scale in returned scales.
		for (int aScale : actualScales) {
			boolean found = false;
			
			for (int rScale : returnedScales) {
				if (aScale == rScale) found = true;
			}
			
			// Normally found should be always set to true, so allFound never change.
			allFound = allFound && found;
		}
		
		assertTrue(allFound);
	}
	
	@Test
	public void testCalculateWeightsWithOneScaleAndOneFactor() {
		SliderMathModel m = new SliderMathModel(1.0);
		
		HashSet<Integer> scales = new HashSet<Integer>();
		scales.add(SliderMathModel.MID);
		
		Map<Integer, Double> weights = m.calculateWeights(scales);
		
		// Weight when just one scale is given should be 1.0
		assertEquals(1.0, weights.get(SliderMathModel.MID), 0.01);
	}
	
	@Test
	public void testCalculateWeightsWithOneScaleAndFiveFactor() {
		SliderMathModel m = new SliderMathModel(5.0);
		
		HashSet<Integer> scales = new HashSet<Integer>();
		scales.add(SliderMathModel.MID);
		
		Map<Integer, Double> weights = m.calculateWeights(scales);
		
		// Weight when just one scale is given should be 1.0
		assertEquals(1.0, weights.get(SliderMathModel.MID), 0.01);
	}
	
	@Test
	public void testCalculateWeightsWithThreeScalesAndOneFactor() {
		SliderMathModel m = new SliderMathModel(1.0);
		
		HashSet<Integer> scales = new HashSet<Integer>();
		scales.add(SliderMathModel.MID);
		scales.add(SliderMathModel.VERY_LOW);
		scales.add(SliderMathModel.HIGH);
		
		Map<Integer, Double> weights = m.calculateWeights(scales);
		
		assertEquals(0.03511, weights.get(SliderMathModel.VERY_LOW), 0.01);
		assertEquals(0.25949, weights.get(SliderMathModel.MID), 0.01);
		assertEquals(0.70538, weights.get(SliderMathModel.HIGH), 0.01);
	}
	
	@Test
	public void testCalculateWeightsWithThreeScalesAndTwoFactor() {
		SliderMathModel m = new SliderMathModel(2.0);
		
		HashSet<Integer> scales = new HashSet<Integer>();
		scales.add(SliderMathModel.MID);
		scales.add(SliderMathModel.VERY_LOW);
		scales.add(SliderMathModel.HIGH);
		
		Map<Integer, Double> weights = m.calculateWeights(scales);
		
		assertEquals(0.00217, weights.get(SliderMathModel.VERY_LOW), 0.001);
		assertEquals(0.11894, weights.get(SliderMathModel.MID), 0.001);
		assertEquals(0.87887, weights.get(SliderMathModel.HIGH), 0.001);
	}
	
	@Test
	public void testCalculateWeightsWithAllScalesAndOneFactor() {
		SliderMathModel m = new SliderMathModel(1.0);
		
		HashSet<Integer> scales = new HashSet<Integer>();
		scales.add(SliderMathModel.MID);
		scales.add(SliderMathModel.VERY_LOW);
		scales.add(SliderMathModel.HIGH);
		scales.add(SliderMathModel.LOW);
		scales.add(SliderMathModel.VERY_HIGH);
		
		Map<Integer, Double> weights = m.calculateWeights(scales);
		
		assertEquals(0.01165, weights.get(SliderMathModel.VERY_LOW), 0.001);
		assertEquals(0.03168, weights.get(SliderMathModel.LOW), 0.001);
		assertEquals(0.08612, weights.get(SliderMathModel.MID), 0.001);
		assertEquals(0.23412, weights.get(SliderMathModel.HIGH), 0.001);
		assertEquals(0.63640, weights.get(SliderMathModel.VERY_HIGH), 0.001);
	}
	
	@Test
	public void testCalculateWeightsWithAllScalesAndHalfFactor() {
		SliderMathModel m = new SliderMathModel(0.5);
		
		HashSet<Integer> scales = new HashSet<Integer>();
		scales.add(SliderMathModel.MID);
		scales.add(SliderMathModel.VERY_LOW);
		scales.add(SliderMathModel.HIGH);
		scales.add(SliderMathModel.LOW);
		scales.add(SliderMathModel.VERY_HIGH);
		
		Map<Integer, Double> weights = m.calculateWeights(scales);
		
		assertEquals(0.05801, weights.get(SliderMathModel.VERY_LOW), 0.001);
		assertEquals(0.09564, weights.get(SliderMathModel.LOW), 0.001);
		assertEquals(0.15769, weights.get(SliderMathModel.MID), 0.001);
		assertEquals(0.25999, weights.get(SliderMathModel.HIGH), 0.001);
		assertEquals(0.42865, weights.get(SliderMathModel.VERY_HIGH), 0.001);
	}
}
