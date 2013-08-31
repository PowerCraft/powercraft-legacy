package weasel.compiler.v2;

import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselBlockInfo;
import weasel.compiler.WeaselClassCompiler;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerMessage;
import weasel.compiler.WeaselCompilerMessage.MessageType;
import weasel.compiler.WeaselInstructionList;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.WeaselVariableInfo;
import weasel.compiler.keywords.WeaselKeyWord;
import weasel.compiler.v2.tokentree.WeaselParameterCompileReturn;
import weasel.compiler.v2.tokentree.WeaselTree;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselGenericField;
import weasel.interpreter.WeaselGenericMethod;
import weasel.interpreter.WeaselGenericMethod2;
import weasel.interpreter.WeaselMethod;
import weasel.interpreter.WeaselMethodBody;
import weasel.interpreter.WeaselModifier;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionInvoke;
import weasel.interpreter.bytecode.WeaselInstructionLoadVariable;

public class WeaselMethodBodyCompilerV2 extends WeaselMethodBody implements WeaselKeyWordCompilerHelper {

	protected final WeaselCompiler compiler;
	protected final WeaselClassCompilerV2 classCompiler;
	protected final List<WeaselToken> methodTokens;
	protected int methodTokenPos;
	protected final List<String> paramNames;
	protected final List<Integer> paramModifier;
	protected final WeaselGenericMethod2 wgm;
	protected WeaselBlockInfo block;
	protected boolean superCaller;
	
	protected WeaselMethodBodyCompilerV2(WeaselMethod method, WeaselClassCompilerV2 classCompiler, List<WeaselToken> methodTokens, List<String> paramNames, List<Integer> paramModifier, WeaselCompiler compiler) {
		super(method, classCompiler);
		this.compiler = compiler;
		this.classCompiler = classCompiler;
		instructions = new WeaselInstruction[0];
		this.methodTokens = methodTokens;
		this.paramNames = paramNames;
		this.paramModifier = paramModifier;
		wgm = new WeaselGenericMethod(classCompiler.genericClass, method).getMethod(new WeaselGenericClass[0]);
	}

	protected WeaselMethodBodyCompilerV2(WeaselMethod method, WeaselClassCompilerV2 classCompiler) {
		super(method, classCompiler);
		this.classCompiler = classCompiler;
		compiler = null;
		methodTokens = null;
		paramNames = null;
		paramModifier = null;
		wgm = new WeaselGenericMethod(classCompiler.genericClass, method).getMethod(new WeaselGenericClass[0]);
	}

	public void compile() {
		if(isNative()){
			return;
		}
		System.out.println(method+":"+methodTokens);
		if(WeaselModifier.isStatic(method.getModifier())){
			block = new WeaselBlockInfo(false, -paramNames.size() + 1);
		}else{
			block = new WeaselBlockInfo(false, -paramNames.size());
			block.newVar(0, "this", classCompiler.genericClass);
		}
		for(int i=0; i<paramNames.size(); i++){
			WeaselGenericMethod2 genericMethod = classCompiler.genericClass.getGenericMethod(method.getNameAndDesk(), null);
			block.newVar(paramModifier.get(i), paramNames.get(i), genericMethod.getGenericParams()[i]);
		}
		WeaselInstructionList instructions = new WeaselInstructionList();
		ListIterator<WeaselToken> iterator = methodTokens.listIterator();
		if(method.getName().equals("<preInit>")){
			if(method.getParentClass().getSuperClass()!=null){
				instructions.add(0, new WeaselInstructionLoadVariable(getVariable("this").pos));
				instructions.add(0, new WeaselInstructionInvoke(method.getParentClass().getSuperClass().getRealName()+".<preInit>()"));
			}
		}else if(method.getName().equals("<init>")){
			boolean auto = true;
			if(iterator.hasNext()){
				WeaselToken token = iterator.next();
				if(token!=null && token.tokenType==WeaselTokenType.KEYWORD){
					if(iterator.next().tokenType==WeaselTokenType.OPENBRACKET){
						try{
							if(token.param==WeaselKeyWord.THIS){
								superCaller = false;
								auto = false;
								WeaselTree tree = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
								List<WeaselGenericMethod2> methods = classCompiler.genericClass.getGenericMethodsOfThis("<init>", true);
								WeaselParameterCompileReturn wpcr = WeaselTree.compileParamList(token.line, "<init>", compiler, this, tree, methods);
								instructions.addAll(wpcr.instructions);
								instructions.add(token.line, new WeaselInstructionInvoke(wpcr.method.getMethod().getMethod().getNameAndDesk()));
								token = iterator.next();
								if(token.tokenType!=WeaselTokenType.SEMICOLON)
									throw new WeaselCompilerException(token.line, "Expect ; but got %s", token);
							}else if(token.param==WeaselKeyWord.SUPER){
								superCaller = true;
								auto = false;
								WeaselTree tree = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
								List<WeaselGenericMethod2> methods = classCompiler.genericClass.getGenericSuperClass().getGenericMethodsOfThis("<init>", true);
								WeaselParameterCompileReturn wpcr = WeaselTree.compileParamList(token.line, "<init>", compiler, this, tree, methods);
								instructions.addAll(wpcr.instructions);
								instructions.add(token.line, new WeaselInstructionInvoke(wpcr.method.getMethod().getMethod().getNameAndDesk()));
								token = iterator.next();
								if(token.tokenType!=WeaselTokenType.SEMICOLON)
									throw new WeaselCompilerException(token.line, "Expect ; but got %s", token);
							}else{
								iterator.previous();
								iterator.previous();
							}
						}catch(WeaselCompilerException e){
							compiler.addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, e.getLine(), parentClass.getFileName(), e.getMessage()));
							iterator.previous();
							token = iterator.next();
							while(token.tokenType!=WeaselTokenType.SEMICOLON && iterator.hasNext()){
								token = iterator.next();
							}
						}
					}else{
						iterator.previous();
						iterator.previous();
					}
				}else{
					iterator.previous();
				}
			}
			if(auto){
				superCaller = true;
				if(classCompiler.genericClass.getGenericSuperClass()!=null){
					WeaselGenericMethod2 method = classCompiler.genericClass.getGenericSuperClass().getGenericMethodOfThis("<init>()", new WeaselGenericClass[0]);
					if(method==null){
						compiler.addWeaselCompilerMessage(
								new WeaselCompilerMessage(MessageType.ERROR, 0, parentClass.getFileName(), 
										String.format("No default constructor in %s found", classCompiler.genericClass.getGenericSuperClass())));
					}else{
						instructions.add(0, new WeaselInstructionInvoke(method.getMethod().getMethod().getNameAndDesk()));
					}
				}
			}
		}
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
		System.out.println(instructions);
		this.instructions = instructions.getInstructions();
	}
	
	@Override
	public WeaselVariableInfo getVariable(String name) {
		return block.getVar(name);
	}

	@Override
	public List<WeaselGenericMethod2> getGenericMethods(String name) throws WeaselCompilerException {
		return classCompiler.genericClass.getGenericMethods(name, !WeaselModifier.isStatic(method.getModifier()));
	}

	@Override
	public WeaselGenericField getGenericField(String variable) {
		return classCompiler.genericClass.getGenericField(variable);
	}

	@Override
	public WeaselGenericMethod2 getCompilingMethod() {
		return wgm;
	}

	@Override
	public WeaselClassCompiler getClassCompiler() {
		return classCompiler;
	}

	@Override
	public WeaselVariableInfo newVar(int modifier, String varName, WeaselGenericClass wgc) {
		return block.newVar(modifier, varName, wgc);
	}

	@Override
	public void writeVar(WeaselVariableInfo wvi) {
		
	}

	@Override
	public void openBlock(boolean canAddBreaks) {
		block = new WeaselBlockInfo(canAddBreaks, block);
	}

	@Override
	public WeaselBlockInfo closeBlock() {
		WeaselBlockInfo b = block;
		block = block.base;
		return b;
	}

	@Override
	public int getVarCount() {
		WeaselBlockInfo b = block;
		int count = 0;
		while(b!=null){
			count += b.varsToPop();
			b = b.base;
		}
		return count;
	}
	
}
