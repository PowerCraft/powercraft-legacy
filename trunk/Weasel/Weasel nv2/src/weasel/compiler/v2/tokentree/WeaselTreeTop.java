package weasel.compiler.v2.tokentree;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.WeaselVariableInfo;
import weasel.compiler.equationSolverNew.WeaselCompileReturn;
import weasel.compiler.keywords.WeaselKeyWord;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselGenericField;
import weasel.interpreter.WeaselGenericMethod;
import weasel.interpreter.WeaselGenericMethod2;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstBoolean;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstDouble;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstInteger;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstString;
import weasel.interpreter.bytecode.WeaselInstructionLoadNull;
import weasel.interpreter.bytecode.WeaselInstructionLoadVariable;
import weasel.interpreter.bytecode.WeaselInstructionReadFieldOf;
import weasel.interpreter.bytecode.WeaselInstructionSaveVariable;
import weasel.interpreter.bytecode.WeaselInstructionWriteFieldOf;

public class WeaselTreeTop extends WeaselTree {

	private WeaselToken token;
	private String newClass;
	private WeaselTree tree;
	private WeaselTree func;
	private boolean isFunc;
	private boolean isIndex;
	private WeaselTreeGeneric generic;
	private List<Integer> arraySize;
	private WeaselArrayInit arrayInit;
	
	public WeaselTreeTop(WeaselTree tree, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		this.tree = tree;
		if(iterator.hasNext()){
			WeaselToken token = iterator.next();
			if(token.tokenType==WeaselTokenType.OPENBRACKET){
				isFunc = true;
				func = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
			}else if(token.tokenType==WeaselTokenType.OPENINDEX){
				isIndex = true;
				func = WeaselTree.parse(iterator, WeaselTokenType.CLOSEINDEX);
			}else{
				iterator.previous();
			}
		}
	}

	public WeaselTreeTop(WeaselToken token, WeaselTreeGeneric generic, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		this.token = token;
		this.generic = generic;
		if(token.tokenType==WeaselTokenType.KEYWORD && token.param == WeaselKeyWord.NEW){
			
			do{
				token = iterator.next();
				if(token.tokenType!=WeaselTokenType.IDENT)
					throw new WeaselCompilerException(token.line, "Expect Ident but got %s", token);
				if(newClass==null){
					newClass = (String)token.param;
				}else{
					newClass += "."+(String)token.param;
				}
				token = iterator.next();
			}while(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.ELEMENT);
			if(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.LESS){
				generic = new WeaselTreeGeneric(iterator);
				if(generic.close){
					throw new WeaselCompilerException(token.line, "Expect Ident but got >");
				}
				token = iterator.next();
			}
			if(token.tokenType==WeaselTokenType.OPENBRACKET){
				func = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
			}else if(token.tokenType==WeaselTokenType.OPENINDEX){
				arraySize = new ArrayList<Integer>();
				boolean size = true;
				boolean anySizeDesk=false;
				while(true){
					token = iterator.next();
					if(token.tokenType!=WeaselTokenType.CLOSEINDEX && size){
						if(token.tokenType!=WeaselTokenType.INTEGER){
							throw new WeaselCompilerException(token.line, "Expect Integer as array size but got %s", token);
						}
						arraySize.add((Integer) token.param);
						token = iterator.next();
						anySizeDesk = true;
					}else{
						size = false;
					}
					if(token.tokenType!=WeaselTokenType.CLOSEINDEX){
						throw new WeaselCompilerException(token.line, "Expect ] but got %s", token);
					}
					if(iterator.hasNext()){
						token = iterator.next();
						if(token.tokenType!=WeaselTokenType.OPENINDEX){
							iterator.previous();
							break;
						}
					}else{
						break;
					}
				}
				if(iterator.hasNext()){
					token = iterator.next();
					if(token.tokenType==WeaselTokenType.OPENBLOCK){
						if(anySizeDesk){
							throw new WeaselCompilerException(token.line, "Can't make array initializer while array have static size");
						}
						arrayInit = new WeaselArrayInit(iterator, arraySize.size());
					}else{
						iterator.previous();
					}
				}
			}else{
				throw new WeaselCompilerException(token.line, "Expect ( or [ but got %s", token);
			}
		}else{
			if(iterator.hasNext()){
				token = iterator.next();
				if(token.tokenType==WeaselTokenType.OPENBRACKET){
					isFunc = true;
					func = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
				}else if(token.tokenType==WeaselTokenType.OPENINDEX){
					isIndex = true;
					func = WeaselTree.parse(iterator, WeaselTokenType.CLOSEINDEX);
				}else{
					iterator.previous();
				}
			}
		}
	}
	
	@Override
	public WeaselTreeAddResult add(List<WeaselToken> suffix, WeaselToken infix, List<WeaselToken> prefix, WeaselTree weaselTree, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		WeaselTree wtl;
		if(infix!=null){
			wtl = new WeaselTreeLevel(this, suffix, infix, prefix, weaselTree, iterator);
		}else if(suffix!=null && !suffix.isEmpty()){
			wtl = new WeaselTreeLevel(this, suffix, iterator);
		}else if(prefix!=null && !prefix.isEmpty()){
			wtl = new WeaselTreeLevel(prefix, this, iterator);
		}else{
			return new WeaselTreeAddResult(this);
		}
		return new WeaselTreeAddResult(wtl);
	}

	@Override
	public WeaselCompileReturn compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselGenericClass write, WeaselGenericClass expect, WeaselGenericClass elementParent, boolean isVariable) throws WeaselCompilerException {
		List<WeaselInstruction> instructions = new ArrayList<WeaselInstruction>();
		if(newClass!=null){
			
			WeaselClass weaselClass = compiler.getWeaselClass(newClass);
			
			
			
			return null;
		}else if(isFunc){
			List<WeaselGenericMethod2> methods;
			if(elementParent==null){
				methods = compilerHelper.getGenericMethods((String)token.param);
			}else{
				methods = elementParent.getGenericMethods((String)token.param, isVariable);
			}
			if(methods.isEmpty()){
				throw new WeaselCompilerException(token.line, "Method not found %s", token);
			}
			System.out.println(methods);
			return null;
		}else if(isIndex){
			
			return null;
		}else if(token==null){
			return tree.compile(compiler, compilerHelper, write, expect, elementParent, isVariable);
		}else{
			switch(token.tokenType){
			case BOOL:
				if(write!=null){
					throw new WeaselCompilerException(token.line, "Can't write %s to constant %s", write, token);
				}
				instructions.add(new WeaselInstructionLoadConstBoolean((Boolean) token.param));
				return new WeaselCompileReturn(instructions, compiler.baseTypes.booleanClass, compiler);
			case DOUBLE:
				if(write!=null){
					throw new WeaselCompilerException(token.line, "Can't write %s to constant %s", write, token);
				}
				instructions.add(new WeaselInstructionLoadConstDouble((Double) token.param));
				return new WeaselCompileReturn(instructions, compiler.baseTypes.doubleClass, compiler);
			case IDENT:
				String variable = (String)token.param;
				WeaselVariableInfo wvi = compilerHelper.getVariable(variable);
				if(wvi==null){
					wvi = compilerHelper.getVariable("this");
					if(wvi==null){
						throw new WeaselCompilerException(token.line, "Variable not declared bevore %s", variable);
					}
					WeaselGenericField wf = wvi.type.getGenericField(variable);
					if(wf==null){
						throw new WeaselCompilerException(token.line, "Variable not declared bevore %s", variable);
					}
					if(write==null){
						instructions.add(new WeaselInstructionReadFieldOf(wvi.pos, wf.getField().getDesk()));
					}else{
						if(!write.canCastTo(wf.getGenericType())){
							throw new WeaselCompilerException(token.line, "Can't write %s to variable %s", write, wf);
						}
						instructions.add(new WeaselInstructionWriteFieldOf(wvi.pos, wf.getField().getDesk()));
					}
					return new WeaselCompileReturn(instructions, wf.getGenericType());
				}else{
					if(write==null){
						instructions.add(new WeaselInstructionLoadVariable(wvi.pos));
					}else{
						if(!write.canCastTo(wvi.type)){
							throw new WeaselCompilerException(token.line, "Can't write %s to variable %s", write, wvi);
						}
						instructions.add(new WeaselInstructionSaveVariable(wvi.pos));
					}
					return new WeaselCompileReturn(instructions, wvi.type);
				}
			case INTEGER:
				if(write!=null){
					throw new WeaselCompilerException(token.line, "Can't write %s to constant %s", write, token);
				}
				instructions.add(new WeaselInstructionLoadConstInteger((Integer) token.param));
				return new WeaselCompileReturn(instructions, compiler.baseTypes.intClass, compiler);
			case NULL:
				if(write!=null){
					throw new WeaselCompilerException(token.line, "Can't write %s to constant %s", write, token);
				}
				instructions.add(new WeaselInstructionLoadNull());
				return new WeaselCompileReturn(instructions, null);
			case STRING:
				if(write!=null){
					throw new WeaselCompilerException(token.line, "Can't write %s to constant %s", write, token);
				}
				instructions.add(new WeaselInstructionLoadConstString((String) token.param));
				return new WeaselCompileReturn(instructions, compiler.baseTypes.getStringClass(), compiler);
			default:
				throw new WeaselCompilerException(token.line, "Expect ident but got %s", token);
			}
		}
	}

	@Override
	public String toString() {
		if(newClass==null){
			return (generic==null?"":generic.toString())+(token==null?"("+tree.toString()+")":token.toString())+(isFunc?func==null?"()":"("+func.toString()+")":"")+(isIndex?func==null?"[]":"["+func.toString()+"]":"");
		}else{
			String s = "new " + newClass + (generic==null?"":generic.toString());
			if(arraySize==null){
				return s + "("+(func==null?"":func.toString())+")";
			}
			for(Integer i:arraySize){
				s += "["+(i==null?"":i)+"]";
			}
			return s + (arrayInit==null?"":arrayInit.toString());
		}
	}

}
