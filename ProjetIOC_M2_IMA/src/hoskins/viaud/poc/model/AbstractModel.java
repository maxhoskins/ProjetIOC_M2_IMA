/**
 * 
 */
package hoskins.viaud.poc.model;

import ilog.cplex.IloCplex;

/**
 * Abstract class for CPLEX model
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public abstract class AbstractModel {

	protected IloCplex cplex = null;
	
	public abstract void solve();
}
