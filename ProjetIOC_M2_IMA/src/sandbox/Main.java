/**
 * 
 */
package sandbox;

import hoskins.viaud.poc.bound.ColumnGeneration;
import hoskins.viaud.poc.bound.SubGradient;
import hoskins.viaud.poc.heuristic.ConstructiveHeuristic;
import hoskins.viaud.poc.metaheuristic.MSEA;
import hoskins.viaud.poc.metaheuristic.VNS;
import hoskins.viaud.poc.model.AbruptModel;
import hoskins.viaud.poc.model.ContinuousModel;
import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;
import hoskins.viaud.poc.utils.InstanceReader;
import hoskins.viaud.poc.utils.SolutionWriter;

import java.io.File;
import java.io.IOException;

/**
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//Folder where instance files are located and parameters of the problem in hour
		String instanceFolderPath = "./instances"; String resultFolderPath = "./resultsMSEA";
		int l = 12; int s = 3; int c = 60;

		//runOneInstance(l, s, c, "./instances/I0O50E15R1.csv", resultFolderPath);
		runAllInstances(l, s, c, instanceFolderPath, resultFolderPath);
	}


	public static  void runOneInstance(int l, int s, int c, String instancePath, String resultFolderPath) throws Exception{
		//Instantiate Instance object associate to the current file
		File file = new File(instancePath);

		//Instantiate Instance object associate to the current file
		InstanceReader.readFile(file,l,s,c);

		//Solve the problem with heuristics & meta-heuristics
		Solution sol = solvingMethods();
		
		if(!sol.isFeasible())
			System.out.print("Solution failed <=> not feasible");

		//Write the solution in a .csv file
		new SolutionWriter(sol, file.getName(), resultFolderPath);
		
		new ColumnGeneration().computeBound(sol, 10);
		//upperBoundMethods(sol);

		System.out.println();
		
	}

	public static void runAllInstances(int l, int s, int c, String instanceFolderPath, String resultFolderPath) throws Exception{
		//Get all files to read
		File[] listOfFiles = new File(instanceFolderPath).listFiles();

		//Count number of fails
		int counter = 0;

		for(File file : listOfFiles){

			//Instantiate Instance object associate to the current file
			InstanceReader.readFile(file,l,s,c);

			//Solve the problem with heuristics & meta-heuristics
			Solution sol = solvingMethods();
			
			if(!sol.isFeasible()){
				System.out.print("Solution failed <=> not feasible");
				counter ++;
			}

			//Write the solution in a .csv file
			new SolutionWriter(sol, file.getName(), resultFolderPath);

			upperBoundMethods(sol);
			
			System.out.println();

		}

		System.out.println(counter+"/100 instances fail");
	}

	public static Solution solvingMethods(){
		
		//Solve the problem with heuristic method
		Solution sol = ConstructiveHeuristic.performHeuristic();
		System.out.print("Heuristic => Feasibility : "+ sol.isFeasible()+ ", OF : "+sol.getOf()+"\n");

		//Solve the problem with VNS algorithm
		sol = new VNS().performMetaHeuristic(sol, 50, 3);
		System.out.print("VNS => Feasibility : "+ sol.isFeasible()+ ", OF : "+sol.getOf()+"\n");

		//Solve the problem with MSEA algorithm
		sol = new MSEA().performMetaHeuristic(sol, 50, 10);
		System.out.print("MSEA => Feasibility : "+ sol.isFeasible()+ ", OF : "+sol.getOf()+"\n");

		return sol;
	}

	public static void upperBoundMethods(Solution sol){
		
		//Abrupt relaxation
		//new AbruptModel().solve();
		
		//Continuous relaxation
		//new ContinuousModel().solve();
		
		//Sub-gradient algorithm
		new SubGradient().computeBound(sol, 100);
		
		new ColumnGeneration().computeBound(sol, 10);
	}

}
