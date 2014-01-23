/**
 * 
 */
package hoskins.viaud.poc.bound;

import hoskins.viaud.poc.model.CGModel;
import hoskins.viaud.poc.model.PSModel;
import hoskins.viaud.poc.model.RPSModel;
import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;
import hoskins.viaud.poc.structure.SolutionColumn;

/**
 * Column generation algorithm
 * Start from a solution and create affectation profile for each team
 * Iterate through teams and overtime hours to find the best new profile for a team
 * Add the new profile and restart algorithm
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class ColumnGeneration implements IBound {

	/**
	 * Column generation algorithm
	 */
	public void computeBound(Solution s, int nbIterations) {
		//Instantiate best column to add and result found
		SolutionColumn bestColumn = null;
		double result = 0;

		//Create V Matrix from solution s
		int[][] matrix = generateMatrix(s);

		//Create a table with profit value for each profile 
		double[] profitTable = generateProfit(matrix);

		//Create a table with number of team assign to each selected profile
		int[] teamTable = generateTeam();

		//Column generation algorithm
		do{		
			//Solve PS(V)
			result = new PSModel().solve(matrix, profitTable, teamTable);

			//Create pi vector by solving dual problem of PS(V)
			double[] pi = new RPSModel().solveDual(matrix, profitTable, teamTable);

			//Solve CG sub problem and keep best column found
			bestColumn = new SolutionColumn(null,-1);
			int bestTeam = -1;
			for(int e = 0; e < Instance.instance.getNe(); e++){
				for(int theta = 0; theta < (int) Math.ceil(Instance.instance.getS()/60); theta++){
					SolutionColumn sc = new CGModel().solveGC(pi, e, theta);
					if(sc.getProfit() > bestColumn.getProfit()){
						bestColumn = sc;
						bestTeam = e;
					}
				}
			}			

			//Merge matrix and new column
			matrix = merge(matrix,bestColumn.getA());

			//Update profit value and number of team for each profile
			profitTable = updateProfit(matrix, profitTable, bestTeam);
			
			teamTable = updateTeam(teamTable, bestTeam);
		}
		while(bestColumn.getProfit() < 0);

		System.out.print("-----------------------------------------------------------------\n");
		System.out.print("Column Generation Bound : "+Math.round(result*100.0)/100.0 +"\n");
		System.out.print("-----------------------------------------------------------------\n");

	}

	/**
	 * Create matrix from solution in parameter
	 * @param s solution
	 * @return matrix
	 */
	private int[][] generateMatrix(Solution s){	
		int[][] matrix = new int[Instance.instance.getNo()][Instance.instance.getNe()];
		for(int j = 0; j < Instance.instance.getNo(); j++)
			for(int i = 0; i < Instance.instance.getNe(); i++)
				if(s.getX()[i][j] == 1)
					matrix[j][i] = 1;
				else
					matrix[j][i] = 0;
		return matrix;
	}

	/**
	 * Generate a table of which team perform which profile
	 * @return table of team
	 */
	private int[] generateTeam(){
		int[] t = new int[Instance.instance.getNe()];
		for(int i = 0; i < Instance.instance.getNe(); i++){
			t[i] = i;
		}
		return t;
	}

	/**
	 * Create a table of profit for each profile
	 * @param matrix
	 * @return table of profit
	 */
	private double[] generateProfit(int[][] matrix){
		double[] t = new double[matrix[0].length];
		for(int i = 0; i < t.length; i++){
			double profit = 0.0;
			double nbH = 0.0;
			for(int j = 0; j < Instance.instance.getNo(); j++){
				if(matrix[j][i] == 1){
					profit += Instance.instance.getP()[i][j];
					nbH += Instance.instance.getT()[j];
				}
			}
			profit -= Instance.instance.getC() * (Math.max(0.0, Math.ceil(nbH/60.0) - Instance.instance.getL()));
			t[i] = profit;		
		}
		return t;
	}

	/**
	 * Merge matrix V and new column A
	 * @param matrix
	 * @param new column
	 * @return merged matrix
	 */
	private int[][] merge(int[][] matrix, int[] a){
		int[][] mergedMatrix = new int[matrix.length][matrix[0].length+1];
		for(int i = 0; i < mergedMatrix.length; i++)
			for(int j = 0; j < mergedMatrix[0].length; j++)
				if(j == mergedMatrix[0].length-1)
					mergedMatrix[i][j] = a[i];
				else
					mergedMatrix[i][j] = matrix[i][j];
		return mergedMatrix;
	}

	/**
	 * Update profit table
	 * @param matrix
	 * @param currentProfitTable current profit table
	 * @param team new team to add
	 * @return updated profit table
	 */
	private double[] updateProfit(int[][] matrix,double[] currentProfitTable, int team){
		double[] t = new double[matrix[0].length];
		for(int i = 0; i < t.length; i++){
			if(i < currentProfitTable.length)
				t[i] = currentProfitTable[i];
			else{
				int profit = 0, nbH = 0;
				for(int j = 0; j < Instance.instance.getNo(); j++){
					if(matrix[j][team] == 1){
						profit += Instance.instance.getP()[team][j];
						nbH += Instance.instance.getT()[j];
					}
				}
				profit -= Instance.instance.getC() * (Math.max(0.0, Math.ceil(nbH/60.0) - Instance.instance.getL()));
				t[i] = profit;	
			}
		}
		return t;
	}
	
	/**
	 * Update team who performed each profile
	 * @param teamTable, team/profile association
	 * @param bestTeam newTeam
	 * @return updated teamTable
	 */
	private int[] updateTeam(int[] teamTable, int bestTeam){
		int[] t = new int[teamTable.length + 1];
		
		for(int i = 0; i < teamTable.length; i++)
			t[i] = teamTable[i];
		
		t[teamTable.length] = bestTeam;
		
		return t;
	}
}
