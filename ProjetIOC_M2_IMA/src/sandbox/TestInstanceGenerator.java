/**
 * 
 */
package sandbox;

import java.util.Random;

import hoskins.viaud.m2Poc.instanceGenerator.InstanceGenerator;

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
		// change based of "solution complexity"
		int minNbEquipes = 10; // value between 0 and 15
		int minNbOperations = 30; // value between 0 and 49
		
		Random rGen = new Random(seed);
		int nbOp, nbEq;
		for(int i = 0; i < 100; i++){
			nbOp = rGen.nextInt(50-minNbOperations)+10;
			nbEq = rGen.nextInt(15-minNbEquipes)+5;
			new InstanceGenerator(nbEq, nbOp, seed);
			System.out.println("Generated O"+nbOp+"E"+nbEq);
		}
		System.out.println("Complete");
	}

}
