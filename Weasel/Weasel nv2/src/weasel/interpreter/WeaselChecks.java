package weasel.interpreter;


public class WeaselChecks {

	public static boolean isAlphabetical(char c){
		return (c>='A'&&c<='Z')||(c>='a'&&c<='z')||c=='_';
	}
	
	public static boolean isDigit(char c){
		return c>='0'&&c<='9';
	}
	
	public static boolean isAlphabeticalOrDigit(char c){
		return isAlphabetical(c)||isDigit(c);
	}
	
	public static void checkName(String name){
		if(name==null||name.equals(""))
			throw new WeaselNativeException("Name can not be nothing");
		if(!isAlphabetical(name.charAt(0))){
			throw new WeaselNativeException("Name have to start with an alphabetical");
		}
		int i=1;
		while(i<name.length()){
			if(!isAlphabeticalOrDigit(name.charAt(i))){
				throw new WeaselNativeException("Char at %s %s not accepted", i, name.charAt(i));
			}
			i++;
		}
	}
	
	public static void checkModifier(int modifier, int allowed){
		int notAllowed = modifier^(modifier & allowed);
		if(notAllowed!=0){
			throw new WeaselNativeException("Modifier %s %s not allowed, only %s %s permitted", WeaselModifier.toString(notAllowed), 
					WeaselModifier.count(notAllowed)==1?"is":"are", WeaselModifier.toString(allowed), 
							WeaselModifier.count(allowed)==1?"is":"are");
		}
		int allowedVisibilities = allowed&(WeaselModifier.PUBLIC|WeaselModifier.PRIVATE|WeaselModifier.PROTECTED);
		boolean isPublic = WeaselModifier.isPublic(modifier);
		boolean isPrivate = WeaselModifier.isPrivate(modifier);
		boolean isProtected = WeaselModifier.isProtected(modifier);
		
		if((isPublic&&isPrivate)||(isPublic&&isProtected)||(isPrivate&&isProtected)){
			throw new WeaselNativeException("Can only use %s", (WeaselModifier.count(allowedVisibilities)==1?"":"one of ")+WeaselModifier.toString(allowedVisibilities));
		}
		
		if(WeaselModifier.isAbstract(modifier)){
			int notAllowedAbstracts = WeaselModifier.PRIVATE|WeaselModifier.STATIC|WeaselModifier.FINAL|WeaselModifier.NATIVE;
			notAllowed = modifier & notAllowedAbstracts;
			if(notAllowed!=0){
				throw new WeaselNativeException("Abstract can't be used with %s", WeaselModifier.toString(notAllowed));
			}
		}
	}

	public static void checkSuperClass(WeaselClass superClass) {
		if(superClass==null)
			return;
		if(superClass.isInterface()){
			throw new WeaselNativeException("Interface %s cannot be a superClass", superClass);
		}
		if(WeaselModifier.isFinal(superClass.getModifier())){
			throw new WeaselNativeException("superClass %s is final", superClass);
		}
		WeaselClass sc = superClass;
		while(sc!=null){
			sc = sc.getSuperClass();
			if(sc==superClass){
				throw new WeaselNativeException("recursively super%s", superClass);
			}
		}
	}

	public static void checkInterface(WeaselClass interfaceClass) {
		if(!interfaceClass.isInterface()){
			throw new WeaselNativeException("%s cannot be an Interface", interfaceClass);
		}
	}

	public static void checkInterface2(WeaselClass interfaceClass, WeaselClass base) {
		if(!interfaceClass.isInterface()){
			throw new WeaselNativeException("%s cannot be an Interface", interfaceClass);
		}
		if(interfaceClass==base){
			throw new WeaselNativeException("recursively %s", interfaceClass);
		}
		for(WeaselClass childs:interfaceClass.getInterfaces()){
			checkInterface2(childs, base);
		}
	}
	
	public static void checkCreationClass(WeaselClass weaselClass) {
		if(weaselClass.isInterface()){
			throw new WeaselNativeException("%s cannot be a instantiate", weaselClass);
		}
		if(WeaselModifier.isAbstract(weaselClass.getModifier())){
			throw new WeaselNativeException("Abstract %s cannot be a instantiate", weaselClass);
		}
	}

	public static void checkCast(WeaselClass weaselClass, WeaselClass weaselClass2){
		if(weaselClass.canCastTo(weaselClass2)){
			throw new WeaselNativeException("Can't cast %s to %s", weaselClass, weaselClass2);
		}
	}
	
	public static void checkCast2(WeaselClass weaselClass, WeaselClass weaselClass2){
		if(weaselClass.canCastTo(weaselClass2)){
			throw new WeaselNativeException("Can't cast %s to %s", weaselClass, weaselClass2);
		}
		if((weaselClass.isPrimitive() || weaselClass2.isPrimitive())&& weaselClass2!=weaselClass){
			throw new WeaselNativeException("Can't cast %s to %s", weaselClass, weaselClass2);
		}
	}
	
	public static void checkArray(WeaselObject array, int index, WeaselClass setClass) {
		WeaselClass arrayClass = array.getWeaselClass();
		if(!arrayClass.isArray()){
			throw new WeaselNativeException("%s is not an array", array.getWeaselClass());
		}
		checkCast2(setClass, arrayClass.getArrayClass());
		if(index<0 || index >= array.easyTypes[0]){
			throw new WeaselNativeException("Array out of bounds %s %s", array.easyTypes[0], index);
		}
	}
	
	public static void checkArray2(WeaselObject array, int index, WeaselClass setClass) {
		WeaselClass arrayClass = array.getWeaselClass();
		if(!arrayClass.isArray()){
			throw new WeaselNativeException("%s is not an array", array.getWeaselClass());
		}
		checkCast2(arrayClass.getArrayClass(), setClass);
		if(index<0 || index >= array.easyTypes[0]){
			throw new WeaselNativeException("Array out of bounds %s %s", array.easyTypes[0], index);
		}
	}
	
}
