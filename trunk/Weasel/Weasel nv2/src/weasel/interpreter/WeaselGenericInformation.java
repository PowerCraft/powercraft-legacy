package weasel.interpreter;

public class WeaselGenericInformation {

	public String genericName;
	
	public WeaselGenericClassInfo genericInfo;
	
	public WeaselGenericInformation(String genericName, WeaselClass baseClass, int genericID){
		this.genericName = genericName;
		genericInfo = new WeaselGenericClassInfo(baseClass, genericID, new WeaselGenericClassInfo[0]);
	}

	@Override
	public String toString() {
		return "WeaselGenericInformation [genericName=" + genericName
				+ ", genericInfo=" + genericInfo
				+ "]";
	}
	
	public String getName(WeaselClass weaselClass){
		String ret = genericName;
		if(genericInfo.genericClass!=weaselClass.interpreter.baseTypes.getObjectClass()){
			ret += " extends ";
			ret += genericInfo.getName(weaselClass);
		}
		return ret;
	}
	
}
