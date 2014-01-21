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
 * Write a solution in a .csv file
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
			throw new Exception("Destination folder does not exist");
		}
		this.s = s;
		this.sourceFileName = sourceFileName;
		this.folderPath = folderPath;
		openFile(createFileName());
		buildResult();
		closeFile();
	}

	/**
	 * Build file
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
