package hoskins.viaud.poc.structure;

public class SolutionColonne {

	private int[] A;
	private double of;
	
	public SolutionColonne(int[] a, double of) {
		super();
		A = a;
		this.of = of;
	}
	public int[] getA() {
		return A;
	}
	public void setA(int[] a) {
		A = a;
	}
	public double getOf() {
		return of;
	}
	public void setOf(double of) {
		this.of = of;
	}
}
