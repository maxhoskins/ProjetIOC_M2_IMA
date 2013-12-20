/**
 * 
 */
package sandbox;

import hoskins.viaud.poc.heuristic.Heuristic;
import hoskins.viaud.poc.metaheuristic.VNS;
import hoskins.viaud.poc.structure.Solution;
import hoskins.viaud.poc.utils.InstanceReader;

import java.io.File;
import java.io.IOException;

/**
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//Folder where instance files are located and parameters of the problem in hour
		String folderPath = "./instances";
		int l = 12; int s = 3; int c = 60;
		
		//Get all files to read
		File[] listOfFiles = new File(folderPath).listFiles();
		
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
			
		}
		
		System.out.println(counter+"/100 instances fail");
	}

}
