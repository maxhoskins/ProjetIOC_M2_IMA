/**
 * 
 */
package hoskins.viaud.poc.localsearch;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;

/**
 * Perform a swap method as local search method (swap between all operations).<br />
 * The LS swaps two operations between each other while an improvement modification is available.
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class SwapLS implements LocalSearch {

	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.localsearch.LocalSearch#performLocalSearch(hoskins.viaud.poc.structure.Solution)
	 */
	@Override
	public Solution performLocalSearch(Solution s) {
		boolean noMoreImprovement = false;
		Solution bestSol = s.clone();
		Solution s2 = null;
		
		while(!noMoreImprovement){
			double bestImprovement = 0;
			for(int i = 0; i < s.getS().length - 1; i++)
				for(int j = i + 1; j < s.getS().length; j++)
					if(Instance.instance.getA()[s.getS()[j]][i] == 1 && Instance.instance.getA()[s.getS()[i]][j] == 1 && s.getS()[i] != s.getS()[j]){
						//Copy the current solution
						s2 = s.clone();

						//Change decision variables by swapping teams affected to operations
						s2.setXValue(s2.getS()[i], i, 0); s2.setXValue(s2.getS()[j], j, 0);
						s2.setXValue(s2.getS()[j], i, 1); s2.setXValue(s2.getS()[i], j, 1);
						s2.buildRepresentation();
						
						//Change overtime value in the new solution
						s2.computeOvertime();
						
						//Check if the new solution is feasible
						if(s2.isFeasible()){
							
							//Compute OF value for the current solution
							s2.calculateOF();
							
							//Set the best solution if gap between solutions is better than the best improvement found
							if(s2.getOf() - s.getOf() > bestImprovement){
								bestImprovement = s2.getOf() - s.getOf();
								bestSol = s2.clone();
							}
						}
					}
			
			//Stop the method if no improvement is found, set current solution has best found solution otherwise
			if(bestImprovement == 0)
				noMoreImprovement = true;
			else
				s = bestSol.clone();
		}
		
		return s;
	}
	
	@Override
	public Solution performLocalSearch(Solution s1, Solution s2) {
		// TODO Auto-generated method stub
		return null;
	}
}
