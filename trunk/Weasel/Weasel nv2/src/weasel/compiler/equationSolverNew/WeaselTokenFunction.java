package weasel.compiler.equationSolverNew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.equationSolverNew.Solver.Security;
import weasel.compiler.equationSolverNew.Solver.String2D;

public class WeaselTokenFunction extends IWeaselTokenTreeElement {
	
	private final String name;
	private final FunctionType type;
	private WeaselToken generics[];
	private WeaselToken params[];
	private enum FunctionType{
		CREATE, CALL
	}
	
	public WeaselTokenFunction(String name, FunctionType ft, WeaselToken... gens){
		this.name = name;
		type = ft;
		generics = gens;
	}
	
	public void addGenerics(WeaselToken...tokens) throws WeaselCompilerException{
		WeaselToken error;
		if((error=Security.checkAllowed(WeaselTokenType.IDENT, tokens))!=null)
			throw new WeaselCompilerException(error.line, "The generic parameter %s is no Identifier", error.param);
		List<WeaselToken> t;
		if((t=Arrays.asList(generics)).addAll(Arrays.asList(tokens)))
			generics=t.toArray(generics);
	}
	
	public void addParams(WeaselToken...tokens){
		List<WeaselToken> t;
		if((t=Arrays.asList(params)).addAll(Arrays.asList(tokens)))
			params=t.toArray(params);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	private <T> List<T> instance(){
		
		return new ArrayList<T>();
	}

	@Override
	public String toString(){
		return toReadableString();
	}
	
	@Override
	public String toReadableString() {
		return getName();
	}
	
	@Override
	public String toEncryptedString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toClassView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WeaselToken simplify() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toAdvancedEncryptedString(String2D str) {

	}

	@Override
	public WeaselCompileReturn compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper) throws WeaselCompilerException {
		
		return null;
	}
}

