/**
 * 
 */
package hoskins.viaud.poc.bound;

import java.util.ArrayList;
import java.util.Collections;

import hoskins.viaud.poc.model.CGModel;
import hoskins.viaud.poc.model.PSModel;
import hoskins.viaud.poc.model.RPSModel;
import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;
import hoskins.viaud.poc.structure.SolutionColumn;

/**
 * Column generation algorithm
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
		int[][] matriceV = generateMatrix(s);

		//Create a table with profit value for each profile 
		double[] tableauProfit = generateProfit(matriceV);

		//Create a table with number of team assign to each selected profile
		int[] tableauEquipe = generateTeam();

		//Column generation algorithm
		do{		
			//Solve PS(V)
			result = new PSModel().solve(matriceV, tableauProfit, tableauEquipe);

			//Create pi vector by solving dual problem of PS(V)
			double[] pi = new RPSModel().solveDual(matriceV, tableauProfit, tableauEquipe);

			//Solve CG sub problem and keep best column found
			bestColumn = new SolutionColumn(null,-1);
			int bestTeam = -1;
			for(int e = 0; e < Instance.instance.getNe(); e++){
				for(int theta = 0; theta < Math.round(Instance.instance.getS()/60); theta++){
					SolutionColumn sc = new CGModel().solveGC(pi, e, theta);
					if(sc.getOf() > bestColumn.getOf()){
						bestColumn = sc;
						bestTeam = e;
					}
				}
			}			

			//Merge V and new column A
			matriceV = merge(matriceV,bestColumn.getA());

			//Update profit value and number of team for each profile
			tableauProfit = updateProfit(matriceV, tableauProfit, bestTeam);
			
			tableauEquipe = updateTeam(tableauEquipe, bestTeam);
		}
		while(bestColumn.getOf() < 0);

		System.out.print("-----------------------------------------------------------------\n");
		System.out.print("Column Generation Bound : "+Math.round(result*100.0)/100.0 +"\n");
		System.out.print("-----------------------------------------------------------------\n");

	}



	/**
	 * Create matrix from solution in parameter
	 * @param s solution
	 * @return matrix
	 */
	public static int[][] generateMatrix(Solution s){	
		int[][] matriceV = new int[Instance.instance.getNo()][Instance.instance.getNe()];
		for(int j = 0; j < Instance.instance.getNo(); j++)
			for(int i = 0; i < Instance.instance.getNe(); i++)
				if(s.getX()[i][j] == 1)
					matriceV[j][i] = 1;
				else
					matriceV[j][i] = 0;
		return matriceV;
	}

	/**
	 * Generate a table of which team perform which profile
	 * @return table of team
	 */
	public static int[] generateTeam(){
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
	public static double[] generateProfit(int[][] matriceV){
		double[] t = new double[matriceV[0].length];
		for(int i = 0; i < t.length; i++){
			double profit = 0.0;
			double nbH = 0.0;
			for(int j = 0; j < Instance.instance.getNo(); j++){
				if(matriceV[j][i] == 1){
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
	 * Merge matrix V and column A
	 * @param V, matrix
	 * @param A, new column
	 * @return merged matrix
	 */
	public static int[][] merge(int[][] V, int[] A){
		int[][] matriceV = new int[V.length][V[0].length+1];
		for(int i = 0; i < matriceV.length; i++)
			for(int j = 0; j < matriceV[0].length; j++)
				if(j == matriceV[0].length-1)
					matriceV[i][j] = A[i];
				else
					matriceV[i][j] = V[i][j];
		return matriceV;
	}

	/**
	 * Update profit
	 * @param matrix
	 * @param current profit table
	 * @param team new team to add
	 * @return tableauDeProfit
	 */
	public static double[] updateProfit(int[][] matriceV,double[] currentT, int team){
		double[] t = new double[matriceV[0].length];
		for(int i = 0; i < t.length; i++){
			if(i < currentT.length)
				t[i] = currentT[i];
			else{
				int profit = 0, nbH = 0;
				for(int j = 0; j < Instance.instance.getNo(); j++){
					if(matriceV[j][team] == 1){
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
	 * Update team
	 * @param tableauEquipe, team/profile association
	 * @param bestTeam newTeam
	 * @return updated tableauEquipe
	 */
	private int[] updateTeam(int[] tableauEquipe, int bestTeam){
		int[] t = new int[tableauEquipe.length + 1];
		
		for(int i = 0; i < tableauEquipe.length; i++)
			t[i] = tableauEquipe[i];
		
		t[tableauEquipe.length] = bestTeam;
		
		return t;
	}

	private int[][] refineMatriceV(int[][]matriceV){
		ArrayList<Integer> es = new ArrayList<Integer>(Collections.nCopies(matriceV[0].length, 0));

		ArrayList<Integer>t = null;
		for(int e = 0; e < Instance.instance.getNe(); e++){
			t = checkValidity(matriceV, e);
			for(int j : t){
				if(es.get(j) != 1)
					es.set(j, 1);
			}
		}

		int nbCol = 0;
		for(int n : es)
			nbCol += n;

		int[][] result = new int[Instance.instance.getNo()][nbCol];

		for(int i = 0; i < es.size(); i++){
			if(es.get(i) == 1){
				for(int j = 0; j < Instance.instance.getNo(); j++)
					result[j][i] = matriceV[j][i];
			}else{
				for(int j = 0; j < Instance.instance.getNo(); j++)
					result[j][i] = 0;
			}
		}
		return result;
	}

	/**
	 * Checks if at least one work profile can be affected to a team
	 * @param matriceV matrix of columns
	 * @param team id of team
	 * @return true if work profile can be performed by team, false otherwise
	 */
	private ArrayList<Integer> checkValidity(int[][] matriceV, int team){
		ArrayList<Integer> es = new ArrayList<Integer>();
		boolean result = true;
		int e = -1, o = -1;	
		while(++e < matriceV[0].length){
			result = true;
			while(++o < matriceV.length){
				if(matriceV[o][e] == 1)
					if(Instance.instance.getA()[team][o] == 0)
						result = false;
			}
			if(result)
				es.add(e);
		}
		return es;
	}

}
