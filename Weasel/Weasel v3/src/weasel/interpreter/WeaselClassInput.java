package weasel.interpreter;


public class WeaselClassInput {

	public final String classPackage;
	public final String className;
	public final byte[] classBytes;
	
	public WeaselClassInput(String classPackage, String className, byte[] classBytes){
		this.classPackage = classPackage;
		this.className = className;
		this.classBytes = classBytes;
	}
	
}
