/**
 * 
 */
package sandbox;

import hoskins.viaud.poc.heuristic.Heuristic;
import hoskins.viaud.poc.metaheuristic.VNS;
import hoskins.viaud.poc.structure.Solution;
import hoskins.viaud.poc.utils.InstanceReader;
import hoskins.viaud.poc.utils.SolutionWriter;

import java.io.File;

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
		String instanceFolderPath = "./instances"; String resultFolderPath = "./results";
		int l = 12; int s = 3; int c = 60;
		
		//Get all files to read
		File[] listOfFiles = new File(instanceFolderPath).listFiles();
		
		//Count number of fails
		int counter = 0;
		
		for(File file : listOfFiles){
			
			//Instantiate Instance object associate to the current file
			InstanceReader.readFile(file,l,s,c);
			
			//Solve the problem with heuristic method
			Solution sol = Heuristic.performHeuristic();
			
			//Solve the problem with VNS algorithm
			sol = new VNS().performMetaHeuristic(sol, 200);
			
			System.out.println("VNS : "+ sol.isFeasible()+ ","+sol.getOf());
			
			if(!sol.isFeasible())
				counter++;
			
			//Write the solution in a .csv file
			new SolutionWriter(sol, file.getName(), resultFolderPath);
			
		}
		
		System.out.println(counter+"/100 instances fail");
	}

}
