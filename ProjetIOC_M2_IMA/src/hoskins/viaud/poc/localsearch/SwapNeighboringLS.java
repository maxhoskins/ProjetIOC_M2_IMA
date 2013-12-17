/**
 * 
 */
package hoskins.viaud.poc.localsearch;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;

/**
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class SwapNeighboringLS implements LocalSearch{

	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.localsearch.LocalSearch#performLocalSearch(hoskins.viaud.poc.structure.Solution)
	 */
	@Override
	public Solution performLocalSearch(Solution s) {
		boolean noMoreImprovement = false;
		Solution s2 = null, bestSol = null;

		while(!noMoreImprovement){
			double bestImprovement = 0;
			for(int i = 0; i < s.getS().length; i++){
				int j = i + 1;
				if(Instance.instance.getA()[s.getS()[j]][i] == 1 && Instance.instance.getA()[s.getS()[i]][j] == 1){
					s2 = s.clone();

					//Change decision variables
					s2.getX()[s2.getS()[i]][i] = 0; s2.getX()[s2.getS()[j]][j] = 0;
					s2.getX()[s2.getS()[j]][i] = 1; s2.getX()[s2.getS()[i]][j] = 1;

					//Change working time for each team
					s2.computeOvertime();

					if(s2.isFeasible()){
						s2.calculateOF();

						if(s2.getOf() - s.getOf() > bestImprovement){
							bestImprovement = s2.getOf() - s.getOf();
							bestSol = s2.clone();
						}
					}
				}
			}

			if(bestImprovement == 0)
				noMoreImprovement = true;
			else
				s = bestSol.clone();
		}

		return s;
	}
}