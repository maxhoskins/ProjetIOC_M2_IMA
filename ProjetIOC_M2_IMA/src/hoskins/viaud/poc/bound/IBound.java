/**
 * 
 */
package hoskins.viaud.poc.bound;

import hoskins.viaud.poc.structure.Solution;

/**
 * Interface to implement bound method
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public interface IBound {

	double computeBound(Solution s, int nbIterations);
	
}
