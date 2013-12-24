/**
 * 
 */
package hoskins.viaud.poc.model;

import hoskins.viaud.poc.structure.Instance;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * Run the abrupt mixed integer linear program
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class AbruptModel implements CPLEXModel {

	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.model.CPLEXModel#solveModel()
	 */
	@Override
	public void solve() {
		//Run the Cplex solver
		try {
			//Create the IloCplex model
			IloCplex cplex = new IloCplex();

			//Decision variable - x_ij
			IloNumVar[][] x = new IloNumVar[Instance.instance.getNe()][Instance.instance.getNo()];
			for(int i = 0; i < Instance.instance.getNe(); i++)
				for(int j = 0; j < Instance.instance.getNo(); j++)
					x[i][j] = cplex.intVar(0,1);

			//Decision variable - h_i
			IloNumVar[] h = new IloNumVar[Instance.instance.getNe()];
			for(int i = 0; i < Instance.instance.getNe(); i++)
				h[i] = cplex.intVar(0,(int)Math.ceil(Instance.instance.getS()/60.0));


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

			//Constraint - x_ij <= a_ij
			for(int i = 0; i < Instance.instance.getNe(); i++)
				for(int j = 0; j < Instance.instance.getNo(); j++)
					cplex.addLe(x[i][j],Instance.instance.getA()[i][j]);

			//Solve the model
			if (cplex.solve()){
				System.out.println("O.F. (Abrupt model) = " + cplex.getObjValue());
				System.out.println("CPU = " + cplex.getCplexTime());
			}
			cplex.end();

		} catch (IloException e) {
			e.printStackTrace();
		}
	}

}