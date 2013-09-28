package weasel.interpreter;


public class WeaselClassGenericBuildPlan {

	private int index;
	private WeaselClassBuildPlan classBuildPlan;
	private WeaselClassGenericBuildPlan[] generics;
	
	public WeaselClassGenericBuildPlan(WeaselClassBuildPlan classBuildPlan, WeaselClassGenericBuildPlan...generics) {
		this.classBuildPlan = classBuildPlan;
		this.generics = generics;
	}

	public WeaselClassGenericBuildPlan(int index) {
		this.index = index;
	}
	
	public WeaselClassBuildPlan getClassBuildPlan() {
		return classBuildPlan;
	}
	
	public WeaselClass getWeaselClass(WeaselClass generics[]) {
		if(classBuildPlan==null){
			return generics[index];
		}else{
			WeaselClass[] generics2 = new WeaselClass[this.generics.length];
			for(int i=0; i<generics2.length; i++){
				generics2[i] = this.generics[i].getWeaselClass(generics);
			}
			return classBuildPlan.getGenericClass(generics2);
		}
	}
	
}
