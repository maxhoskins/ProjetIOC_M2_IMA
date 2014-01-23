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
	private final static String resultCG = "./resultsCG/";
	private final static String resultH = "./resultsH/";
	private final static String resultLagrange = "./resultsLagrange/";
	private final static String resultMSEA = "./resultsMSEA/";
	private final static String resultVNS = "./resultsVNS/";
	private final static String resultEM = "./resultsEM/";

	//Static parameters of the problem
	private final static int legalWorkingTime = 12;
	private final static int overtimeWorkingTime = 3;
	private final static int overtimeCost = 60;
//	private final static int overtimeCost = 1;
	
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
		
		System.out.print("Instance; Heuristique; VNS; MSEA; Sous-gradient; Génération de colonne; Solution exacte\n");
		
		for(File file : listOfFiles){
			
			//Instantiate Instance object associate to the current file
			InstanceReader.readFile(file,legalWorkingTime,overtimeWorkingTime,overtimeCost);
			
			//Solve the problem with heuristic method
			sol = ConstructiveHeuristic.performHeuristic();
			//System.out.print("Heuristic => Feasibility : "+ sol.isFeasible()+ ", OF : "+sol.getOf()+"\n");
			if(sol.isFeasible()){
				new SolutionWriter(sol, file.getName(), resultH);
				//System.out.println("Solution written");
			}
			
			System.out.print(file.getName()+";");
			if(sol.isFeasible())
				System.out.print(sol.getOf()+";");
			else
				System.out.print("X;");
			
			//Solve the problem with VNS algorithm
			sol = new VNS().performMetaHeuristic(sol, nbIterationsMH, nbLocalSearchMethod);
			//System.out.print("VNS => Feasibility : "+ sol.isFeasible()+ ", OF : "+sol.getOf()+"\n");
			if(sol.isFeasible()){
				new SolutionWriter(sol, file.getName(), resultVNS);
				//System.out.println("Solution written");
			}
			
			if(sol.isFeasible())
				System.out.print(sol.getOf()+";");
			else
				System.out.print("X;");
			
			//Solve the problem with MSEA algorithm
			sol = new MSEA().performMetaHeuristic(sol, nbIterationsMH, poolSizeMSEA);
			//System.out.print("MSEA => Feasibility : "+ sol.isFeasible()+ ", OF : "+sol.getOf()+"\n");
			if(sol.isFeasible()){
				new SolutionWriter(sol, file.getName(), resultMSEA);
				//System.out.println("Solution written");
			}
			
			if(sol.isFeasible())
				System.out.print(sol.getOf()+";");
			else
				System.out.print("X;");
				
			//Compute upper bound with sub gradient algorithm
			upperBoundValue = new SubGradient().computeBound(sol, 100);
			//System.out.print("Lagrange Upper Bound : " + upperBoundValue + "\n");
			if(sol.isFeasible()){
				new SolutionWriter(upperBoundValue, file.getName(), resultLagrange);
				//System.out.println("Solution written");
			}
			
			if(sol.isFeasible())
				System.out.print(upperBoundValue+";");
			else
				System.out.print("X;");

			//Compute upper bound with column generation algorithm
			/*
			int[][] x = {{1,0,0,1,0,0,0,0,0,1},{0,0,0,0,1,0,0,0,1,0},{0,0,1,0,0,0,1,1,0,0},{0,1,0,0,0,1,0,0,0,0}};
			int[] h = {3,0,0,0};
			sol = new Solution(x,h);
			*/
			upperBoundValue = new ColumnGeneration().computeBound(sol, 10);
			//System.out.print("Column Generation Bound : " + upperBoundValue + "\n");
			if(sol.isFeasible()){
				new SolutionWriter(upperBoundValue, file.getName(), resultCG);
				//System.out.println("Solution written");
			}
			
			if(sol.isFeasible())
				System.out.print(upperBoundValue+";");
			else
				System.out.print("X;");
			
			//Compute exact model with cplex
			upperBoundValue = new ExactModel().solve(null, null, null);
			//System.out.print("Exact model : " + upperBoundValue + "\n");
			if(sol.isFeasible()){
				new SolutionWriter(upperBoundValue, file.getName(), resultEM);
				//System.out.println("Solution written");
			}
			
			if(sol.isFeasible())
				System.out.print((int)upperBoundValue+"");
			else
				System.out.print("X");
			
			if(!sol.isFeasible())
				counter ++;
			
			System.out.println();

		}

		System.out.println(counter + "/100 instances fail");

	}
}
