/**
 * 
 */
package hoskins.viaud.poc.model;

import java.util.ArrayList;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.SolutionColonne;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

/**
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class CGModel extends AbstractModel {

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
	public SolutionColonne solveGC(double[] pi, int team, int theta) {
		//Run the Cplex solver
		try {
			//Create the IloCplex model
			cplex = new IloCplex();

			//Decision variable - u_j
			IloIntVar[] u = new IloIntVar[Instance.instance.getNo()];
			for(int j = 0; j < u.length; j++)
				u[j] = cplex.intVar(0,Instance.instance.getA()[team][j]);
			
			//Fake decision variable - a
			IloIntVar a = cplex.intVar(1, 1);

			//Create objective function
			IloLinearNumExpr of = cplex.linearNumExpr();

			//SUM[c_je + mu_j] - pi_e - c * theta
			for(int j = 0; j < u.length; j++){
				of.addTerm(Instance.instance.getP()[team][j] - pi[j], u[j]);
			}
			of.addTerm(-(pi[Instance.instance.getNo() + team] + Instance.instance.getC() * theta), a);
			
			cplex.addMaximize(of);

			//Constraint - u_j
			IloLinearNumExpr sumU = cplex.linearNumExpr();
			for(int j = 0; j < u.length; j++){
				sumU.addTerm(Instance.instance.getT()[j], u[j]);
			}
			cplex.addLe(sumU, Instance.instance.getL() + theta);
			
			//Solve the model
			SolutionColonne sc = null;
			cplex.setOut(null);
			if (cplex.solve()){
				//System.out.println("O.F. (Column Generation) = " + cplex.getObjValue());
				//System.out.println("CPU = " + cplex.getCplexTime());
				
				int[] result = new int[Instance.instance.getNo()];
				for(int j = 0; j < Instance.instance.getNo(); j++)
					result[j] = (int) cplex.getValue(u[j]);
				
				sc = new SolutionColonne(result, cplex.getObjValue());
			}
			cplex.end();
			
			return sc;

		} catch (IloException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Checks if at least one work profile can be affected to a team
	 * @param matriceV matrix of columns
	 * @param team id of team
	 * @return true if work profile can be performed by team, false otherwise
	 */
	private boolean checkValidity(int[][] matriceV, int team){
		boolean result = true;
		int e = 0, o = 0;	
		while(e++ < matriceV[0].length && result){
			while(o++ < matriceV.length && result){
				if(matriceV[o][e] == 1)
					if(Instance.instance.getA()[team][o] == 0)
						result = false;
			}
		}
		return result;
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
	
	private int[] getOperations(int team){
		int sum = 0;
		for(int j = 0; j < Instance.instance.getNo(); j++)
			if(Instance.instance.getA()[team][j] == 1)
				sum ++;
		
		int[] result = new int[sum];
		int i = 0;
		for(int j = 0; j < Instance.instance.getNo(); j++)
			if(Instance.instance.getA()[team][j] == 1)
				result[i++] = j;
		
		return result;
	}

	@Override
	public double solve(int[][] matriceV, double[] profit) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] solveDual(int[][] matriceV, double[] profit) {
		// TODO Auto-generated method stub
		return null;
	}
}