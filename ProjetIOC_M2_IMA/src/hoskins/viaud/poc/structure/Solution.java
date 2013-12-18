/**
 * 
 */
package hoskins.viaud.poc.structure;

/**
 * Store the representation of a solution
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class Solution implements Cloneable{

	/**
	 * Matrix to indicate team/operation pair
	 */
	private int[][] x;
	
	/**
	 * Table to indicate the number of overtime hours per team
	 */
	private int[] h;
	
	/**
	 * Representation of a solution to perform local search and meta-heuristic
	 */
	private int[] s;
	
	/**
	 * Value of of the solution (i.e OF value)
	 */
	private double of = 0;
	
	/**
	 * Create a solution
	 * @param x team/operation matrix
	 * @param h overtime working time
	 */
	public Solution(int[][] x, int[] h) {
		this.x = x;
		this.h = h;
		this.s = buildRepresentation();
	}
	
	/**
	 * Create a solution only for cloning
	 * @param x team/operation matrix
	 * @param h overtime working time
	 */
	private Solution(int[][] x, int[] h, double of) {
		this.x = x;
		this.h = h;
		this.s = buildRepresentation();
		this.of = of;
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
	
	public int[] getS() {
		return s;
	}

	public void setS(int[] s) {
		this.s = s;
	}

	public double getOf() {
		return of;
	}

	public void setOf(double of) {
		this.of = of;
	}
	
	/**
	 * Change overtime value for each team depending on team/operation affectations
	 */
	public void computeOvertime(){
		for(int i = 0; i < Instance.instance.getNe(); i++){
			int sum = 0;
			for(int j = 0; j < Instance.instance.getNo(); j++)
				if(x[i][j] == 1)
					sum ++;
			if(Instance.instance.getL() >= sum)
				this.h[i] = 0;
			else
				this.h[i] = sum - Instance.instance.getL();
		}
	}
	
	/**
	 * Check if a solution is feasible
	 * @return true if solution is feasible, false otherwise
	 */
	public boolean isFeasible(){
		boolean isFeasible = true;
		
		//Check if operations are performed by only one team allowed to do it
		for(int i = 0; i < Instance.instance.getNe(); i++)
			for(int j = 0; j < Instance.instance.getNo(); j++)
				if(x[i][j] > Instance.instance.getA()[i][j])
					isFeasible = false;
		
		//Check if teams worked more than overtime working time
		for(int i = 0; i < Instance.instance.getNe(); i++)
			if(Instance.instance.getS() < h[i])
				isFeasible = false;
		
		return isFeasible;
	}
	
	/**
	 * Calculate OF
	 */
	public void calculateOF(){
		of = 0;
		for(int i = 0; i < Instance.instance.getNe(); i++){
			for(int j = 0; j < Instance.instance.getNo(); j++)
				if(x[i][j] == 1)
					of += Instance.instance.getP()[i][j];
			if(h[i] > 0){
				//Need to convert the value of h[i] in hours before counting
				of -= Math.ceil((double)h[i]/60.0) * Instance.instance.getC();
			}
		}
	}
	
	/**
	 * Build solution representation for local search method
	 */
	private int[] buildRepresentation(){
		int[] s = new int[Instance.instance.getNo()];
		
		for(int j = 0; j < Instance.instance.getNo(); j++)
			for(int i = 0; i < Instance.instance.getNe(); i++)
				if(this.x[i][j] == 1)
					s[j] = i;
		
		return s;
	}
	
	/**
	 * Clone a solution
	 */
	public Solution clone(){
		//Perform deep cloning of x matrix
		int[][] y = x.clone();
		for (int i = 0; i < y.length; i++) 
		    y[i] = y[i].clone();
		
		return new Solution(y, h.clone(), of);
	}
	
}
