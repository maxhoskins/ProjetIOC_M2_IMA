/**
 * 
 */
package hoskins.viaud.poc.localsearch;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;

/**
 * Perform a change operation method as local search. <br />
 * The LS modifies a solution by changing one and only one team affected an operation while an improvement is possible.
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class ChangeOperationLS implements LocalSearch {

	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.localsearch.LocalSearch#performLocalSearch(hoskins.viaud.poc.structure.Solution)
	 */
	@Override
	public Solution performLocalSearch(Solution s) {
		boolean noMoreImprovement = false;
		Solution s2 = null, bestSol = null;

		while(!noMoreImprovement){
			double bestImprovement = 0;
			for(int i = 0; i < s.getS().length; i++)
				for(int j = 0; j < Instance.instance.getNe(); j++)
					if(Instance.instance.getA()[j][i] == 1 && s.getS()[i] != j){
						//Copy the current solution
						s2 = s.clone();

						//Change decision variables by changing current team by another able to perform operation
						s2.setXValue(s2.getS()[i], i, 0);
						s2.setXValue(j, i, 1);
						s2.buildRepresentation();
						
						//Change overtime value in the new solution
						s2.computeOvertime();

						//Check if the new solution is feasible
						if(s2.isFeasible()){
							
							//Compute OF value for the new solution
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
		return null;
	}

}
