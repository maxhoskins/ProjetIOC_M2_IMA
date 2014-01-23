/**
 * 
 */
package hoskins.viaud.poc.model;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.SolutionColumn;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.cplex.IloCplex;

/**
 * Column generation model use in the column generation algorithm
 * The objective is to find the best new profile (ie new column) to add
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class CGModel extends AbstractModel {

	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.model.AbstractModel#solve()
	 */
	@Override
	public SolutionColumn solveGC(double[] pi, int team, int theta) {
		//Run the Cplex solver
		try {
			//Create the IloCplex model
			IloCplex cplex = new IloCplex();

			//Decision variable - u_j
			IloIntVar[] u = new IloIntVar[Instance.instance.getNo()];
			for(int j = 0; j < u.length; j++)
				u[j] = cplex.intVar(0,Instance.instance.getA()[team][j]);
			
			//Create objective function
			IloLinearNumExpr of = cplex.linearNumExpr();

			//SUM[c_je - mu_j] - pi_e - c * theta
			for(int j = 0; j < u.length; j++){
				of.addTerm(Instance.instance.getP()[team][j] - pi[j], u[j]);
			}
			of.setConstant(-pi[Instance.instance.getNo() + team] - Instance.instance.getC() * theta);
			
			cplex.addMaximize(of);

			//Constraint - u_j
			IloLinearNumExpr sumU = cplex.linearNumExpr();
			for(int j = 0; j < u.length; j++){
				sumU.addTerm(Instance.instance.getT()[j], u[j]);
			}
			cplex.addLe(sumU, Instance.instance.getL() + theta);
			
			//Solve the model
			SolutionColumn sc = null;
			
			cplex.setOut(null);
			
			if (cplex.solve()){
				//System.out.println("O.F. (Column Generation) = " + cplex.getObjValue());
				//System.out.println("CPU = " + cplex.getCplexTime());
				
				int[] result = new int[Instance.instance.getNo()];
				for(int j = 0; j < Instance.instance.getNo(); j++)
					result[j] = (int) cplex.getValue(u[j]);
				
				sc = new SolutionColumn(result, cplex.getObjValue());
			}
			cplex.end();
			
			return sc;

		} catch (IloException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public double solve(int[][] matrix, double[] profitTable, int[] teamTable) {
		return 0;
	}

	@Override
	public double[] solveDual(int[][] matrix, double[] profitTable, int[] teamTable) {
		return null;
	}
}