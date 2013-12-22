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
		int nbOp, nbEq;
		for(int i = 0; i < 10; i++){
			nbOp = 150;
			nbEq = 45;
			try {
				new InstanceGenerator(nbEq, nbOp, "./instancesBig");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Generated O"+nbOp+"E"+nbEq);
		}
		System.out.println("Complete");
	}

}
