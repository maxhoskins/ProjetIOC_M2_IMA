/**
 * 
 */
package hoskins.viaud.poc.model;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.SolutionColumn;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * Solve the dual of PS model with CPLEX
 * Obtain the dual variable of the PS problem
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class RPSModel extends AbstractModel {

	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.model.AbstractModel#solve()
	 */
	@Override
	public double[] solveDual(int[][] matrix, double[] profitTable, int[] teamTable) {
		//Run the Cplex solver
		try {
			//Create the IloCplex model
			IloCplex cplex = new IloCplex();

			//Decision variable - mu_j
			IloNumVar[] mu = new IloNumVar[Instance.instance.getNo()];
			for(int j = 0; j < mu.length; j++)
				mu[j] = cplex.numVar(-Double.MAX_VALUE,Double.MAX_VALUE);

			//Decision variable - pi_e
			IloNumVar[] pi = new IloNumVar[Instance.instance.getNe()];
			for(int e = 0; e < pi.length; e++)
				pi[e] = cplex.numVar(0,Double.MAX_VALUE);

			//Create objective function
			IloLinearNumExpr of = cplex.linearNumExpr();

			//SUM[mu_j + pi_e]
			for(int j = 0; j < mu.length; j++)
				of.addTerm(1, mu[j]);
			for(int e = 0; e < pi.length; e++)
				of.addTerm(1, pi[e]);

			cplex.addMinimize(of);

			//Constraint - x_ij
			for(int p = 0; p < matrix[0].length; p++){
				IloLinearNumExpr sumP = cplex.linearNumExpr();
				for(int j = 0; j < mu.length; j++){
					sumP.addTerm(matrix[j][p], mu[j]);
				}
				for(int i = 0; i < pi.length; i++){
					if(teamTable[p] == i)
						sumP.addTerm(1.0, pi[i]);
				}
				cplex.addGe(sumP, profitTable[p]);
			}

			//Solve the model
			//First j values are the mu's and the rest are the pi's
			cplex.setOut(null);
			
			double[] result = new double[Instance.instance.getNo()+Instance.instance.getNe()];
			if (cplex.solve()){
				
				//System.out.println("O.F. (Column Generation Dual model) = " + cplex.getObjValue());
				//System.out.println("CPU = " + cplex.getCplexTime());
				
				for(int j = 0; j < Instance.instance.getNo(); j++)
					result[j] = cplex.getValue(mu[j]);
				
				for(int e = 0; e < Instance.instance.getNe(); e++)
					result[Instance.instance.getNo() + e] = cplex.getValue(pi[e]);
				
			}
			cplex.end();
			
			return result;

		} catch (IloException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public SolutionColumn solveGC(double[] pi, int team, int theta) {
		return null;
	}

	@Override
	public double solve(int[][] matrix, double[] profitTable, int[] teamTable) {
		return 0;
	}
}