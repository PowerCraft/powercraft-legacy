package weasel.interpreter;

public class WeaselVirtualMethodTable {

	private WeaselMethodBody[] methods;
	
	public WeaselVirtualMethodTable(WeaselMethodBody[] methods){
		this.methods = methods;
	}
	
	public WeaselMethodBody[] getMethods(){
		return methods;
	}
	
}
