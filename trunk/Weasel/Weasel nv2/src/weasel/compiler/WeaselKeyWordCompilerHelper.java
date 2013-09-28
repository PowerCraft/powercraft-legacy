package weasel.compiler;

import java.util.List;

import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselGenericField;
import weasel.interpreter.WeaselGenericMethod2;
import weasel.interpreter.bytecode.WeaselInstructionJump;



public interface WeaselKeyWordCompilerHelper {

	public WeaselVariableInfo getVariable(String name);

	public List<WeaselGenericMethod2> getGenericMethods(String param) throws WeaselCompilerException;

	public WeaselGenericField getGenericField(String variable);

	public WeaselGenericMethod2 getCompilingMethod();

	public WeaselClassCompiler getClassCompiler();

	public WeaselVariableInfo newVar(int modifier, String varName, WeaselGenericClass wgc);

	public void writeVar(WeaselVariableInfo wvi);

	public void openBlock(boolean b);

	public WeaselBlockInfo closeBlock();

	public int getVarCount();

	public void addBreak(int s, WeaselInstructionJump breakJump);

	public void addContinue(int s, WeaselInstructionJump continueJump);

	public void addClosingsAndFrees(int s, WeaselInstructionList instructionList, boolean b);
	
}
