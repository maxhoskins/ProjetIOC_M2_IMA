/**
 * 
 */
package hoskins.viaud.poc.heuristic;

import java.util.Arrays;

import hoskins.viaud.poc.localsearch.ChangeOperationLS;
import hoskins.viaud.poc.localsearch.SwapLS;
import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;

/**
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class Heuristic {
	
	private static int[] o;
	
	/**
	 * Run heuristic method on problem instance
	 * @return solution solution to the problem
	 */
	public static Solution performHeuristic(){
		initialization();
		
		Solution s = construction();
		
		Solution s2 = new ChangeOperationLS().performLocalSearch(s);
		
		if(s2.getOf() > s.getOf())
			s = s2.clone();
		
		s2 = new SwapLS().performLocalSearch(s);
		
		if(s2.getOf() > s.getOf())
			s = s2.clone();
				
		return s;
	}
	
	/**
	 * Initialize the problem to be solved by heuristic method
	 */
	private static void initialization(){
		o = new int[Instance.instance.getNo()];
		
		for(int i = 0; i < Instance.instance.getNo(); i++){
			int sum = 0;
			for(int j = 0; j < Instance.instance.getNe(); j++)
				if(Instance.instance.getA()[j][i] == 1)
					sum++;
			o[i] = sum;
		}
		
		Arrays.sort(o);
	}
	
	/**
	 * Construct a solution
	 * @return solution solution to the problem
	 */
	private static Solution construction(){
		int[][] x = new int[Instance.instance.getNe()][Instance.instance.getNo()];
		
		//Instantiate a table which represents the working time for each team
		int[] t = new int[Instance.instance.getNe()];
		for(int i = 0; i < t.length; i++)
			t[i] = 0;
		
		//Construct a solution
		for(int i = 0; i < o.length; i++){
			int workingTime = Integer.MAX_VALUE;
			int chosenTeam = 0;
			for(int j = 0; j < Instance.instance.getNe(); j++)
				if(Instance.instance.getA()[j][i] == 1)
					if(t[j] + Instance.instance.getT()[o[i]] < workingTime){
						workingTime = t[j] + Instance.instance.getT()[o[i]];
						chosenTeam = j;
					}
			t[chosenTeam] = t[chosenTeam] + Instance.instance.getT()[o[i]];
			x[chosenTeam][i] = 1;
		}
		
		Solution s = new Solution(x,new int[Instance.instance.getNe()]);
		
		s.computeOvertime();
		
		s.calculateOF();
		
		return s;
	}

}
