package xscript.runtime.clazz;

import xscript.runtime.genericclass.XClassPtr;

public class XClassTable {

	private final XClass xClass;
	private final int fieldStartID;
	private final int[] methodID;
	private final XClassPtr[] generics;
	
	protected XClassTable(XClass xClass, int fieldStartID, int[] methodID, XClassPtr[] generics){
		this.xClass = xClass;
		this.fieldStartID = fieldStartID;
		this.methodID = methodID;
		this.generics = generics;
	}
	
	public XClass getXClass(){
		return xClass;
	}
	
	public int getFieldStartID(){
		return fieldStartID;
	}
	
	public int getMethodID(int method){
		return methodID[method];
	}

	public XClassPtr getGenericPtr(int genericID) {
		return generics[genericID];
	}
	
}
