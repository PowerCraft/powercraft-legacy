package weasel.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselNativeException;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselBlockInfo {

	public final WeaselBlockInfo base;
	
	public final boolean canAddBreaks;
	
	public int varIndex;
	
	public final List<WeaselInstruction> breaks = new ArrayList<WeaselInstruction>();
	
	public final List<WeaselInstruction> continues = new ArrayList<WeaselInstruction>();
	
	public final HashMap<String, WeaselVariableInfo> variables = new HashMap<String, WeaselVariableInfo>();
	
	public final ExitingCreator exiting;
	
	public WeaselBlockInfo(boolean canAddBreaks, int varIndex) {
		base = null;
		this.canAddBreaks = canAddBreaks;
		this.varIndex = varIndex;
		exiting = null;
	}


	public WeaselBlockInfo(boolean canAddBreaks, WeaselBlockInfo base) {
		this.base = base;
		this.canAddBreaks = canAddBreaks;
		varIndex = base.varIndex;
		exiting = null;
	}


	public WeaselVariableInfo newVar(int modifier, String name, WeaselGenericClass type) {
		if(getVar(name)!=null)
			throw new WeaselNativeException("Duplicated definition of %s", name);
		WeaselVariableInfo wvi = new WeaselVariableInfo(modifier, name, type, varIndex++);
		variables.put(name, wvi);
		return wvi;
	}
	
	public WeaselVariableInfo getVar(String name){
		WeaselVariableInfo wvi = variables.get(name);
		if(wvi == null){
			if(base!=null)
				wvi = base.getVar(name);
		}
		return wvi;
	}
	
	public int varsToPop(){
		return variables.size();
	}
	
	public List<WeaselInstruction> getExitings(boolean isReturn){
		List<WeaselInstruction> instructions;
		if(isReturn || !canAddBreaks){
			instructions = base.getExitings(isReturn);
		}else{
			instructions = new ArrayList<WeaselInstruction>();
		}
		if(exiting!=null){
			instructions.add(0, exiting.create());
		}
		return instructions;
	}
	
	public static interface ExitingCreator{
		public WeaselInstruction create();
	}
	
}
