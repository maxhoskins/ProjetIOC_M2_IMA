/**
 * 
 */
package sandbox;

import java.util.Random;

import hoskins.viaud.poc.utils.InstanceGenerator;

/**
 * Used to test instance generator and build sets of instances
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class TestInstanceGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// seed must be changed to not duplicate instances between runs
		long seed = 1000;
		
		Random rGen = new Random(seed);
		int nbOp = 50, nbEq = 15;
		for(int i = 0; i < 100; i++){
			try {
				new InstanceGenerator(nbEq, nbOp, "./instances");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Generated O"+nbOp+"E"+nbEq);
		}
		System.out.println("Complete");
	}

}
