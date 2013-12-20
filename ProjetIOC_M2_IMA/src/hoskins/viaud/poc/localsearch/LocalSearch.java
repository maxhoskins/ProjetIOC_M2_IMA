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

}
