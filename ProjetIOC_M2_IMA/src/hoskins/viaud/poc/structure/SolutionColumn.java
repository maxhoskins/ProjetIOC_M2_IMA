package hoskins.viaud.poc.structure;

public class SolutionColumn {

	private int[] A;
	private double of;
	
	public SolutionColumn(int[] a, double of) {
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
