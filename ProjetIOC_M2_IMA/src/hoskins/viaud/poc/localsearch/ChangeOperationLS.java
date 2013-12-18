/**
 * 
 */
package hoskins.viaud.poc.localsearch;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;

/**
 * Perform a change operation method as local search
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
						s2.getX()[s2.getS()[i]][i] = 0;
						s2.getX()[j][i] = 1;

						//Change working time for each team
						s2.computeOvertime();

						//Check if the new solution is feasible
						if(s2.isFeasible()){
							
							//Compute OF value for the current solution
							s2.calculateOF();

							//Set the best solution if best improvement is surpassed
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

}
