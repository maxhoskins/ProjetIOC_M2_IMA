/**
 * 
 */
package hoskins.viaud.poc.localsearch;

import hoskins.viaud.poc.structure.Solution;

/**
 * Local search method interface
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public interface LocalSearch {
	
	/**
	 * Perform a local search method with solution s as starting point
	 * @param s solution
	 * @return a new solution (greater than or equal to s)
	 */
	public abstract Solution performLocalSearch(Solution s);
	
	/**
	 * Perform a local search method with solution s1 and s2 as starting points
	 * @param s1 first solution
	 * @param s2 second solution
	 * @return a new solution (greater than or equal to s)
	 */
	public abstract Solution performLocalSearch(Solution s1, Solution s2);

}
