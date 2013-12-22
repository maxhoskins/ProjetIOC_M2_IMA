/**
 * 
 */
package hoskins.viaud.poc.metaheuristic;

import hoskins.viaud.poc.structure.Solution;

/**
 * Meta-heuristic interface
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public interface MetaHeuristic {
	
	/**
	 * Run a meta-heuristic with solution s as starting point
	 * @param s solution
	 * @param nbIterations number of iterations of the algorithm
	 * @return a new solution (greater than or equal to s)
	 */
	public abstract Solution performMetaHeuristic(Solution s, int nbIterations, Object ... params);

}
