/**
 * 
 */
package hoskins.viaud.poc.structure;

/**
 * Store the representation of a solution.
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
		computeOvertime();
		calculateOF();
	}
	

	/**
	 * Create a solution only for cloning
	 * @param x team/operation matrix
	 * @param h overtime working time
	 * @param of value of objective function
	 */
	private Solution(int[][] x, int[] h, double of) {
		this.x = x;
		this.h = h;
		this.s = buildRepresentation();
		this.of = of;
	}
	
	/**
	 * Change value in x[i][j]
	 * @param i team index
	 * @param j operation index
	 * @param value
	 */
	public void setXValue(int i, int j, int value){
		this.x[i][j] = value;
	}

	/**
	 * 
	 * @return Matrix of team/operations affectations
	 */
	public int[][] getX() {
		return x;
	}

	/**
	 * Update matrix of team/operations affectations
	 * @param x new matrix of team/operations affectations
	 */
	public void setX(int[][] x) {
		this.x = x;
	}

	/**
	 * 
	 * @return Table of number of overtime hours of each team
	 */
	public int[] getH() {
		return h;
	}

	/**
	 * Update table of number of overtime hours of each team
	 * @param h new table of number of overtime hours of each team
	 */
	public void setH(int[] h) {
		this.h = h;
	}
	
	/**
	 * 
	 * @return "Chromosome" representation of a solution
	 */
	public int[] getS() {
		return s;
	}

	/**
	 * Update "Chromosome" representation of a solution
	 * @param s new "Chromosome" representation of a solution
	 */
	public void setS(int[] s) {
		this.s = s;
	}

	/**
	 * 
	 * @return Value of objective function
	 */
	public double getOf() {
		return of;
	}

	/**
	 * Update value of objective function
	 * @param of new value of objective function
	 */
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
					sum += Instance.instance.getT()[j];
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
	 * Calculate objective function
	 */
	public double calculateOF(){
		of = 0.0;
		for(int i = 0; i < Instance.instance.getNe(); i++){
			for(int j = 0; j < Instance.instance.getNo(); j++)
				if(x[i][j] == 1)
					of += Instance.instance.getP()[i][j];
			if(h[i] > 0)
				//Need to convert the value of h[i] in hours before counting
				of -= Math.ceil((double)h[i]/60.0) * Instance.instance.getC();
		}
		return of;
	}
	
	/**
	 * Build solution representation for local search method
	 * @return solution representation
	 */
	public int[] buildRepresentation(){
		s = new int[Instance.instance.getNo()];
		
		for(int j = 0; j < Instance.instance.getNo(); j++)
			for(int i = 0; i < Instance.instance.getNe(); i++)
				if(this.x[i][j] == 1)
					s[j] = i;
		
		return s;
	}
	
	/**
	 * Clone a solution
	 * @return cloned Solutions
	 */
	public Solution clone(){
		//Perform deep cloning of x matrix
		int[][] y = this.x.clone();
		for (int i = 0; i < y.length; i++) 
		    y[i] = y[i].clone();
		
		return new Solution(y, this.h.clone(), this.of);
	}
	
	/**
	 * Print solution information
	 */
	public void printSolution(){
		System.out.println("Objective Function : "+of);
		
		System.out.println("\nOperation to team affecatations");	
		for(int i = 0; i < Instance.instance.getNo(); i++)
			System.out.print(i+"\t");
		for(int op = 0; op < Instance.instance.getNo(); op++)
			System.out.print(s[op]+"\t");
		
		System.out.println("\nTeam overtime work");
		for(int i = 0; i < Instance.instance.getNe(); i++)
			System.out.print(i+"\t");
		for(int eq = 0; eq < Instance.instance.getNe(); eq++)
			System.out.print(h[eq]+"\t");
		
		System.out.println();
	}
}
