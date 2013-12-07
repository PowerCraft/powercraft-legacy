package xscript.runtime.object;

import xscript.runtime.XRuntimeException;
import xscript.runtime.XVirtualMachine;
import xscript.runtime.genericclass.XGenericClass;

public class XObjectProvider {

	private XVirtualMachine virtualMachine;
	private XObject[] objects;
	
	public XObjectProvider(XVirtualMachine virtualMachine, int memSize){
		this.virtualMachine = virtualMachine;
		objects = new XObject[memSize];
	}
	
	public XObject getObject(long pointer) {
		if(pointer<=0 || pointer>objects.length)
			return null;
		return objects[(int) pointer-1];
	}
	
	public long getPointer(XObject object){
		if(object==null)
			return 0;
		for(int i=0; i<objects.length; i++){
			if(objects[i] == object)
				return i+1;
		}
		return 0;
	}
	
	public void gc() {
		for(int i=0; i<objects.length; i++){
			if(objects[i]!=null){
				objects[i].resetVisibility();
			}
		}
		virtualMachine.getClassProvider().markVisible();
		virtualMachine.getThreadProvider().markVisible();
		for(int i=0; i<objects.length; i++){
			if(objects[i]!=null && !objects[i].isVisible()){
				objects[i] = null;
			}
		}
	}
	
	private long getNextFreePointer(){
		for(int i=0; i<objects.length; i++){
			if(objects[i]==null){
				return i+1;
			}
		}
		gc();
		for(int i=0; i<objects.length; i++){
			if(objects[i]==null){
				return i+1;
			}
		}
		return 0;
	}
	
	protected long createPointerForObject(XObject xObject) {
		long pointer = getNextFreePointer();
		if(pointer==0)
			throw new XRuntimeException("Out of memory");
		objects[(int) (pointer-1)] = xObject;
		return pointer;
	}
	
	public long createObject(XGenericClass xClass){
		return createPointerForObject(new XObject(xClass));
	}
	
	public long createArray(XGenericClass xClass, int size){
		return createPointerForObject(new XObject(xClass, size));
	}

	public long createString(String value) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
}
