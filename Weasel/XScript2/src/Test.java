import xscript.compiler.XMessageList;
import xscript.compiler.XTree;
import xscript.compiler.XTreePrinter;
import xscript.compiler.standart.XStandartTreeMaker;



public class Test {
	
	public static void main(String[] args){
		
		XStandartTreeMaker maker = new XStandartTreeMaker();
		XTree tree = maker.makeTree("public class A {public void v(){x=a+(b*3)/(2+x)%2;}}", new XMessageList());
		XTreePrinter treePrinter = new XTreePrinter();
		tree.accept(treePrinter);
		
	}
	
}
