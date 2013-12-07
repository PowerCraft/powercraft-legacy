package xscript.runtime;

import xscript.runtime.clazz.XClass;
import xscript.runtime.clazz.XField;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.method.XMethod;

/**
 * 
 * some checks for several things
 * 
 * @author XOR
 *
 */
public final class XChecks {

	/**
	 * no object of this class should be created
	 */
	private XChecks(){
		throw new InstantiationError();
	}
	
	/**
	 * checks if an class can be superclass of an other class
	 * 
	 * @param xClass the class whitch will extand the other class
	 * @param superClass the superclass
	 */
	public static void checkSuperClass(XClass xClass, XClass superClass) {
		checkAccess(xClass, superClass);
		int modifier = superClass.getModifier();
		if(XModifier.isFinal(modifier)){
			throw new XRuntimeException("%s can't extend final class %s", xClass, superClass);
		}
	}

	/**
	 * checks if an class can access an other class
	 * 
	 * @param xClass the class that want to access the other class
	 * @param xClass2 the class that should be accessed
	 */
	
	public static void checkAccess(XClass xClass, XClass xClass2){
		if(xClass.getVirtualMachine()!=xClass2.getVirtualMachine()){
			throw new XRuntimeException("%s has a diferent VM than %s", xClass, xClass2);
		}
		int modifier = xClass2.getModifier();
		XClass checkClass1 = xClass;
		while(checkClass1.getOuterClass()!=null){
			checkClass1 = checkClass1.getOuterClass();
		}
		XClass checkClass2 = xClass2;
		while(checkClass2.getOuterClass()!=null){
			checkClass2 = checkClass2.getOuterClass();
		}
		boolean sameOuterClass = checkClass1==checkClass2;
		if(XModifier.isPrivate(modifier)){
			if(!sameOuterClass){
				throw new XRuntimeException("%s can't access %s", xClass, xClass2);
			}
		}else if(XModifier.isProtected(modifier)){
			if(!sameOuterClass && xClass.canCastTo(xClass2) && xClass.getPackage()!=xClass2.getPackage()){
				throw new XRuntimeException("%s can't access %s", xClass, xClass2);
			}
		}else if(!XModifier.isPublic(modifier)){
			if(!sameOuterClass && xClass.getPackage()!=xClass2.getPackage()){
				throw new XRuntimeException("%s can't access %s", xClass, xClass2);
			}
		}
	}

	/**
	 * checks if there are only allowed mofifier used
	 * 
	 * @param xClass the class who whats to check
	 * @param modifier the mofifiers to check
	 * @param okModifier the modifiers which are allowed
	 */
	
	public static void checkModifier(XClass xClass, int modifier, int okModifier){
		final int ACCESS = XModifier.PUBLIC | XModifier.PRIVATE | XModifier.PROTECTED;
		int notOkModifier = ~okModifier & modifier;
		if(notOkModifier!=0){
			throw new XRuntimeException("Illegal modifier %s in %s, only allowed %s", XModifier.toString(notOkModifier), xClass, XModifier.toString(okModifier));
		}
		if((XModifier.isPublic(modifier)?1:0)+(XModifier.isPrivate(modifier)?1:0)+(XModifier.isProtected(modifier)?1:0)>1){
			throw new XRuntimeException("Only one of %s visible modifier is allowed in %s", XModifier.toString(okModifier & ACCESS), xClass);
		}
		if(XModifier.isAbstract(modifier) && XModifier.isFinal(modifier)){
			throw new XRuntimeException("Only abstract or final is allowd, but not both together in %s", xClass);
		}
		if(XModifier.isAbstract(modifier) && XModifier.isNative(modifier)){
			throw new XRuntimeException("Only abstract or native is allowd, but not both together in %s", xClass);
		}
	}

	public static void checkCast(XGenericClass xClass, XGenericClass to) {
		if(!xClass.canCastTo(to))
			throw new XRuntimeException("Can't cast %s to %s", xClass, to);
	}

	public static void checkAccess(XClass xClass, XField field) {
		XClass xClass2 = field.getDeclaringClass();
		if(xClass.getVirtualMachine()!=xClass2.getVirtualMachine()){
			throw new XRuntimeException("%s has a diferent VM than %s", xClass, xClass2);
		}
		int modifier = xClass2.getModifier();
		int fModifier = field.getModifier();
		XClass checkClass1 = xClass;
		while(checkClass1.getOuterClass()!=null){
			checkClass1 = checkClass1.getOuterClass();
		}
		XClass checkClass2 = xClass2;
		while(checkClass2.getOuterClass()!=null){
			checkClass2 = checkClass2.getOuterClass();
		}
		boolean sameOuterClass = checkClass1==checkClass2;
		if(XModifier.isPrivate(modifier) || XModifier.isPrivate(fModifier)){
			if(!sameOuterClass){
				throw new XRuntimeException("%s can't access %s", xClass, field);
			}
		}else if(XModifier.isProtected(modifier) || XModifier.isProtected(fModifier)){
			if(!sameOuterClass && xClass.canCastTo(xClass2) && xClass.getPackage()!=xClass2.getPackage()){
				throw new XRuntimeException("%s can't access %s", xClass, field);
			}
		}else if(!XModifier.isPublic(modifier) || !XModifier.isPublic(fModifier)){
			if(!sameOuterClass && xClass.getPackage()!=xClass2.getPackage()){
				throw new XRuntimeException("%s can't access %s", xClass, field);
			}
		}
	}

	public static void checkAccess(XClass xClass, XMethod method) {
		XClass xClass2 = method.getDeclaringClass();
		if(xClass.getVirtualMachine()!=xClass2.getVirtualMachine()){
			throw new XRuntimeException("%s has a diferent VM than %s", xClass, xClass2);
		}
		int modifier = xClass2.getModifier();
		int fModifier = method.getModifier();
		XClass checkClass1 = xClass;
		while(checkClass1.getOuterClass()!=null){
			checkClass1 = checkClass1.getOuterClass();
		}
		XClass checkClass2 = xClass2;
		while(checkClass2.getOuterClass()!=null){
			checkClass2 = checkClass2.getOuterClass();
		}
		boolean sameOuterClass = checkClass1==checkClass2;
		if(XModifier.isPrivate(modifier) || XModifier.isPrivate(fModifier)){
			if(!sameOuterClass){
				throw new XRuntimeException("%s can't access %s", xClass, method);
			}
		}else if(XModifier.isProtected(modifier) || XModifier.isProtected(fModifier)){
			if(!sameOuterClass && xClass.canCastTo(xClass2) && xClass.getPackage()!=xClass2.getPackage()){
				throw new XRuntimeException("%s can't access %s", xClass, method);
			}
		}else if(!XModifier.isPublic(modifier) || !XModifier.isPublic(fModifier)){
			if(!sameOuterClass && xClass.getPackage()!=xClass2.getPackage()){
				throw new XRuntimeException("%s can't access %s", xClass, method);
			}
		}
	}
	
}
