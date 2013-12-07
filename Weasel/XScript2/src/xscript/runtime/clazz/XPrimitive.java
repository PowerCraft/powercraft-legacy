package xscript.runtime.clazz;

import java.io.IOException;

import xscript.runtime.XRuntimeException;
import xscript.runtime.XVirtualMachine;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.object.XObject;


public class XPrimitive extends XClass {

	public static final int OBJECT = 0;
	public static final int BOOL = 1;
	public static final int BYTE = 2;
	public static final int CHAR = 3;
	public static final int SHORT = 4;
	public static final int INT = 5;
	public static final int LONG = 6;
	public static final int FLOAT = 7;
	public static final int DOUBLE = 8;
	public static final int VOID = 9;
	
	private static final String[] NAME = {"Object", "bool", "byte", "char", "short", "int", "long", "float", "double", "void"};
	private static final String[] WRAPPER = {"", "Bool", "Byte", "Char", "Short", "Int", "Long", "Float", "Double", "Void"};
	
	private static final int[] SIZE = {4, 1, 1, 2, 2, 4, 8, 4, 8, 0};
	
	private int primitiveID;
	
	protected XPrimitive(XVirtualMachine virtualMachine, int primitiveID) {
		super(virtualMachine, NAME[primitiveID]);
		this.primitiveID = primitiveID;
		state = XClass.STATE_RUNNABLE;
	}

	@Override
	public void save(XOutputStream outputStream) throws IOException {
		throw new XRuntimeException("Can't save Primitive Classes");
	}

	public static int getPrimitiveID(XClass xClass){
		if(xClass instanceof XPrimitive){
			return ((XPrimitive) xClass).primitiveID;
		}
		return 0;
	}
	
	public static int getSize(XClass xClass) {
		return SIZE[getPrimitiveID(xClass)];
	}

	public static String getWrapper(int primitiveID){
		return WRAPPER[primitiveID];
	}

	public static String getName(int primitiveID) {
		return NAME[primitiveID];
	}

	public static int getSize(int primitiveID) {
		return SIZE[primitiveID];
	}

	public static XGenericClass getXClass(XVirtualMachine vm, long l, int i) {
		switch (i) {
		case OBJECT:
			XObject obj = vm.getObjectProvider().getObject(l);
			if(obj==null)
				return null;
			return obj.getXClass();
		case BOOL:
			return new XGenericClass(vm.getClassProvider().BOOL);
		case BYTE:
			return new XGenericClass(vm.getClassProvider().BYTE);
		case CHAR:
			return new XGenericClass(vm.getClassProvider().CHAR);
		case SHORT:
			return new XGenericClass(vm.getClassProvider().SHORT);
		case INT:
			return new XGenericClass(vm.getClassProvider().INT);
		case LONG:
			return new XGenericClass(vm.getClassProvider().LONG);
		case FLOAT:
			return new XGenericClass(vm.getClassProvider().FLOAT);
		case DOUBLE:
			return new XGenericClass(vm.getClassProvider().DOUBLE);
		case VOID:
			return new XGenericClass(vm.getClassProvider().VOID);
		}
		throw new XRuntimeException("Unknown primitiveID %s", i);
	}

	public static int getPrimitiveID(String param) {
		for(int i=1; i<NAME.length; i++){
			if(NAME[i].equals(param)){
				return i;
			}
		}
		return -1;
	}
	
}
