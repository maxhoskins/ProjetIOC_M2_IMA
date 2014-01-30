/**
 * 
 */
package hoskins.viaud.poc.main;

import hoskins.viaud.poc.bound.ColumnGeneration;
import hoskins.viaud.poc.bound.SubGradient;
import hoskins.viaud.poc.heuristic.ConstructiveHeuristic;
import hoskins.viaud.poc.metaheuristic.MSEA;
import hoskins.viaud.poc.metaheuristic.VNS;
import hoskins.viaud.poc.model.ExactModel;
import hoskins.viaud.poc.structure.Solution;
import hoskins.viaud.poc.utils.InstanceReader;
import hoskins.viaud.poc.utils.SolutionWriter;

import java.io.File;

/**
 * Main class that runs all algorithms and writes the solutions to file
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class RunAlgorithms {

	//Manage result informations
	private final static String instanceFolder = "./instances";
//	private final static String resultCG = "./resultsCG/";
	private final static String resultH = "./resultsH/";
	private final static String resultLagrange = "./resultsLagrange/";
	private final static String resultMSEA = "./resultsMSEA/";
	private final static String resultVNS = "./resultsVNS/";
	private final static String resultEM = "./resultsEM/";

	//Static parameters of the problem
	private final static int legalWorkingTime = 12;
	private final static int overtimeWorkingTime = 3;
	private final static int overtimeCost = 60;
	
	//Parameters for algorithms
	private final static int nbLocalSearchMethod = 3;
	private final static int poolSizeMSEA = 10;
	private final static int nbIterationsMH = 10;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Solution sol = null;
		double upperBoundValue = 0;
		
		//Get all files to read
		File[] listOfFiles = new File(instanceFolder).listFiles();

		//Count number of fails
		int counter = 0;
		
		for(File file : listOfFiles){
			
			//Instantiate Instance object associate to the current file
			InstanceReader.readFile(file,legalWorkingTime,overtimeWorkingTime,overtimeCost);
			
			//Solve the problem with heuristic method
			sol = ConstructiveHeuristic.performHeuristic();
			if(sol.isFeasible()){
				new SolutionWriter(sol, file.getName(), resultH);
			}
			
			//Solve the problem with VNS algorithm
			sol = new VNS().performMetaHeuristic(sol, nbIterationsMH, nbLocalSearchMethod);
			if(sol.isFeasible()){
				new SolutionWriter(sol, file.getName(), resultVNS);
			}
			
			//Solve the problem with MSEA algorithm
			sol = new MSEA().performMetaHeuristic(sol, nbIterationsMH, poolSizeMSEA);
			if(sol.isFeasible()){
				new SolutionWriter(sol, file.getName(), resultMSEA);
			}
			
			//Compute upper bound with sub gradient algorithm
			upperBoundValue = new SubGradient().computeBound(sol, 100);
			if(sol.isFeasible()){
				new SolutionWriter(upperBoundValue, file.getName(), resultLagrange);
			}
			
			//Compute upper bound with column generation algorithm
//			upperBoundValue = new ColumnGeneration().computeBound(sol, 10);
//			if(sol.isFeasible()){
//				new SolutionWriter(upperBoundValue, file.getName(), resultCG);
//			}
			
			//Compute exact model with cplex
//			upperBoundValue = new ExactModel().solve(null, null, null);
//			if(sol.isFeasible()){
//				new SolutionWriter(upperBoundValue, file.getName(), resultEM);
//			}

			//If no solution is found, this solution is infeasible
			if(!sol.isFeasible())
				counter ++;
			
		}

		System.out.println(counter + "/100 instances fail");

	}
}
