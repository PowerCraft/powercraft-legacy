package weasel.interpreter;

public class WeaselGenericInformation {

	public String genericName;
	
	public WeaselClass baseClass;
	public WeaselGenericInfo genericInfo;
	
	public WeaselGenericInformation(String genericName, WeaselClass baseClass){
		this.genericName = genericName;
		this.baseClass = baseClass;
		genericInfo = new WeaselGenericInfo(new Object[0]);
	}
	
}
