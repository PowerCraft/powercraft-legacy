package weasel.interpreter;

public class WeaselChecks {

	public static void checkSuperClass(WeaselClassBuildPlan weaselClassBuildPlan) {
		if(WeaselModifier.isFinal(weaselClassBuildPlan.getModifier()))
			throw new WeaselRuntimeException("Can't extend class %s", weaselClassBuildPlan.getName());
	}

}
