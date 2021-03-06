/**
 * 
 */
package hoskins.viaud.poc.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;

/**
 * Write a solution in a .csv file.<br />
 * The format of the file is the following : <br />
 * Line 1 : Net profit<br />
 * Line 2 : Number of overtime hours worked<br />
 * For each of the other lines:  list of operations performed by each team<br />
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class SolutionWriter {

	/**
	 * Solution created by solving method
	 */
	private Solution s;

	/**
	 * File name of the source file
	 */
	private String sourceFileName;

	/**
	 * Folder path to store results
	 */
	private String folderPath;

	/**
	 * FileWriter
	 */
	private FileWriter fw;

	/**
	 * PrintWriter
	 */
	private PrintWriter out;

	/**
	 * Create result file
	 * @param s solution
	 * @param sourceFileName instance file name
	 * @param folderPath folder to store result file
	 * @throws Exception
	 */
	public SolutionWriter(Solution s, String sourceFileName, String folderPath) throws Exception{
		if (!new File(folderPath).exists()) {
			new File(folderPath).mkdir();
		}
		this.s = s;
		this.sourceFileName = sourceFileName;
		this.folderPath = folderPath;
		openFile(createFileName());
		buildResult();
		closeFile();
	}
	
	/**
	 * Create a upper bound result file
	 * @param value upper bound value
	 * @param sourceFileName instance file name
	 * @param folderPath folder to store result file
	 * @throws IOException 
	 * @throws Exception
	 */
	public SolutionWriter(double value, String sourceFileName, String folderPath) throws IOException{
		if (!new File(folderPath).exists()) {
			new File(folderPath).mkdir();
		}
		this.sourceFileName = sourceFileName;
		this.folderPath = folderPath;
		openFile(createFileName());
		out.println(value);
		closeFile();
	}

	/**
	 * Build solution and write to file
	 */
	private void buildResult(){
		out.println(this.s.getOf());

		//Write overtime for all teams
		for(int i = 0; i < Instance.instance.getNe(); i++)
			if(i < Instance.instance.getNe() - 1)
				out.print(Math.ceil(this.s.getH()[i]/60.0) + ";");
			else
				out.println(Math.ceil(this.s.getH()[i]/60.0));

		//Write assignment between team and operation
		for(int i = 0; i < Instance.instance.getNe(); i++){
			for(int j = 0; j < Instance.instance.getNo(); j++){
				if(this.s.getX()[i][j] == 1)
					if(j < Instance.instance.getNo() - 1)
						out.print(j + 1 + ";");
					else
						out.print(j + 1);
			}
			if(i < Instance.instance.getNe() - 1)
				out.print("\n");
		}
	}

	/**
	 * Open file to write in
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
	 * Close file and print writers
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
	 * Create a filename based on source file name
	 * @return file name and path
	 */
	private String createFileName(){
		return this.folderPath + "/R" + this.sourceFileName;
	}

}
