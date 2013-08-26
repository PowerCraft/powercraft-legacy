package weasel.compiler.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerMessage;
import weasel.compiler.WeaselCompilerMessage.MessageType;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.WeaselVariableInfo;
import weasel.compiler.v2.tokentree.WeaselTree;
import weasel.interpreter.WeaselGenericMethod2;
import weasel.interpreter.WeaselMethod;
import weasel.interpreter.WeaselMethodBody;
import weasel.interpreter.WeaselModifier;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselMethodBodyCompilerV2 extends WeaselMethodBody implements WeaselKeyWordCompilerHelper {

	protected final WeaselCompiler compiler;
	protected final WeaselClassCompilerV2 classCompiler;
	protected final List<WeaselToken> methodTokens;
	protected int methodTokenPos;
	protected final List<String> paramNames;
	protected final List<Integer> paramModifier;
	protected final HashMap<String, WeaselVariableInfo> variables = new HashMap<String, WeaselVariableInfo>();
	
	protected WeaselMethodBodyCompilerV2(WeaselMethod method, WeaselClassCompilerV2 classCompiler, List<WeaselToken> methodTokens, List<String> paramNames, List<Integer> paramModifier, WeaselCompiler compiler) {
		super(method, classCompiler);
		this.compiler = compiler;
		this.classCompiler = classCompiler;
		instructions = new WeaselInstruction[0];
		this.methodTokens = methodTokens;
		this.paramNames = paramNames;
		this.paramModifier = paramModifier;
	}

	protected WeaselMethodBodyCompilerV2(WeaselMethod method, WeaselClassCompilerV2 classCompiler) {
		super(method, classCompiler);
		this.classCompiler = classCompiler;
		compiler = null;
		methodTokens = null;
		paramNames = null;
		paramModifier = null;
	}

	public void compile() {
		if(isNative()){
			return;
		}
		for(int i=0; i<paramNames.size(); i++){
			WeaselGenericMethod2 genericMethod = classCompiler.genericClass.getGenericMethod(method.getNameAndDesk(), null);
			variables.put(paramNames.get(i), new WeaselVariableInfo(paramModifier.get(i), paramNames.get(i), genericMethod.getGenericParams()[i], -i));
		}
		if(!WeaselModifier.isStatic(method.getModifier())){
			variables.put("this", new WeaselVariableInfo(0, "this", classCompiler.genericClass, -paramNames.size()));
		}
		List<WeaselInstruction> instructions = new ArrayList<WeaselInstruction>();
		ListIterator<WeaselToken> iterator = methodTokens.listIterator();
		while(iterator.hasNext()){
			try{
				instructions.addAll(WeaselTree.parseAndCompile(compiler, this, iterator));
			}catch(WeaselCompilerException e){
				compiler.addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, e.getLine(), parentClass.getFileName(), e.getMessage()));
				iterator.previous();
				WeaselToken token = iterator.next();
				while(token.tokenType!=WeaselTokenType.SEMICOLON && iterator.hasNext()){
					token = iterator.next();
				}
			}
		}
		System.out.println("instructions:"+instructions);
		this.instructions = instructions.toArray(this.instructions);
	}
	
	@Override
	public WeaselVariableInfo getVariable(String name) {
		return variables.get(name);
	}

	@Override
	public List<WeaselGenericMethod2> getGenericMethods(String name) throws WeaselCompilerException {
		return classCompiler.genericClass.getGenericMethods(name, !WeaselModifier.isStatic(method.getModifier()));
	}
	
}
