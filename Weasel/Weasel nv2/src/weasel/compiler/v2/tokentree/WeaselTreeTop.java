package weasel.compiler.v2.tokentree;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.WeaselVariableInfo;
import weasel.compiler.keywords.WeaselKeyWord;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselGenericField;
import weasel.interpreter.WeaselGenericMethod2;
import weasel.interpreter.WeaselModifier;
import weasel.interpreter.WeaselNativeException;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionInvoke;
import weasel.interpreter.bytecode.WeaselInstructionInvokeStatic;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstBoolean;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstChar;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstDouble;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstInteger;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstString;
import weasel.interpreter.bytecode.WeaselInstructionLoadNull;
import weasel.interpreter.bytecode.WeaselInstructionLoadVariable;
import weasel.interpreter.bytecode.WeaselInstructionNew;
import weasel.interpreter.bytecode.WeaselInstructionNewArray;
import weasel.interpreter.bytecode.WeaselInstructionPush;
import weasel.interpreter.bytecode.WeaselInstructionReadField;
import weasel.interpreter.bytecode.WeaselInstructionReadFieldOf;
import weasel.interpreter.bytecode.WeaselInstructionReadIndex;
import weasel.interpreter.bytecode.WeaselInstructionReadStaticField;
import weasel.interpreter.bytecode.WeaselInstructionSaveVariable;
import weasel.interpreter.bytecode.WeaselInstructionWriteField;
import weasel.interpreter.bytecode.WeaselInstructionWriteIndex;
import weasel.interpreter.bytecode.WeaselInstructionWriteStaticField;

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
				System.out.println("Func");
				func = WeaselTree.parse(iterator, WeaselTokenType.CLOSEBRACKET);
			}else if(token.tokenType==WeaselTokenType.OPENINDEX){
				isIndex = true;
				System.out.println("Index");
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
				this.generic = new WeaselTreeGeneric(iterator);
				if(this.generic.close){
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
	public WeaselCompilerReturn compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselGenericClass write, WeaselGenericClass expect, WeaselGenericClass elementParent, boolean isVariable) throws WeaselCompilerException {
		List<WeaselInstruction> instructions = new ArrayList<WeaselInstruction>();
		if(newClass!=null){
			
			WeaselClass weaselClass = compiler.getWeaselClass(WeaselClass.mapClassNames(newClass));
			//TODO
			WeaselGenericClass genericClass = new WeaselGenericClass(weaselClass);
			
			if(arraySize==null){
				
				instructions.add(new WeaselInstructionNew(weaselClass.getByteName()));
				
				instructions.add(new WeaselInstructionPush(1));
				
				instructions.add(new WeaselInstructionInvoke(weaselClass.getRealName()+".<preInit>()"));
				
				List<WeaselGenericMethod2> methods = genericClass.getGenericMethods("<init>", true);
				
				instructions.add(new WeaselInstructionPush(1));
				
				WeaselParameterCompileReturn wcr = WeaselTree.compileParamList(compiler, compilerHelper, func, methods);
				
				instructions.addAll(wcr.instructios);
				
				instructions.add(new WeaselInstructionInvoke(wcr.method.getMethod().getMethod().getNameAndDesk()));
				
			}else{
				
				String className = weaselClass.getByteName();
				for(int i=0; i<arraySize.size(); i++){
					className = "["+className;
				}
				
				genericClass = new WeaselGenericClass(compiler.getWeaselClass(className), genericClass.getGenerics());
				
				if(arrayInit==null){
					
					instructions.addAll(arrayInit.compile(compiler, compilerHelper, genericClass));
					
				}else{
					
					List<Integer> realSize = new ArrayList<Integer>();
					
					className = weaselClass.getByteName();
					
					for(Integer i:arraySize){
						if(i==null){
							className = "["+className;
						}else{
							realSize.add(i);
						}
					}
					
					int[] rs = new int[realSize.size()];
					for(int i=0; i<rs.length; i++){
						rs[i] = realSize.get(i);
					}
					
					instructions.add(new WeaselInstructionNewArray(className, rs));
					
				}
				
			}
			
			return new WeaselCompilerReturn(instructions, genericClass);
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
			
			WeaselParameterCompileReturn wcr = WeaselTree.compileParamList(compiler, compilerHelper, func, methods);
			
			if(elementParent==null){
				if(!WeaselModifier.isStatic(wcr.method.getMethod().getMethod().getModifier())){
					instructions.add(new WeaselInstructionLoadVariable(compilerHelper.getVariable("this").pos));
				}
			}
			
			instructions.addAll(wcr.instructios);
			
			if(elementParent==null){
				if(WeaselModifier.isStatic(wcr.method.getMethod().getMethod().getModifier())){
					instructions.add(new WeaselInstructionInvokeStatic(wcr.method.getMethod().getMethod().getNameAndDesk()));
				}else{
					instructions.add(new WeaselInstructionInvoke(wcr.method.getMethod().getMethod().getNameAndDesk()));
				}
			}else{
				if(isVariable){
					instructions.add(new WeaselInstructionInvoke(wcr.method.getMethod().getMethod().getNameAndDesk()));
				}else{
					instructions.add(new WeaselInstructionInvokeStatic(wcr.method.getMethod().getMethod().getNameAndDesk()));
				}
			}
			
			return new WeaselCompilerReturn(instructions, wcr.method.getGenericReturn());
		}else if(isIndex){
			WeaselGenericClass arrayClass;
			if(elementParent==null){
				WeaselVariableInfo wvi = compilerHelper.getVariable((String)token.param);
				if(wvi==null){
					wvi = compilerHelper.getVariable("this");
					if(wvi==null)
						throw new WeaselCompilerException(token.line, "Variable %s not declared bevore", token);
					WeaselGenericField field = wvi.type.getGenericField((String)token.param);
					if(field==null)
						throw new WeaselCompilerException(token.line, "Variable %s not declared bevore", token);
					arrayClass = field.getGenericType();
					instructions.add(new WeaselInstructionReadFieldOf(wvi.pos, field.getField().getDesk()));
				}else{
					arrayClass = wvi.type;
					instructions.add(new WeaselInstructionLoadVariable(wvi.pos));
				}
			}else{
				WeaselGenericField field = elementParent.getGenericField((String)token.param);
				if(field==null)
					throw new WeaselCompilerException(token.line, "Variable %s not declared in %s", token, elementParent);
				arrayClass = field.getGenericType();
				instructions.add(new WeaselInstructionReadField(field.getField().getDesk()));
			}
			if(!arrayClass.getBaseClass().isArray()){
				throw new WeaselCompilerException(token.line, "%s is not an array", arrayClass);
			}
			WeaselCompilerReturn wcr = func.compile(compiler, compilerHelper, null, new WeaselGenericClass(compiler.baseTypes.intClass), null, false);
			if(wcr.returnType.getBaseClass()!=compiler.baseTypes.intClass)
				throw new WeaselCompilerException(token.line, "Index have to be a int not a %s", wcr.returnType);
			instructions.addAll(wcr.instructions);
			if(write==null){
				instructions.add(new WeaselInstructionReadIndex(WeaselPrimitive.getPrimitiveID(arrayClass.getBaseClass().getArrayClass())));
			}else{
				instructions.add(new WeaselInstructionPlaceHolder());
				WeaselTree.autoCast(compiler, write, new WeaselGenericClass(arrayClass.getBaseClass().getArrayClass(), arrayClass.getGenerics()), token.line, instructions, true);
				instructions.add(new WeaselInstructionWriteIndex(WeaselPrimitive.getPrimitiveID(arrayClass.getBaseClass().getArrayClass())));
			}
			return new WeaselCompilerReturn(instructions, new WeaselGenericClass(arrayClass.getBaseClass().getArrayClass(), arrayClass.getGenerics()));
		}else if(token==null){
			return tree.compile(compiler, compilerHelper, write, expect, elementParent, isVariable);
		}else{
			switch(token.tokenType){
			case BOOL:
				if(write!=null){
					throw new WeaselCompilerException(token.line, "Can't write %s to constant %s", write, token);
				}
				instructions.add(new WeaselInstructionLoadConstBoolean((Boolean) token.param));
				return new WeaselCompilerReturn(instructions, new WeaselGenericClass(compiler.baseTypes.booleanClass));
			case DOUBLE:
				if(write!=null){
					throw new WeaselCompilerException(token.line, "Can't write %s to constant %s", write, token);
				}
				instructions.add(new WeaselInstructionLoadConstDouble((Double) token.param));
				return new WeaselCompilerReturn(instructions, new WeaselGenericClass(compiler.baseTypes.doubleClass));
			case KEYWORD:
				if(token.param!=WeaselKeyWord.THIS)
					throw new WeaselCompilerException(token.line, "Expect ident but got %s", token);
			case IDENT:
				String variable = token.toString();
				if(elementParent==null){
					WeaselVariableInfo wvi = compilerHelper.getVariable(variable);
					if(wvi==null){
						WeaselGenericField wf = compilerHelper.getGenericField(variable);
						if(wf==null){
							WeaselClass weaselClass;
							try{
								weaselClass = compiler.getWeaselClass(WeaselClass.mapClassNames(variable));
							}catch(WeaselNativeException e){
								throw new WeaselCompilerException(token.line, "Variable not declared bevore %s", variable);
							}
							return new WeaselCompilerReturn(instructions, new WeaselGenericClass(weaselClass), true);
						}
						if(WeaselModifier.isStatic(compilerHelper.getCompilingMethod().getMethod().getMethod().getModifier())){
							if(!WeaselModifier.isStatic(wf.getField().getModifier())){
								throw new WeaselCompilerException(token.line, "Variable %s is not static", variable);
							}
						}
						if(write==null){
							instructions.add(new WeaselInstructionReadStaticField(wf.getField().getDesk()));
						}else{
							instructions.add(new WeaselInstructionPlaceHolder());
							WeaselTree.autoCast(compiler, write, wf.getGenericType(), token.line, instructions, true);
							instructions.add(new WeaselInstructionWriteStaticField(wf.getField().getDesk()));
						}
						return new WeaselCompilerReturn(instructions, wf.getGenericType());
					}else{
						if(write==null){
							instructions.add(new WeaselInstructionLoadVariable(wvi.pos));
						}else{
							instructions.add(new WeaselInstructionPlaceHolder());
							WeaselTree.autoCast(compiler, write, wvi.type, token.line, instructions, true);
							instructions.add(new WeaselInstructionSaveVariable(wvi.pos));
						}
						return new WeaselCompilerReturn(instructions, wvi.type);
					}
				}else{
					WeaselGenericField wf = elementParent.getGenericField(variable);
					if(wf==null){
						WeaselClass weaselClass;
						try{
							weaselClass = compiler.getWeaselClass(WeaselClass.mapClassNames(variable));
						}catch(WeaselNativeException e){
							throw new WeaselCompilerException(token.line, "Variable not declared bevore %s", variable);
						}
						if(isVariable){
							throw new WeaselCompilerException(token.line, "Can't get class form variable", variable);
						}
						return new WeaselCompilerReturn(instructions, new WeaselGenericClass(weaselClass), true);
					}
					if(isVariable){
						if(write==null){
							instructions.add(new WeaselInstructionReadField(wf.getField().getDesk()));
						}else{
							instructions.add(new WeaselInstructionPlaceHolder());
							WeaselTree.autoCast(compiler, write, wf.getGenericType(), token.line, instructions, true);
							instructions.add(new WeaselInstructionWriteField(wf.getField().getDesk()));
						}
					}else{
						if(!WeaselModifier.isStatic(wf.getField().getModifier()))
							throw new WeaselCompilerException(token.line, "Filed %s isn't static", wf);
						if(write==null){
							instructions.add(new WeaselInstructionReadStaticField(wf.getField().getDesk()));
						}else{
							instructions.add(new WeaselInstructionPlaceHolder());
							WeaselTree.autoCast(compiler, write, wf.getGenericType(), token.line, instructions, true);
							instructions.add(new WeaselInstructionWriteStaticField(wf.getField().getDesk()));
						}
					}
					return new WeaselCompilerReturn(instructions, wf.getGenericType());
				}
			case INTEGER:
				if(write!=null){
					throw new WeaselCompilerException(token.line, "Can't write %s to constant %s", write, token);
				}
				instructions.add(new WeaselInstructionLoadConstInteger((Integer) token.param));
				return new WeaselCompilerReturn(instructions, new WeaselGenericClass(compiler.baseTypes.intClass));
			case NULL:
				if(write!=null){
					throw new WeaselCompilerException(token.line, "Can't write %s to constant %s", write, token);
				}
				instructions.add(new WeaselInstructionLoadNull());
				return new WeaselCompilerReturn(instructions, null);
			case STRING:
				if(write!=null){
					throw new WeaselCompilerException(token.line, "Can't write %s to constant %s", write, token);
				}
				instructions.add(new WeaselInstructionLoadConstString((String) token.param));
				return new WeaselCompilerReturn(instructions, new WeaselGenericClass(compiler.baseTypes.getStringClass()));
			case CHAR:
				if(write!=null){
					throw new WeaselCompilerException(token.line, "Can't write %s to constant %s", write, token);
				}
				String s = (String) token.param;
				if(s.length()!=1)
					throw new WeaselCompilerException(token.line, "Only one char expected");
				instructions.add(new WeaselInstructionLoadConstChar(s.charAt(0)));
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
