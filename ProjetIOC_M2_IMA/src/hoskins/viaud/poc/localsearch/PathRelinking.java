/**
 * 
 */
package hoskins.viaud.poc.localsearch;

import java.util.ArrayList;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;


/**
 * Performs path relinking local search
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class PathRelinking implements LocalSearch {

	/** 
	 * Best of the two initial solutions 
	 */
	private Solution bestSol;
	/** 
	 * Destination solution (final solution on the path) 
	 */
	private Solution solToReach;
	/** 
	 * Initial solution (initial solution on the path) 
	 */
	private Solution solToBegin;
	/** 
	 * Best solution cost on the path 
	 */
	private double bestCost;
	/** 
	 * Symmetric difference between the solution and the final solution 
	 */
	private ArrayList<Integer> symDiff;
	/**
	 * temporary solution
	 */
	private Solution tempSol;

	/**
	 * Class constructor
	 */
	public PathRelinking(){
	}

	/**
	 * Apply the path-relinking method on the two solutions
	 * @param s1 First solution
	 * @param s2 Second solution
	 * @return bestSol, The best solution on the path between s1 and s2 included
	 */
	@Override
	public Solution performLocalSearch(Solution s1, Solution s2){

		String direction = "up";
		//Find which solution is the worst, which is the best and then the best cost
		if(direction.equals("up")){
			if(s1.getOf() > s2.getOf()){
				solToBegin = s1.clone();
				solToReach = s2.clone();
				bestSol = s1.clone();
				bestCost = s1.getOf();
			}else{
				solToBegin = s2.clone();
				solToReach = s1.clone();
				bestSol = s2.clone();
				bestCost = s2.getOf();
			}
		}else if(direction.equals("down")){
			if(s1.getOf() > s2.getOf()){
				solToBegin = s2.clone();
				solToReach = s1.clone();
				bestSol = s1.clone();
				bestCost = s1.getOf();
			}else{
				solToBegin = s1.clone();
				solToReach = s2.clone();
				bestSol = s2.clone();
				bestCost = s2.getOf();
			}
		}

		//Start from the worst solution (the most expensive cost)
		tempSol = solToBegin.clone();

		//Find the symetric difference between the 2 solutions in parameters
		symDiff = new ArrayList<Integer>();
		calculateSymDiff();

		int tempOp;
		double tempCost;  
		//Perform algorithm while the temp solution is not the same as the best solution
		while(!symDiff.isEmpty()){
			// change a part of solution
			tempOp = symDiff.get(0);		
			for(int i = 0; i < Instance.instance.getNe(); i++){
				tempSol.setXValue(i, tempOp, 0);
			}
			tempSol.setXValue(solToReach.getS()[tempOp], tempOp, 1);
			tempSol.buildRepresentation();
			
			// calculate new of
			tempSol.computeOvertime();
			tempCost = tempSol.calculateOF();

			// if the new solution IS FEASIBLE and more profitable then update best solution
			if(tempSol.isFeasible() && tempCost > bestCost){
				bestCost = tempCost;
				bestSol = tempSol.clone();
			}
			// remove updated difference
			symDiff.remove(0);
		}
		return bestSol;
	}

	/**
	 * Calculates the symmetric differences between the current and destination solution
	 */
	private void calculateSymDiff(){
		symDiff.clear();
		for(int i = 0; i < Instance.instance.getNo();i++)
			if(tempSol.getS()[i] != solToReach.getS()[i])
				symDiff.add(i);
	}




	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.localsearch.LocalSearch#performLocalSearch(hoskins.viaud.poc.structure.Solution)
	 */
	@Override
	public Solution performLocalSearch(Solution s) {
		// TODO Auto-generated method stub
		return null;
	}



}
