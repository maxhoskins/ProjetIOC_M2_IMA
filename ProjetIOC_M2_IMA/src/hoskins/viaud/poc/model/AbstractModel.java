/**
 * 
 */
package hoskins.viaud.poc.model;

import hoskins.viaud.poc.structure.SolutionColonne;
import ilog.cplex.IloCplex;

/**
 * Abstract class for CPLEX model
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public abstract class AbstractModel {

	protected IloCplex cplex = null;
	
	public abstract void solve();

	public abstract double solve(int[][] matriceV, double[] profit);
	
	public abstract double[] solveDual(int[][] matriceV, double[] profit);

	public abstract SolutionColonne solveGC(double[] pi, int team, int theta);

}
