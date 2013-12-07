package xscript.runtime;

import xscript.runtime.clazz.XClassLoader;
import xscript.runtime.clazz.XClassProvider;
import xscript.runtime.nativemethod.XNativeProvider;
import xscript.runtime.object.XObjectProvider;
import xscript.runtime.threads.XThreadProvider;

public class XVirtualMachine {

	private XClassProvider classProvider;
	private XObjectProvider objectProvider;
	private XNativeProvider nativeProvider;
	private XThreadProvider threadProvider;

	public XVirtualMachine(XClassLoader standartClassLoader, int memSize){
		classProvider = createClassProvider();
		objectProvider = new XObjectProvider(this, memSize);
		nativeProvider = new XNativeProvider(this);
		threadProvider = new XThreadProvider(this);
		classProvider.addClassLoader(standartClassLoader);
	}
	
	protected XClassProvider createClassProvider(){
		return new XClassProvider(this);
	}
	
	public XClassProvider getClassProvider() {
		return classProvider;
	}
	
	public XObjectProvider getObjectProvider() {
		return objectProvider;
	}
	
	public XNativeProvider getNativeProvider() {
		return nativeProvider;
	}
	
	public XThreadProvider getThreadProvider() {
		return threadProvider;
	}
	
}
