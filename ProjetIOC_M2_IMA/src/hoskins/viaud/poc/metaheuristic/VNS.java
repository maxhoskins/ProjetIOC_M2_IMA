/**
 * 
 */
package hoskins.viaud.poc.metaheuristic;

import hoskins.viaud.poc.localsearch.ChangeOperationLS;
import hoskins.viaud.poc.localsearch.ChangeOperationWithInfeasibilityLS;
import hoskins.viaud.poc.localsearch.SwapLS;
import hoskins.viaud.poc.localsearch.SwapNeighboringWithInfeasibilityLS;
import hoskins.viaud.poc.localsearch.SwapWithInfeasibilityLS;
import hoskins.viaud.poc.structure.Solution;

/**
 * Run a Variable Neighborhood Search (VNS) algorithm as meta-heuristic for the problem
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class VNS implements MetaHeuristic {

	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.metaheuristic.MetaHeuristic#performMetaHeuristic(hoskins.viaud.poc.structure.Solution)
	 */
	@Override
	public Solution performMetaHeuristic(Solution s, int nbIterations, Object ... params) {
		//Initialize iterations and local serach counters
		int i = 0, k, nbLSMethod = (int)params[0];
		
		//Start VNS algorithm
		do {
			//Set counter of local search methods to zero
			k = 0;
			
			//Try to find a better solution in different neighborhoods
			do{
				//Find a new solution in s neighborhood
				Solution s2 = chooseLocalSearchMethod(s,k);
				
				//Transform s2 in feasible solution
				Solution s3 = new SwapLS().performLocalSearch(s2);
				
				//Let s3 be the best solution if OF value of s3 is better than OF value of s, skip to next neighborhood otherwise
				if(s3.isFeasible() && s3.getOf() > s.getOf())
					s = s3.clone();
				else
					k++;
			}
			while(k != nbLSMethod);
			
			//Go to next iteration
			i++;
		}
		while(i > nbIterations);
		
		return s;
	}
	
	/**
	 * Choose local search method to apply on s depending on k value
	 * @param s a solution
	 * @param k value of the neighborhood
	 * @return a new solution (greater than or equal to s)
	 */
	private Solution chooseLocalSearchMethod(Solution s, int k){
		Solution sol = s;
		switch (k) {
			case 0 : {
				sol = new ChangeOperationWithInfeasibilityLS().performLocalSearch(s);
				break;
			}
			case 1 : {
				sol = new SwapNeighboringWithInfeasibilityLS().performLocalSearch(s);
				break;
			}
			case 2 : {
				sol = new SwapWithInfeasibilityLS().performLocalSearch(s);
				break;
			}
			default : 
				break;
		}
		return sol;
	}

}
