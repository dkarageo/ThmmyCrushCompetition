package gr.auth.ee.dsproject.crush.heuristics;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;


/** 
 * SliderMathModel provides weights based on a balanced exponential model.
 * 
 * The model provides the following scales as constants:
 * 	VERY_LOW, LOW, MID, HIGH, VERY_HIGH
 * 
 * Given that  weight for VERY_LOW is 'vl', for LOW 'l' and so no
 * model is the following:
 * """
 * 	vl*e^(0*factor) + l*e^(1*factor) + m*e^(2*factor) + 
 *  h*e^(3*factor) + vh*e^(4*factor) = 1.0
 * """
 * Factor is a double that defines how fast the mode escalates and can
 * vary from 0 to double's max value. Though values over 3.0 would be
 * rarely in use, as it would practically eliminate completely the
 * lower scales and top ones will dominate.
 * 
 * 
 * Constants defined in SliderMathModel:
 * -public static final int VERY_LOW = 0;
 * -public static final int LOW = 1;
 * -public static final int MID = 2;
 * -public static final int HIGH = 3;
 * -public static final int VERY_HIGH = 4;
 * 
 * Public constructors defined in SliderMathModel:
 * -public SliderMathModel()
 * -public SliderMathModel(double factor)
 * 
 * Public Methods Defined in SliderMathModel:
 * -public void setFactor(double Factor)
 * -public double getFactor()
 * -public Integer[] getWeightScales()
 * 
 * Protected Method Defined in SliderMathModel:
 * -protected Map<Integer, Double> processModelScales(Set<Integer> weightScales)
 * 
 * @author Dimitrios Karageorgiou
 * @version 0.1
 */
public class SliderMathModel extends HeuristicsMathModel {
	
	// Constants for all scales this model supports.
	public static final int VERY_LOW = 0;
	public static final int LOW = 1;
	public static final int MID = 2;
	public static final int HIGH = 3;
	public static final int VERY_HIGH = 4;
	
	// Exponents used for every scale.
	private static final int[] EXPONENTS = {0, 1, 2, 3, 4, 5};
	
	// The factor every exponent is multiplied by. Higher factor means
	// bigger weight on higher scales and less weight on bottom scales.
	private double factor;
	
	
	// ==== Public Constructors ====
	public SliderMathModel() { factor = 2.0; }
	public SliderMathModel(double factor) { this.factor = factor; }
	
	
	// ==== Public Setters ====
	public void setFactor(double factor) { this.factor = factor; }
	
	
	// ==== Public Getters ====
	public double getFactor() { return factor; }
	
	
	// ==== Public Methods ====
	
	/**
	 * Returns all the possible scales values defined in this model
	 * as an Integer array.
	 */
	@Override
	public Integer[] getWeightScales() {
		Integer[] scales = {VERY_LOW, LOW, MID, HIGH, VERY_HIGH};
		return scales;
	}
	
	@Override
	protected Map<Integer, Double> processModelScales(Set<Integer> weightScales) {
		HashMap<Integer, Double> weights = new HashMap<Integer, Double>();
		
		// Create the denominator of the equation here that consists of the
		// sum of all exponentials exposed to exponent defined in EXPONENTS const 
		// array and multiplied by scaling factor.
		double denom = 0.0;
		for (Integer i : weightScales) {
			denom += Math.exp(EXPONENTS[i] * factor);
		}
		
		// Divide every one exponential value that belongs to a single scale, by
		// the denominator in order to find the actuall weight for this scale.
		for (Integer i : weightScales) {
			weights.put(i, Math.exp(EXPONENTS[i] * factor) / denom);
		}
			
		return weights;
	}
	
}