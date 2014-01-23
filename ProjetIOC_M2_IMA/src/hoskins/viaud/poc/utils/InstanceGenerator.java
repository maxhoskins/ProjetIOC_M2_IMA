/**
 * 
 */
package hoskins.viaud.poc.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Creates a random instance using a uniform distribution.<br />
 * Each instance if named the following : I_O_E_R_.csv where<br />
 * I = Instance index (generated based of current files in instance folder)<br />
 * O = Number of operations (parameter given)<br />
 * E = Number of teams (parameter given)<br />
 * R = Percentage of operations that cannot be performed by each team (randomly generated between 10 and 50%<br />
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class InstanceGenerator {

	/**
	 * Minimum allowed operation duration
	 */
	private final int minOpL = 30;
	/**
	 * Maximum allowed operation duration
	 */
	private final int maxOpL = 390;
	/**
	 * Minimum allowed team/operation profit
	 */
	private final int minEqP = 700;
	/**
	 * Maximum allowed team/operation profit
	 */
	private final int maxEqP = 1200;
	/**
	 * Number of teams in future instance
	 */
	private int nbEquipes;
	/**
	 * Number of operations in future instance
	 */
	private int nbOperations;
	/**
	 * Folder wher instance should be stored
	 */
	private String folderPath;
	/**
	 * Percentage of incompatibility between teams and operations
	 */
	private int prctIncmp;
	/**
	 * FileWriter
	 */
	private FileWriter fw;
	/**
	 * PrintWriter
	 */
	private PrintWriter out;
	/**
	 * Random generator using Uniform distribution
	 */
	private Random rGen;


	/**
	 * Generates file with random generator seeded. Must only be used for testing purposes
	 * @param nbEquipes number of teams
	 * @param nbOperations number of operations
	 * @param folderPath folder path where instance should be stored (e.g. ./instances)
	 * @param rSeed seed of random generator
	 * @throws Exception Generated if destination folder does note exist
	 */
	public InstanceGenerator(int nbEquipes, int nbOperations, String folderPath, long rSeed) throws Exception{
		if (!new File(folderPath).exists()) {
		    throw new Exception("Destination folder does not exist");
		}
		this.nbEquipes = nbEquipes;
		this.nbOperations = nbOperations;
		this.folderPath = folderPath;
		this.rGen = new Random(rSeed);
		this.prctIncmp = this.rGen.nextInt(5-1)+1;
		openFile(createFileName());
		buildInstance();
		closeFile();

	}


	/**
	 * Generates unrepeatable random file
	 * @param nbEquipes number of teams
	 * @param nbOperations number of operations
	 * @param folderPath folder path where instance should be stored (e.g. ./instances)
	 * @param rSeed seed of random generator
	 * @throws Exception Generated if destination folder does note exist
	 */
	public InstanceGenerator(int nbEquipes, int nbOperations, String folderPath) throws Exception{
		if (!new File(folderPath).exists()) {
		    throw new Exception("Destination folder does not exist");
		}
		this.nbEquipes = nbEquipes;
		this.nbOperations = nbOperations;
		this.folderPath = folderPath;
		this.rGen = new Random();
		this.prctIncmp = this.rGen.nextInt(5-1)+1;
		openFile(createFileName());
		buildInstance();
		closeFile();

	}


	/**
	 * Builds instance and writes it to file
	 */
	private void buildInstance(){
		out.println(this.nbOperations);
		out.println(this.nbEquipes);
		// build operations lengths
		for(int o = 0; o < this.nbOperations; o++){
			if(o < this.nbOperations-1)
				out.print(rGen.nextInt(this.maxOpL-this.minOpL)+this.minOpL+";");
			else
				out.println(rGen.nextInt(this.maxOpL-this.minOpL)+this.minOpL);
		}
		//build team/operation profits
		for(int e = 0; e < this.nbEquipes; e++){
			for(int o = 0; o < this.nbOperations; o++){
				if(this.rGen.nextInt(10) <= this.prctIncmp){
					if(o < this.nbOperations-1)
						out.print(0+";");
					else
						out.print(0);
				}else{
					if(o < this.nbOperations-1)
						out.print(rGen.nextInt(this.maxEqP-this.minEqP)+this.minEqP+";");
					else
						out.print(rGen.nextInt(this.maxEqP-this.minEqP)+this.minEqP);
				}
			}
			if(e < this.nbEquipes-1)
				out.print("\n");
		}
	}


	/**
	 * Opens file to write in
	 * @param filename name and path of file
	 */
	private void openFile(String filename){
		try {
			this.fw = new FileWriter(filename);
			this.out = new PrintWriter(fw);
		} catch (IOException e) {
			System.out.println("Unable to open file to write");
			e.printStackTrace();
		}
	}


	/**
	 * Closes file and print writers
	 */
	private void closeFile(){	
		try {
			this.out.flush();
			this.out.close();
			this.fw.close();
		} catch (IOException e) {
			System.out.println("Unable to close file");
			e.printStackTrace();
		}  
	}


	/**
	 * Creates a unique filename based on number of teams and operations
	 * @return file name and path
	 */
	private String createFileName(){
		File[] files = new File(this.folderPath).listFiles();
		String filename;
		int nbEq, nbOp, nbR, iInst, maxInst = 0;
		for (File file : files){
			if (file.isFile()){
				filename = file.getName();
				nbR = Integer.parseInt(filename.substring(filename.indexOf("R")+1, filename.indexOf(".")));
				nbEq = Integer.parseInt(filename.substring(filename.indexOf("E")+1, filename.indexOf("R")));
				nbOp = Integer.parseInt(filename.substring(filename.indexOf("O")+1, filename.indexOf("E")));
				if(nbEq == this.nbEquipes && nbOp == this.nbOperations && nbR == this.prctIncmp){
					iInst = Integer.parseInt(filename.substring(filename.indexOf("I")+1, filename.indexOf("O")));
					if(iInst >= maxInst)
						maxInst = iInst+1;
				}
			}
		}	    
		return this.folderPath+"/I"+maxInst+"O"+this.nbOperations+"E"+this.nbEquipes+"R"+this.prctIncmp+".csv";
	}
}
