/**
 * 
 */
package hoskins.viaud.poc.model;

import hoskins.viaud.poc.structure.SolutionColumn;
import ilog.cplex.IloCplex;

/**
 * Abstract class for CPLEX model
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public abstract class AbstractModel {

	protected IloCplex cplex = null;
	
	/**
	 * Solve the profile affectation model used in column generation model (ie solve PS)
	 * @param matrix a matrix of profile/operation
	 * @param profitTable table of profit for each profile
	 * @param teamTable table of team who performed each profile
	 * @return
	 */
	public abstract double solve(int[][] matrix, double[] profitTable, int[] teamTable);

	/**
	 * Solve the dual problem of the profile affectation model used in column generation model (ie solve dual of PS)
	 * @param matrix a matrix of profile/operation
	 * @param profitTable table of profit for each profile
	 * @param teamTable table of team who performed each profile
	 * @return
	 */
	public abstract double[] solveDual(int[][] matrix, double[] profitTable, int[] teamTable);

	/**
	 * Solve the column generation model (ie find the best new column to add depending on parameters)
	 * @param pi vector of dual variables
	 * @param team team to consider
	 * @param theta number of working team allowed
	 * @return
	 */
	public abstract SolutionColumn solveGC(double[] pi, int team, int theta);
}
