package xscript.runtime.object;

import xscript.runtime.XModifier;
import xscript.runtime.XRuntimeException;
import xscript.runtime.clazz.XArray;
import xscript.runtime.clazz.XPrimitive;
import xscript.runtime.genericclass.XGenericClass;

public class XObject {

	private XGenericClass xClass;
	private byte[] data;
	private byte[] userData;
	private boolean isVisible;
	
	protected XObject(XGenericClass xClass){
		if(XModifier.isAbstract(xClass.getXClass().getModifier()))
			throw new XRuntimeException("Can't create Object form abstract class %s", xClass);
		this.xClass = xClass;
		data = new byte[xClass.getXClass().getObjectSize()];
	}
	
	protected XObject(XGenericClass xClass, int size) {
		if(XModifier.isAbstract(xClass.getXClass().getModifier()))
			throw new XRuntimeException("Can't create Object form abstract class %s", xClass);
		if(!(xClass.getXClass() instanceof XArray))
			throw new XRuntimeException("%s isn't an array", xClass);
		this.xClass = xClass;
		data = new byte[xClass.getXClass().getObjectSize()+size];
		((XArray)xClass.getXClass()).getLengthField().set(this, size);
	}
	
	public XGenericClass getXClass(){
		return xClass;
	}
	
	public byte[] getData(){
		return data;
	}
	
	public boolean isArray(){
		return xClass.getXClass() instanceof XArray;
	}
	
	public int getArrayLength(){
		if(isArray()){
			((XArray)xClass.getXClass()).getLengthField().get(this);
		}
		return 0;
	}
	
	public long getArrayElement(int index){
		if(isArray()){
			int size = XPrimitive.getSize(((XArray)xClass.getXClass()).getPrimitiveID());
			int i = xClass.getXClass().getObjectSize()+size*index;
			long l = 0;
			for(int j=0; j<size; j++){
				l <<= 8;
				l |= data[i+j];
			}
			return l;
		}
		return 0;
	}
	
	public void setArrayElement(int index, long value){
		if(isArray()){
			int size = XPrimitive.getSize(((XArray)xClass.getXClass()).getPrimitiveID());
			int i = xClass.getXClass().getObjectSize()+size*index;
			for(int j=size-1; j>=0; j--){
				data[i+j] = (byte) (value & 255);
				value >>>= 8;
			}
		}
	}
	
	public byte[] getUserData(){
		return userData;
	}
	
	public void setUserData(byte[] userData){
		this.userData = userData;
	}
	
	public void resetVisibility(){
		isVisible = false;
	}
	
	public void markVisible(){
		if(!isVisible){
			isVisible = true;
			xClass.getXClass().markObjectObjectsVisible(this);
		}
	}
	
	public boolean isVisible(){
		return isVisible;
	}
	
}
