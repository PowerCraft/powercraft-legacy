package weasel.compiler;

import java.util.ArrayList;
import java.util.List;

import weasel.compiler.keywords.WeaselKeyWord;
import weasel.interpreter.WeaselChecks;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselField;
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
	
	protected WeaselClassCompilerV2(WeaselCompiler compiler, Object parent, String name, String fileName) {
		super(compiler, parent, name, fileName);
	}

	@Override
	public void compileEasy(String classSourceFor) {
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
			onException(new WeaselSyntaxError(token.line, "ClassFile name %s and classname %s is not epual", name, token.param));
		}
		token = getNextToken();
		if(isClass){
			if(token.param==WeaselKeyWord.EXTENDS){
				expect(token = getNextToken(), WeaselTokenType.IDENT);
				try{
					superClass = interpreter.getWeaselClass("O"+token.param+";");
					WeaselChecks.checkSuperClass(superClass);
					ids.method = superClass.getIDS().method;
					ids.easyType = superClass.getIDS().easyType;
					ids.objectRef = superClass.getIDS().objectRef;
				}catch(WeaselNativeException e){
					onException(new WeaselSyntaxError(token.line, e));
				}
				token = getNextToken();
			}else{
				if(name.equals("Object")){
					ids.method = 1;
				}else{
					superClass = interpreter.getWeaselClass("OObject;");
					ids.method = superClass.getIDS().method;
					ids.easyType = superClass.getIDS().easyType;
					ids.objectRef = superClass.getIDS().objectRef;
				}
			}
			if(token.param==WeaselKeyWord.IMPLEMENTS){
				List<WeaselClass> interfaceList = new ArrayList<WeaselClass>();
				do{
					expect(token = getNextToken(), WeaselTokenType.IDENT);
					try{
						WeaselClass interfa = interpreter.getWeaselClass("O"+token.param+";");
						if(interfaceList.contains(interfa)){
							onException(new WeaselSyntaxError(token.line, "Duplicated implements of %s", interfa));
						}else{
							interfaceList.add(interfa);
						}
						WeaselChecks.checkInterface(interfa);
					}catch(WeaselNativeException e){
						onException(new WeaselSyntaxError(token.line, e));
					}
					token = getNextToken();
				}while(token.tokenType==WeaselTokenType.COMMA);
				interfaces = interfaceList.toArray(new WeaselClass[0]);
			}
		}
		if(isInterface){
			if(token.param==WeaselKeyWord.EXTENDS){
				List<WeaselClass> interfaceList = new ArrayList<WeaselClass>();
				do{
					expect(token = getNextToken(), WeaselTokenType.IDENT);
					try{
						WeaselClass interfa = interpreter.getWeaselClass("O"+token.param+";");
						if(interfaceList.contains(interfa)){
							onException(new WeaselSyntaxError(token.line, "Duplicated extends of %s", interfa));
						}else{
							interfaceList.add(interfa);
						}
						WeaselChecks.checkInterface2(interfa, this);
					}catch(WeaselNativeException e){
						onException(new WeaselSyntaxError(token.line, e));
					}
					token = getNextToken();
				}while(token.tokenType==WeaselTokenType.COMMA);
				interfaces = interfaceList.toArray(new WeaselClass[0]);
			}
		}
		if(isEnum){
			superClass = interpreter.getWeaselClass("OEnum;");
			ids.method = superClass.getIDS().method;
			ids.easyType = superClass.getIDS().easyType;
			ids.objectRef = superClass.getIDS().objectRef;
		}
		
		expect(token, WeaselTokenType.OPENBLOCK);
		
		token = getNextToken();
		
		ids.staticMethod++;
		
		methods = new WeaselMethod[2];
		staticMethodBodys = new WeaselMethodBody[ids.staticMethod];
		if(!isInterface())
			methodBodys = new WeaselMethodBody[ids.method];
		fields = new WeaselField[0];
		
		methods[0] = compiler.createMethod("<staticInit>", WeaselModifier.STATIC, this, interpreter.voidClass, new WeaselClass[0], ids.staticMethod-1);
		staticMethodBodys[ids.staticMethod-1] = new WeaselMethodBodyCompiler(methods[0], this, classStaticInit);
		if(!isInterface()){
			methods[1] = compiler.createMethod("<preInit>", 0, this, interpreter.voidClass, new WeaselClass[0], 0);
			methodBodys[0] = new WeaselMethodBodyCompiler(methods[1], this, classPreInit);
		}
		
		
		while(token.tokenType!=WeaselTokenType.CLOSEBLOCK&&token.tokenType!=WeaselTokenType.NONE){
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
				compileMethod(modifiers, className, name, token);
			}else{
				if(token.tokenType==WeaselTokenType.OPENBRACKET){
					compileMethod(modifiers, className, name, token);
				}else{
					compileField(modifiers, className, name, token);
				}
			}
			token = getNextToken();
		}
		
		expect(token, WeaselTokenType.CLOSEBLOCK);
		expect(getNextToken(), WeaselTokenType.NONE);
		
		tokenParser = null;
		
	}
	
	private String readClass(WeaselToken token){
		expect(token, WeaselTokenType.IDENT);
		String className = WeaselCompiler.mapClassNames((String)token.param);
		interpreter.getWeaselClass(className);
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
	
	private void compileField(List<WeaselToken> modifiers, String type, String name, WeaselToken token){
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
	
	private WeaselToken compileField2(int modifier, String type, String name, WeaselToken token){
		WeaselClass typeC = interpreter.getWeaselClass(type);
		if(token.tokenType==WeaselTokenType.OPERATOR && token.param == WeaselOperator.LET){
			List<WeaselToken> init;
			if(WeaselModifier.isStatic(modifier)){
				init = classStaticInit;
			}else{
				init = classPreInit;
				if(isInterface()){
					onException(new WeaselSyntaxError(token.line, "Interface field %s cant be set here", name));
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
		
		WeaselField field = compiler.createField(name, modifier, this, typeC, id);
		
		WeaselField[] newFields = new WeaselField[fields.length+1];
		for(int i=0; i<fields.length; i++){
			newFields[i] = fields[i];
		}
		newFields[fields.length] = field;
		fields = newFields;
		return token;
	}
	
	private void compileMethod(List<WeaselToken> modifiers, String type, String name, WeaselToken token){
		boolean isConstructor = name.equals("<init>");
		boolean isPreConstructor = name.equals("<preInit>");
		boolean isStaticConstructor = name.equals("<staticInit>");
		int modifier = getModifier(modifiers, isStaticConstructor||isPreConstructor?0:isConstructor?isEnum()?0:
			WeaselMethod.constructorModifier:WeaselModifier.isAbstract(getModifier())?WeaselMethod.abstractModifier:WeaselMethod.normalModifier);
		if(isInterface() && !WeaselModifier.isStatic(modifier)){
			modifier |= WeaselModifier.ABSTRACT;
		}
		if(isInterface() && (isConstructor || isPreConstructor)){
			onException(new WeaselSyntaxError(token.line, "Interface can't have constructors"));
		}
		WeaselClass returnType = interpreter.getWeaselClass(type);
		List<WeaselClass> params = new ArrayList<WeaselClass>();
		token = getNextToken();
		if(token.tokenType!=WeaselTokenType.CLOSEBRACKET){
			while(true){
				String classParam = readClass(token);
				params.add(interpreter.getWeaselClass(classParam));
				token = getNextToken();
				if(token.tokenType==WeaselTokenType.IDENT){
					token = getNextToken();
				}else if(!WeaselModifier.isAbstract(modifier)){
					expect(token, WeaselTokenType.IDENT);
				}
				if(token.tokenType!=WeaselTokenType.COMMA)
					break;
				token = getNextToken();
			}
		}
		expect(token, WeaselTokenType.CLOSEBRACKET);
		int id = WeaselModifier.isStatic(modifier)?ids.staticMethod++:ids.method++;
		WeaselMethod method = compiler.createMethod(name, modifier, this, returnType, params.toArray(new WeaselClass[0]), id);
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
				staticMethodBodys[id] = new WeaselMethodBodyCompiler(method, this);
			}else{
				methodBodys[id] = new WeaselMethodBodyCompiler(method, this);
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
				token = getNextToken();
			}
			WeaselMethodBodyCompiler mb = new WeaselMethodBodyCompiler(method, this, methodTokens);
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
				onException(new WeaselSyntaxError(modifierToken.line, "Modifier %s is not allowed, only %s %s permitted", WeaselModifier.toString(m),
							WeaselModifier.toString(allowed), WeaselModifier.count(allowed)==1?"is":"are"));
			}else if((m&modifier)!=0){
				onException(new WeaselSyntaxError(modifierToken.line, "Duplicated modifier %s", WeaselModifier.toString(m)));
			}else{
				modifier |= m;
				try {
					WeaselChecks.checkModifier(modifier, allowed);
				} catch (WeaselNativeException e) {
					onException(new WeaselSyntaxError(modifierToken.line, e));
				}
			}
		}
		return modifier;
	}
	
	@Override
	public void finishCompile() {
		for(int i=0; i<staticMethodBodys.length; i++){
			((WeaselMethodBodyCompiler)staticMethodBodys[i]).compile();
		}
		if(!isInterface()){
			for(int i=0; i<methodBodys.length; i++){
				((WeaselMethodBodyCompiler)methodBodys[i]).compile();
			}
		}
	}

}
