package xscript.runtime.genericclass;

import java.io.IOException;

import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XClass;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.threads.XGenericMethodProvider;

public class XClassPtrClass extends XClassPtr{
	
	private String className;
	private XGenericClass generic;
	
	public XClassPtrClass(String className){
		this.className = className;
	}

	@Override
	public XClass getXClass(XVirtualMachine virtualMachine) {
		if(generic==null){
			generic = new XGenericClass(virtualMachine.getClassProvider().getXClass(className));
		}
		return generic.getXClass();
	}
	
	@Override
	public XGenericClass getXClass(XVirtualMachine virtualMachine, XGenericClass genericClass, XGenericMethodProvider methodExecutor) {
		if(generic==null){
			generic = new XGenericClass(virtualMachine.getClassProvider().getXClass(className));
		}
		return generic;
	}

	@Override
	public boolean isStatic() {
		return true;
	}

	@Override
	public void save(XOutputStream outputStream) throws IOException {
		if(className.equals("boolean")){
			outputStream.writeByte('z');
		}else if(className.equals("byte")){
			outputStream.writeByte('b');
		}else if(className.equals("char")){
			outputStream.writeByte('c');
		}else if(className.equals("short")){
			outputStream.writeByte('s');
		}else if(className.equals("int")){
			outputStream.writeByte('i');
		}else if(className.equals("long")){
			outputStream.writeByte('l');
		}else if(className.equals("float")){
			outputStream.writeByte('f');
		}else if(className.equals("double")){
			outputStream.writeByte('d');
		}else if(className.equals("void")){
			outputStream.writeByte('v');
		}else if(className.equals("xscript.lang.ArrayBoolean")){
			outputStream.writeByte('[');
			outputStream.writeByte('z');
		}else if(className.equals("xscript.lang.ArrayByte")){
			outputStream.writeByte('[');
			outputStream.writeByte('b');
		}else if(className.equals("xscript.lang.ArrayChar")){
			outputStream.writeByte('[');
			outputStream.writeByte('c');
		}else if(className.equals("xscript.lang.ArrayShort")){
			outputStream.writeByte('[');
			outputStream.writeByte('s');
		}else if(className.equals("xscript.lang.ArrayInt")){
			outputStream.writeByte('[');
			outputStream.writeByte('i');
		}else if(className.equals("xscript.lang.ArrayLong")){
			outputStream.writeByte('[');
			outputStream.writeByte('l');
		}else if(className.equals("xscript.lang.ArrayFloat")){
			outputStream.writeByte('[');
			outputStream.writeByte('f');
		}else if(className.equals("xscript.lang.ArrayDouble")){
			outputStream.writeByte('[');
			outputStream.writeByte('d');
		}else{
			outputStream.writeByte('N');
			outputStream.writeUTF(className);
		}
	}

	@Override
	public String toString() {
		return className;
	}
	
}