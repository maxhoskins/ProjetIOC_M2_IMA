package hoskins.viaud.poc.structure;


/**
 * Represents a solution as a column for the column generation algorithm
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class SolutionColumn {

	/**
	 * Table representing operations completed in solution
	 */
	private int[] A;
	/**
	 * Profit of the column
	 */
	private double profit;
	
	/**
	 * Default constructor
	 * @param a Table representing operations completed in solution
	 * @param profit Profit of the column
	 */
	public SolutionColumn(int[] a, double profit) {
		super();
		A = a;
		this.profit = profit;
	}
	
	/**
	 * 
	 * @return Table representing operations completed in solution
	 */
	public int[] getA() {
		return A;
	}
	
	/**
	 * Update table representing operations completed in solution
	 * @param a new table representing operations completed in solution
	 */
	public void setA(int[] a) {
		A = a;
	}
	
	/**
	 * 
	 * @return Profit of the column
	 */
	public double getProfit() {
		return profit;
	}
	
	/**
	 * Update profit of the column
	 * @param profit new profit of the column
	 */
	public void setProfit(double profit) {
		this.profit = profit;
	}
}
