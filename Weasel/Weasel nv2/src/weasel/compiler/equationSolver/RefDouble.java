package weasel.compiler.equationSolver;


public class RefDouble {
	public double v;
	public RefDouble(double x) {
		v=x;
	}
	public RefDouble copy() {
		return new RefDouble(v);
	}
}
