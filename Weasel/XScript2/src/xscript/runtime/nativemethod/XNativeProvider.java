package xscript.runtime.nativemethod;

import java.util.HashMap;

import xscript.runtime.XModifier;
import xscript.runtime.XRuntimeException;
import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XClass;
import xscript.runtime.clazz.XPrimitive;
import xscript.runtime.clazz.XWrapper;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.genericclass.XGenericMethodProviderImp;
import xscript.runtime.method.XMethod;
import xscript.runtime.object.XObject;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;

public class XNativeProvider {

	private int TIME_OUT=-1;
	private XVirtualMachine virtualMachine;
	private HashMap<String, XNativeMethod> nativeMethods;
	
	public XNativeProvider(XVirtualMachine virtualMachine) {
		this.virtualMachine = virtualMachine;
	}

	public void call(XThread thread, XMethodExecutor methodExecutor, XMethod method, XGenericClass[] generics, long[] params) {
		XNativeMethod nativeMethod = method.getNativeMethod();
		
		XObject _this = null;
		int i=0;
		if(!XModifier.isStatic(method.getModifier())){
			_this = virtualMachine.getObjectProvider().getObject(params[0]);
			i=1;
		}
		Object[] oParam = new Object[params.length-i];
		XGenericClass[] genericClasses = method.getParams(_this==null?null:_this.getXClass(), new XGenericMethodProviderImp(method, generics));
		if(oParam.length!=genericClasses.length)
			throw new XRuntimeException("An native call error happened");
		for(int j=0; j<oParam.length; j++){
			long value = params[j+i];
			XGenericClass genericClass = genericClasses[j];
			oParam[j] = XWrapper.getJavaObject(virtualMachine.getObjectProvider(), genericClass, value);
		}
		
		Object ret = invokeNative(nativeMethod, thread, methodExecutor, generics, _this, oParam);
		XGenericClass genericClass  = method.getReturnType(_this==null?null:_this.getXClass(), new XGenericMethodProviderImp(method, generics));
		if(XPrimitive.getPrimitiveID(genericClass.getXClass())!=XPrimitive.VOID){
			long l = XWrapper.getXObject(virtualMachine.getObjectProvider(), genericClass, ret);
			methodExecutor.push(l, XPrimitive.getPrimitiveID(genericClass.getXClass()));
		}
	}

	private Object invokeNative(XNativeMethod nativeMethod, XThread thread, XMethodExecutor methodExecutor, XGenericClass[] generics, XObject _this, Object[] params){
		if(TIME_OUT==-1){
			return nativeMethod.invoke(virtualMachine, thread, methodExecutor, generics, _this, params);
		}else{
			Object sync = new Object();
			InvokeThread invoke = new InvokeThread(sync, nativeMethod, virtualMachine, thread, methodExecutor, generics, _this, params);
			synchronized(sync){
				while(true){
					try {
						sync.wait(TIME_OUT);
						break;
					} catch (InterruptedException e) {}
				}
			}
			if(invoke.hasResult())
				return invoke.getReturn();
			throw new XRuntimeException("Timeout of native call");
		}
	}
	
	public XNativeMethod removeNativeMethod(String name) {
		return nativeMethods.remove(name);
	}

	public void addNativeMethod(String classname, String name, XNativeMethod nativeMethod){
		XClass xClass = virtualMachine.getClassProvider().getLoadedXClass(classname);
		XMethod method = xClass==null?null:xClass.getMethod(name);
		if(method==null){
			if(nativeMethod==null){
				nativeMethods.remove(classname+"."+name);
			}else{
				nativeMethods.put(classname+"."+name, nativeMethod);
			}
		}else{
			method.setNativeMethod(nativeMethod);
		}
	}
	
	private static class InvokeThread extends Thread{
		
		private Object ret;
		private boolean hasRet;
		private Object sync;
		private XNativeMethod nativeMethod;
		private XVirtualMachine virtualMachine;
		private XThread thread;
		private XMethodExecutor methodExecutor;
		private XGenericClass[] generics;
		private XObject _this;
		private Object[] params;
		
		public InvokeThread(Object sync, XNativeMethod nativeMethod, XVirtualMachine virtualMachine, XThread thread, XMethodExecutor methodExecutor, XGenericClass[] generics, XObject _this, Object[] params){
			this.sync = sync;
			this.nativeMethod = nativeMethod;
			this.virtualMachine = virtualMachine;
			this.thread = thread;
			this.methodExecutor = methodExecutor;
			this.generics = generics;
			this._this = _this;
			this.params = params;
			setDaemon(true);
			start();
		}

		public void run(){
			ret = nativeMethod.invoke(virtualMachine, thread, methodExecutor, generics, _this, params);
			hasRet = true;
			synchronized(sync){
				sync.notify();
			}
		}
		
		private Object getReturn(){
			return ret;
		}
		
		public boolean hasResult() {
			return hasRet;
		}
		
	}
	
}
