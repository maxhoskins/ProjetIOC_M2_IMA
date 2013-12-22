/**
 * 
 */
package hoskins.viaud.poc.metaheuristic.msea;

import hoskins.viaud.poc.localsearch.LocalSearch;
import hoskins.viaud.poc.localsearch.PathRelinking;
import hoskins.viaud.poc.structure.Solution;



/**
 * Class that performs the post optimization
 * @author Maxim Hoskins & Quentin Viaud
 */
public class PostOptimization {

	/** * Pool at iteration i-1 */
	private ElitePool previousPool;
	/** * Pool at end of iteration i */
	private ElitePool newPool;
	
	/**
	 * Class constructor
	 */
	public PostOptimization(){
	}

	/**
	 * Main method to perform post optimization
	 * @param elitePool Initial pool to start optimizing on
	 * @return The best solution found between all the pools
	 */
	public Solution performPostOptimization(ElitePool elitePool){

		LocalSearch pr = new PathRelinking();
		previousPool = new ElitePool(elitePool.getMaxSize());
		newPool = new ElitePool(elitePool.getMaxSize());
		Solution result;
		boolean noMoreImprovement = false;
		
		updatePool(elitePool,"previous");

		while(!noMoreImprovement){
			for(int i=0; i<previousPool.getPool().size()-1; i++){
				for(int j=i+1; j<previousPool.getPool().size(); j++){
					result = pr.performLocalSearch(previousPool.getPool().get(i), previousPool.getPool().get(j));
					newPool.addSolution(result);
				}
			}
			if(newPool.getPool().isEmpty()){ 
				noMoreImprovement = true;
			}else if (newPool.getPool().get(newPool.getBestSolution()).getOf() <= previousPool.getPool().get(previousPool.getBestSolution()).getOf()){
				noMoreImprovement = true;
			}else{
				updatePool(newPool,"previous");
				updatePool(null,"new");
			}
		}

		if(newPool.getPool().isEmpty()){
			if(previousPool.getPool().get(previousPool.getBestSolution()).getOf() <= elitePool.getPool().get(elitePool.getBestSolution()).getOf()) 
				return elitePool.getPool().get(elitePool.getBestSolution());
			else
				return previousPool.getPool().get(previousPool.getBestSolution());
		}else if(newPool.getPool().get(newPool.getBestSolution()).getOf() <= elitePool.getPool().get(elitePool.getBestSolution()).getOf()){
			return elitePool.getPool().get(elitePool.getBestSolution());
		}else{
			return newPool.getPool().get(newPool.getBestSolution());
		}
	}
	
	/**
	 * Sets the new pool to become the previous pool
	 * @param pool The latest pool
	 * @param whichPool Which pool is to be updated: previous or new
	 */
	private void updatePool(ElitePool pool, String whichPool){
		if(whichPool.equals("previous")){
			if(pool.getBestSolution()==0 && pool.getWorstSolution()==2)
				System.out.println();
			previousPool.getPool().clear();
			previousPool.setBestSolution(-1);
			previousPool.setWorstSolution(-1);
			
			for(int i=0; i<pool.getPool().size(); i++){
				previousPool.getPool().add(pool.getPool().get(i));
			}
			previousPool.setBestSolution(pool.getBestSolution());
			previousPool.setWorstSolution(pool.getWorstSolution());

			
		}else if(whichPool.equals("new")){	
			newPool.getPool().clear();
			newPool.setBestSolution(-1);
			newPool.setWorstSolution(-1);
		}
				
	}
}
