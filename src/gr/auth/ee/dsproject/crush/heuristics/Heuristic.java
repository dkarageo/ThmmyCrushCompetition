/**
 * 
 */
package gr.auth.ee.dsproject.crush.heuristics;


/**
 * Heuristic abstract class provides a way to create discrete heuristics.
 * 
 * In order to be used it should be subclassed and implement the follow:
 * 	1. evaluate() method where all the processing should be done and return
 *     a value between 0 and range according to how well it scored.
 * 	2. probably switch the default range that is 100.0
 * 
 * Public constructors defined in Heuristic:
 * -public Heuristic()
 * -public Heuristic(double range)
 * 
 * Public methods defined in Heuristic:
 * -public void setRange(double range)
 * -public double getRange()
 * -public abstract double evaluate()
 * 
 * @author Dimitrios Karageorgiou
 * @version 0.1
 */
public abstract class Heuristic {
	// Instance Variables
	private double range;
	
	// Public Constructors
	public Heuristic() { this.range = 100.0; }
	public Heuristic(double range) { this.range = range; }
	
	// Public Setters
	public void setRange(double range) { this.range = range; }
	
	// Public Getters
	public double getRange() { return range; }
	
	
	// Public Methods
	
	/**
	 * The main method of heuristic where process is actually taking place.
	 * 
	 * It returns a double between 0 and range specified according to how
	 * well evaluated criteria scored.
	 */
	public abstract double evaluate();
}