package weasel.compiler.equationSolverOld;


public class RefDouble {
	public double v;
	public RefDouble(double x) {
		v=x;
	}
	public RefDouble copy() {
		return new RefDouble(v);
	}
}
