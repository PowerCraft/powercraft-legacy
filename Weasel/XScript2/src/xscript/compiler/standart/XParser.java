package xscript.compiler.standart;

import java.util.ArrayList;
import java.util.List;

import xscript.compiler.XLineDesk;
import xscript.compiler.XMessageLevel;
import xscript.compiler.XMessageList;
import xscript.compiler.XOperator;
import xscript.compiler.XOperator.Type;
import xscript.compiler.XToken;
import xscript.compiler.XTokenKind;
import xscript.compiler.XTree;
import xscript.compiler.XTree.XAnnotation;
import xscript.compiler.XTree.XBlock;
import xscript.compiler.XTree.XBreak;
import xscript.compiler.XTree.XClassDecl;
import xscript.compiler.XTree.XClassFile;
import xscript.compiler.XTree.XConstant;
import xscript.compiler.XTree.XContinue;
import xscript.compiler.XTree.XDo;
import xscript.compiler.XTree.XFor;
import xscript.compiler.XTree.XGroup;
import xscript.compiler.XTree.XIdent;
import xscript.compiler.XTree.XIf;
import xscript.compiler.XTree.XIfOperator;
import xscript.compiler.XTree.XImport;
import xscript.compiler.XTree.XIndex;
import xscript.compiler.XTree.XMethodCall;
import xscript.compiler.XTree.XMethodDecl;
import xscript.compiler.XTree.XModifier;
import xscript.compiler.XTree.XNew;
import xscript.compiler.XTree.XOperatorPrefixSuffix;
import xscript.compiler.XTree.XOperatorStatement;
import xscript.compiler.XTree.XReturn;
import xscript.compiler.XTree.XStatement;
import xscript.compiler.XTree.XSynchroized;
import xscript.compiler.XTree.XTag;
import xscript.compiler.XTree.XType;
import xscript.compiler.XTree.XTypeParam;
import xscript.compiler.XTree.XVarDecl;
import xscript.compiler.XTree.XVarDecls;
import xscript.compiler.XTree.XWhile;

public class XParser {

	private XLexer lexer;
	private XToken token;
	private XMessageList messages;
	private List<XLineDesk> lines = new ArrayList<XLineDesk>();
	private boolean unhandledUnexpected;
	private List<MessageBuffer> messageBuffer;
	
	public XParser(XLexer lexer, XMessageList messages){
		this.lexer = lexer;
		this.messages = messages;
		nextToken();
	}
	
	public void skip(boolean stopAtModifier, boolean stopAtType, boolean stopAtStatement, boolean stopAtClassDecl, boolean stopAtImport, boolean stopAtIdent, boolean stopAtGroup){
		unhandledUnexpected = false;
		while(token.kind!=XTokenKind.EOF){
			switch (token.kind) {
			case SEMICOLON:
				nextToken();
				return;
			case SYNCHRONIZED:
				if(stopAtModifier || stopAtStatement)
					return;
				break;
			case ABSTRACT:
			case AT:
			case FINAL:
			case NATIVE:
			case PRIVATE:
			case PROTECTED:
			case PUBLIC:
			case STATIC:
				if(stopAtModifier)
					return;
				break;
			case BOOL:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
			case VOID:
				if(stopAtType)
					return;
				break;
			case BREAK:
			case CASE:
			case CONTINUE:
			case DEFAULT:
			case DO:
			case ELSE:
			case FOR:
			case IF:
			case RETURN:
			case SWITCH:
			case WHILE:
			case THROW:
			case TRY:
			case CATCH:
			case FINALLY:
			case NEW:
				if(stopAtStatement)
					return;
				break;
			case CLASS:
			case ENUM:
			case INTERFACE:
			case ANNOTATION:
				if(stopAtClassDecl)
					return;
				break;
			case RBRAKET:
				if(stopAtGroup){
					return;
				}else if(stopAtStatement){
					nextToken();
					return;
				}
				break;
			case IMPORT:
				if(stopAtImport)
					return;
				break;
			case IDENT:
				if(stopAtIdent)
					return;
				break;
			case LBRAKET:
				if(stopAtStatement || stopAtGroup)
					return;
				break;
			default:
				break;
			}
			nextToken();
		}
	}
	
	public void syntaxError(String key, Object... args){
		parserMessage(XMessageLevel.ERROR, key, args);
	}
	
	public boolean expected(XTokenKind kind){
		if(token.kind==kind){
			nextToken();
			return true;
		}else{
			syntaxError("expected", kind);
			unhandledUnexpected = true;
			return false;
		}
	}
	
	public void startLineBlock(){
		lines.add(new XLineDesk(token.lineDesk.startLine, token.lineDesk.startLinePos, -1, -1));
	}
	
	public XLineDesk endLineBlock(){
		XLineDesk line = lines.remove(lines.size()-1);
		if(line.endLine==-1 && line.endLinePos==-1)
			return null;
		return line;
	}
	
	public int getModifier(){
		if(token.kind==XTokenKind.PUBLIC){
			return xscript.runtime.XModifier.PUBLIC;
		}else if(token.kind==XTokenKind.PRIVATE){
			return xscript.runtime.XModifier.PRIVATE;
		}else if(token.kind==XTokenKind.PROTECTED){
			return xscript.runtime.XModifier.PROTECTED;
		}else if(token.kind==XTokenKind.FINAL){
			return xscript.runtime.XModifier.FINAL;
		}else if(token.kind==XTokenKind.ABSTRACT){
			return xscript.runtime.XModifier.ABSTRACT;
		}else if(token.kind==XTokenKind.NATIVE){
			return xscript.runtime.XModifier.NATIVE;
		}else if(token.kind==XTokenKind.STATIC){
			return xscript.runtime.XModifier.STATIC;
		}else if(token.kind==XTokenKind.SYNCHRONIZED){
			return xscript.runtime.XModifier.SYNCHRONIZED;
		}
		return 0;
	}
	
	public XAnnotation makeAnnotation(){
		return null;
	}
	
	public XModifier makeModifier(){
		List<XAnnotation> annotations = null;
		int modifier = 0;
		int m;
		startLineBlock();
		while((m=getModifier())!=0 || token.kind==XTokenKind.AT){
			if(token.kind==XTokenKind.AT){
				if(annotations==null)
					annotations = new ArrayList<XTree.XAnnotation>();
				annotations.add(makeAnnotation());
			}else{
				if((m & modifier)==0){
					modifier |= m;
				}else{
					parserMessage(XMessageLevel.ERROR, "duplicated.modifier", token.kind.name);
				}
			}
			nextToken();
		}
		return new XModifier(endLineBlock(), modifier, annotations);
	}
	
	public String ident(){
		String name;
		if(token.kind==XTokenKind.IDENT){
			name = token.param;
			nextToken();
		}else{
			expected(XTokenKind.IDENT);
			name = "!error!";
		}
		return name;
	}
	
	public XIdent qualident(){
		startLineBlock();
		String name = ident();
		while(token.kind==XTokenKind.ELEMENT){
			nextToken();
			name += "."+ident();
		}
		return new XIdent(endLineBlock(), name);
	}
	
	public XImport makeImport(){
		startLineBlock();
		if(expected(XTokenKind.IMPORT)){
			boolean staticImport = false;
			if(token.kind==XTokenKind.STATIC){
				staticImport = true;
				nextToken();
			}
			String name = ident();
			boolean indirect = false;
			while(token.kind==XTokenKind.ELEMENT){
				nextToken();
				if(token.kind==XTokenKind.MUL){
					indirect = true;
					nextToken();
					break;
				}else{
					name += "."+ident();
				}
			}
			return new XImport(endLineBlock(), name, indirect, staticImport);
		}
		endLineBlock();
		return new XImport(token.lineDesk, "!error!", false, false);
	}
	
	public XType makeType(){
		startLineBlock();
		XIdent name;
		List<XType> typeParam = null;
		if(token.kind==XTokenKind.BOOL || token.kind==XTokenKind.BYTE || token.kind==XTokenKind.SHORT ||
				token.kind==XTokenKind.CHAR || token.kind==XTokenKind.INT || token.kind==XTokenKind.FLOAT ||
				token.kind==XTokenKind.DOUBLE || token.kind==XTokenKind.VOID){
			name = new XIdent(new XLineDesk(token.lineDesk), token.kind.name);
			nextToken();
		}else{
			name = qualident();
			if(token.kind==XTokenKind.SMALLER){
				nextToken();
				typeParam = new ArrayList<XTree.XType>();
				typeParam.add(makeType());
				while(token.kind==XTokenKind.COMMA){
					nextToken();
					typeParam.add(makeType());
				}
				expected(XTokenKind.GREATER);
			}
		}
		int array = 0;
		while(token.kind==XTokenKind.LINDEX){
			nextToken();
			expected(XTokenKind.RINDEX);
			array++;
		}
		return new XType(endLineBlock(), name, typeParam, array);
	}
	
	public List<XType> makeTypeList(XTokenKind split){
		List<XType> list = new ArrayList<XTree.XType>();
		list.add(makeType());
		while(token.kind==split){
			nextToken();
			list.add(makeType());
		}
		return list;
	}
	
	public XTypeParam makeTypeParam(){
		startLineBlock();
		XIdent name = qualident();
		boolean isSuper = false;
		List<XType> extend = null;
		if(token.kind==XTokenKind.EXTENDS || token.kind==XTokenKind.COLON){
			nextToken();
			extend = makeTypeList(XTokenKind.AND);
		}else if(token.kind==XTokenKind.SUPER){
			isSuper = true;
			nextToken();
			extend = makeTypeList(XTokenKind.AND);
		}
		return new XTypeParam(endLineBlock(), name, extend, isSuper);
	}
	
	public List<XTypeParam> makeTypeParamList(){
		List<XTypeParam> list = null;
		if(token.kind == XTokenKind.SMALLER){
			nextToken();
			list = new ArrayList<XTypeParam>();
			list.add(makeTypeParam());
			while(token.kind==XTokenKind.COMMA){
				nextToken();
				list.add(makeTypeParam());
			}
			expected(XTokenKind.GREATER);
		}
		return list;
	}
	
	public XVarDecl makeParamDecl(){
		startLineBlock();
		XModifier modifier = makeModifier();
		XType type = makeType();
		String name = ident();
		return makeVarDecl(endLineBlock(), modifier, type, name, 0);
	}
	
	public List<XVarDecl> makeParamList(){
		expected(XTokenKind.LGROUP);
		List<XVarDecl> list = new ArrayList<XTree.XVarDecl>();
		if(token.kind!=XTokenKind.RGROUP){
			list.add(makeParamDecl());
			while(token.kind==XTokenKind.COMMA){
				nextToken();
				list.add(makeParamDecl());
			}
		}
		expected(XTokenKind.RGROUP);
		return list;
	}
	
	public XVarDecls makeVarDeclStatement(XModifier modifier){
		XType type = makeType();
		XLineDesk line = new XLineDesk(token.lineDesk);
		String name = ident();
		XVarDecls varDecl = makeVarDecls(line, modifier, type, name);
		expected(XTokenKind.SEMICOLON);
		return varDecl;
	}
	
	public XStatement makeDeclStatement(){
		XModifier modifier = makeModifier();
		if(token.kind==XTokenKind.CLASS || token.kind==XTokenKind.INTERFACE || token.kind==XTokenKind.ENUM || token.kind==XTokenKind.ANNOTATION){
			return makeClassDecl(modifier);
		}else{
			return makeVarDeclStatement(modifier);
		}
	}
	
	public boolean isOperator(XTokenKind kind){
		return kind==XTokenKind.ADD || kind==XTokenKind.SUB || kind==XTokenKind.MUL
				|| kind==XTokenKind.DIV || kind==XTokenKind.MOD || kind==XTokenKind.NOT
				|| kind==XTokenKind.BNOT || kind==XTokenKind.AND || kind==XTokenKind.OR
				|| kind==XTokenKind.XOR || kind==XTokenKind.EQUAL || kind==XTokenKind.GREATER
				|| kind==XTokenKind.SMALLER || kind==XTokenKind.INSTANCEOF 
				|| kind==XTokenKind.ELEMENT || kind==XTokenKind.COLON || kind==XTokenKind.QUESTIONMARK;
	}
	
	public List<XToken> readOperators(){
		List<XToken> list = new ArrayList<XToken>();
		while(isOperator(token.kind)){
			list.add(token);
			nextToken();
		}
		return list;
	}
	
	public List<XStatement> makeMethodCallParamList() {
		expected(XTokenKind.LGROUP);
		List<XStatement> list = null;
		if(token.kind!=XTokenKind.RGROUP){
			list = new ArrayList<XTree.XStatement>();
			list.add(makeInnerStatement());
			while(token.kind==XTokenKind.COMMA){
				nextToken();
				list.add(makeInnerStatement());
			}
		}
		expected(XTokenKind.RGROUP);
		return list;
	}
	
	public XStatement makeNumRead(boolean b){
		XToken oldToken = token;
		switch(token.kind){
		case LGROUP:
			return makeGroup();
		case IDENT:
			return qualident();
		case FLOATLITERAL:
			nextToken();
			return new XConstant(oldToken.lineDesk, XTag.FLOATLITERAL, oldToken.param);
		case DOUBLELITERAL:
			nextToken();
			return new XConstant(oldToken.lineDesk, XTag.DOUBLELITERAL, oldToken.param);
		case LONGLITERAL:
			nextToken();
			return new XConstant(oldToken.lineDesk, XTag.LONGLITERAL, oldToken.param);
		case INTLITERAL:
			nextToken();
			return new XConstant(oldToken.lineDesk, XTag.INTLITERAL, oldToken.param);
		case CHARLITERAL:
			nextToken();
			return new XConstant(oldToken.lineDesk, XTag.CHARLITERAL, oldToken.param);
		case STRINGLITERAL: 
			nextToken();
			return new XConstant(oldToken.lineDesk, XTag.STRINGLITERAL, oldToken.param);
		case TRUE:
			nextToken();
			return new XConstant(oldToken.lineDesk, XTag.TRUE, oldToken.param);
		case FALSE:
			nextToken();
			return new XConstant(oldToken.lineDesk, XTag.FALSE, oldToken.param);
		case NULL:
			nextToken();
			return new XConstant(oldToken.lineDesk, XTag.NULL, oldToken.param);
		case NEW:
			startLineBlock();
			nextToken();
			XIdent className = qualident();
			if(token.kind==XTokenKind.LINDEX){
				
			}else{
				List<XStatement> params = makeMethodCallParamList();
				return new XNew(endLineBlock(), className, params);
			}
		default:
			if(b)
				parserMessage(XMessageLevel.ERROR, "unexpected", token.kind.name);
			return null;
		}
	}
	
	public XStatement makeStatementWithSuffixAndPrefix(){
		startLineBlock();
		XOperator operator;
		List<XOperator> prefix = new ArrayList<XOperator>();
		while(isOperator(token.kind)){
			operator = readOperator(Type.PREFIX);
			if(operator==XOperator.NONE)
				break;
			prefix.add(0, operator);
		}
		if(prefix.isEmpty())
			prefix = null;
		XStatement statement = makeNumRead(true);
		List<XOperator> suffix = new ArrayList<XOperator>();
		while(isOperator(token.kind)){
			operator = readOperator(Type.SUFFIX);
			if(operator==XOperator.NONE)
				break;
			suffix.add(operator);
		}
		if(suffix.isEmpty())
			suffix = null;
		if(prefix == null && suffix == null){
			endLineBlock();
		}else{
			statement = new XOperatorPrefixSuffix(endLineBlock(), prefix, statement, suffix);
		}
		while(token.kind==XTokenKind.LGROUP || token.kind==XTokenKind.LINDEX){
			startLineBlock();
			startLineBlock();
			if(token.kind==XTokenKind.LGROUP){
				List<XStatement> list = makeMethodCallParamList();
				statement = new XMethodCall(endLineBlock(), statement, list);
			}else if(token.kind==XTokenKind.LINDEX){
				nextToken();
				XStatement index = makeInnerStatement();
				expected(XTokenKind.RINDEX);
				statement = new XIndex(endLineBlock(), statement, index);
			}
			suffix = new ArrayList<XOperator>();
			while(isOperator(token.kind)){
				operator = readOperator(Type.SUFFIX);
				if(operator==XOperator.NONE)
					break;
				suffix.add(operator);
			}
			if(suffix.isEmpty())
				suffix = null;
			if(suffix == null){
				endLineBlock();
			}else{
				statement = new XOperatorPrefixSuffix(endLineBlock(), null, statement, suffix);
			}
		}
		return statement;
	}
	
	public XOperator readOperator(Type type){
		if(!isOperator(token.kind)){
			return XOperator.NONE;
		}
		XOperator[] all = XOperator.values();
		String s = token.kind.name;
		XToken oldToken = token;
		XOperator best = XOperator.NONE;
		boolean hasNext;
		lexer.notSure();
		do{
			nextToken();
			hasNext = false;
			for(XOperator o:all){
				if(o.type==type){
					if(o.op.equals(s)){
						best=o;
					}else if(o.op.startsWith(s)){
						hasNext = true;
					}
				}
			}
			if(hasNext && isOperator(token.kind) && !token.space){
				s += token.kind.name;
			}else{
				hasNext = false;
			}
		}while(hasNext);
		lexer.reset();
		token = oldToken;
		if(best!=XOperator.NONE){
			for(int i=0; i<best.op.length(); i++){
				nextToken();
			}
		}
		return best;
	}
	
	public XStatement mergeStatements(XLineDesk line, XStatement left, XOperator o, XStatement right, XStatement between){
		if(left instanceof XOperatorStatement){
			XOperatorStatement oLeft = (XOperatorStatement) left;
			if(oLeft.operator.priority<o.priority || (oLeft.operator.priority==o.priority && !XOperator.L2R[o.priority])){
				oLeft.right = mergeStatements(line, oLeft.right, o, right, between);
				return oLeft;
			}else{
				if(o==XOperator.IF)
					return new XIfOperator(line, left, between, right);
				return new XOperatorStatement(line, left, o, right);
			}
		}else{
			if(o==XOperator.IF)
				return new XIfOperator(line, left, between, right);
			return new XOperatorStatement(line, left, o, right);
		}
	}
	
	public XStatement makeInnerStatement(){
		XStatement statement = makeStatementWithSuffixAndPrefix();
		XStatement between = null;
		while(isOperator(token.kind)){
			startLineBlock();
			between = null;
			XOperator o = readOperator(Type.INFIX);
			if(o==XOperator.IF){
				between = makeStatementWithSuffixAndPrefix();
				expected(XTokenKind.COLON);
			}
			statement = mergeStatements(endLineBlock(), statement, o, makeStatementWithSuffixAndPrefix(), between);
		}
		return statement;
	}
	
	public XStatement makeStatement(){
		String lable = null;
		XStatement block = null;
		XStatement block2 = null;
		XStatement statement = null;
		XStatement statement2 = null;
		XStatement statement3 = null;
		XToken oldtoken;
		startLineBlock();
		switch(token.kind){
		case SYNCHRONIZED:
			lexer.notSure();
			oldtoken = token;
			nextToken();
			if(token.kind==XTokenKind.LGROUP){
				lexer.sure();
				nextToken();
				statement = makeInnerStatement();
				block = makeBlock();
				return new XSynchroized(endLineBlock(), statement, block);
			}else{
				lexer.reset();
				token = oldtoken;
			}
		case ABSTRACT:
		case AT:
		case FINAL:
		case NATIVE:
		case PRIVATE:
		case PROTECTED:
		case PUBLIC:
		case STATIC:
		case BOOL:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
		case VOID:
		case CLASS:
		case ENUM:
		case INTERFACE:
		case ANNOTATION:
			return makeDeclStatement();
		case BREAK:
			nextToken();
			if(token.kind==XTokenKind.IDENT){
				lable = ident();
			}
			expected(XTokenKind.SEMICOLON);
			return new XBreak(endLineBlock(), lable);
		case CONTINUE:
			nextToken();
			if(token.kind==XTokenKind.IDENT){
				lable = ident();
			}
			expected(XTokenKind.SEMICOLON);
			return new XContinue(endLineBlock(), lable);
		case DO:
			nextToken();
			block = makeStatement();
			expected(XTokenKind.WHILE);
			expected(XTokenKind.LGROUP);
			statement = makeInnerStatement();
			expected(XTokenKind.RGROUP);
			expected(XTokenKind.SEMICOLON);
			return new XDo(endLineBlock(), block, statement);
		case FOR:
			nextToken();
			expected(XTokenKind.LGROUP);
			if(token.kind==XTokenKind.SEMICOLON){
				nextToken();
			}else{
				statement = makeStatement();
			}
			if(token.kind==XTokenKind.SEMICOLON){
				nextToken();
			}else{
				statement2 = makeInnerStatement();
			}
			if(token.kind==XTokenKind.SEMICOLON){
				nextToken();
			}else{
				statement3 = makeInnerStatement();
			}
			expected(XTokenKind.RGROUP);
			block = makeStatement();
			return new XFor(endLineBlock(), statement, statement2, statement3, block);
		case IF:
			nextToken();
			expected(XTokenKind.LGROUP);
			statement = makeInnerStatement();
			expected(XTokenKind.RGROUP);
			block = makeStatement();
			if(token.kind==XTokenKind.ELSE){
				nextToken();
				block = makeStatement();
			}
			return new XIf(endLineBlock(), statement, block, block2);
		case RETURN:
			nextToken();
			statement = makeInnerStatement();
			return new XReturn(endLineBlock(), statement);
		case SWITCH:
			parserMessage(XMessageLevel.ERROR, "unexpected.keyword", token.kind.name);
			return null;
		case WHILE:
			nextToken();
			expected(XTokenKind.LGROUP);
			statement = makeInnerStatement();
			expected(XTokenKind.RGROUP);
			block = makeStatement();
			return new XWhile(endLineBlock(), block, statement);
		case THROW:
			nextToken();
			statement = makeInnerStatement();
			return new XReturn(endLineBlock(), statement);
		case TRY:
			nextToken();
			parserMessage(XMessageLevel.ERROR, "unexpected.keyword", token.kind.name);
			return null;
		case LBRAKET:
			endLineBlock();
			return makeBlock();
		case CASE:
		case DEFAULT:
			endLineBlock();
			nextToken();
			parserMessage(XMessageLevel.ERROR, "unexpected.keyword", token.kind.name);
			return null;
		case ELSE:
		case CATCH:
		case FINALLY:
			parserMessage(XMessageLevel.ERROR, "unexpected.keyword", token.kind.name);
			nextToken();
			makeBlock();
			endLineBlock();
			return null;
		case IDENT:
			lexer.notSure();
			oldtoken = token;
			startMessageBuffer();
			boolean bv = unhandledUnexpected;
			unhandledUnexpected = false;
			XType type = makeType();
			boolean knowRealy = type.array!=0;
			XVarDecls decl = null;
			if(token.kind==XTokenKind.IDENT || knowRealy){
				knowRealy |= type.typeParam==null || type.typeParam.size()!=1;
				XLineDesk line = new XLineDesk(token.lineDesk);
				String name = ident();
				if(token.kind==XTokenKind.LINDEX){
					nextToken();
					expected(XTokenKind.RINDEX);
					decl = makeVarDecls(line, new XModifier(line, 0), type, name, 1);
				}else if(token.kind==XTokenKind.EQUAL || token.kind==XTokenKind.SEMICOLON){
					decl = makeVarDecls(line, new XModifier(line, 0), type, name);
				}
			}
			if(knowRealy || decl!=null){
				endMessageBuffer(true);
				lexer.sure();
				unhandledUnexpected|=bv;
				expected(XTokenKind.SEMICOLON);
				return decl;
			}else{
				endMessageBuffer(false);
				lexer.reset();
				unhandledUnexpected=bv;
				token = oldtoken;
			}
		case ADD:
		case BNOT:
		case CHARLITERAL:
		case DOUBLELITERAL:
		case FALSE:
		case FLOATLITERAL:
		case INTLITERAL:
		case LGROUP:
		case LONGLITERAL:
		case NOT:
		case NULL:
		case STRINGLITERAL:
		case SUB:
		case SUPER:
		case THIS:
		case TRUE:
			statement = makeInnerStatement();
			expected(XTokenKind.SEMICOLON);
			return statement;
		case SEMICOLON:
			endLineBlock();
			nextToken();
			return null;
		default:
			endLineBlock();
			parserMessage(XMessageLevel.ERROR, "unexpected", token.kind.name);
			return null;
		}
	}

	private XStatement makeGroup() {
		startLineBlock();
		expected(XTokenKind.LGROUP);
		XStatement statement = makeInnerStatement();
		expected(XTokenKind.RGROUP);
		return new XGroup(endLineBlock(), statement);
	}

	public XBlock makeBlock(){
		startLineBlock();
		if(expected(XTokenKind.LBRAKET)){
			List<XStatement> statements = new ArrayList<XTree.XStatement>();
			while(token.kind!=XTokenKind.RBRAKET && token.kind!=XTokenKind.EOF){
				statements.add(makeStatement());
				if(unhandledUnexpected){
					skip(false, true, true, true, false, true, true);
				}
			}
			expected(XTokenKind.RBRAKET);
			return new XBlock(endLineBlock(), statements);
		}
		endLineBlock();
		return null;
	}
	
	public XMethodDecl makeMethodDecl(XLineDesk line, XModifier modifier, List<XTypeParam> typeParam, XType returnType, String name, boolean isInterface){
		List<XVarDecl> paramTypes = makeParamList();
		List<XType> throwList = null;
		if(token.kind==XTokenKind.THROWS){
			throwList = makeTypeList(XTokenKind.COMMA);
		}
		XBlock block = null;
		if((isInterface && token.kind!=XTokenKind.DEFAULT) || xscript.runtime.XModifier.isAbstract(modifier.modifier)){
			if(!expected(XTokenKind.SEMICOLON) && token.kind==XTokenKind.LBRAKET){
				block = makeBlock();
			}
		}else{
			if(isInterface){
				nextToken();
			}
			block = makeBlock();
		}
		return new XMethodDecl(line, modifier, name, typeParam, returnType, paramTypes, throwList, block);
	}
	
	public XVarDecl makeVarDecl(XLineDesk line, XModifier modifier, XType type, String name, int arrayAdd){
		while(token.kind==XTokenKind.LINDEX){
			nextToken();
			expected(XTokenKind.RINDEX);
			arrayAdd++;
		}
		if(arrayAdd!=0){
			type = new XType(type.line, type.name, type.typeParam, type.array+arrayAdd);
		}
		XStatement init = null;
		if(token.kind==XTokenKind.EQUAL){
			nextToken();
			init = makeInnerStatement();
		}
		return new XVarDecl(line, modifier, name, type, init);
	}
	
	public XVarDecls makeVarDecls(XLineDesk line, XModifier modifier, XType type, String name, int arrayAdd){
		List<XVarDecl> list = new ArrayList<XVarDecl>();
		startLineBlock();
		list.add(makeVarDecl(line, modifier, type, name, arrayAdd));
		while(token.kind==XTokenKind.COMMA){
			nextToken();
			XLineDesk lline = new XLineDesk(token.lineDesk);
			name = ident();
			list.add(makeVarDecl(lline, modifier, type, name, 0));
		}
		line.endLine = token.lineDesk.endLine;
		line.endLinePos = token.lineDesk.endLinePos;
		return new XVarDecls(line, list);
	}
	
	public XVarDecls makeVarDecls(XLineDesk line, XModifier modifier, XType type, String name){
		return makeVarDecls(line, modifier, type, name, 0);
	}
	
	public XTree classAndInterfaceBodyDecl(boolean isInterface, String className){
		XModifier modifier = makeModifier();
		if(token.kind==XTokenKind.CLASS || token.kind==XTokenKind.INTERFACE || token.kind==XTokenKind.ENUM || token.kind==XTokenKind.ANNOTATION){
			return classDecl(modifier);
		}
		List<XTypeParam> typeParam = makeTypeParamList();
		XType type = makeType();
		boolean isConstructor = token.kind==XTokenKind.LGROUP && type.name.name.equals(className);
		XLineDesk line = new XLineDesk(token.lineDesk);
		String name = isConstructor?"<init>":ident();
		if(isConstructor || token.kind==XTokenKind.LGROUP){
			return makeMethodDecl(line, modifier, typeParam, type, name, isInterface);
		}else{
			XTree tree = makeVarDecls(line, modifier, type, name);
			expected(XTokenKind.SEMICOLON);
			return tree;
		}
	}
	
	public List<XTree> classAndInterfaceBody(boolean isInterface, String className){
		expected(XTokenKind.LBRAKET);
		if(unhandledUnexpected){
			skip(false, false, false, false, false, false, true);
			if(token.kind==XTokenKind.LBRAKET)
				nextToken();
		}
		List<XTree> list = new ArrayList<XTree>();
		while(token.kind!=XTokenKind.EOF && token.kind!=XTokenKind.RBRAKET){
			list.add(classAndInterfaceBodyDecl(isInterface, className));
			if(unhandledUnexpected){
				skip(true, true, false, true, false, true, true);
			}
		}
		expected(XTokenKind.RBRAKET);
		return list;
	}
	
	public XClassDecl classDecl(XModifier modifier){
		startLineBlock();
		expected(XTokenKind.CLASS);
		String name = ident();
		List<XTypeParam> typeParam = makeTypeParamList();
		List<XType> superClasses = null;
		if(token.kind==XTokenKind.COLON){
			nextToken();
			superClasses = makeTypeList(XTokenKind.COMMA);
		}else if(token.kind==XTokenKind.EXTENDS || token.kind==XTokenKind.IMPLEMENTS){
			superClasses = new ArrayList<XType>();
			parserMessage(XMessageLevel.INFO, "newextends");
			if(token.kind==XTokenKind.EXTENDS){
				nextToken();
				superClasses.add(makeType());
			}
			if(token.kind==XTokenKind.IMPLEMENTS){
				nextToken();
				superClasses.addAll(makeTypeList(XTokenKind.COMMA));
			}
		}
		XLineDesk line = endLineBlock();
		List<XTree> body = classAndInterfaceBody(false, name);
		return new XClassDecl(line, modifier, name, typeParam, superClasses, body);
	}
	
	public XClassDecl interfaceDecl(XModifier modifier){
		startLineBlock();
		expected(XTokenKind.INTERFACE);
		modifier.modifier |= xscript.runtime.XModifier.ABSTRACT;
		String name = ident();
		List<XTypeParam> typeParam = makeTypeParamList();
		List<XType> superClasses = null;
		if(token.kind==XTokenKind.COLON){
			nextToken();
			superClasses = makeTypeList(XTokenKind.COMMA);
		}else if(token.kind==XTokenKind.EXTENDS){
			nextToken();
			parserMessage(XMessageLevel.INFO, "newextends");
			superClasses = makeTypeList(XTokenKind.COMMA);
		}
		XLineDesk line = endLineBlock();
		List<XTree> body = classAndInterfaceBody(true, name);
		return new XClassDecl(line, modifier, name, typeParam, superClasses, body);
	}
	
	public List<XTree> enumBody(String name){
		expected(XTokenKind.LBRAKET);
		if(unhandledUnexpected){
			skip(true, true, false, true, false, true, true);
			if(token.kind==XTokenKind.LBRAKET)
				nextToken();
		}
		List<XTree> list = new ArrayList<XTree>();
		if(token.kind==XTokenKind.SEMICOLON){
			nextToken();
			while(token.kind!=XTokenKind.EOF && token.kind!=XTokenKind.RBRAKET){
				list.add(classAndInterfaceBodyDecl(false, name));
				if(unhandledUnexpected){
					skip(true, true, false, true, false, true, true);
				}
			}
		}
		expected(XTokenKind.RBRAKET);
		return list;
	}
	
	public XClassDecl enumDecl(XModifier modifier){
		startLineBlock();
		expected(XTokenKind.ENUM);
		modifier.modifier |= xscript.runtime.XModifier.FINAL;
		String name = ident();
		List<XType> superClasses = new ArrayList<XTree.XType>();
		if(token.kind==XTokenKind.COLON){
			nextToken();
			superClasses.addAll(makeTypeList(XTokenKind.COMMA));
		}else if(token.kind==XTokenKind.IMPLEMENTS){
			nextToken();
			parserMessage(XMessageLevel.INFO, "newextends");
			superClasses.addAll(makeTypeList(XTokenKind.COMMA));
		}
		XLineDesk line = endLineBlock();
		List<XTree> body = enumBody(name);
		return new XClassDecl(line, modifier, name, null, superClasses, body);
	}
	
	public List<XTree> annotationBody(String name){
		return null;
	}
	
	public XClassDecl annotationDecl(XModifier modifier){
		startLineBlock();
		expected(XTokenKind.ANNOTATION);
		String name = ident();
		XLineDesk line = endLineBlock();
		List<XTree> body = annotationBody(name);
		return new XClassDecl(line, modifier, name, null, new ArrayList<XTree.XType>(), body);
	}
	
	public XClassDecl makeClassDecl(XModifier modifier){
		if(token.kind==XTokenKind.CLASS){
			return classDecl(modifier);
		}else if(token.kind==XTokenKind.INTERFACE){
			return interfaceDecl(modifier);
		}else if(token.kind==XTokenKind.ENUM){
			return enumDecl(modifier);
		}else if(token.kind==XTokenKind.ANNOTATION){
			return annotationDecl(modifier);
		}else{
			parserMessage(XMessageLevel.ERROR, "expected", "class, interface & enum");
			unhandledUnexpected = true;
			return null;
		}
	}
	
	public XTree makeTree(){
		startLineBlock();
		List<XTree> defs = new ArrayList<XTree>();
		List<XAnnotation> annotations = null;
		XIdent packageName = null;
		XModifier modifier = makeModifier();
		if(token.kind==XTokenKind.PACKAGE){
			annotations = modifier.annotations;
			if(modifier.modifier!=0){
				parserMessage(XMessageLevel.ERROR, "modifier.not_expected", xscript.runtime.XModifier.toString(modifier.modifier));
			}
			packageName = qualident();
			expected(XTokenKind.SEMICOLON);
			modifier = null;
		}
		while(token.kind!=XTokenKind.EOF){
			if(unhandledUnexpected){
				skip(true, false, false, true, true, false, false);
			}
			if(modifier==null)
				modifier = makeModifier();
			if(unhandledUnexpected){
				modifier=null;
				continue;
			}
			if(token.kind==XTokenKind.IMPORT){
				defs.add(makeImport());
			}else{
				defs.add(makeClassDecl(modifier));
			}
			modifier = null;
		}
		return new XClassFile(endLineBlock(), annotations, packageName, defs);
	}
	
	public void nextToken(){
		for(XLineDesk line:lines){
			line.endLine = token.lineDesk.endLine;
			line.endLinePos = token.lineDesk.endLinePos;
		}
		token = lexer.getNextToken();
	}
	
	private void parserMessage(XMessageLevel level, String key, Object...args){
		parserMessage(level, key, token.lineDesk, args);
	}
	
	private void parserMessage(XMessageLevel level, String key, XLineDesk lineDesk, Object...args){
		if(messageBuffer!=null){
			MessageBuffer buffer = new MessageBuffer();
			buffer.level = level;
			buffer.key = key;
			buffer.lineDesk = lineDesk;
			buffer.args = args;
			messageBuffer.add(buffer);
		}else{
			messages.postMessage(level, "parser."+key, lineDesk, args);
		}
	}
	
	private void startMessageBuffer(){
		messageBuffer = new ArrayList<XParser.MessageBuffer>();
	}
	
	private void endMessageBuffer(boolean post){
		if(post){
			for(MessageBuffer message:messageBuffer){
				messages.postMessage(message.level, "parser."+message.key, message.lineDesk, message.args);
			}
		}
		messageBuffer = null;
	}
	
	private static class MessageBuffer{
		XMessageLevel level;
		String key;
		XLineDesk lineDesk;
		Object[]args;
	}
	
}
