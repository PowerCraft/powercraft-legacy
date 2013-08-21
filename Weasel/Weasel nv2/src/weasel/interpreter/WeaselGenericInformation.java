package weasel.interpreter;

public class WeaselGenericInformation {

	public String genericName;
	
	public WeaselClass baseClass;
	public WeaselGenericClassInfo genericInfo;
	
	public WeaselGenericInformation(String genericName, WeaselClass baseClass){
		this.genericName = genericName;
		this.baseClass = baseClass;
		genericInfo = new WeaselGenericClassInfo(new Object[0]);
	}

	@Override
	public String toString() {
		return "WeaselGenericInformation [genericName=" + genericName
				+ ", baseClass=" + baseClass + ", genericInfo=" + genericInfo
				+ "]";
	}
	
}
