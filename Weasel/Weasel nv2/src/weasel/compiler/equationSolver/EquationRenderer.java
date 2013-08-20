package weasel.compiler.equationSolver;



public class EquationRenderer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initializate();
	}
	
	public static void initializate() {
		int i=2;
		System.out.println(Equation.parse("5*(4-5)/5/6/3*0+4*(7-4*6*4*(3-5-4+2)*-4)").toString());
		//Renderer.openWindow(800, 600, "Equation Renderer");
		//gameLoop();
		//Renderer.closeWindow();
	}
}
