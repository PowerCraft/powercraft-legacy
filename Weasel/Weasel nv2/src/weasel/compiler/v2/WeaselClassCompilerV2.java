package weasel.compiler.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import weasel.compiler.WeaselClassCompiler;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerMessage;
import weasel.compiler.WeaselCompilerMessage.MessageType;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselOperator.Properties;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenParser;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.keywords.WeaselKeyWord;
import weasel.compiler.v2.tokentree.WeaselTreeGenericElement;
import weasel.interpreter.WeaselChecks;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselField;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselGenericClassInfo;
import weasel.interpreter.WeaselGenericInformation;
import weasel.interpreter.WeaselGenericMethod2;
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
	
	public WeaselGenericClass genericClass;
	
	protected WeaselClassCompilerV2(WeaselCompiler compiler, Object parent, String name, String fileName) {
		super(compiler, parent, name, fileName);
	}

	private WeaselClass getWeaselClass(int line, String className) throws WeaselCompilerException{
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
	
	private List<WeaselToken> readModifier(){
		List<WeaselToken> modifiers = new ArrayList<WeaselToken>();
		WeaselToken token = getNextToken();
		while(token.tokenType==WeaselTokenType.MODIFIER){
			modifiers.add(token);
			token = getNextToken();
		}
		setNextToken(token);
		return modifiers;
	}
	
	private WeaselToken readClassHead(WeaselToken token) throws WeaselCompilerException{
		if(token.param==WeaselKeyWord.EXTENDS){
			ListIterator<WeaselToken> iterator = tokenParser.listIterator();
			genericSuperClass = readGenericClass(iterator.next(), iterator);
			WeaselChecks.checkSuperClass(genericSuperClass.genericClass);
			ids.method = genericSuperClass.genericClass.getIDS().method;
			ids.easyType = genericSuperClass.genericClass.getIDS().easyType;
			ids.objectRef = genericSuperClass.genericClass.getIDS().objectRef;
			token = iterator.next();
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
				ListIterator<WeaselToken> iterator = tokenParser.listIterator();
				WeaselGenericClassInfo interfa = readGenericClass(iterator.next(), iterator);
				interfaceGenericList.add(interfa);
				WeaselChecks.checkInterface(interfa.genericClass);
				iterator.next();
			}while(token.tokenType==WeaselTokenType.COMMA);
			genericInterfaces = interfaceGenericList.toArray(genericInterfaces);
		}
		return token;
	}
	
	private WeaselToken readInterfaceHead(WeaselToken token) throws WeaselCompilerException{
		if(token.param==WeaselKeyWord.EXTENDS){
			List<WeaselGenericClassInfo> interfaceGenericList = new ArrayList<WeaselGenericClassInfo>();
			do{
				ListIterator<WeaselToken> iterator = tokenParser.listIterator();
				WeaselGenericClassInfo interfa = readGenericClass(iterator.next(), iterator);
				interfaceGenericList.add(interfa);
				WeaselChecks.checkInterface2(interfa.genericClass, this);
				token = iterator.next();
			}while(token.tokenType==WeaselTokenType.COMMA);
			genericInterfaces = interfaceGenericList.toArray(genericInterfaces);
		}
		return token;
	}
	
	private WeaselToken readEnumHead(WeaselToken token) throws WeaselCompilerException{
		genericSuperClass = new WeaselGenericClassInfo(interpreter.baseTypes.getEnumClass(), -1, new WeaselGenericClassInfo[]{new WeaselGenericClassInfo(this, -1, new WeaselGenericClassInfo[0])});
		ids.method = genericSuperClass.genericClass.getIDS().method;
		ids.easyType = genericSuperClass.genericClass.getIDS().easyType;
		ids.objectRef = genericSuperClass.genericClass.getIDS().objectRef;
		if(token.param==WeaselKeyWord.IMPLEMENTS){
			List<WeaselGenericClassInfo> interfaceGenericList = new ArrayList<WeaselGenericClassInfo>();
			do{
				ListIterator<WeaselToken> iterator = tokenParser.listIterator();
				WeaselGenericClassInfo interfa = readGenericClass(iterator.next(), iterator);
				interfaceGenericList.add(interfa);
				WeaselChecks.checkInterface(interfa.genericClass);
				token = iterator.next();
			}while(token.tokenType==WeaselTokenType.COMMA);
			genericInterfaces = interfaceGenericList.toArray(genericInterfaces);
		}
		return token;
	}
	
	private WeaselToken readEnumConstants(WeaselToken token) throws WeaselCompilerException{
		List<String> constants = new ArrayList<String>();
		while(true){
			WeaselCompiler.expect(token, WeaselTokenType.IDENT);
			String constantName = (String)token.param;
			constants.add(constantName);
			WeaselField field = createField(constantName, WeaselModifier.FINAL | WeaselModifier.STATIC | WeaselModifier.PUBLIC, this, new WeaselGenericClassInfo(this, -1, new WeaselGenericClassInfo[0]), ids.staticObjectRef++);
			WeaselField[] newFields = new WeaselField[fields.length+1];
			for(int i=0; i<fields.length; i++){
				newFields[i] = fields[i];
			}
			newFields[fields.length] = field;
			fields = newFields;
			WeaselToken nameToken = token;
			classStaticInit.add(nameToken);
			classStaticInit.add(new WeaselToken(WeaselTokenType.OPERATOR, nameToken.line, WeaselOperator.ASSIGN));
			classStaticInit.add(new WeaselToken(WeaselTokenType.KEYWORD, nameToken.line, WeaselKeyWord.NEW));
			classStaticInit.add(new WeaselToken(WeaselTokenType.IDENT, nameToken.line, name));
			token = getNextToken();
			if(token.tokenType==WeaselTokenType.OPENBRACKET){
				int count = 0;
				classStaticInit.add(token);
				token = getNextToken();
				while(token.tokenType!=WeaselTokenType.NONE){
					classStaticInit.add(token);
					if(token.tokenType==WeaselTokenType.OPENBRACKET)
						count++;
					else if(token.tokenType==WeaselTokenType.CLOSEBRACKET){
						if(count<=0)
							break;
						count--;
					}
					token = getNextToken();
				}
				token = getNextToken();
				classStaticInit.add(new WeaselToken(WeaselTokenType.SEMICOLON, token.line));
			}else{
				classStaticInit.add(new WeaselToken(WeaselTokenType.OPENBRACKET, nameToken.line));
				classStaticInit.add(new WeaselToken(WeaselTokenType.CLOSEBRACKET, nameToken.line));
				classStaticInit.add(new WeaselToken(WeaselTokenType.SEMICOLON, nameToken.line));
			}
			if(token.tokenType!=WeaselTokenType.COMMA)
				break;
			token = getNextToken();
		}
		WeaselField field = createField("values", WeaselModifier.FINAL | WeaselModifier.STATIC | WeaselModifier.PRIVATE, this, new WeaselGenericClassInfo(interpreter.getWeaselClass("["+getByteName()), -1, new WeaselGenericClassInfo[0]), ids.staticObjectRef++);
		WeaselField[] newFields = new WeaselField[fields.length+1];
		for(int i=0; i<fields.length; i++){
			newFields[i] = fields[i];
		}
		newFields[fields.length] = field;
		fields = newFields;
		classStaticInit.add(new WeaselToken(WeaselTokenType.IDENT, 0, "values"));
		classStaticInit.add(new WeaselToken(WeaselTokenType.OPERATOR, 0, WeaselOperator.ASSIGN));
		classStaticInit.add(new WeaselToken(WeaselTokenType.KEYWORD, 0, WeaselKeyWord.NEW));
		classStaticInit.add(new WeaselToken(WeaselTokenType.IDENT, 0, name));
		classStaticInit.add(new WeaselToken(WeaselTokenType.OPENINDEX, 0));
		classStaticInit.add(new WeaselToken(WeaselTokenType.CLOSEINDEX, 0));
		classStaticInit.add(new WeaselToken(WeaselTokenType.OPENBLOCK, 0));
		classStaticInit.add(new WeaselToken(WeaselTokenType.IDENT, 0, constants.get(0)));
		for(int i=1; i<constants.size(); i++){
			classStaticInit.add(new WeaselToken(WeaselTokenType.COMMA, 0));
			classStaticInit.add(new WeaselToken(WeaselTokenType.IDENT, 0, constants.get(i)));
		}
		classStaticInit.add(new WeaselToken(WeaselTokenType.CLOSEBLOCK, 0));
		classStaticInit.add(new WeaselToken(WeaselTokenType.SEMICOLON, 0));
		WeaselCompiler.expect(token, WeaselTokenType.SEMICOLON, WeaselTokenType.CLOSEBLOCK);
		token = getNextToken();
		return token;
	}
	
	@Override
	public void compileEasy(String classSourceFor) {
		compiler.addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.INFO, 0, getFileName(), "Compiling Class"));
		tokenParser = new WeaselTokenParser(classSourceFor);
		List<WeaselToken> modifiers = readModifier();
		WeaselToken token = getNextToken();
		try{
			WeaselCompiler.expectKeyWord(token, WeaselKeyWord.CLASS, WeaselKeyWord.INTERFACE, WeaselKeyWord.ENUM);
		}catch(WeaselCompilerException e){
			onException(e);
		}
		boolean isEnum = token.param == WeaselKeyWord.ENUM;
		isInterface = token.param == WeaselKeyWord.INTERFACE;
		boolean isClass = !(isEnum||isInterface);
		modifier = getModifier(modifiers, isEnum?enumModifier:isInterface?interfaceModifier:classModifier);
		modifiers = null;
		if(isInterface)
			modifier |= WeaselModifier.ABSTRACT;
		if(isEnum)
			modifier |= WeaselModifier.FINAL;
		token = getNextToken();
		try{
			WeaselCompiler.expect(token, WeaselTokenType.IDENT);
			if(!name.equals(token.param)){
				onException(token.line, "ClassFile name %s and classname %s is not epual", name, token.param);
			}
		}catch(WeaselCompilerException e){
			onException(e);
		}
		token = getNextToken();
		if(isEnum){
			genericInformation = new WeaselGenericInformation[0];
		}else{
			try{
				genericInformation = makeGenericInformations(token);
			}catch(WeaselCompilerException e){
				onException(e);
			}
			token = getNextToken();
		}
		genericInterfaces = new WeaselGenericClassInfo[0];
		try{
			if(isClass){
				token = readClassHead(token);
			}
			if(isInterface){
				token = readInterfaceHead(token);
			}
			if(isEnum){
				token = readEnumHead(token);
			}
			WeaselCompiler.expect(token, WeaselTokenType.OPENBLOCK);
		}catch(WeaselCompilerException e){
			onException(e);
			while(getNextToken().tokenType!=WeaselTokenType.OPENBLOCK);
		}
		
		token = getNextToken();
		
		ids.staticMethod++;
		
		methods = new WeaselMethod[isInterface()?1:2];
		staticMethodBodys = new WeaselMethodBody[ids.staticMethod];
		if(!isInterface())
			methodBodys = new WeaselMethodBody[++ids.method];
		fields = new WeaselField[0];
		
		methods[0] = createMethod("<staticInit>", WeaselModifier.STATIC, this, new WeaselGenericClassInfo(interpreter.baseTypes.voidClass, -1, new WeaselGenericClassInfo[0]), new WeaselGenericClassInfo[0], new WeaselGenericInformation[0], ids.staticMethod-1);
		staticMethodBodys[ids.staticMethod-1] = new WeaselMethodBodyCompilerV2(methods[0], this, classStaticInit, new ArrayList<String>(), new ArrayList<Integer>(), compiler);
		if(!isInterface()){
			methods[1] = createMethod("<preInit>", 0, this, new WeaselGenericClassInfo(interpreter.baseTypes.voidClass, -1, new WeaselGenericClassInfo[0]), new WeaselGenericClassInfo[0], new WeaselGenericInformation[0], 0);
			methodBodys[0] = new WeaselMethodBodyCompilerV2(methods[1], this, classPreInit, new ArrayList<String>(), new ArrayList<Integer>(), compiler);
		}
		
		if(isEnum){
			try {
				token = readEnumConstants(token);
			} catch (WeaselCompilerException e) {
				onException(e);
				while(getNextToken().tokenType!=WeaselTokenType.SEMICOLON);
			}
		}
		
		while(token.tokenType!=WeaselTokenType.CLOSEBLOCK&&token.tokenType!=WeaselTokenType.NONE){
			try{
				setNextToken(token);
				modifiers = readModifier();
				token = getNextToken();
				
				//WeaselGenericInformation[] genericInformations = makeGenericInformations(token);
				//token = getNextToken();
				String name;
				boolean isConstructor=token.tokenType == WeaselTokenType.IDENT && token.param.equals(this.name);
				if(isConstructor){
					WeaselToken token2 = getNextToken();
					if(token2.tokenType!=WeaselTokenType.OPENBRACKET){
						isConstructor = false;
					}
					setNextToken(token2);
				}
				WeaselGenericClassInfo typeInfo;
				if(isConstructor){
					if(isInterface)
						onException(token.line, "Interface can't have a constructor");
					//if(genericInformations.length>0)
						//onException(token.line, "Constructor can't have generic informations");
					typeInfo = new WeaselGenericClassInfo(interpreter.baseTypes.voidClass, -1, new WeaselGenericClassInfo[0]);
					name = "<init>";
					typeInfo = new WeaselGenericClassInfo(interpreter.baseTypes.voidClass, -1, new WeaselGenericClassInfo[0]);
				}else{
					ListIterator<WeaselToken> iterator;
					typeInfo = readGenericClass(token, iterator=tokenParser.listIterator());
					WeaselCompiler.expect(token = iterator.next(), WeaselTokenType.IDENT);
					name = (String)token.param;
					if(name.equals("operator")){
						WeaselCompiler.expect(token = getNextToken(), WeaselTokenType.OPERATOR, WeaselTokenType.COMMA);
						name += ((Properties)token.param).operator;
					}
				}
				token = getNextToken();
				if(isConstructor){
					WeaselCompiler.expect(token, WeaselTokenType.OPENBRACKET);
					try{
						compileMethod(modifiers, typeInfo, name, token, new WeaselGenericInformation[0]);
					}catch(WeaselCompilerException e){
						onException(e);
					}
				}else{
					if(token.tokenType==WeaselTokenType.OPENBRACKET){
						try{
							compileMethod(modifiers, typeInfo, name, token, new WeaselGenericInformation[0]);
						}catch(WeaselCompilerException e){
							onException(e);
						}
					}else{
						//if(genericInformations.length>0)
						//	onException(token.line, "Fields can't have generic informations");
						try{
							compileField(modifiers, typeInfo, name, token);
						}catch(WeaselCompilerException e){
							onException(e);
						}
					}
				}
				token = getNextToken();
			}catch(WeaselCompilerException e){
				onException(e);
				token = getNextToken();
			}
		}
		
		try {
			WeaselCompiler.expect(token, WeaselTokenType.CLOSEBLOCK);
			WeaselCompiler.expect(getNextToken(), WeaselTokenType.NONE);
		} catch (WeaselCompilerException e) {
			onException(e);
		}
		tokenParser = null;
		
		genericClass = new WeaselGenericClass(this);
		
		checkOverrides();
	}

	private void checkOverrides(){
		List<WeaselGenericMethod2> methodsToOverride = new ArrayList<WeaselGenericMethod2>();
		
		genericClass.addMethodsToOverride(methodsToOverride);
		
	}
	
	private WeaselGenericInformation[] makeGenericInformations(WeaselToken token) throws WeaselCompilerException{
		List<WeaselGenericInformation> genericList = new ArrayList<WeaselGenericInformation>();
		List<WeaselTreeGenericElement> genericListClass = new ArrayList<WeaselTreeGenericElement>();
		WeaselTreeGenericElement lwtge = null;
		if(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.LESS){
			do{
				token = getNextToken();
				WeaselCompiler.expect(token, WeaselTokenType.IDENT);
				String generic = (String)token.param;
				token = getNextToken();
				WeaselClass genericClass;
				if(token.tokenType == WeaselTokenType.KEYWORD && token.param == WeaselKeyWord.EXTENDS){
					ListIterator<WeaselToken> li;
					genericListClass.add(lwtge = new WeaselTreeGenericElement(li = tokenParser.listIterator()));
					if(lwtge.close){
						genericList.add(new WeaselGenericInformation(generic, lwtge.getWeaselClass(interpreter), -1));
						break;
					}
					token = li.next();
					genericClass = lwtge.getWeaselClass(interpreter);
				}else{
					genericClass = interpreter.baseTypes.getObjectClass();
					genericListClass.add(null);
					lwtge = null;
				}
				genericList.add(new WeaselGenericInformation(generic, genericClass, -1));
			}while(token.tokenType == WeaselTokenType.COMMA);
			if(!(lwtge!=null && lwtge.close) && !(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.GREATER)){
				throw new WeaselCompilerException(token.line, "WeaselCompiler.expect > at end of generic declaration but got %s", token);
			}
		}else{
			setNextToken(token);
		}
		genericInformation = genericList.toArray(new WeaselGenericInformation[0]);
		for(int i=0; i<genericInformation.length; i++){
			
			WeaselTreeGenericElement wtge = genericListClass.get(i);
			if(wtge!=null){
				genericInformation[i].genericInfo = wtge.getGenericClassInfo(this);
			}
		}
		return genericInformation;
	}
	
	private void compileField(List<WeaselToken> modifiers, WeaselGenericClassInfo typeInfo, String name, WeaselToken token) throws WeaselCompilerException{
		int modifier = getModifier(modifiers, WeaselField.normalModifier);
		String stype = typeInfo.genericClass.getByteName();
		while(token.tokenType==WeaselTokenType.OPENINDEX){
			token = getNextToken();
			WeaselCompiler.expect(token, WeaselTokenType.CLOSEINDEX);
			token = getNextToken();
			stype = "["+stype;
		}
		token = compileField2(modifier, new WeaselGenericClassInfo(getWeaselClass(token.line, stype), typeInfo.genericID, typeInfo.generics), name, token);
		while(token.tokenType==WeaselTokenType.COMMA){
			stype = typeInfo.genericClass.getByteName();
			token = getNextToken();
			WeaselCompiler.expect(token, WeaselTokenType.IDENT);
			name = (String)token.param;
			token = getNextToken();
			while(token.tokenType==WeaselTokenType.OPENINDEX){
				token = getNextToken();
				WeaselCompiler.expect(token, WeaselTokenType.CLOSEINDEX);
				token = getNextToken();
				stype = "["+stype;
			}
			token = compileField2(modifier, new WeaselGenericClassInfo(getWeaselClass(token.line, stype), typeInfo.genericID, typeInfo.generics), name, token);
		}
		WeaselCompiler.expect(token, WeaselTokenType.SEMICOLON);
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
		WeaselToken nameToken = token;
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
			int h=0;
			while(token.tokenType!=WeaselTokenType.SEMICOLON&&(h>0 || token.tokenType!=WeaselTokenType.COMMA)){
				if(token.tokenType==WeaselTokenType.OPENBLOCK||token.tokenType==WeaselTokenType.OPENINDEX||token.tokenType==WeaselTokenType.OPENBRACKET)
					h++;
				if(token.tokenType==WeaselTokenType.CLOSEBLOCK||token.tokenType==WeaselTokenType.CLOSEINDEX||token.tokenType==WeaselTokenType.CLOSEBRACKET)
					if(h>0)
						h--;
				init.add(token);
				token = getNextToken();
				if(token.tokenType==WeaselTokenType.NONE){
					WeaselCompiler.expect(token, WeaselTokenType.SEMICOLON);
					break;
				}
			}
			init.add(new WeaselToken(WeaselTokenType.SEMICOLON, token.line));
		}
		WeaselField field = null;
		try{
			field = getField(name);
		}catch(WeaselNativeException e){}
		if(field!=null && field.getParentClass()==this)
			throw new WeaselCompilerException(nameToken.line, "Duplicated declaration of filed %s", field);
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
		field = createField(name, modifier, this, typeInfo, id);
		
		WeaselField[] newFields = new WeaselField[fields.length+1];
		for(int i=0; i<fields.length; i++){
			newFields[i] = fields[i];
		}
		newFields[fields.length] = field;
		fields = newFields;
		return token;
	}
	
	private void compileMethod(List<WeaselToken> modifiers, WeaselGenericClassInfo typeInfo, String name, WeaselToken token, WeaselGenericInformation[] genericInformations) throws WeaselCompilerException{
		WeaselToken tokenName = token;
		boolean isConstructor = name.equals("<init>");
		boolean isPreConstructor = name.equals("<preInit>");
		boolean isStaticConstructor = name.equals("<staticInit>");
		int modifier = getModifier(modifiers, isStaticConstructor||isPreConstructor?0:isConstructor?isEnum()?0:
			WeaselMethod.constructorModifier:WeaselModifier.isAbstract(getModifier())?WeaselMethod.abstractModifier:WeaselMethod.normalModifier);
		if(isInterface() && !WeaselModifier.isStatic(modifier)){
			modifier |= WeaselModifier.ABSTRACT;
		}
		if(isConstructor&&isEnum()){
			modifier |= WeaselModifier.PRIVATE;
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
				ListIterator<WeaselToken> iterator;
				genericInfos.add(readGenericClass(token, iterator = tokenParser.listIterator()));
				token = iterator.next();
				if(token.tokenType==WeaselTokenType.IDENT){
					paramNames.add((String)token.param);
					token = getNextToken();
				}else if(!WeaselModifier.isAbstract(modifier)){
					paramNames.add(null);
					WeaselCompiler.expect(token, WeaselTokenType.IDENT);
				}else{
					paramNames.add(null);
				}
				if(token.tokenType!=WeaselTokenType.COMMA)
					break;
				token = getNextToken();
			}
		}
		WeaselCompiler.expect(token, WeaselTokenType.CLOSEBRACKET);
		WeaselGenericClassInfo[] wgci = genericInfos.toArray(new WeaselGenericClassInfo[0]);
		WeaselMethod method = null;
		try{
			method = getMethod(name, wgci);
		}catch(WeaselNativeException e){}
		if(method!=null && method.getParentClass()==this)
			throw new WeaselCompilerException(tokenName.line, "Duplicated method %s", method);
		int id = WeaselModifier.isStatic(modifier)?ids.staticMethod++:ids.method++;
		method = createMethod(name, modifier, this, typeInfo, wgci, genericInformations, id);
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
			WeaselCompiler.expect(token, WeaselTokenType.SEMICOLON);
		}else if(WeaselModifier.isNative(modifier)){
			WeaselCompiler.expect(token, WeaselTokenType.SEMICOLON);
			if(WeaselModifier.isStatic(modifier)){
				staticMethodBodys[id] = new WeaselMethodBodyCompilerV2(method, this);
			}else{
				methodBodys[id] = new WeaselMethodBodyCompilerV2(method, this);
			}
		}else{
			WeaselCompiler.expect(token, WeaselTokenType.OPENBLOCK);
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
					WeaselCompiler.expect(token, WeaselTokenType.CLOSEBLOCK);
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
			if(staticMethodBodys[i]!=null){
				try{
					((WeaselMethodBodyCompilerV2)staticMethodBodys[i]).compile();
				}catch(Throwable e){
					e.printStackTrace();
					onException(0, "Native exception in %s %s", staticMethodBodys[i].getMethod(), e);
				}
			}
		}
		if(!isInterface()){
			for(int i=0; i<methodBodys.length; i++){
				if(methodBodys[i]!=null){
					try{
						((WeaselMethodBodyCompilerV2)methodBodys[i]).compile();
					}catch(Throwable e){
						e.printStackTrace();
						onException(0, "Native exception in %s %s", methodBodys[i].getMethod(), e);
					}
				}
			}
		}
	}

	@Override
	public WeaselGenericClassInfo readGenericClass(WeaselToken token, ListIterator<WeaselToken> iterator) throws WeaselCompilerException {
		WeaselCompiler.expect(token, WeaselTokenType.IDENT);
		String className = WeaselClass.mapClassNames((String)token.param);
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
		token = iterator.next();
		List<WeaselGenericClassInfo> genericObjects = new ArrayList<WeaselGenericClassInfo>();
		if(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.LESS){
			do{
				token = iterator.next();
				WeaselCompiler.expect(token, WeaselTokenType.IDENT);
				genericObjects.add(readGenericClass(token, iterator));
				token = iterator.next();
			}while(token.tokenType == WeaselTokenType.COMMA);
			if(!(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.GREATER)){
				if(!(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.RSHIFT)){
					onException(token.line, "WeaselCompiler.expect > at end of generic");
				}else{
					token = new WeaselToken(WeaselTokenType.OPERATOR, token.line, WeaselOperator.GREATER);
				}
			}else{
				token = iterator.next();
			}
		}
		while(token.tokenType==WeaselTokenType.OPENINDEX){
			token = iterator.next();
			WeaselCompiler.expect(token, WeaselTokenType.CLOSEINDEX);
			token = iterator.next();
			className = "["+className;
		}
		iterator.previous();
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

	@Override
	public WeaselGenericClass getGenericClass() {
		return genericClass;
	}
	
}
