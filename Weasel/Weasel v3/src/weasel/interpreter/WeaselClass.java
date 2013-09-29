package weasel.interpreter;

public class WeaselClass {

	private final WeaselClassBuildPlan classBuildPlan;
	private final WeaselClass[] generics;
	private WeaselClass[] superClasses;
	private WeaselClass[] allSuperClasses;
	
	protected WeaselClass(WeaselClassBuildPlan classBuildPlan, WeaselClass[] generics){
		this.classBuildPlan = classBuildPlan;
		this.generics = generics;
		superClasses = new WeaselClass[classBuildPlan.superClasses.length];
		for(int i=0; i<superClasses.length; i++){
			superClasses[i] = classBuildPlan.superClasses[i].getWeaselClass(generics);
		}
		allSuperClasses = new WeaselClass[classBuildPlan.allSuperClasses.length];
		int j=0;
		for(int i=0; i<superClasses.length; i++){
			allSuperClasses[j] = superClasses[i];
			j = superClasses[i].addOwnSuperClasses(allSuperClasses, j+1);
		}
	}

	private int addOwnSuperClasses(WeaselClass[] allSuperClasses2, int j) {
		for(int i=0; i<allSuperClasses.length; i++){
			allSuperClasses2[j++] = allSuperClasses[i];
		}
		return j;
	}

	public WeaselClassBuildPlan getClassBuildPlan(){
		return classBuildPlan;
	}
	
	public WeaselClass[] getGenerics(){
		return generics;
	}
	
	public WeaselClass[] getSuperClasses(){
		return superClasses;
	}

	public WeaselClass[] getAllSuperClasses() {
		return allSuperClasses;
	}

	public void getWeaselClassIndex(WeaselClass wc) {
		
	}
	
}
