package xscript.runtime.genericclass;

import java.io.IOException;

import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XClass;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.threads.XGenericMethodProvider;

public class XClassPtrGeneric extends XClassPtr{
	
	private String className;
	private XClassPtr[] genericPtrs;
	private XClass xClass;
	private XGenericClass generic;
	
	public XClassPtrGeneric(String className, XClassPtr[] genericPtrs){
		this.className = className;
		this.genericPtrs = genericPtrs;
	}
	
	@Override
	public XClass getXClass(XVirtualMachine virtualMachine) {
		if(xClass==null){
			xClass = virtualMachine.getClassProvider().getXClass(className);
		}
		return xClass;
	}
	
	@Override
	public XGenericClass getXClass(XVirtualMachine virtualMachine, XGenericClass genericClass, XGenericMethodProvider methodExecutor) {
		if(generic!=null)
			return generic;
		if(xClass==null){
			xClass = virtualMachine.getClassProvider().getXClass(className);
			if(isStatic()){
				XGenericClass[] generics = new XGenericClass[genericPtrs.length];
				for(int i=0; i<generics.length; i++){
					generics[i] = genericPtrs[i].getXClass(virtualMachine, genericClass, methodExecutor);
				}
				return generic = new XGenericClass(xClass, generics);
			}
		}
		XGenericClass[] generics = new XGenericClass[genericPtrs.length];
		for(int i=0; i<generics.length; i++){
			generics[i] = genericPtrs[i].getXClass(virtualMachine, genericClass, methodExecutor);
		}
		return new XGenericClass(xClass, generics);
	}

	@Override
	public boolean isStatic() {
		for(int i=0; i<genericPtrs.length; i++){
			if(!genericPtrs[i].isStatic())
				return false;
		}
		return true;
	}

	@Override
	public void save(XOutputStream outputStream) throws IOException {
		if(className.equals("xscript.lang.Array")){
			outputStream.writeByte('[');
			genericPtrs[0].save(outputStream);
		}else{
			outputStream.writeByte('G');
			outputStream.writeUTF(className);
			outputStream.writeByte(genericPtrs.length);
			for(int i=0; i<genericPtrs.length; i++){
				genericPtrs[i].save(outputStream);
			}
		}
	}

	@Override
	public String toString() {
		String g = "";
		if(genericPtrs.length>0){
			g = "<"+genericPtrs[0];
			for(int i=1; i<genericPtrs.length; i++){
				g += ", "+genericPtrs[i];
			}
			g += ">";
		}
		return className+g;
	}
}