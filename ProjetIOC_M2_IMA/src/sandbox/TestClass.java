/**
 * 
 */
package sandbox;

import hoskins.viaud.poc.heuristic.ConstructiveHeuristic;
import hoskins.viaud.poc.structure.Solution;

/**
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class TestClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Solve the problem with heuristic method
		Solution sol = ConstructiveHeuristic.performHeuristic();
		System.out.print("Heuristic => Feasibility : "+ sol.isFeasible()+ ", OF : "+sol.getOf()+"\n");
	}

}
