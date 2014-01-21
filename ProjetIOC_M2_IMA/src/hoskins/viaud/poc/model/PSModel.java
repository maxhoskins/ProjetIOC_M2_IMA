/**
 * 
 */
package hoskins.viaud.poc.model;

import java.util.Iterator;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.SolutionColonne;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLPMatrix;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class PSModel extends AbstractModel {

	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.model.AbstractModel#solve()
	 */
	@Override
	public double solve(int[][] matriceV, double[] profit) {
		//Run the Cplex solver
		try {
			//Create the IloCplex model
			cplex = new IloCplex();

			//Decision variable - x_p
			IloNumVar[] x = new IloNumVar[matriceV[0].length];
			for(int p = 0; p < x.length; p++)
				x[p] = cplex.numVar(0,Double.MAX_VALUE,"x_"+p);

			//Create objective function
			IloLinearNumExpr of = cplex.linearNumExpr();

			//SUM[w_p.x_p]
			for(int p = 0; p < x.length; p++)
				of.addTerm(profit[p], x[p]);

			cplex.addMaximize(of);

			//Constraint - a_jp.x_ij = 1
			for(int j = 0; j < Instance.instance.getNo(); j++){
				IloLinearNumExpr sumP = cplex.linearNumExpr();
				for(int p = 0; p < x.length; p++){
					sumP.addTerm(matriceV[j][p], x[p]);
				}
				cplex.addEq(sumP, 1, "C1_"+j);
			}

			//Constraint - x_p <= 1
			for(int i = 0; i < Instance.instance.getNe(); i++){
				IloLinearNumExpr sumP = cplex.linearNumExpr();
				for(int p = 0; p < x.length; p++){
					if(checkValidity(matriceV, p, i))
						sumP.addTerm(1, x[p]);
				}
				cplex.addLe(sumP,1, "C2_"+i);
			}

			//Solve the model
			cplex.exportModel("./tmp/debug.mps");
			double result = 0.0;
			if (cplex.solve()){
				
				System.out.println("O.F. (Column Generation Primal model) = " + cplex.getObjValue());
				System.out.println("CPU = " + cplex.getCplexTime());

				result = Math.round(cplex.getObjValue()*100.0)/100.0;					
			}
			cplex.end();

			return result;

		} catch (IloException e) {
			e.printStackTrace();
		}
		return 0;
	}



	/**
	 * Checks if a work profile can be affected to a team
	 * @param matriceV matrix of columns
	 * @param column id of profile
	 * @param team id of team
	 * @return true if work profile can be performed by team, false otherwise
	 */
	private boolean checkValidity(int[][] matriceV, int column, int team){
		boolean result = true;
		int o = -1;	
		while(++o < matriceV.length && result){
			if(matriceV[o][column] == 1)
				if(Instance.instance.getA()[team][o] == 0)
					result = false;
		}
		return result;
	}

	@Override
	public void solve() {
		// TODO Auto-generated method stub
	}

	@Override
	public double[] solveDual(int[][] matriceV, double[] profit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SolutionColonne solveGC(double[] pi, int team, int theta) {
		// TODO Auto-generated method stub
		return null;
	}

}
