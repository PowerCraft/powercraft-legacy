package xscript.runtime.genericclass;

import xscript.runtime.XRuntimeException;
import xscript.runtime.method.XMethod;
import xscript.runtime.threads.XGenericMethodProvider;

public class XGenericMethodProviderImp implements XGenericMethodProvider {

	private XMethod method;
	private XGenericClass[] genericClasses;
	
	public XGenericMethodProviderImp(XMethod method, XGenericClass[] genericClasses){
		this.method = method;
		this.genericClasses = genericClasses;
		if(method.getGenericParams()!=genericClasses.length)
			throw new XRuntimeException("Wrong count of generics");
	}
	
	@Override
	public XMethod getMethod() {
		return method;
	}

	@Override
	public XGenericClass getGeneric(int genericID) {
		return genericClasses[genericID];
	}

}
