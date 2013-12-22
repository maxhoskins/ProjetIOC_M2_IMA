/**
 * 
 */
package hoskins.viaud.poc.metaheuristic.msea;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;

import java.util.ArrayList;
import java.util.Random;

/**
 * Elite Pool structure
 * @author Maxim Hoskins & Quentin Viaud
 */
public class ElitePool {
	/** * List containing the solutions */
	private ArrayList<Solution> pool;
	/** * Index of the worst solution in the pool */
	private int worstSolution;
	/** * Index of the best solution in the pool */
	private int bestSolution;
	/** * Maximum size the pool may reach */
	private int maxSize;
	/** * Minimum symmetric distance any two solutions are allowed to have in the pool */
	private int minSymDiff = Instance.instance.getNo()/10;
	
	/**
	 * Elite Pool Constructor, initialization of all the variables
	 * @param maxSize Maximum size the pool may reach
	 */
	public ElitePool(int maxSize){
		this.pool = new ArrayList<Solution>();
		this.worstSolution = -1;
		this.bestSolution = -1;
		this.maxSize = maxSize;
	}
	
	
	/**
	 * Adds a solution to the pool if allowed and readjusts best and worst solution indices
	 * @param solution Solution to add to the pool
	 */
	public void addSolution(Solution solution){		
		boolean valid = true;
		
		// check difference with more profitable solution is at least minSymDiff
		for (int s=0; s<this.pool.size(); s++){
			if(this.pool.get(s).getOf() > solution.getOf())
				if(calculateSymmetricDifference(this.pool.get(s),solution) < minSymDiff)
					valid = false;
		}
		
		if(valid){
			if(this.pool.size() == maxSize){
				// Check solution is better than least profitable solution currently in the pool
				if(this.pool.get(worstSolution).getOf() < solution.getOf()){
					int difference = Integer.MAX_VALUE, closestDifference = Integer.MAX_VALUE;
					int closestDifferenceIndex=-1;
					// Find the solution in the pool that costs the same or less than the new solution it is most similar to
					for (int s=0; s<this.pool.size(); s++){
						if(this.pool.get(s).getOf() <= solution.getOf()){
							difference = calculateSymmetricDifference(this.pool.get(s),solution);
							if(difference < closestDifference){
								closestDifference = difference;
								closestDifferenceIndex = s;
							}
						}
					}		
					//Swap the solutions
					removeSolution(closestDifferenceIndex);
					this.pool.add(solution);
					// Update best and worst indices
					worstSolution = getWorstSolutionIndex();
					bestSolution = getBestSolutionIndex();	
					
				}
			}else{
				// if the pool is not full, then check difference with same cost or less profitable solutions is at least minSymDiff
				valid = true;
				for (int s=0; s<this.pool.size(); s++){
					if(this.pool.get(s).getOf() <= solution.getOf())
						if(calculateSymmetricDifference(this.pool.get(s),solution) < minSymDiff)
							valid = false;
				}
				
				if(valid){
					this.pool.add(solution);
					
					if(this.worstSolution == -1){// If this is the first solution being added to the pool
						worstSolution = 0;
						bestSolution = 0;
					}else{
						worstSolution = getWorstSolutionIndex();
						bestSolution = getBestSolutionIndex();			
					}
				}
			}
		}
	}
	
	
	/**
	 * Picks a solution in the pool with probability proportional to symmetric difference with "solution"
	 * @param solution Solution that will be combined with the returned solution
	 * @return The solution chosen 
	 */
	public Solution selectSolution(Solution solution){
		if(this.pool.isEmpty())
			return null;
		// calculate symmetric differences with each solution in the pool
		int [] symDifference = new int[this.pool.size()];
		int sumOfDifferences = 0;
		for (int s=0; s<this.pool.size(); s++){
			symDifference[s] = calculateSymmetricDifference(this.pool.get(s),solution);
			sumOfDifferences += symDifference[s];
		}
		
		// This happens when there is only 1 solution in the pool and it's symmetric difference is the same as the solution to
		// be compared with. In that case we return the first solution in the pool
		if(sumOfDifferences < 1)
			return this.pool.get(0);
			
		
		// Retrieve random value between 1 and sum of symmetric differences
		int randomValue = Instance.instance.getRandom().nextInt(sumOfDifferences)+1;
		
		// Go through table adding the differences until sum equal to or greater than random value
		int sumValue = 0, i=-1;
		while(sumValue < randomValue){
			i++;
			if(i == this.pool.size())
				throw new IllegalStateException("Index problem in ElitePool.selectSolution");
			sumValue += symDifference[i];
		}
		
		return this.pool.get(i);
	}
	
	/**
	 * Removes a solution from the pool and readjusts best and worst solution indices
	 * @param index index of solution to remove
	 */
	private void removeSolution(int index){
		this.pool.remove(index);
		worstSolution = getWorstSolutionIndex();
		bestSolution = getBestSolutionIndex();		
	}
	
	
	/**
	 * Finds the solution in the pool with the most profit
	 * @return The index of the solution in the pool
	 */
	private int getBestSolutionIndex(){
		double bestCost = -Double.MAX_VALUE;
		int tempBestSolution=-1;
		for(int i = 0; i<this.pool.size(); i++){
			if(this.pool.get(i).getOf() > bestCost){
				tempBestSolution = i;
				bestCost = this.pool.get(i).getOf();
			}
		}
		return tempBestSolution;
	}
	
	/**
	 * Finds the solution in the pool with the least profit
	 * @return The index of the solution in the pool
	 */
	private int getWorstSolutionIndex(){
		double worstCost = Double.MAX_VALUE;
		int tempWorstSolution=-1;
		for(int i = 0; i<this.pool.size(); i++){
			if(this.pool.get(i).getOf() < worstCost){
				tempWorstSolution = i;
				worstCost = this.pool.get(i).getOf();
			}
		}
		return tempWorstSolution;
	}
	
	/**
	 * check the symmetric difference between 2 solutions
	 * @param solution1 solution to compare with solution2
	 * @param solution2 solution to compare with solution1
	 * @return number of difference between solution1 & solution2
	 */
	private int calculateSymmetricDifference(Solution solution1, Solution solution2){
		int diff = 0;
		for(int i = 0; i < Instance.instance.getNo();i++)
			if(solution1.getS()[i] != solution2.getS()[i])
				diff++;
		return diff;
	}

	/**
	 * Pool list getter
	 * @return Pool List
	 */
	public ArrayList<Solution> getPool(){
		return this.pool;
	}

	/**
	 * Minimum symmetric difference getter
	 * @return Minimum symmetric difference
	 */
	public int getMinSymDiff() {
		return minSymDiff;
	}

	/**
	 * Pool maximum size getter
	 * @return Maximum size the pool may have
	 */
	public int getMaxSize(){
		return this.maxSize;
	}

	/**
	 * Best solution index getter
	 * @return The index of the best solution in the pool
	 */
	public int getBestSolution(){
		return this.bestSolution;
	}
	
	/**
	 * Best solution index setter
	 * @param bestSolution New index of the best solution in the pool
	 */
	public void setBestSolution(int bestSolution) {
		this.bestSolution = bestSolution;
	}
	
	/**
	 * Worst solution index getter
	 * @return The index of the worst solution in the pool
	 */
	public int getWorstSolution(){
		return this.worstSolution;
	}
	
	/**
	 * Worst solution index setter
	 * @param worstSolution New index of the worst solution in the pool
	 */
	public void setWorstSolution(int worstSolution) {
		this.worstSolution = worstSolution;
	}

}
