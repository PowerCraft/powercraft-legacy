package xscript.runtime.genericclass;

import xscript.runtime.XRuntimeException;
import xscript.runtime.clazz.XClass;
import xscript.runtime.clazz.XClassTable;

public class XGenericClass {

	private XClass xClass;
	private XGenericClass[] generics;
	
	public XGenericClass(XClass xClass){
		this.xClass = xClass;
		if(xClass.getGenericParams()!=0){
			throw new XRuntimeException("Can't create a generic class of %s without generic params, need %s generic params", xClass, xClass.getGenericParams());
		}
	}
	
	public XGenericClass(XClass xClass, XGenericClass[] generics) {
		this.xClass = xClass;
		this.generics = generics;
		if(xClass.getGenericParams()!=generics.length){
			throw new XRuntimeException("Can't create a generic class of %s with %s generic params, need %s generic params", xClass, generics.length, xClass.getGenericParams());
		}
	}

	public XClass getXClass() {
		return xClass;
	}

	public XGenericClass getGeneric(int genericID) {
		return generics[genericID];
	}

	public boolean canCastTo(XGenericClass to) {
		XClass oClass = to.getXClass();
		if(!xClass.canCastTo(oClass))
			return false;
		if(to.generics==null)
			return true;
		XClassTable classTable = oClass.getClassTable(xClass);
		for(int i=0; i<to.generics.length; i++){
			if(!classTable.getGenericPtr(i).getXClass(xClass.getVirtualMachine(), generics[i], null).equals(to.generics[i])){
				return false;
			}
		}
		return true;
	}

}
