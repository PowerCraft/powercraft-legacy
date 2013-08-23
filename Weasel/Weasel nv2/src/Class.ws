public final class Class {

	private final String className;
	private final String classByteName;
	
	private Class(String className, String classByteName){
		this.className = className;
		this.classByteName = classByteName;
	}
	
	public String getName(){
		return className;
	}
	
	public native Class getArrayClass();
	
}