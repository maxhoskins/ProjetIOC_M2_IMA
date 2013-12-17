/**
 * 
 */
package hoskins.viaud.poc.utils;

import hoskins.viaud.poc.structure.Instance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to read instance from .csv files
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class InstanceReader {

	/**
	 * List of all instances to solve
	 */
	private Instance[] instanceList;
	
	/**
	 * List of all files to read
	 */
	private File[] listOfFiles;
	
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
	private int c;
	
	/**
	 * BufferedReader
	 */
	BufferedReader reader;
	
	/**
	 * From a folderPath, read all instance files into it
	 * @param folderPath path of the folder where all instances are
	 * @throws IOException 
	 */
	public InstanceReader(String folderPath, int l, int s, int c) throws IOException{
		this.l = l;
		this.s = s;
		this.c = c;
		
		this.listOfFiles = new File(folderPath).listFiles();
		
		this.instanceList = new Instance[listOfFiles.length];
		
		readFiles();
	}
	
	public Instance[] getInstanceList() {
		return instanceList;
	}

	/**
	 * Read all files from class constructor
	 * @throws IOException 
	 */
	private void readFiles() throws IOException{
		for(int i = 0; i < this.listOfFiles.length; i++){
			this.instanceList[i] = readFile(this.listOfFiles[i]);
		}
	}
	
	/**
	 * Read datas from a file and convert them to an instance
	 * @param file file to read
	 * @return instance new instance of the problem
	 * @throws IOException 
	 */
	private Instance readFile(File file) throws IOException{
		int i = 0, k = 0, nbOperations = 0, nbEquipes = 0;
		int[] t = null;
		int[][] p = null;		
		this.reader = new BufferedReader(new FileReader(file));

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
		return new Instance(nbOperations, t, nbEquipes, this.l, this.s, this.c, p);
	}
}
