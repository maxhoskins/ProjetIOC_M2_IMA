/**
 * 
 */
package hoskins.viaud.poc.structure;

import java.util.Random;

/**
 * Store data for an instance.
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class Instance {

	/**
	 * Number of operations
	 */
	private int no;
	
	/**
	 * Table of operation durations
	 */
	private int[] t;
	
	/**
	 * Number of teams
	 */
	private int ne;
	
	/**
	 * Number of legal working time
	 */
	private int l;
	
	/**
	 * Number of maximal overtime hour
	 */
	private int s;
	
	/**
	 * Cost of an overtime hour
	 */
	private double c;
	
	/**
	 * Matrix of profit by team/operation pair
	 */
	private int[][] p;
	
	/**
	 * Matrix of availability by team/operation pair
	 */
	private int[][] a;
	
	/**
	 * Random generator
	 */
	private Random rand;
	
	/**
	 * Instance of the class
	 */
	public static Instance instance = null;

	/**
	 * Default constructor
	 * @param no Number of operations
	 * @param t Table of operation durations
	 * @param ne Number of legal working time
	 * @param l Number of legal working time
	 * @param s Number of maximal overtime hour
	 * @param c Cost of an overtime hour
	 * @param p Matrix of profit by team/operation pair
	 */
	public Instance(int no, int[] t, int ne, int l, int s, int c, int[][] p) {
		this.no = no;
		this.t = t;
		this.ne = ne;
		this.l = l * 60;
		this.s = s * 60;
		this.c = c;
		this.p = p;
		this.a = buildAvailability(this.p);
		this.rand = new Random(1000);
	}
	
	
	/**
	 * 
	 * @return random number generator
	 */
	public Random getRandom(){
		return this.rand;
	}
	
	/**
	 * 
	 * @return Number of operations
	 */
	public int getNo() {
		return no;
	}

	/**
	 * Update number of operations
	 * @param no number of operations
	 */
	public void setNo(int no) {
		this.no = no;
	}

	
	/**
	 * 
	 * @return Table of operation durations
	 */
	public int[] getT() {
		return t;
	}

	/**
	 * Update table of operation durations
	 * @param t new table of operation durations
	 */
	public void setT(int[] t) {
		this.t = t;
	}

	/**
	 * 
	 * @return Number of teams
	 */
	public int getNe() {
		return ne;
	}

	/**
	 * Update number of teams
	 * @param ne new number of teams
	 */
	public void setNe(int ne) {
		this.ne = ne;
	}

	/**
	 * 
	 * @return Legal working time
	 */
	public int getL() {
		return l;
	}

	
	/**
	 * Update legal working time
	 * @param l new legal working time
	 */
	public void setL(int l) {
		this.l = l;
	}

	/**
	 * 
	 * @return Max number of overtime hours
	 */
	public int getS() {
		return s;
	}

	/**
	 * Update max number of overtime hours
	 * @param s new number of overtime hours
	 */
	public void setS(int s) {
		this.s = s;
	}

	/**
	 * 
	 * @return Cost of an hour of overtime
	 */
	public double getC() {
		return c;
	}

	/**
	 * Update cost of an hour of overtime
	 * @param s new cost per hour of overtime
	 */
	public void setC(int c) {
		this.c = c;
	}

	
	/**
	 * 
	 * @return Matrix of team profits for each operation
	 */
	public int[][] getP() {
		return p;
	}

	/**
	 * Update matrix of team profits for each operation
	 * @param pn new matrix of team profits for each operation
	 */
	public void setP(int[][] p) {
		this.p = p;
	}

	/**
	 * 
	 * @return Matrix of availability by team/operation pair
	 */
	public int[][] getA() {
		return a;
	}
	
	/**
	 * Update matrix of availability by team/operation pair
	 * @param a new matrix of availability by team/operation pair
	 */
	public void setA(int[][] a) {
		this.a = a;
	}

	/**
	 * Build availability for a team to perform an operation as a matrix from profit matrix
	 * @param p profit matrix
	 * @return matrix of availability
	 */
	public int[][] buildAvailability(int[][] p){
		int[][] a = new int[p.length][p[0].length];
		for(int i = 0; i < p.length; i++)
			for(int j = 0; j < p[i].length; j++)
				if(p[i][j] > 0)
					a[i][j] = 1;
				else
					a[i][j] = 0;
		return a;
	}
}
