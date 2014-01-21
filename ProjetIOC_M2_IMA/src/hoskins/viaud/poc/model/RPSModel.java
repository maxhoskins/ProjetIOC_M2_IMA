/**
 * 
 */
package hoskins.viaud.poc.model;

import javax.swing.text.html.HTMLDocument.Iterator;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.SolutionColonne;
import ilog.concert.IloException;
import ilog.concert.IloLPMatrix;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class RPSModel extends AbstractModel {

	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.model.AbstractModel#solve()
	 */
	@Override
	public void solve() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see hoskins.viaud.poc.model.AbstractModel#solve()
	 */
	@Override
	public double[] solveDual(int[][] matriceV, double[] profit) {
		//Run the Cplex solver
		try {
			//Create the IloCplex model
			cplex = new IloCplex();

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
			for(int p = 0; p < matriceV[0].length; p++){
				IloLinearNumExpr sumP = cplex.linearNumExpr();
				for(int j = 0; j < mu.length; j++){
					sumP.addTerm(matriceV[j][p], mu[j]);
				}
				for(int e = 0; e < pi.length; e++){
					if(checkValidity(matriceV, p, e))
						sumP.addTerm(1.0, pi[e]);
				}
				cplex.addGe(sumP, profit[p]);
			}

			//Solve the model
			//first j values are the mu's and the rest are the pi's
			double[] result = new double[Instance.instance.getNo()+Instance.instance.getNe()];
			if (cplex.solve()){
				System.out.println("O.F. (Column Generation Dual model) = " + cplex.getObjValue());
				System.out.println("CPU = " + cplex.getCplexTime());
				
				for(int j = 0; j < Instance.instance.getNo(); j++){
					result[j] = cplex.getValue(mu[j]);
				}
				for(int e = 0; e < Instance.instance.getNe(); e++){
					result[Instance.instance.getNo()+e] = cplex.getValue(pi[e]);
				}
			}
			cplex.end();
			
			return result;

		} catch (IloException e) {
			e.printStackTrace();
		}
		return null;
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
	public double solve(int[][] matriceV, double[] profit) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SolutionColonne solveGC(double[] pi, int team, int theta) {
		// TODO Auto-generated method stub
		return null;
	}
}