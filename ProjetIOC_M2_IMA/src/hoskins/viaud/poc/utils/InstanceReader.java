/**
 * 
 */
package hoskins.viaud.poc.utils;

import hoskins.viaud.poc.structure.Instance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to read instance from .csv files
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class InstanceReader {

	/**
	 * Read datas from a file and convert them to an instance
	 * @param file file to read
	 * @return instance new instance of the problem
	 * @throws IOException 
	 */
	public static void readFile(File file, int l, int s, int c) throws IOException{
		int i = 0, k = 0, nbOperations = 0, nbEquipes = 0;
		int[] t = null;
		int[][] p = null;		
		BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = "";
        while((line = reader.readLine())!=null){
            switch (i) {
            case 0 :
            	nbOperations = Integer.parseInt(line);
            	t = new int[nbOperations];
            	i++;
            	break;
            case 1 :	
            	nbEquipes = Integer.parseInt(line);
            	p = new int[nbEquipes][nbOperations];
            	i++;
            	break;
            case 2 :
            	String[] duration = line.split(";");
            	for(int j = 0; j < duration.length; j++)
            		t[j] = Integer.parseInt(duration[j]);
            	i++;
            	break;
            default :
            	String[] profit = line.split(";");
            	for(int j = 0; j < profit.length; j++)
            		p[k][j] = Integer.parseInt(profit[j]);
            	k++;
            	break;
            }
        }
        
        reader.close();
        
		Instance.instance = new Instance(nbOperations, t, nbEquipes, l, s, c, p);
	}
}
