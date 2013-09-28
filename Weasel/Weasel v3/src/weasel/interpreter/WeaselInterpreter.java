package weasel.interpreter;

import java.util.ArrayList;
import java.util.List;

import weasel.interpreter.io.WeaselClassFile;

public class WeaselInterpreter {

	private WeaselPackage defaultPackage = new WeaselPackage("");
	private List<WeaselClassBuildPlan> toResolve = new ArrayList<WeaselClassBuildPlan>();
	private int calls=0;
	
	public WeaselClassBuildPlan getWeaselClassBuildPlan(String name) {
		WeaselPackage p = null;
		try{
			p = defaultPackage.getPackage(name, false);
		}catch(WeaselRuntimeException e){
			
		}
		if(p==null){
			calls++;
			loadClass(name);
			p = defaultPackage.getPackage(name, false);
			if(calls==1){
				while(!toResolve.isEmpty()){
					toResolve.remove(0).resolve();
				}
			}
			calls--;
		}
		if(p==null){
			throw new WeaselRuntimeException("Can't find class %s", name);
		}
		if(!(p instanceof WeaselClassBuildPlan)){
			throw new WeaselRuntimeException("%s isn't a class", name);
		}
		return (WeaselClassBuildPlan)p;
	}
	
	public WeaselClass getWeaselClass(String name, WeaselClass...generics){
		WeaselClassBuildPlan buildPlan = getWeaselClassBuildPlan(name);
		return buildPlan.getGenericClass(generics);
	}
	
	private void loadClass(String name){
		WeaselClassInput wci = getClassInput(name);
		WeaselPackage p = defaultPackage.getPackage(wci.classPackage, false);
		WeaselClassBuildPlan wcbp = new WeaselClassBuildPlan(this, wci.className, WeaselClassFile.load(wci.classBytes));
		p.addPackage(wcbp);
		toResolve.add(wcbp);
	}
	
	private WeaselClassInput getClassInput(String name){
		return null;
	}

	public WeaselClassGenericBuildPlan getGenericBuildPlan(weasel.interpreter.io.WeaselClassFile.WeaselClass weaselClass, WeaselClassBuildPlan weaselClassBuildPlan) {
		String name = weaselClass.name;
		int index = weaselClassBuildPlan.getGenericIndex(name);
		if(index!=-1)
			return new WeaselClassGenericBuildPlan(index);
		WeaselClassBuildPlan wcbp = getWeaselClassBuildPlan(name);
		WeaselClassGenericBuildPlan[] genericBuildPlans = new WeaselClassGenericBuildPlan[weaselClass.typeParams.length];
		for(int i=0; i<genericBuildPlans.length; i++){
			genericBuildPlans[i] = getGenericBuildPlan(weaselClass.typeParams[i], weaselClassBuildPlan);
		}
		if(wcbp.getGenericInfos().length != genericBuildPlans.length){
			throw new WeaselRuntimeException("Wrong argument of generic types for %s", name);
		}
		return new WeaselClassGenericBuildPlan(wcbp, genericBuildPlans);
	}
	
}
