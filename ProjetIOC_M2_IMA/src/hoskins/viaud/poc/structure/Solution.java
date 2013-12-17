/**
 * 
 */
package hoskins.viaud.poc.structure;

/**
 * Class to store the representation of a solution
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class Solution {

	/**
	 * Matrix to indicate team/operation pair
	 */
	private int[][] x;
	
	/**
	 * Table to indicate the number of overtime hours per team
	 */
	private int[] h;
	
	/**
	 * Value of of the solution (i.e OF)
	 */
	private double of = 0;

	/**
	 * Create a solution
	 * @param x team/operation matrix
	 * @param h overtime working time
	 */
	public Solution(int[][] x, int[] h) {
		super();
		this.x = x;
		this.h = h;
	}

	public int[][] getX() {
		return x;
	}

	public void setX(int[][] x) {
		this.x = x;
	}

	public int[] getH() {
		return h;
	}

	public void setH(int[] h) {
		this.h = h;
	}

	public double getOf() {
		return of;
	}

	public void setOf(double of) {
		this.of = of;
	}
	
}
