package weasel.interpreter;


public class WeaselClassGenericBuildPlan {

	private WeaselClassBuildPlan classBuildPlan;
	private Object[] generics;
	
	public WeaselClassGenericBuildPlan(WeaselClassBuildPlan classBuildPlan, Object...generics) {
		this.classBuildPlan = classBuildPlan;
		this.generics = generics;
	}

	public WeaselClassBuildPlan getClassBuildPlan() {
		return classBuildPlan;
	}
	
}
