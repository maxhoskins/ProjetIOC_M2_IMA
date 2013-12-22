/**
 * 
 */
package hoskins.viaud.poc.structure;

import java.util.Random;

/**
 * Store data for an instance 
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

	public Instance(int no, int[] t, int ne, int l, int s, int c, int[][] p) {
		this.no = no;
		this.t = t;
		this.ne = ne;
		this.l = l*60;
		this.s = s*60;
		this.c = c;
		this.p = p;
		this.a = buildAvailability(this.p);
		this.rand = new Random(1000);
	}
	
	public Random getRandom(){
		return this.rand;
	}
	
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int[] getT() {
		return t;
	}

	public void setT(int[] t) {
		this.t = t;
	}

	public int getNe() {
		return ne;
	}

	public void setNe(int ne) {
		this.ne = ne;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public double getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public int[][] getP() {
		return p;
	}

	public void setP(int[][] p) {
		this.p = p;
	}

	public int[][] getA() {
		return a;
	}
	
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
