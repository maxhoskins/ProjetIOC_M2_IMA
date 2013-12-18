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
	
	public abstract Solution performLocalSearch(Solution s);

}
