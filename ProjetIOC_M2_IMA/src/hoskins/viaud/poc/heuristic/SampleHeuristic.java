/**
 * 
 */
package hoskins.viaud.poc.heuristic;

import java.util.ArrayList;

import hoskins.viaud.poc.localsearch.ChangeOperationLS;
import hoskins.viaud.poc.localsearch.SwapLS;
import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;

/**
 * Heuristic method using Sample GRASP methodology (selection of best improvement within sub group of possible improvements).<br />
 * Rather than selected the best improvement out of all possible improvements, it selects the best improvement out
 * of a subgroup of possible improvements.
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class SampleHeuristic {


	/**
	 * List of operations sorted by number of teams able to handle operation
	 */
	private static int[] o;

	/**
	 * Run heuristic method on problem instance
	 * @return solution solution to the problem
	 */
	public static Solution performHeuristic(){
		//Initialize the problem to be solved by heuristic method
		initialization();

		//Construct a solution
		Solution s = construction();

		//Try to improve solution by performing operation changes
		Solution s2 = new ChangeOperationLS().performLocalSearch(s);

		//Find the best solution
		if(s2.getOf() > s.getOf())
			s = s2.clone();

		//Try to improve solution by performing swap
		s2 = new SwapLS().performLocalSearch(s);

		//Find the best solution
		if(s2.getOf() > s.getOf())
			s = s2.clone();

		return s;
	}

	/**
	 * Initialize the problem to be solved by heuristic method
	 * (ie sort operations by increasing number of teams that can make it)
	 */
	private static void initialization(){
		ArrayList<Integer> temp = new ArrayList<Integer>();

		//Find the number of teams allowed to perform each operation
		for(int j = 0; j < Instance.instance.getNo(); j++){
			int sum = 0;
			for(int i = 0; i < Instance.instance.getNe(); i++)
				if(Instance.instance.getA()[i][j] == 1)
					sum++;
			temp.add(sum);
		}

		o = new int[Instance.instance.getNo()];

		//Sort the previous table by increasing number of teams
		for(int j = 0; j < Instance.instance.getNo(); j++){
			int nbTeam = Instance.instance.getNe() + 1, op = -1;
			for(int j2 = 0; j2 < temp.size(); j2++)
				if(temp.get(j2) < nbTeam){
					nbTeam = temp.get(j2);
					op = j2;
				}
			o[j] = op;
			temp.set(op, Instance.instance.getNe() + 1);
		}
	}

	/**
	 * Construct a solution
	 * @return solution solution to the problem
	 */
	private static Solution construction(){
		int nbPossibleChoices = (int)((double)Instance.instance.getNe()/7.0);
		int[][] x = new int[Instance.instance.getNe()][Instance.instance.getNo()];

		//Instantiate a table which represents the working time for each team
		int[] t = new int[Instance.instance.getNe()];
		for(int i = 0; i < t.length; i++)
			t[i] = 0;


		ArrayList<Integer> possibleChoices = new ArrayList<Integer>();
		/*
		 * Construct a solution
		 * 
		 * For each operation, the selected team will be the one with the minimum working time increase
		 */
		for(int i = 0; i < o.length; i++){
			int workingTime = Integer.MAX_VALUE;
			int chosenTeam = 0;
			possibleChoices.clear();


			// identify all possible choices
			for(int j = 0; j < Instance.instance.getNe(); j++){
				if(Instance.instance.getA()[j][o[i]] == 1){
					possibleChoices.add(j);
				}
			}
			// in case there are not as many possible choices...
			if(nbPossibleChoices > possibleChoices.size())
				nbPossibleChoices = possibleChoices.size();
			// select best team within subset of possible teams
			int tempTeam, tempIndex;
			for(int r = 0; r < nbPossibleChoices; r++){
				tempIndex = Instance.instance.getRandom().nextInt(possibleChoices.size());
				tempTeam = possibleChoices.get(tempIndex);
				if(t[tempTeam] + Instance.instance.getT()[o[i]] < workingTime){
					workingTime = t[tempTeam] + Instance.instance.getT()[o[i]];
					chosenTeam = tempTeam;
				}
				possibleChoices.remove(tempIndex);
			}


			//Apply change on the selected team (ie set x = 1)
			t[chosenTeam] = t[chosenTeam] + Instance.instance.getT()[o[i]];
			x[chosenTeam][o[i]] = 1;
		}

		// Create the solution
		Solution s = new Solution(x,new int[Instance.instance.getNe()]);

		return s;
	}

}
