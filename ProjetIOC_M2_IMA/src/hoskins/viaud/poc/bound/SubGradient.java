/**
 * 
 */
package hoskins.viaud.poc.bound;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;

/**
 * Sub gradient algorithm
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class SubGradient implements IBound {

	/**
	 * Sub gradient algorithm
	 */
	@Override
	public void computeBound(Solution s, int nbIterations) {
		//Instantiate iterations counter, lower bound value and Lagrange multipliers vector
		int k = 0;
		double result = Double.MAX_VALUE;
		double lb = s.getOf();
		double[] pi = new double[Instance.instance.getNe()];
		for(int i = 0; i < pi.length; i++)
			pi[i] = 0;

		//Run sub gradient algorithm
		while(k < nbIterations){
			
			//Solve SPL1(pi) & SPL2(pi)
			int[][] x = solveSPL1(pi);
			int[] h = solveSPL2(pi);

			//Compute Lagrange function value L(pi)
			double lPi = computeOF(pi,x,h);
			
			if(lPi < result)
				result = lPi;

			//Compute sub gradient vector
			double[] gamma = computeGamma(x,h);

			//Change Lagrange multipliers vector
			double p = (double)0.9*(double)2*(lPi-lb)/Math.sqrt(sumGamma(gamma));
			for(int i = 0; i < Instance.instance.getNe(); i++){
				pi[i] += p*gamma[i];
			}
			
			k++;
		}
		
		System.out.print("-----------------------------------------------------------------\n");
		System.out.print("Lagrange Upper Bound : "+Math.round(result*100.0)/100.0 +"\n");
		System.out.print("-----------------------------------------------------------------\n");
	}

	/**
	 * Find which team will performed each operation
	 * @param pi Lagrange multipliers vector
	 * @return team/operation matrix
	 */
	private int[][] solveSPL1(double[] pi){
		int[][] x = new int[Instance.instance.getNe()][Instance.instance.getNo()];
		for(int j = 0; j < Instance.instance.getNo(); j++){
			double maxProfit = -1; int team = 0;
			for(int i = 0; i < Instance.instance.getNe(); i++)
				if(Instance.instance.getA()[i][j] == 1)
					if(Instance.instance.getP()[i][j] + pi[i] * Instance.instance.getT()[j] > maxProfit){
						maxProfit = Instance.instance.getP()[i][j] + pi[i] * Instance.instance.getT()[j];
						team = i;
					}
			x[team][j] = 1;
		}
		return x;
	}

	/**
	 * Find which team will do overtime
	 * @param pi
	 * @return team vector with overtime hours
	 */
	private int[] solveSPL2(double[] pi){
		int[] h = new int[Instance.instance.getNe()];
		for(int i = 0; i < Instance.instance.getNe(); i++)
			if(Instance.instance.getC() + pi[i] < 0)
				h[i] = Instance.instance.getS();
			else
				h[i] = 0;
		return h;
	}

	/**
	 * Compute Lagrange OF
	 * @param pi
	 * @param x
	 * @param h
	 * @return Lagrange OF value
	 */
	private double computeOF(double[] pi,int[][] x, int[] h){
		double lPi = 0;
		for(int i = 0; i < Instance.instance.getNe(); i++){
			for(int j = 0; j < Instance.instance.getNo(); j++)
				lPi += (Instance.instance.getP()[i][j] + pi[i] * Instance.instance.getT()[j])*x[i][j];
			lPi -= (Instance.instance.getC() + pi[i]) * h[i];
			lPi -= Instance.instance.getL() * pi[i];
		}
		return lPi;
	}

	/**
	 * Compute gamma vector (ie sub gradient vector)
	 * @param x
	 * @param h
	 * @return sub gradient vector
	 */
	private double[] computeGamma(int[][] x, int[] h){
		double[] gamma = new double[Instance.instance.getNe()];
		for(int i =0; i < Instance.instance.getNe(); i++){
			gamma[i] = 0;
			for(int j = 0; j < Instance.instance.getNo(); j++)
				gamma[i] += Instance.instance.getT()[j] * x[i][j];
			gamma[i] -= Instance.instance.getL() + h[i];
		}
		return gamma;
	}
	

	/**
	 * Calculate gamma sum
	 * @param gamma values
	 * @return gamma sum
	 */
	private double sumGamma(double[] gamma){
		double res = 0;
		for(int i = 0; i < Instance.instance.getNe(); i ++)
			res += gamma[i]*gamma[i];
		return res;
	}

}