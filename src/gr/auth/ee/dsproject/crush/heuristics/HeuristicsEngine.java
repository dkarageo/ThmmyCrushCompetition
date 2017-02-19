/**
 * 
 */
package gr.auth.ee.dsproject.crush.heuristics;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;


/**  
 * HeuristicsEngine provides a way to run multiple heuristics and compose
 * an overall score out of them all. A mathematical model is used to
 * calculate the weight of partial scores in order to combine them
 * into the overall one.
 * 
 * Mathematical model should be a subclass of abstract class
 * HeuristicsMathModel and provides the possible scales of interest
 * that can be used and a numeric weight value that will be used in 
 * order to compose the overall score.
 * 
 * Heuristics should be a subclass of abstract Heuristic class and can be
 * added to the engine using add(Heuristic h, Integer weight) method.
 * Weights are provided by the math model itself and different
 * models may allow completely different scales. Thus, math model
 * SHOULD NOT be changed when heuristics of another model have been
 * previously added to the engine. In this case, a new engine object
 * should be used.
 * 
 * A common use can be considered the following one:
 * 	HeuristicsEngine engine = new HeuristicsEngine(new MyMathModel());
 *  engine.add(heur1, MyMathModel.High);
 *  engine.add(heur2, MyMathModel.Low);
 *  engine.add(heur3, MyMathModel.Medium);
 *  engine.add(heur3, MyMathModel.Low);
 *  double score = engine.evaluate();
 * 
 * @author Dimitrios Karageorgiou 
 * @version 0.1
 */
public class HeuristicsEngine {
	
    // ==== Instance Variables ====
	
	// The model to be used for calculating weights.
	private HeuristicsMathModel model;
	
	// weightedHeuristics contain every heuristic added to this engine instance,
	// grouped by their weights, as they are defined in model. Weights in this
	// case are also the keys to the HashMap that provides access to the
	// ArrayList containing all heuristics with the same weight. 
	private HashMap<Integer, ArrayList<Heuristic>> weightedHeuristics;
	
	
    // ==== Public Constructors ====
	
	public HeuristicsEngine() { 
		weightedHeuristics = new HashMap<Integer, ArrayList<Heuristic>>(); 
	}
	
	public HeuristicsEngine(HeuristicsMathModel model) {
		this();
		this.model = model;
	}
	
	public HeuristicsEngine(HeuristicsMathModel model, 
							HashMap<Integer, ArrayList<Heuristic>> weightedHeuristics) 
	{		
		this.model = model;
		this.weightedHeuristics = weightedHeuristics;
	}
	
	
	// ==== Setter Methods ====
	
	public void setModel(HeuristicsMathModel model) {
		this.model = model;
	}
	
	public void setWeightedHeuristics(HashMap<Integer, ArrayList<Heuristic>> weightedHeuristics) {
		this.weightedHeuristics = weightedHeuristics;
	}
	
	
	// ==== Getter Methods ====
	
	public HeuristicsMathModel getModel() { return model; }
	
	public HashMap<Integer, ArrayList<Heuristic>> getWeightedHeuristics() { 
		return weightedHeuristics; 
	}
	
	
	// ==== Public Methods ====
	
	/**
	 * Adds a new Heuristic to the engine whose importance is defined
	 * by weight parameter.
	 * 
	 * Appropriate values for weight are defined by HeuristicMathModel.
	 * 
	 * Weights are not checked whether they make sense in the model
	 * or not.
	 */
	public void add(Heuristic heur, Integer weight) {		
		ArrayList<Heuristic> heurList;
		
		// If no heuristics were previously added with current weight value,
		// the corresponding container for them (i.e. the corresponding ArrayList)
		// should be also created.
		if (!weightedHeuristics.containsKey(weight)) {
			heurList = new ArrayList<Heuristic>();  // create the container
			weightedHeuristics.put(weight, heurList);  // then map container to
													   // the weight it represents
		} else {
			heurList = weightedHeuristics.get(weight);
		}
		
		heurList.add(heur);
	}
	
	/**
	 * Evaluates all the heuristics given to the engine and composes the final
	 * score out of them all, using a HeuristicsMathModel to provide the weights
	 * for Heuristics of different importance groups.
	 * 
	 * The logic for the final value returned by evaluate() is to run
	 * every group of equally weighted heuristics and calculate the average value 
	 * of each one. Then all these partial values compose the overall one, 
	 * where each one is multiplied by its weight and added to the overall one. 
	 * Weight for every group is calculated by the provided HeuristicsMathModel.
	 * 
	 * Example: 
	 * 	A possible calculation where there are two groups (Group1 and Group2) of
	 *  different weighted heuristics, with Group1 containing three and Group2
	 *  containing two heuristics is the following:
	 *  	overall_weight = (heur1_1 + heur1_2 + heur1_3) / 3 * group1Weight + 
	 *					     (heur2_1 + heur2_2) / 2 * group2Weight  
	 */
	public double evaluate() {		
		double overall = 0;
		
		// Get from HeuristicsMathModel the weights that will be used in order to
		// compose the overall value out of every single value returned by heuristics.
		Map<Integer, Double> weights = model.calculateWeights(weightedHeuristics.keySet());
		
		for (Map.Entry<Integer, ArrayList<Heuristic>> heursGroup : weightedHeuristics.entrySet()) {
			double partialOverall = 0;
			
			ArrayList<Heuristic> heursList = heursGroup.getValue();
			  
			for (Heuristic h : heursList) {
				partialOverall += h.evaluate();
			}
			
			// Compose the overall value that is actually the sum of 
			// average values of every heuristic group, multiplied by
			// its weight. 
			// e.g. overallValue = (heur1_1 + heur1_2 + heur1_3) / 3 * group1Weight + 
			//					   (heur2_1 + heur2_2) / 2 * group2Weight + .... 
			overall += (partialOverall / heursList.size()) * weights.get(heursGroup.getKey());
		}
		
		return overall;
	}
}