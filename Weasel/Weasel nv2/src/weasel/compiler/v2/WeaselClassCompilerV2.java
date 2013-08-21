package weasel.compiler.v2;

import java.util.ArrayList;
import java.util.List;

import weasel.compiler.WeaselClassCompiler;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerMessage;
import weasel.compiler.WeaselCompilerMessage.MessageType;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenParser;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.keywords.WeaselKeyWord;
import weasel.interpreter.WeaselChecks;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselField;
import weasel.interpreter.WeaselGenericClassInfo;
import weasel.interpreter.WeaselGenericInformation;
import weasel.interpreter.WeaselMethod;
import weasel.interpreter.WeaselMethodBody;
import weasel.interpreter.WeaselModifier;
import weasel.interpreter.WeaselNativeException;
import weasel.interpreter.WeaselPrimitive;


public class WeaselClassCompilerV2 extends WeaselClassCompiler {

	public static final int classModifier = WeaselModifier.PUBLIC|WeaselModifier.FINAL|WeaselModifier.ABSTRACT;
	public static final int interfaceModifier = WeaselModifier.PUBLIC;
	public static final int enumModifier = WeaselModifier.PUBLIC;
	
	public List<WeaselToken> classPreInit = new ArrayList<WeaselToken>();
	public List<WeaselToken> classStaticInit = new ArrayList<WeaselToken>();
	
	protected WeaselClassCompilerV2(WeaselCompiler compiler, Object parent, String name, String fileName) {
		super(compiler, parent, name, fileName);
	}

	public WeaselClass getWeaselClass(int line, String className){
		for(int i=0; i<genericInformation.length; i++){
			if(className.equals("O"+genericInformation[i].genericName+";")){
				return genericInformation[i].genericInfo.genericClass;
			}
		}
		try{
			return interpreter.getWeaselClass(className);
		}catch(WeaselNativeException e){
			onException(line, e.getMessage());
		}
		return null;
	}
	
	public WeaselClass getWeaselClass2(int line, String className) throws WeaselCompilerException{
		for(int i=0; i<genericInformation.length; i++){
			if(className.equals("O"+genericInformation[i].genericName+";")){
				return genericInformation[i].genericInfo.genericClass;
			}
		}
		try{
			return interpreter.getWeaselClass(className);
		}catch(WeaselNativeException e){
			throw new WeaselCompilerException(line, e.getMessage());
		}
	}
	
	@Override
	public void compileEasy(String classSourceFor) {
		compiler.addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.INFO, 0, getFileName(), "Compiling Class"));
		tokenParser = new WeaselTokenParser(classSourceFor);
		List<WeaselToken> modifiers = new ArrayList<WeaselToken>();
		WeaselToken token = getNextToken();
		while(token.tokenType==WeaselTokenType.MODIFIER){
			modifiers.add(token);
			token = getNextToken();
		}
		expectKeyWord(token, WeaselKeyWord.CLASS, WeaselKeyWord.INTERFACE, WeaselKeyWord.ENUM);
		boolean isEnum = token.param == WeaselKeyWord.ENUM;
		isInterface = token.param == WeaselKeyWord.INTERFACE;
		boolean isClass = !(isEnum||isInterface);
		modifier = getModifier(modifiers, isEnum?enumModifier:isInterface?interfaceModifier:classModifier);
		modifiers.clear();
		if(isInterface)
			modifier |= WeaselModifier.ABSTRACT;
		if(isEnum)
			modifier |= WeaselModifier.FINAL;
		
		expect(token = getNextToken(), WeaselTokenType.IDENT);
		if(!name.equals(token.param)){
			onException(token.line, "ClassFile name %s and classname %s is not epual", name, token.param);
		}
		token = getNextToken();
		List<WeaselGenericInformation> genericList = new ArrayList<WeaselGenericInformation>();
		List<List<WeaselToken>> genericListClass = new ArrayList<List<WeaselToken>>();
		if(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.LESS && !isEnum){
			do{
				token = getNextToken();
				expect(token, WeaselTokenType.IDENT);
				String generic = (String)token.param;
				token = getNextToken();
				WeaselClass genericClass;
				if(token.tokenType == WeaselTokenType.KEYWORD && token.param == WeaselKeyWord.EXTENDS){
					token = getNextToken();
					expect(token, WeaselTokenType.IDENT);
					genericClass = interpreter.getWeaselClass("O"+(String)token.param+";");
					token = getNextToken();
					if(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.LESS){
						List<WeaselToken> genericListClass2 = new ArrayList<WeaselToken>();
						do{
							token = getNextToken();
							expect(token, WeaselTokenType.IDENT);
							genericListClass2.add(token);
							token = getNextToken();
						}while(token.tokenType == WeaselTokenType.COMMA);
						if(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.SHIFT_RIGHT){
							token = new WeaselToken(WeaselTokenType.OPERATOR, token.line, WeaselOperator.GREATER);
							tokenParser.setNextToken(token);
						}
						if(!(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.GREATER)){
							onException(token.line, "Expect > at end of generic but got %s", token);
						}
						token = getNextToken();
						genericListClass.add(genericListClass2);
					}else{
						genericListClass.add(null);
					}
				}else{
					genericClass = interpreter.baseTypes.getObjectClass();
					genericListClass.add(null);
				}
				genericList.add(new WeaselGenericInformation(generic, genericClass, -1));
			}while(token.tokenType == WeaselTokenType.COMMA);
			if(!(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.GREATER)){
				onException(token.line, "Expect > at end of generic declaration but got %s", token);
			}
			token = getNextToken();
		}
		genericInformation = genericList.toArray(new WeaselGenericInformation[0]);
		for(int i=0; i<genericInformation.length; i++){
			List<WeaselToken> genericListClass2 = genericListClass.get(i);
			if(genericListClass2!=null){
				genericInformation[i].genericInfo = makeGenericInfo(genericInformation[i].genericInfo.genericClass, genericListClass2.toArray(new WeaselToken[0]));
			}
		}
		System.out.println(genericList);
		genericInterfaces = new WeaselGenericClassInfo[0];
		if(isClass){
			if(token.param==WeaselKeyWord.EXTENDS){
				expect(token = getNextToken(), WeaselTokenType.IDENT);
				try{
					genericSuperClass = readGenericClass(token);
					WeaselChecks.checkSuperClass(genericSuperClass.genericClass);
					ids.method = genericSuperClass.genericClass.getIDS().method;
					ids.easyType = genericSuperClass.genericClass.getIDS().easyType;
					ids.objectRef = genericSuperClass.genericClass.getIDS().objectRef;
				}catch(WeaselNativeException e){
					onException(token.line, e.getMessage());
				}catch (WeaselCompilerException e) {
					onException(e.getLine(), e.getMessage());
				}
				token = getNextToken();
			}else{
				if(name.equals("Object")){
					ids.method = 1;
				}else{
					genericSuperClass = new WeaselGenericClassInfo(interpreter.baseTypes.getObjectClass(), -1, new WeaselGenericClassInfo[0]);
					ids.method = genericSuperClass.genericClass.getIDS().method;
					ids.easyType = genericSuperClass.genericClass.getIDS().easyType;
					ids.objectRef = genericSuperClass.genericClass.getIDS().objectRef;
				}
			}
			if(token.param==WeaselKeyWord.IMPLEMENTS){
				List<WeaselGenericClassInfo> interfaceGenericList = new ArrayList<WeaselGenericClassInfo>();
				do{
					expect(token = getNextToken(), WeaselTokenType.IDENT);
					try{
						WeaselGenericClassInfo interfa = readGenericClass(token);
						interfaceGenericList.add(interfa);
						WeaselChecks.checkInterface(interfa.genericClass);
					}catch(WeaselNativeException e){
						onException(token.line, e.getMessage());
					}catch (WeaselCompilerException e) {
						onException(e.getLine(), e.getMessage());
					}
					token = getNextToken();
				}while(token.tokenType==WeaselTokenType.COMMA);
				genericInterfaces = interfaceGenericList.toArray(genericInterfaces);
			}
		}
		if(isInterface){
			if(token.param==WeaselKeyWord.EXTENDS){
				List<WeaselGenericClassInfo> interfaceGenericList = new ArrayList<WeaselGenericClassInfo>();
				do{
					expect(token = getNextToken(), WeaselTokenType.IDENT);
					try{
						WeaselGenericClassInfo interfa = readGenericClass(token);
						interfaceGenericList.add(interfa);
						WeaselChecks.checkInterface2(interfa.genericClass, this);
					}catch(WeaselNativeException e){
						onException(token.line, e.getMessage());
					}catch (WeaselCompilerException e) {
						onException(e.getLine(), e.getMessage());
					}
					token = getNextToken();
				}while(token.tokenType==WeaselTokenType.COMMA);
				genericInterfaces = interfaceGenericList.toArray(genericInterfaces);
			}
		}
		if(isEnum){
			genericSuperClass = new WeaselGenericClassInfo(interpreter.baseTypes.getEnumClass(), -1, new WeaselGenericClassInfo[]{new WeaselGenericClassInfo(this, -1, new WeaselGenericClassInfo[0])});
			ids.method = genericSuperClass.genericClass.getIDS().method;
			ids.easyType = genericSuperClass.genericClass.getIDS().easyType;
			ids.objectRef = genericSuperClass.genericClass.getIDS().objectRef;
			if(token.param==WeaselKeyWord.IMPLEMENTS){
				List<WeaselGenericClassInfo> interfaceGenericList = new ArrayList<WeaselGenericClassInfo>();
				do{
					expect(token = getNextToken(), WeaselTokenType.IDENT);
					try{
						WeaselGenericClassInfo interfa = readGenericClass(token);
						interfaceGenericList.add(interfa);
						WeaselChecks.checkInterface(interfa.genericClass);
					}catch(WeaselNativeException e){
						onException(token.line, e.getMessage());
					}catch (WeaselCompilerException e) {
						onException(e.getLine(), e.getMessage());
					}
					token = getNextToken();
				}while(token.tokenType==WeaselTokenType.COMMA);
				genericInterfaces = interfaceGenericList.toArray(genericInterfaces);
			}
		}
		
		expect(token, WeaselTokenType.OPENBLOCK);
		
		token = getNextToken();
		
		ids.staticMethod++;
		
		methods = new WeaselMethod[isInterface()?1:2];
		staticMethodBodys = new WeaselMethodBody[ids.staticMethod];
		if(!isInterface())
			methodBodys = new WeaselMethodBody[ids.method];
		fields = new WeaselField[0];
		
		methods[0] = createMethod("<staticInit>", WeaselModifier.STATIC, this, new WeaselGenericClassInfo(interpreter.baseTypes.voidClass, -1, new WeaselGenericClassInfo[0]), new WeaselGenericClassInfo[0], ids.staticMethod-1);
		staticMethodBodys[ids.staticMethod-1] = new WeaselMethodBodyCompilerV2(methods[0], this, classStaticInit, new ArrayList<String>(), new ArrayList<Integer>(), compiler);
		if(!isInterface()){
			methods[1] = createMethod("<preInit>", 0, this, new WeaselGenericClassInfo(interpreter.baseTypes.voidClass, -1, new WeaselGenericClassInfo[0]), new WeaselGenericClassInfo[0], 0);
			methodBodys[0] = new WeaselMethodBodyCompilerV2(methods[1], this, classPreInit, new ArrayList<String>(), new ArrayList<Integer>(), compiler);
		}
		
		
		while(token.tokenType!=WeaselTokenType.CLOSEBLOCK&&token.tokenType!=WeaselTokenType.NONE){
			try{
				modifiers = new ArrayList<WeaselToken>();
				while(token.tokenType==WeaselTokenType.MODIFIER){
					modifiers.add(token);
					token = getNextToken();
				}
				
				String name;
				boolean isConstructor=token.tokenType == WeaselTokenType.IDENT && token.param.equals(this.name);
				WeaselGenericClassInfo typeInfo;
				if(isConstructor){
					typeInfo = new WeaselGenericClassInfo(interpreter.baseTypes.voidClass, -1, new WeaselGenericClassInfo[0]);
					name = "<init>";
					typeInfo = new WeaselGenericClassInfo(interpreter.baseTypes.voidClass, -1, new WeaselGenericClassInfo[0]);
				}else{
					typeInfo = readGenericClass(token);
					expect(token = getNextToken(), WeaselTokenType.IDENT);
					name = (String)token.param;
				}
				token = getNextToken();
				if(isConstructor){
					expect(token, WeaselTokenType.OPENBRACKET);
					try{
						compileMethod(modifiers, typeInfo, name, token);
					}catch(WeaselCompilerException e){
						onException(e.getLine(), e.getMessage());
					}
				}else{
					if(token.tokenType==WeaselTokenType.OPENBRACKET){
						try{
							compileMethod(modifiers, typeInfo, name, token);
						}catch(WeaselCompilerException e){
							onException(e.getLine(), e.getMessage());
						}
					}else{
						try{
							compileField(modifiers, typeInfo, name, token);
						}catch(WeaselCompilerException e){
							onException(e.getLine(), e.getMessage());
						}
					}
				}
				token = getNextToken();
			}catch(WeaselCompilerException e){
				onException(e.getLine(), e.getMessage());
				token = getNextToken();
			}
		}
		
		expect(token, WeaselTokenType.CLOSEBLOCK);
		expect(getNextToken(), WeaselTokenType.NONE);
		tokenParser = null;
		
	}
	
	private WeaselGenericClassInfo readGenericClass(WeaselToken token) throws WeaselCompilerException{
		expect(token, WeaselTokenType.IDENT);
		String className = WeaselCompiler.mapClassNames((String)token.param);
		int genericID = -1;
		WeaselClass weaselClass = null;
		for(int i=0; i<genericInformation.length; i++){
			if(className.equals("O"+genericInformation[i].genericName+";")){
				genericID = i;
				weaselClass = genericInformation[i].genericInfo.genericClass;
				break;
			}
		}
		if(genericID==-1){
			try{
				interpreter.getWeaselClass(className);
			}catch(WeaselNativeException e){
				throw new WeaselCompilerException(token.line, e.getMessage());
			}
		}
		token = getNextToken();
		List<WeaselGenericClassInfo> genericObjects = new ArrayList<WeaselGenericClassInfo>();
		if(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.LESS){
			do{
				token = getNextToken();
				expect(token, WeaselTokenType.IDENT);
				genericObjects.add(readGenericClass(token));
				token = getNextToken();
			}while(token.tokenType == WeaselTokenType.COMMA);
			if(!(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.GREATER)){
				if(!(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.SHIFT_RIGHT)){
					onException(token.line, "Expect > at end of generic");
				}else{
					token = new WeaselToken(WeaselTokenType.OPERATOR, token.line, WeaselOperator.GREATER);
				}
			}else{
				token = getNextToken();
			}
		}
		while(token.tokenType==WeaselTokenType.OPENINDEX){
			token = getNextToken();
			expect(token, WeaselTokenType.CLOSEINDEX);
			token = getNextToken();
			className = "["+className;
		}
		tokenParser.setNextToken(token);
		if(genericID==-1){
			try{
				weaselClass = interpreter.getWeaselClass(className);
			}catch(WeaselNativeException e){
				throw new WeaselCompilerException(token.line, e.getMessage());
			}
			return new WeaselGenericClassInfo(weaselClass, -1, genericObjects.toArray(new WeaselGenericClassInfo[0]));
		}else{
			while(className.charAt(0)=='['){
				weaselClass = new WeaselClass(interpreter, weaselClass, className, null);
				className = className.substring(1);
			}
			return new WeaselGenericClassInfo(weaselClass, genericID, genericObjects.toArray(new WeaselGenericClassInfo[0]));
		}
	}
	
	private WeaselGenericClassInfo makeGenericInfo(WeaselClass weaselClass, WeaselToken[]infos){
		WeaselGenericClassInfo[] gi = new WeaselGenericClassInfo[infos.length];
		for(int i=0; i<gi.length; i++){
			gi[i] = new WeaselGenericClassInfo(getWeaselClass(infos[i].line, "O"+(String)infos[i].param+";"), -1, new WeaselGenericClassInfo[0]);
		}
		return new WeaselGenericClassInfo(weaselClass, -1, gi);
	}
	
	private int getGenericInformationIndex(String generic){
		for(int i=0; i<genericInformation.length; i++){
			if(genericInformation[i].genericName.equals(generic)){
				return i;
			}
		}
		return -1;
	}
	
	private void compileField(List<WeaselToken> modifiers, WeaselGenericClassInfo typeInfo, String name, WeaselToken token) throws WeaselCompilerException{
		int modifier = getModifier(modifiers, WeaselField.normalModifier);
		String stype = typeInfo.genericClass.getByteName();
		while(token.tokenType==WeaselTokenType.OPENINDEX){
			token = getNextToken();
			expect(token, WeaselTokenType.CLOSEINDEX);
			token = getNextToken();
			stype = "["+stype;
		}
		token = compileField2(modifier, new WeaselGenericClassInfo(getWeaselClass2(token.line, stype), typeInfo.genericID, typeInfo.generics), name, token);
		while(token.tokenType==WeaselTokenType.COMMA){
			stype = typeInfo.genericClass.getByteName();
			token = getNextToken();
			expect(token, WeaselTokenType.IDENT);
			name = (String)token.param;
			while(token.tokenType==WeaselTokenType.OPENINDEX){
				token = getNextToken();
				expect(token, WeaselTokenType.CLOSEINDEX);
				token = getNextToken();
				stype = "["+stype;
			}
			token = compileField2(modifier, new WeaselGenericClassInfo(getWeaselClass2(token.line, stype), typeInfo.genericID, typeInfo.generics), name, token);
		}
		expect(token, WeaselTokenType.SEMICOLON);
	}
	
	private void arrayMaker(List<WeaselToken> tl, WeaselClass typeC, int line){
		if(typeC.isArray()){
			arrayMaker(tl, typeC.getArrayClass(), line);
			tl.add(new WeaselToken(WeaselTokenType.OPENINDEX, line));
			tl.add(new WeaselToken(WeaselTokenType.CLOSEINDEX, line));
		}else{
			tl.add(new WeaselToken(WeaselTokenType.IDENT, line, typeC.getRealName()));
		}
	}
	
	private WeaselToken compileField2(int modifier, WeaselGenericClassInfo typeInfo, String name, WeaselToken token) throws WeaselCompilerException{
		System.out.println("F:"+typeInfo);
		if(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.ASSIGN){
			List<WeaselToken> init;
			if(WeaselModifier.isStatic(modifier)){
				init = classStaticInit;
			}else{
				init = classPreInit;
				if(isInterface()){
					onException(token.line, "Interface field %s cant be set here", name);
				}
			}
			init.add(new WeaselToken(WeaselTokenType.IDENT, token.line, name));
			init.add(token);
			token = getNextToken();
			if(token.tokenType==WeaselTokenType.OPENBLOCK){
				init.add(new WeaselToken(WeaselTokenType.KEYWORD, token.line, WeaselKeyWord.NEW));
				arrayMaker(init, typeInfo.genericClass, token.line);
			}
			while(token.tokenType!=WeaselTokenType.SEMICOLON&&token.tokenType!=WeaselTokenType.COMMA){
				init.add(token);
				token = getNextToken();
				if(token.tokenType==WeaselTokenType.NONE){
					expect(token, WeaselTokenType.SEMICOLON);
					break;
				}
			}
			init.add(new WeaselToken(WeaselTokenType.SEMICOLON, token.line));
		}
		
		int id;
		if(WeaselModifier.isStatic(modifier)){
			if(typeInfo.genericClass.isPrimitive()){
				id = ids.staticEasyType++;
				if(WeaselPrimitive.getPrimitiveID(typeInfo.genericClass)==WeaselPrimitive.LONG||WeaselPrimitive.getPrimitiveID(typeInfo.genericClass)==WeaselPrimitive.DOUBLE){
					ids.staticEasyType++;
				}
			}else{
				id = ids.staticObjectRef++;
			}
		}else{
			if(typeInfo.genericClass.isPrimitive()){
				id = ids.easyType++;
				if(WeaselPrimitive.getPrimitiveID(typeInfo.genericClass)==WeaselPrimitive.LONG||WeaselPrimitive.getPrimitiveID(typeInfo.genericClass)==WeaselPrimitive.DOUBLE){
					ids.easyType++;
				}
			}else{
				id = ids.objectRef++;
			}
		}
		System.out.println("F:"+typeInfo);
		WeaselField field = createField(name, modifier, this, typeInfo, id);
		
		WeaselField[] newFields = new WeaselField[fields.length+1];
		for(int i=0; i<fields.length; i++){
			newFields[i] = fields[i];
		}
		newFields[fields.length] = field;
		fields = newFields;
		return token;
	}
	
	private void compileMethod(List<WeaselToken> modifiers, WeaselGenericClassInfo typeInfo, String name, WeaselToken token) throws WeaselCompilerException{
		boolean isConstructor = name.equals("<init>");
		boolean isPreConstructor = name.equals("<preInit>");
		boolean isStaticConstructor = name.equals("<staticInit>");
		int modifier = getModifier(modifiers, isStaticConstructor||isPreConstructor?0:isConstructor?isEnum()?0:
			WeaselMethod.constructorModifier:WeaselModifier.isAbstract(getModifier())?WeaselMethod.abstractModifier:WeaselMethod.normalModifier);
		if(isInterface() && !WeaselModifier.isStatic(modifier)){
			modifier |= WeaselModifier.ABSTRACT;
		}
		if(isInterface() && (isConstructor || isPreConstructor)){
			onException(token.line, "Interface can't have constructors");
		}
		List<WeaselGenericClassInfo> genericInfos = new ArrayList<WeaselGenericClassInfo>();
		List<String> paramNames = new ArrayList<String>();
		List<Integer> paramModifier = new ArrayList<Integer>();
		token = getNextToken();
		if(token.tokenType!=WeaselTokenType.CLOSEBRACKET){
			while(true){
				List<WeaselToken> mod = new ArrayList<WeaselToken>();
				while(token.tokenType==WeaselTokenType.MODIFIER){
					mod.add(token);
					token = getNextToken();
				}
				paramModifier.add(getModifier(mod, WeaselModifier.FINAL));
				genericInfos.add(readGenericClass(token));
				token = getNextToken();
				if(token.tokenType==WeaselTokenType.IDENT){
					paramNames.add((String)token.param);
					token = getNextToken();
				}else if(!WeaselModifier.isAbstract(modifier)){
					paramNames.add(null);
					expect(token, WeaselTokenType.IDENT);
				}else{
					paramNames.add(null);
				}
				if(token.tokenType!=WeaselTokenType.COMMA)
					break;
				token = getNextToken();
			}
		}
		expect(token, WeaselTokenType.CLOSEBRACKET);
		int id = WeaselModifier.isStatic(modifier)?ids.staticMethod++:ids.method++;
		WeaselMethod method = createMethod(name, modifier, this, typeInfo, genericInfos.toArray(new WeaselGenericClassInfo[0]), id);
		WeaselMethod[] newMethods = new WeaselMethod[methods.length+1];
		for(int i=0; i<methods.length; i++){
			newMethods[i] = methods[i];
		}
		newMethods[methods.length] = method;
		methods = newMethods;
		if(WeaselModifier.isStatic(modifier)){
			WeaselMethodBody[] newStaticMethodBodys = new WeaselMethodBody[ids.staticMethod];
			for(int i=0; i<staticMethodBodys.length; i++){
				newStaticMethodBodys[i] = staticMethodBodys[i];
			}
			staticMethodBodys = newStaticMethodBodys;
		}else{
			if(!isInterface()){
				WeaselMethodBody[] newMethodBodys = new WeaselMethodBody[ids.method];
				for(int i=0; i<methodBodys.length; i++){
					newMethodBodys[i] = methodBodys[i];
				}
				methodBodys = newMethodBodys;
			}
		}
		token = getNextToken();
		if(WeaselModifier.isAbstract(modifier)){
			expect(token, WeaselTokenType.SEMICOLON);
		}else if(WeaselModifier.isNative(modifier)){
			expect(token, WeaselTokenType.SEMICOLON);
			if(WeaselModifier.isStatic(modifier)){
				staticMethodBodys[id] = new WeaselMethodBodyCompilerV2(method, this);
			}else{
				methodBodys[id] = new WeaselMethodBodyCompilerV2(method, this);
			}
		}else{
			expect(token, WeaselTokenType.OPENBLOCK);
			List<WeaselToken> methodTokens = new ArrayList<WeaselToken>();
			token = getNextToken();
			int h=0;
			while(true){
				if(token.tokenType==WeaselTokenType.OPENBLOCK){
					h++;
				}else if(token.tokenType==WeaselTokenType.CLOSEBLOCK){
					if(h<=0){
						break;
					}else{
						h--;
					}
				}else if(token.tokenType==WeaselTokenType.NONE){
					expect(token, WeaselTokenType.CLOSEBLOCK);
				}
				methodTokens.add(token);
				token = getNextToken();
			}
			WeaselMethodBodyCompilerV2 mb = new WeaselMethodBodyCompilerV2(method, this, methodTokens, paramNames, paramModifier, compiler);
			if(WeaselModifier.isStatic(modifier)){
				staticMethodBodys[id] = mb;
			}else{
				methodBodys[id] = mb;
			}
		}
	}
	
	private int getModifier(List<WeaselToken> modifiers, int allowed){
		int modifier = 0;
		for(WeaselToken modifierToken:modifiers){
			int m = (Integer) modifierToken.param;
			if((m&allowed)==0){
				onException(modifierToken.line, "Modifier %s is not allowed, only %s %s permitted", WeaselModifier.toString(m),
							WeaselModifier.toString(allowed), WeaselModifier.count(allowed)==1?"is":"are");
			}else if((m&modifier)!=0){
				onException(modifierToken.line, "Duplicated modifier %s", WeaselModifier.toString(m));
			}else{
				modifier |= m;
				try {
					WeaselChecks.checkModifier(modifier, allowed);
				} catch (WeaselNativeException e) {
					onException(modifierToken.line, e.getMessage());
				}
			}
		}
		return modifier;
	}
	
	@Override
	public void finishCompile() {
		compiler.addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.INFO, 0, getFileName(), "Compiling Methods"));
		for(int i=0; i<staticMethodBodys.length; i++){
			((WeaselMethodBodyCompilerV2)staticMethodBodys[i]).compile();
		}
		if(!isInterface()){
			for(int i=0; i<methodBodys.length; i++){
				if(methodBodys[i]!=null){
					((WeaselMethodBodyCompilerV2)methodBodys[i]).compile();
				}
			}
		}
	}
	
}
