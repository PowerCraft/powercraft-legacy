package weasel.interpreter;

public class WeaselGenericInfo {

	private String name;
	private WeaselClassGenericBuildPlan[] genericBuildPlans;
	
	public WeaselGenericInfo(String name, WeaselClassGenericBuildPlan[] genericBuildPlans){
		this.name = name;
		this.genericBuildPlans = genericBuildPlans;
	}
	
	public String getName(){
		return name;
	}
	
	public WeaselClassGenericBuildPlan[] getBuildPlans(){
		return genericBuildPlans;
	}
	
}
