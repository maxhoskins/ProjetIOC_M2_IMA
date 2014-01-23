package hoskins.viaud.poc.model;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.SolutionColumn;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * Solve basic model
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class ExactModel extends AbstractModel {

	@Override
	public double solve(int[][] matrix, double[] profitTable, int[] teamTable) {
		double result = 0.0;
		
		//Run the Cplex solver
		try {
			
			IloCplex cplex = new IloCplex();

			//Decision variable - x_ij
			IloIntVar[][] x = new IloIntVar[Instance.instance.getNe()][Instance.instance.getNo()];
			for(int i = 0; i < Instance.instance.getNe(); i++)
				for(int j = 0; j < Instance.instance.getNo(); j++)
					x[i][j] = cplex.intVar(0,1);

			//Decision variable - h_i
			IloIntVar[] h = new IloIntVar[Instance.instance.getNe()];
			for(int i = 0; i < Instance.instance.getNe(); i++)
				h[i] = cplex.intVar(0,Instance.instance.getS()/60);

			//Create objective function
			IloLinearNumExpr of = cplex.linearNumExpr();

			//SUM[p_ij.x_ij] - SUM[c_i.h_i]
			for(int i = 0; i < Instance.instance.getNe(); i++){
				for(int j = 0; j < Instance.instance.getNo(); j++){
					of.addTerm(Instance.instance.getP()[i][j], x[i][j]);
				}
				of.addTerm(-Instance.instance.getC(), h[i]);
			}

			cplex.addMaximize(of);

			//Constraint - x_ij
			for(int j = 0; j < Instance.instance.getNo(); j++){
				IloLinearNumExpr sumXij = cplex.linearNumExpr();
				for(int i = 0; i < Instance.instance.getNe(); i++){
					sumXij.addTerm(1.0, x[i][j]);
				}
				cplex.addEq(sumXij, 1.0);
			}

			//Constraint - x_ij & h_i
			//Note : constraint is rewrite as follow : SUM[x_ij.t_j] - h_i <= l
			for(int i = 0; i < Instance.instance.getNe(); i++){
				IloLinearNumExpr sumXijTj = cplex.linearNumExpr();
				for(int j = 0; j < Instance.instance.getNo(); j++){
					sumXijTj.addTerm(Instance.instance.getT()[j], x[i][j]);
				}
				sumXijTj.addTerm(-60, h[i]);
				cplex.addLe(sumXijTj, Instance.instance.getL());
			}

			//Constraint - x_ij <= a_ij
			for(int i = 0; i < Instance.instance.getNe(); i++)
				for(int j = 0; j < Instance.instance.getNo(); j++)
					cplex.addLe(x[i][j],Instance.instance.getA()[i][j]);

			cplex.setOut(null);
			//Solve the model
			if (cplex.solve()){
				
				result = cplex.getObjValue();
				//System.out.println("O.F. (SPL) = " + cplex.getObjValue());
				//System.out.println("CPU = " + cplex.getCplexTime());
			}
			cplex.end();

		} catch (IloException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public double[] solveDual(int[][] matrix, double[] profitTable,int[] teamTable) {
		return null;
	}

	@Override
	public SolutionColumn solveGC(double[] pi, int team, int theta) {
		return null;
	}

}
