package xscript.runtime.threads;

import xscript.runtime.XModifier;
import xscript.runtime.XRuntimeException;
import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XPrimitive;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.instruction.XInstruction;
import xscript.runtime.method.XMethod;
import xscript.runtime.object.XObject;


public class XThread {

	private XVirtualMachine virtualMachine;
	protected String name;
	private XMethodExecutor methodExecutor;
	private long[] result;
	private long exception;
	protected long sleepTime=0;
	protected long lastRunTime;
	protected boolean waiting;
	private byte[] userData;
	
	protected XThread(XVirtualMachine virtualMachine, String name, XMethod method, XGenericClass[] generics, long[] params){
		this.virtualMachine = virtualMachine;
		this.name = name;
		call(method, generics, params);
	}

	public void setUserData(byte[] userData){
		this.userData = userData;
	}
	
	public byte[] getUserData(){
		return userData;
	}
	
	protected void run(){
		if(methodExecutor!=null){
			XInstruction instruction = methodExecutor.getNextInstruction();
			while(instruction==null){
				XMethodExecutor oldMethodExecutor = methodExecutor.getParent();
				if(methodExecutor.getMethod().getReturnTypePrimitive() != XPrimitive.VOID){
					result = methodExecutor.getReturn();
					if(oldMethodExecutor!=null){
						oldMethodExecutor.push(result[0], (int) result[1]);
					}
				}
				methodExecutor = oldMethodExecutor;
				instruction = methodExecutor.getNextInstruction();
			}
			if(instruction!=null){
				try{
					instruction.run(virtualMachine, this, methodExecutor);
				}catch(XRuntimeException e){
					e.printStackTrace();
				}
				XObject obj = virtualMachine.getObjectProvider().getObject(exception);
				if(obj!=null){
					while(methodExecutor!=null && methodExecutor.jumpToExceptionHandlePoint(obj.getXClass(), exception)){
						methodExecutor = methodExecutor.getParent();
					}
				}
			}
		}
	}
	
	public long getResult(){
		if(getThreadState()==XThreadState.TERMINATED){
			return result[0];
		}
		return 0;
	}
	
	public void call(XMethod xMethod, XGenericClass[] generics, long[] params) {
		if(XModifier.isNative(xMethod.getModifier())){
			virtualMachine.getNativeProvider().call(this, methodExecutor, xMethod, generics, params);
		}else{
			methodExecutor = new XMethodExecutor(methodExecutor, xMethod, generics, params);
		}
	}

	protected void markVisible(){
		XObject obj = virtualMachine.getObjectProvider().getObject(exception);
		if(obj!=null){
			obj.markVisible();
		}
		if(result!=null){
			if(result[1]==XPrimitive.OBJECT){
				obj = virtualMachine.getObjectProvider().getObject(result[0]);
				if(obj!=null){
					obj.markVisible();
				}
			}
		}
		methodExecutor.markVisible();
	}
	
	public void setException(long exception){
		this.exception = exception;
	}
	
	public long getException(){
		return exception;
	}
	
	protected void sleepUpdate(){
		long currentTime = System.currentTimeMillis();
		sleepTime -= currentTime-lastRunTime;
		lastRunTime = currentTime;
		if(sleepTime<=0)
			sleepTime = 0;
	}
	
	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}
	
	public void sleep(long ms){
		sleepTime = ms;
	}
	
	public XThreadState getThreadState() {
		if(methodExecutor==null){
			if(exception==0)
				return XThreadState.TERMINATED;
			return XThreadState.ERRORED;
		}
		if(sleepTime>0)
			return XThreadState.SLEEPING;
		if(waiting)
			return XThreadState.WAITING;
		return XThreadState.RUNNING;
	}
	
}
