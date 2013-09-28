package weasel.interpreter;

public class WeaselClass {

	private final WeaselClassBuildPlan classBuildPlan;
	private final WeaselClass[] generics;
	
	protected WeaselClass(WeaselClassBuildPlan classBuildPlan, WeaselClass[] generics){
		this.classBuildPlan = classBuildPlan;
		this.generics = generics;
	}
	
	
	
}
