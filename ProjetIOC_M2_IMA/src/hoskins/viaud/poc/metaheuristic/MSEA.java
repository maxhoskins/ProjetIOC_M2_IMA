/**
 * 
 */
package hoskins.viaud.poc.metaheuristic;

import hoskins.viaud.poc.heuristic.SampleHeuristic;
import hoskins.viaud.poc.localsearch.LocalSearch;
import hoskins.viaud.poc.localsearch.PathRelinking;
import hoskins.viaud.poc.metaheuristic.msea.ElitePool;
import hoskins.viaud.poc.metaheuristic.msea.PostOptimization;
import hoskins.viaud.poc.structure.Solution;

/**
 * Run a Multi-Start Evolutionnary Algorithm (MSEA) with Path-Relinking as meta-heuristic for the problem
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class MSEA implements MetaHeuristic {

	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.metaheuristic.MetaHeuristic#performMetaHeuristic(hoskins.viaud.poc.structure.Solution)
	 */
	@Override
	public Solution performMetaHeuristic(Solution s, int nbIterations, Object ... params) {
		return runMetaHeuristic(s, nbIterations, (int)params[0]);
	}
	
	public Solution runMetaHeuristic(Solution heuristicSol, int maxIte, int eliteSize){

		// Initialise necessary objects and variables
		ElitePool elitePool = new ElitePool(eliteSize);
		LocalSearch pathRelinking = new PathRelinking();
		PostOptimization postOptimization = new PostOptimization();

		Solution s1, s2;
		
		// add heuristic solution to pool
		if(heuristicSol.isFeasible())
			elitePool.addSolution(heuristicSol);
		/*System.out.println("------------------");
		System.out.println("Heuristic solution");
		System.out.println("------------------");
		heuristicSol.printSolution();*/

		
		/*
		 * for i = 1 to maxIte do
		 *	 S1 randomizedBuild() (includes LocalSearch);
		 *	 S2  select(elitePool,S);
		 *	 if (S2 != NULL) then
		 *	    S2  pathRelinking(S1,S2);
		 *		add(elitePool,S2);
		 *	 endif
		 *	 add(elitePool,S1);
		 * endfor
		 */
		for (int i = 0; i < maxIte; i++){
			s1 = SampleHeuristic.performHeuristic();	

			s2 = elitePool.selectSolution(s1);
			if(s2 != null){
				s2 = pathRelinking.performLocalSearch(s1, s2);
				elitePool.addSolution(s2);
			}

			elitePool.addSolution(s1);
		}

		// print best solution
		double bestOf = -Double.MAX_VALUE;
		Solution bestSol = null;
		for(int i = 0; i < elitePool.getPool().size(); i++){
			if(elitePool.getPool().get(i).getOf() > bestOf){
				bestOf = elitePool.getPool().get(i).getOf();
				bestSol = elitePool.getPool().get(i);
			}
		}
		/*System.out.println("--------------------------------------");
		System.out.println("Solution before MSEA post optimisation");
		System.out.println("--------------------------------------");
		bestSol.printSolution();*/

		
		// Do not run post optimization if the pool only has one solution
		if(elitePool.getPool().size()==1){
			s1 = elitePool.getPool().get(0);
			
			/*System.out.println("-------------------------------");
			System.out.println("Post optimisation not performed");
			System.out.println("-------------------------------");*/
		}else{
			s1 = postOptimization.performPostOptimization(elitePool);	
			
			/*System.out.println("-------------------------------------");
			System.out.println("Solution after MSEA post optimisation");
			System.out.println("-------------------------------------");
			s1.printSolution();*/
		}
		
		/*
		if(s1.getOf() > heuristicSol.getOf()){
			System.out.println("------------------------------------------------------");
			System.out.println("META IMPROVEMENT : "+(s1.getOf()-heuristicSol.getOf()));
			System.out.println("------------------------------------------------------");
		}
		*/
	
		return s1;

	}



}
