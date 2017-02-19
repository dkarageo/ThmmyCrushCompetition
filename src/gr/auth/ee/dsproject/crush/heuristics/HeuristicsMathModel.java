/**
 * 
 */
package gr.auth.ee.dsproject.crush.heuristics;

import java.util.Map;
import java.util.Set;

/** 
 * HeuristicsMathModel abstract class provides a way to process a mathematical model
 * and get weight values out of it for predefined weight scales. It also takes cares 
 * of expanding the range of a scale when not all scales are used.
 * 
 * In order to use HeuristicsMathModel it should be subclassed and implement the
 * following:
 * 	1. Constants defining the possible scales the user can use.
 *  2. getWeightScales() method that returns the possible scales in the form of an
 *     array containing an Integer value for every one scale.
 *  3. processModelScales() method that takes a set containing all or some of the
 *     Integer values returned by getWeightScales() and provides the actual weight
 *     values for them.
 *  Actual values used by implementation for scales can differ from the integers
 *  used in getWeightScales() and calculateWeights() methods. Though it is required
 *  that these values get mapped to user accessible integer constants and also have 
 *  the same meaning across these constants and these two methods.  
 *  
 * Public methods defined in HeuristicsMathModel:
 * -public abstract Map<Integer, Double> calculateWeights(Set<Integer> weightScales)
 * -public abstract Integer[] getWeightScales()
 * -public boolean isValidWeight(Integer weight)
 * 
 * Protected methods defined in HeuristicsMathModel:
 * -protected abstract Map<Integer, Double> processModelScales(Set<Integer> weightScales);
 * 
 * Exceptions defined in HeuristicsMathModel:
 * -class IllegalWeightRuntimeException extends RuntimeException
 * 
 * @author Dimitrios Karageorgiou
 * @version 0.1
 */
public abstract class HeuristicsMathModel {
	
	// ==== Public Methods ====
	
	/**
	 * calculateWeights() returns the actual weight values for the given weight scales
	 * defined in math model. It also adjusts values appropriately when not all scales
	 * are used. 
	 * 
	 * weightScales takes a set of the weight scales actually used. 
	 * In case more scales are provided than actually used,
	 * user is in charge of expanding the returned weights in order to fill the space
	 * left by the unused weight.
	 *   
	 * It returns a map with given scales as keys and the actual weight for every scale
	 * as the values.
	 * 
	 * In case scale values that are not provided by the model are provided,
	 * an IllegalWeightRuntimeException is thrown.
	 */
	public Map<Integer, Double> calculateWeights(Set<Integer> weightScales) 
    throws IllegalWeightRuntimeException 
	{
		// Make sure that processing is gonna take place on scales actually exist.
		for (Integer scale : weightScales) {
			if (!isValidWeight(scale)) throw new IllegalWeightRuntimeException();
		}
		
		return processModelScales(weightScales);
	}
	
	/**
	 * Returns all the weight scales defined in math model.
	 * 
	 * These values should not be used for cross-instance purposes and
	 * no guarantee that weight scales are represented by the same value
	 * across different instances of the same model. For this case,
	 * weight scale constants provided by the model should be used. 
	 */
	public abstract Integer[] getWeightScales();
	
	/**
	 * isValidWeight() method checks whether the given weight is defined in
	 * this current math model.
	 * 
	 * Though it should be used carefully and always provide values in the same
	 * way model defines them.
	 */
	public boolean isValidWeight(Integer weight) {
		Integer[] weights = getWeightScales();
		
		boolean validWeight = false;
		
		for ( Integer i : weights ) {
			if ( i.equals(weight) ) { validWeight = true; }
		}
		
		return validWeight;
	}
	
	
	// ==== Protected Methods ====
	
	/**
	 * The main method of the model where process is going to take place. It
	 * gets a set of actually used scales and returns the weight values for
	 * every one of them.
	 * 
	 * weightScales takes a set of the weight scales actually used. 
	 * In case more scales are provided than actually used,
	 * user is in charge of expanding the returned weights in order to fill the space
	 * left by the unused weight.
	 *   
	 * It returns a map with given scales as keys and the actual weight for every scale
	 * as the values.
	 */
	protected abstract Map<Integer, Double> processModelScales(Set<Integer> weightScales);
	
	
	// ==== Public Inner Classes ====
	
	/**
	 * A non-checked exception that may be thrown when a not defined in
	 * the HeuristicMathModel weight is encountered, while a defined one
	 * is expected. 
	 */
	public class IllegalWeightRuntimeException extends RuntimeException {
		private static final long serialVersionUID = 1;		
	}	
}