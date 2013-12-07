package xscript.runtime.genericclass;

import java.io.IOException;

import xscript.runtime.XRuntimeException;
import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XClass;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.threads.XGenericMethodProvider;

public class XClassPtrClassGeneric extends XClassPtr{
	
	private String className;
	private XClass xClass;
	private String genericName;
	private int genericID;
	
	public XClassPtrClassGeneric(String className, String genericName){
		this.className = className;
		this.genericName = genericName;
	}
	
	@Override
	public XClass getXClass(XVirtualMachine virtualMachine) {
		return null;
	}
	
	@Override
	public XGenericClass getXClass(XVirtualMachine virtualMachine, XGenericClass genericClass, XGenericMethodProvider methodExecutor) {
		if(xClass==null){
			xClass = virtualMachine.getClassProvider().getXClass(className);
			genericID = xClass.getGenericID(genericName);
			if(genericID==-1)
				throw new XRuntimeException("Can't find generic class %s", genericName);
		}
		return xClass.getGeneric(genericClass, genericID);
	}

	@Override
	public boolean isStatic() {
		return false;
	}

	@Override
	public void save(XOutputStream outputStream) throws IOException {
		outputStream.writeByte('C');
		outputStream.writeUTF(className);
		outputStream.writeUTF(genericName);
	}

	@Override
	public String toString() {
		return genericName;
	}
	
}