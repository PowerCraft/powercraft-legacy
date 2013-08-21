package weasel.compiler.v2;

import java.util.ArrayList;
import java.util.HashMap;
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
import weasel.interpreter.WeaselGenericInfo;
import weasel.interpreter.WeaselGenericInformation;
import weasel.interpreter.WeaselMethod;
import weasel.interpreter.WeaselMethodBody;
import weasel.interpreter.WeaselModifier;
import weasel.interpreter.WeaselNativeException;


public class WeaselClassCompilerV2 extends WeaselClassCompiler {

	public static final int classModifier = WeaselModifier.PUBLIC|WeaselModifier.FINAL|WeaselModifier.ABSTRACT;
	public static final int interfaceModifier = WeaselModifier.PUBLIC;
	public static final int enumModifier = WeaselModifier.PUBLIC;
	
	public List<WeaselToken> classPreInit = new ArrayList<WeaselToken>();
	public List<WeaselToken> classStaticInit = new ArrayList<WeaselToken>();
	public HashMap<String, WeaselClass> generics = new HashMap<String, WeaselClass>();
	
	protected WeaselClassCompilerV2(WeaselCompiler compiler, Object parent, String name, String fileName) {
		super(compiler, parent, name, fileName);
	}

	public WeaselClass getWeaselClass(int line, String className){
		try{
			return interpreter.getWeaselClass(className);
		}catch(WeaselNativeException e){
			onException(line, e.getMessage());
		}
		return null;
	}
	
	public WeaselClass getWeaselClass2(int line, String className) throws WeaselCompilerException{
		if(generics.containsKey(className))
			return generics.get(className);
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
						if(!(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.GREATER)){
							onException(token.line, "Expect > at end of generic");
						}
						genericListClass.add(genericListClass2);
					}else{
						genericListClass.add(null);
					}
				}else{
					genericClass = interpreter.baseTypes.getObjectClass();
					genericListClass.add(null);
				}
				genericList.add(new WeaselGenericInformation(generic, genericClass));
			}while(token.tokenType == WeaselTokenType.COMMA);
			if(!(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.GREATER)){
				onException(token.line, "Expect > at end of generic");
			}
			token = getNextToken();
		}
		genericInformation = genericList.toArray(new WeaselGenericInformation[0]);
		for(int i=0; i<genericInformation.length; i++){
			List<WeaselToken> genericListClass2 = genericListClass.get(i);
			if(genericListClass2!=null){
				genericInformation[i].genericInfo = makeGenericInfo(genericListClass2.toArray(new WeaselToken[0]));
			}
		}
		if(isClass){
			if(token.param==WeaselKeyWord.EXTENDS){
				expect(token = getNextToken(), WeaselTokenType.IDENT);
				try{
					superClass = getWeaselClass(token.line, "O"+token.param+";");
					WeaselChecks.checkSuperClass(superClass);
					ids.method = superClass.getIDS().method;
					ids.easyType = superClass.getIDS().easyType;
					ids.objectRef = superClass.getIDS().objectRef;
				}catch(WeaselNativeException e){
					onException(token.line, e.getMessage());
				}
				superClassGeneric = readGenericInfo(getNextToken());
				token = getNextToken();
			}else{
				if(name.equals("Object")){
					ids.method = 1;
				}else{
					superClass = interpreter.baseTypes.getObjectClass();
					ids.method = superClass.getIDS().method;
					ids.easyType = superClass.getIDS().easyType;
					ids.objectRef = superClass.getIDS().objectRef;
				}
				superClassGeneric = new WeaselGenericInfo(new Object[0]);
			}
			if(token.param==WeaselKeyWord.IMPLEMENTS){
				List<WeaselClass> interfaceList = new ArrayList<WeaselClass>();
				List<WeaselGenericInfo> interfaceGenericList = new ArrayList<WeaselGenericInfo>();
				do{
					expect(token = getNextToken(), WeaselTokenType.IDENT);
					try{
						WeaselClass interfa = getWeaselClass(token.line, "O"+token.param+";");
						if(interfaceList.contains(interfa)){
							onException(token.line, "Duplicated implements of %s", interfa);
						}else{
							interfaceList.add(interfa);
							interfaceGenericList.add(readGenericInfo(getNextToken()));
						}
						WeaselChecks.checkInterface(interfa);
					}catch(WeaselNativeException e){
						onException(token.line, e.getMessage());
					}
					token = getNextToken();
				}while(token.tokenType==WeaselTokenType.COMMA);
				interfaces = interfaceList.toArray(new WeaselClass[0]);
				interfacesGeneric = interfaceGenericList.toArray(new WeaselGenericInfo[0]);
			}
		}
		if(isInterface){
			if(token.param==WeaselKeyWord.EXTENDS){
				List<WeaselClass> interfaceList = new ArrayList<WeaselClass>();
				List<WeaselGenericInfo> interfaceGenericList = new ArrayList<WeaselGenericInfo>();
				do{
					expect(token = getNextToken(), WeaselTokenType.IDENT);
					try{
						WeaselClass interfa = getWeaselClass(token.line, "O"+token.param+";");
						if(interfaceList.contains(interfa)){
							onException(token.line, "Duplicated extends of %s", interfa);
						}else{
							interfaceList.add(interfa);
							interfaceGenericList.add(readGenericInfo(getNextToken()));
						}
						WeaselChecks.checkInterface2(interfa, this);
					}catch(WeaselNativeException e){
						onException(token.line, e.getMessage());
					}
					token = getNextToken();
				}while(token.tokenType==WeaselTokenType.COMMA);
				interfaces = interfaceList.toArray(new WeaselClass[0]);
				interfacesGeneric = interfaceGenericList.toArray(new WeaselGenericInfo[0]);
			}
		}
		if(isEnum){
			superClass = interpreter.baseTypes.getEnumClass();
			superClassGeneric = new WeaselGenericInfo(new Object[]{this});
			ids.method = superClass.getIDS().method;
			ids.easyType = superClass.getIDS().easyType;
			ids.objectRef = superClass.getIDS().objectRef;
			if(token.param==WeaselKeyWord.IMPLEMENTS){
				List<WeaselClass> interfaceList = new ArrayList<WeaselClass>();
				List<WeaselGenericInfo> interfaceGenericList = new ArrayList<WeaselGenericInfo>();
				do{
					expect(token = getNextToken(), WeaselTokenType.IDENT);
					try{
						WeaselClass interfa = getWeaselClass(token.line, "O"+token.param+";");
						if(interfaceList.contains(interfa)){
							onException(token.line, "Duplicated implements of %s", interfa);
						}else{
							interfaceList.add(interfa);
							interfaceGenericList.add(readGenericInfo(getNextToken()));
						}
						WeaselChecks.checkInterface(interfa);
					}catch(WeaselNativeException e){
						onException(token.line, e.getMessage());
					}
					token = getNextToken();
				}while(token.tokenType==WeaselTokenType.COMMA);
				interfaces = interfaceList.toArray(new WeaselClass[0]);
				interfacesGeneric = interfaceGenericList.toArray(new WeaselGenericInfo[0]);
			}
		}
		
		expect(token, WeaselTokenType.OPENBLOCK);
		
		token = getNextToken();
		
		ids.staticMethod++;
		
		methods = new WeaselMethod[2];
		staticMethodBodys = new WeaselMethodBody[ids.staticMethod];
		if(!isInterface())
			methodBodys = new WeaselMethodBody[ids.method];
		fields = new WeaselField[0];
		
		methods[0] = createMethod("<staticInit>", WeaselModifier.STATIC, this, interpreter.baseTypes.voidClass, new WeaselClass[0], ids.staticMethod-1);
		staticMethodBodys[ids.staticMethod-1] = new WeaselMethodBodyCompilerV2(methods[0], this, classStaticInit, new ArrayList<String>(), new ArrayList<Integer>(), compiler);
		if(!isInterface()){
			methods[1] = createMethod("<preInit>", 0, this, interpreter.baseTypes.voidClass, new WeaselClass[0], 0);
			methodBodys[0] = new WeaselMethodBodyCompilerV2(methods[1], this, classPreInit, new ArrayList<String>(), new ArrayList<Integer>(), compiler);
		}
		
		
		while(token.tokenType!=WeaselTokenType.CLOSEBLOCK&&token.tokenType!=WeaselTokenType.NONE){
			try{
				modifiers = new ArrayList<WeaselToken>();
				while(token.tokenType==WeaselTokenType.MODIFIER){
					modifiers.add(token);
					token = getNextToken();
				}
				String className;
				String name;
				boolean isConstructor=token.tokenType == WeaselTokenType.IDENT && token.param.equals(this.name);
				if(isConstructor){
					className = "O"+this.name+";";
					name = "<init>";
				}else{
					className = readClass(token);
					expect(token = getNextToken(), WeaselTokenType.IDENT);
					name = (String)token.param;
				}
				token = getNextToken();
				if(isConstructor){
					expect(token, WeaselTokenType.OPENBRACKET);
					try{
						compileMethod(modifiers, className, name, token);
					}catch(WeaselCompilerException e){
						onException(e.getLine(), e.getMessage());
					}
				}else{
					if(token.tokenType==WeaselTokenType.OPENBRACKET){
						try{
							compileMethod(modifiers, className, name, token);
						}catch(WeaselCompilerException e){
							onException(e.getLine(), e.getMessage());
						}
					}else{
						try{
							compileField(modifiers, className, name, token);
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
	
	private WeaselGenericInfo makeGenericInfo(WeaselToken[]infos){
		Object[] gi = new Object[infos.length];
		for(int i=0; i<gi.length; i++){
			int gii = getGenericInformationIndex((String)infos[i].param);
			if(gii==-1){
				gi[i] = getWeaselClass(infos[i].line, "O"+(String)infos[i].param+";");
			}else{
				gi[i] = gii;
			}
		}
		return new WeaselGenericInfo(gi);
	}
	
	private int getGenericInformationIndex(String generic){
		for(int i=0; i<genericInformation.length; i++){
			if(genericInformation[i].genericName.equals(generic)){
				return i;
			}
		}
		return -1;
	}
	
	private String readClass(WeaselToken token) throws WeaselCompilerException{
		expect(token, WeaselTokenType.IDENT);
		String className = WeaselCompiler.mapClassNames((String)token.param);
		getWeaselClass2(token.line, className);
		token = getNextToken();
		while(token.tokenType==WeaselTokenType.OPENINDEX){
			token = getNextToken();
			expect(token, WeaselTokenType.CLOSEINDEX);
			token = getNextToken();
			className = "["+className;
		}
		tokenParser.setNextToken(token);
		return className;
	}
	
	private WeaselGenericInfo readGenericInfo(WeaselToken token){
		if(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.LESS){
			List<WeaselToken> genericListClass = new ArrayList<WeaselToken>();
			do{
				token = getNextToken();
				expect(token, WeaselTokenType.IDENT);
				genericListClass.add(token);
				token = getNextToken();
			}while(token.tokenType == WeaselTokenType.COMMA);
			if(!(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.GREATER)){
				onException(token.line, "Expect > at end of generic");
			}
			return makeGenericInfo(genericListClass.toArray(new WeaselToken[0]));
		}else{
			tokenParser.setNextToken(token);
			return new WeaselGenericInfo(new Object[0]);
		}
	}
	
	private void compileField(List<WeaselToken> modifiers, String type, String name, WeaselToken token) throws WeaselCompilerException{
		int modifier = getModifier(modifiers, WeaselField.normalModifier);
		String stype = type;
		while(token.tokenType==WeaselTokenType.OPENINDEX){
			token = getNextToken();
			expect(token, WeaselTokenType.CLOSEINDEX);
			token = getNextToken();
			stype = "["+stype;
		}
		token = compileField2(modifier, stype, name, token);
		while(token.tokenType==WeaselTokenType.COMMA){
			stype = type;
			token = getNextToken();
			expect(token, WeaselTokenType.IDENT);
			name = (String)token.param;
			while(token.tokenType==WeaselTokenType.OPENINDEX){
				token = getNextToken();
				expect(token, WeaselTokenType.CLOSEINDEX);
				token = getNextToken();
				stype = "["+stype;
			}
			token = compileField2(modifier, stype, name, token);
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
	
	private WeaselToken compileField2(int modifier, String type, String name, WeaselToken token) throws WeaselCompilerException{
		WeaselClass typeC = getWeaselClass2(token.line, type);
		if(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.LET){
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
				arrayMaker(init, typeC, token.line);
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
			if(typeC.isPrimitive()){
				id = ids.staticEasyType++;
			}else{
				id = ids.staticObjectRef++;
			}
		}else{
			if(typeC.isPrimitive()){
				id = ids.easyType++;
			}else{
				id = ids.objectRef++;
			}
		}
		
		WeaselField field = createField(name, modifier, this, typeC, id);
		
		WeaselField[] newFields = new WeaselField[fields.length+1];
		for(int i=0; i<fields.length; i++){
			newFields[i] = fields[i];
		}
		newFields[fields.length] = field;
		fields = newFields;
		return token;
	}
	
	private void compileMethod(List<WeaselToken> modifiers, String type, String name, WeaselToken token) throws WeaselCompilerException{
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
		WeaselClass returnType = getWeaselClass2(token.line, type);
		List<WeaselClass> params = new ArrayList<WeaselClass>();
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
				String classParam = readClass(token);
				params.add(getWeaselClass2(token.line, classParam));
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
		WeaselMethod method = createMethod(name, modifier, this, returnType, params.toArray(new WeaselClass[0]), id);
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
				((WeaselMethodBodyCompilerV2)methodBodys[i]).compile();
			}
		}
	}
	
}
