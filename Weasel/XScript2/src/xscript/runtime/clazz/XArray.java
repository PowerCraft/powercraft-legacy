package xscript.runtime.clazz;

import xscript.runtime.XVirtualMachine;
import xscript.runtime.object.XObject;


public class XArray extends XClass {

	private int primitiveID;
	private XField lengthField;
	
	protected XArray(XVirtualMachine virtualMachine, String name, int primitiveID) {
		super(virtualMachine, name);
		this.primitiveID = primitiveID;
	}

	public XField getLengthField(){
		return lengthField;
	}

	public int getPrimitiveID(){
		return primitiveID;
	}
	
	@Override
	public void markObjectObjectsVisible(XObject object) {
		super.markObjectObjectsVisible(object);
		if(primitiveID==XPrimitive.OBJECT){
			int size = object.getArrayLength();
			for(int i=0; i<size; i++){
				long pointer = object.getArrayElement(i);
				XObject element = virtualMachine.getObjectProvider().getObject(pointer);
				if(element!=null){
					element.markVisible();
				}
			}
		}
	}
	
}
