package xscript.runtime.clazz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xscript.runtime.XRuntimeException;
import xscript.runtime.XVirtualMachine;

public class XClassProvider {

	protected XVirtualMachine virtualMachine;
	private XPackage rootPackage = new XPackage(null);
	private List<XClass> toPostLoad = new ArrayList<XClass>();
	private int rec = 0;
	private List<XClassLoader> classLoaders = new ArrayList<XClassLoader>();
	
	public final XPrimitive BOOL;
	public final XPrimitive BYTE;
	public final XPrimitive CHAR;
	public final XPrimitive SHORT;
	public final XPrimitive INT;
	public final XPrimitive LONG;
	public final XPrimitive FLOAT;
	public final XPrimitive DOUBLE;
	public final XPrimitive VOID;
	
	public XClassProvider(XVirtualMachine virtualMachine){
		this.virtualMachine = virtualMachine;
		BOOL = new XPrimitive(virtualMachine, XPrimitive.BOOL);
		BYTE = new XPrimitive(virtualMachine, XPrimitive.BYTE);
		CHAR = new XPrimitive(virtualMachine, XPrimitive.CHAR);
		SHORT = new XPrimitive(virtualMachine, XPrimitive.SHORT);
		INT = new XPrimitive(virtualMachine, XPrimitive.INT);
		LONG = new XPrimitive(virtualMachine, XPrimitive.LONG);
		FLOAT = new XPrimitive(virtualMachine, XPrimitive.FLOAT);
		DOUBLE = new XPrimitive(virtualMachine, XPrimitive.DOUBLE);
		VOID = new XPrimitive(virtualMachine, XPrimitive.VOID);
	}
	
	public XClass getXClass(String name) {
		XPackage xPackage = rootPackage.getChild(name);
		if(xPackage==null){
			rec++;
			try{
				createClass(name);
				xPackage = rootPackage.getChild(name);
				if(xPackage==null){
					throw new XRuntimeException("Class %s not found", name);
				}
				if(rec==1){
					postLoad();
				}
			}catch(Throwable e){
				rec--;
				if(!(e instanceof XRuntimeException)){
					e = new XRuntimeException(e, "Native error while load Class %s", name);
				}
				throw (XRuntimeException)e;
			}
			rec--;
		}
		if(xPackage instanceof XClass){
			XClass xClass = (XClass)xPackage;
			if(xClass.getState()==XClass.STATE_RUNNABLE){
				return xClass;
			}
		}
		throw new XRuntimeException("Class %s not found", name);
	}
	
	private void postLoad(){
		while(!toPostLoad.isEmpty()){
			XClass xClass = toPostLoad.remove(0);
			xClass.postLoad();
		}
	}
	
	protected void createClass(String name) {
		XInputStream inputStream = getInputStream(name);
		if(inputStream!=null){
			String fileName = inputStream.getFileName();
			String s[] = fileName.split("\\.");
			XClass xClass = null;
			for(int i=0; i<9; i++){
				if(fileName.equals("xscript.lang.Array"+XPrimitive.getWrapper(i))){
					xClass = new XArray(virtualMachine, s[s.length-1], i);
					break;
				}
			}
			if(xClass==null)
				xClass = new XClass(virtualMachine, s[s.length-1]);
			addClassToPackage(xClass, s);
			try{
				xClass.load(inputStream);
				inputStream.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	protected void addClassForLoading(XClass xClass){
		toPostLoad.add(xClass);
	}
	
	protected void removeClassForLoading(XClass xClass){
		toPostLoad.remove(xClass);
	}
	
	protected XInputStream getInputStream(String name){
		for(XClassLoader classLoader:classLoaders){
			XInputStream inputStream = classLoader.getInputStream(name);
			if(inputStream!=null)
				return inputStream;
		}
		return null;
	}
	
	protected void addClassToPackage(XClass xClass, String s[]){
		XPackage xPackage = rootPackage;
		for(int i=0; i<s.length-1; i++){
			XPackage xPackage2 = xPackage.getChild(s[i]);
			if(xPackage2==null){
				xPackage2 = new XPackage(s[i]);
				xPackage.addChild(xPackage2);
			}
			xPackage = xPackage2;
		}
		xPackage.addChild(xClass);
	}
	
	public void markVisible() {
		rootPackage.markVisible();
	}
	
	public void addClassLoader(XClassLoader classLoader){
		if(!classLoaders.contains(classLoader))
			classLoaders.add(classLoader);
	}

	public XClass getLoadedXClass(String name) {
		XPackage xPackage = rootPackage.getChild(name);
		if(xPackage instanceof XClass){
			XClass xClass = (XClass)xPackage;
			if(xClass.getState()==XClass.STATE_RUNNABLE){
				return xClass;
			}
		}
		return null;
	}

	public boolean existsClass(String className) {
		return rootPackage.getChild(className) instanceof XClass;
	}
	
}
