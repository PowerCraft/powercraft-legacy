package weasel.compiler.tokenmap;

import java.util.List;

import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselToken;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselTokenMapOperator extends WeaselTokenMap {

	public WeaselTokenMap left;
	public WeaselTokenMap right;
	
	public WeaselTokenMapOperator(WeaselToken token) {
		super(token);
	}

	public WeaselOperator getOperator(){
		return (WeaselOperator)token.param;
	}
	
	@Override
	public WeaselTokenMap addTokenMap(WeaselTokenMap tokenMap) throws WeaselCompilerException {
		if(left==null){
			if(tokenMap instanceof WeaselTokenMapOperator){
				WeaselTokenMapOperator tokenMapOperator = (WeaselTokenMapOperator)tokenMap;
				if(right==null){
					if(tokenMapOperator.getOperator().suffix && (tokenMapOperator.getOperator().precedence==-1||tokenMapOperator.getOperator().precedence>=getOperator().precedence)){
						right = tokenMap;
					}else{
						tokenMap.addLeftTokenMap(this);
						return tokenMap;
					}
				}else{
					right = right.addTokenMap(tokenMap);
				}
				return this;
			}else{
				if(right==null){
					right = tokenMap;
				}else{
					right = right.addTokenMap(tokenMap);
				}
				return this;
			}
		}else{
			if(tokenMap instanceof WeaselTokenMapOperator){
				WeaselTokenMapOperator tokenMapOperator = (WeaselTokenMapOperator)tokenMap;
				boolean addToRight = tokenMapOperator.getOperator().precedence>getOperator().precedence || tokenMapOperator.getOperator().precedence==-1;
				addToRight |= !getOperator().l2r && tokenMapOperator.getOperator().precedence==getOperator().precedence;
				addToRight |= !getOperator().suffix && right == null;
				addToRight &= right != null || tokenMapOperator.getOperator().prefix;
				addToRight &= !(left!=null && getOperator().precedence==-1);
				if(addToRight){
					if(right==null){
						right = tokenMap;
					}else{
						right = right.addTokenMap(tokenMap);
					}
					return this;
				}else{
					tokenMap.addLeftTokenMap(this);
					return tokenMap;
				}
			}else{
				if(right==null){
					right = tokenMap;
				}else{
					right = right.addTokenMap(tokenMap);
				}
				return this;
			}
		}
	}

	@Override
	protected void addLeftTokenMap(WeaselTokenMap tokenMap) throws WeaselCompilerException {
		left = tokenMap;
	}

	@Override
	public String toString() {
		return "["+left+"]"+token+"["+right+"]";
	}

	@Override
	public List<WeaselInstruction> compileTokenMap(WeaselKeyWordCompilerHelper weaselCompiler, int access, boolean pushThis) throws WeaselCompilerException {
		return null;//getOperator().compileOperator(weaselCompiler, access, pushThis, this);
	}

}
