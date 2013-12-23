/**
 * 
 */
package sandbox;

import hoskins.viaud.poc.heuristic.Heuristic;
import hoskins.viaud.poc.metaheuristic.MSEA;
import hoskins.viaud.poc.metaheuristic.VNS;
import hoskins.viaud.poc.model.BasicModel;
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

		//runAllInstances(l, s, c, instanceFolderPath, resultFolderPath);
		runOneInstance(l, s, c, "./instances/I0O50E15R1.csv", resultFolderPath);
	}


	public static  void runOneInstance(int l, int s, int c, String instancePath, String resultFolderPath) throws Exception{
		//Instantiate Instance object associate to the current file
		File file = new File(instancePath);

		InstanceReader.readFile(file,l,s,c);

		//Solve the problem with heuristic method
		Solution sol = Heuristic.performHeuristic();

		//Solve the problem with meta algorithm
		//sol = new VNS().performMetaHeuristic(sol, 200);
		sol = new MSEA().performMetaHeuristic(sol, 200, 10);

		System.out.println("MetaHeuristic : "+ sol.isFeasible()+ ","+sol.getOf());

		if(!sol.isFeasible())
			System.out.println("Solution failed <=> not feasible");

		new BasicModel().solveModel();

		//Write the solution in a .csv file
		new SolutionWriter(sol, file.getName(), resultFolderPath);

	}

	public static void runAllInstances(int l, int s, int c, String instanceFolderPath, String resultFolderPath) throws Exception{
		//Get all files to read
		File[] listOfFiles = new File(instanceFolderPath).listFiles();

		//Count number of fails
		int counter = 0;

		for(File file : listOfFiles){

			//Instantiate Instance object associate to the current file
			InstanceReader.readFile(file,l,s,c);

			//Solve the problem with heuristic method
			Solution sol = Heuristic.performHeuristic();

			//Solve the problem with meta algorithm
			//sol = new VNS().performMetaHeuristic(sol, 200);
			sol = new MSEA().performMetaHeuristic(sol, 20, 10);

			System.out.println("MetaHeuristic : "+ sol.isFeasible()+ ","+sol.getOf());

			if(!sol.isFeasible())
				counter++;

			//Write the solution in a .csv file
			new SolutionWriter(sol, file.getName(), resultFolderPath);

		}

		System.out.println(counter+"/100 instances fail");
	}


}
