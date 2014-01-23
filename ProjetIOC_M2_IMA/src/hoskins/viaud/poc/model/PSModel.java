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
 * Run the PS model with Cplex
 * Maximize the profit by affecting each operation to one profile only and force a team to perform only one profile
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class PSModel extends AbstractModel {

	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.model.AbstractModel#solve()
	 */
	@Override
	public double solve(int[][] matrix, double[] profitTable, int[] teamTable) {
		//Run the Cplex solver
		try {
			//Create the IloCplex model
			IloCplex cplex = new IloCplex();

			//Decision variable - x_p
			IloNumVar[] x = new IloNumVar[matrix[0].length];
			for(int p = 0; p < x.length; p++)
				x[p] = cplex.numVar(0,Double.MAX_VALUE,"x_"+p);

			//Create objective function
			IloLinearNumExpr of = cplex.linearNumExpr();

			//SUM[w_p.x_p]
			for(int p = 0; p < x.length; p++)
				of.addTerm(profitTable[p], x[p]);

			cplex.addMaximize(of);

			//Constraint - a_jp.x_ij = 1
			for(int j = 0; j < Instance.instance.getNo(); j++){
				IloLinearNumExpr sumP = cplex.linearNumExpr();
				for(int p = 0; p < x.length; p++){
					sumP.addTerm(matrix[j][p], x[p]);
				}
				cplex.addEq(sumP, 1, "C1_"+j);
			}

			//Constraint - x_p <= 1
			for(int i = 0; i < Instance.instance.getNe(); i++){
				IloLinearNumExpr sumP = cplex.linearNumExpr();
				for(int p = 0; p < x.length; p++){
					if(teamTable[p] == i)
						sumP.addTerm(1, x[p]);
				}
				cplex.addLe(sumP,1, "C2_"+i);
			}

			//Solve the model
			cplex.setOut(null);
			double result = 0.0;
			if (cplex.solve()){
				
				//System.out.println("O.F. (Column Generation Primal model) = " + cplex.getObjValue());
				//System.out.println("CPU = " + cplex.getCplexTime());

				result = Math.round(cplex.getObjValue()*100.0)/100.0;					
			}
			cplex.end();

			return result;

		} catch (IloException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public SolutionColumn solveGC(double[] pi, int team, int theta) {
		return null;
	}

	@Override
	public double[] solveDual(int[][] matrix, double[] profitTable, int[] teamTable) {
		return null;
	}

}
