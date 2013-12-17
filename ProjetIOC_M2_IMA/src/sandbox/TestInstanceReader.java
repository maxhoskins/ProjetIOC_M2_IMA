/**
 * 
 */
package sandbox;

import java.io.IOException;

import hoskins.viaud.poc.utils.InstanceReader;

/**
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class TestInstanceReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new InstanceReader("./instances", 0, 0, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
