/**
 * 
 */
package hoskins.viaud.poc.bound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import hoskins.viaud.poc.model.CGModel;
import hoskins.viaud.poc.model.PSModel;
import hoskins.viaud.poc.model.RPSModel;
import hoskins.viaud.poc.structure.Instance;
import hoskins.viaud.poc.structure.Solution;
import hoskins.viaud.poc.structure.SolutionColonne;

/**
 * Column generation algorithm
 * @author Maxim HOSKINS and Quentin VIAUD
 *
 */
public class ColumnGeneration implements IBound {


	public void computeBound(Solution s, int nbIterations) {
		SolutionColonne bestColumn = null;
		double res = 0;

		//Crée la matrice V suivant les résultats de l'heuristique en paramètre
		int[][] matriceV = genereMatriceV(s);

		//Crée un tableau avec les profits des différents lots en paramètre
		double[] tableauProfit = genereTableauProfit(matriceV);

		//Algorithme de génération de colonnes
		do{		
			matriceV = refineMatriceV(matriceV);
			//Résoud PS(V)
			System.out.println("Start PS");
			res = new PSModel().solve(matriceV, tableauProfit);

			//Crée le vecteur pi en résolvant le dual de PS(V)
			System.out.println("Start RPS");
			double[] pi = new RPSModel().solveDual(matriceV, tableauProfit);


			//Résoud CG suivant pi et renvoie une nouvelle solution en énnumérant toutes les combinaison équipe/ dépassement heures sup
			bestColumn = new SolutionColonne(null,-1);
			int bestTeam = -1;
			for(int e = 0; e < Instance.instance.getNe(); e++){
				for(int theta = 0; theta < Math.round(Instance.instance.getS()/60); theta++){
					SolutionColonne sc = new CGModel().solveGC(pi, e, theta);
					if(sc.getOf() > bestColumn.getOf()){
						bestColumn = sc;
						bestTeam = e;
					}
				}
			}			

			//Fusionne V avec la nouvelle colonne A
			matriceV = fusion(matriceV,bestColumn.getA());

			//Recalcule le profit de chaque lot d'après la nouvelle matrice V
			tableauProfit = miseAJourTableauProfit(matriceV, tableauProfit, bestTeam);
		}

		//Condition d'arrêt
		while(bestColumn.getOf() > 0);

		System.out.println("-----------------------------------------------------------------\n");
		System.out.println("-----------------------------------------------------------------\n");
		System.out.print("Column Generation Result : "+Math.round(res*100.0)/100.0+"\n");
		System.out.println("-----------------------------------------------------------------\n");
		System.out.println("-----------------------------------------------------------------\n");
		System.out.println();

	}



	/**
	 * Crée la matrice V suivant la liste des profils en paramètre
	 * @param listeLot
	 * @return matriceV
	 */
	public static int[][] genereMatriceV(Solution s){	
		int[][] matriceV = new int[Instance.instance.getNo()][Instance.instance.getNe()];
		for(int j = 0; j < Instance.instance.getNo(); j++)
			for(int i = 0; i < Instance.instance.getNe(); i++)
				if(s.getX()[i][j] == 1)
					matriceV[j][i] = 1;
				else
					matriceV[j][i] = 0;
		return matriceV;
	}

	/**
	 * Crée un tableau de profit de chaque profil
	 * @param listeLot
	 * @return tableauDeProfit
	 */
	public static double[] genereTableauProfit(int[][] matriceV){
		double[] t = new double[matriceV[0].length];
		for(int i = 0; i < t.length; i++){
			double profit = 0.0;
			double nbH = 0.0;
			for(int j = 0; j < Instance.instance.getNo(); j++){
				if(matriceV[j][i] == 1){
					profit += Instance.instance.getP()[i][j];
					nbH += Instance.instance.getT()[j];
				}
			}
			profit -= Instance.instance.getC() * (Math.max(0.0, Math.ceil(nbH/60.0) - Instance.instance.getL()));
			t[i] = profit;		
		}
		return t;
	}

	/**
	 * Fusionne la matrice V avec la nouvelle colonne A
	 * @param V, matrice
	 * @param A, nouvelle colonne
	 * @return matriceV, agrégation de V et A
	 */
	public static int[][] fusion(int[][] V, int[] A){
		int[][] matriceV = new int[V.length][V[0].length+1];
		for(int i=0;i<matriceV.length;i++)
			for(int j=0;j<matriceV[0].length;j++)
				if(j == matriceV[0].length-1)
					matriceV[i][j] = A[i];
				else
					matriceV[i][j] = V[i][j];
		return matriceV;
	}

	/**
	 * Mise à jour du tableau de profit
	 * @param listeLot
	 * @return tableauDeProfit
	 */
	public static double[] miseAJourTableauProfit(int[][] matriceV,double[] currentT, int team){
		double[] t = new double[matriceV[0].length];
		for(int i = 0; i < t.length; i++){
			if(i < currentT.length)
				t[i] = currentT[i];
			else{
				double profit = 0.0;
				double nbH = 0.0;
				for(int j = 0; j < Instance.instance.getNo(); j++){
					if(matriceV[j][team] == 1){
						profit += Instance.instance.getP()[team][j];
						nbH += Instance.instance.getT()[j];
					}
				}
				profit -= Instance.instance.getC() * (Math.max(0.0, Math.ceil(nbH/60.0) - Instance.instance.getL()));
				t[i] = profit;	
			}
		}
		return t;
	}
	
	private int[][] refineMatriceV(int[][]matriceV){
		ArrayList<Integer> es = new ArrayList<Integer>(Collections.nCopies(matriceV[0].length, 0));

		ArrayList<Integer>t = null;
		for(int e = 0; e < Instance.instance.getNe(); e++){
			t = checkValidity(matriceV, e);
			for(int j : t){
				if(es.get(j) != 1)
					es.set(j, 1);
			}
		}
		
		int nbCol = 0;
		for(int n : es)
			nbCol += n;
		
		int[][] result = new int[Instance.instance.getNo()][nbCol];
		
		for(int i = 0; i < es.size(); i++){
			if(es.get(i) == 1){
				for(int j = 0; j < Instance.instance.getNo(); j++)
					result[j][i] = matriceV[j][i];
			}else{
				for(int j = 0; j < Instance.instance.getNo(); j++)
					result[j][i] = 0;
			}
		}
		return result;
	}
	
	/**
	 * Checks if at least one work profile can be affected to a team
	 * @param matriceV matrix of columns
	 * @param team id of team
	 * @return true if work profile can be performed by team, false otherwise
	 */
	private ArrayList<Integer> checkValidity(int[][] matriceV, int team){
		ArrayList<Integer> es = new ArrayList<Integer>();
		boolean result = true;
		int e = -1, o = -1;	
		while(++e < matriceV[0].length){
			result = true;
			while(++o < matriceV.length){
				if(matriceV[o][e] == 1)
					if(Instance.instance.getA()[team][o] == 0)
						result = false;
			}
			if(result)
				es.add(e);
		}
		return es;
	}

	@Override
	public void computeBound() {

	}

}
