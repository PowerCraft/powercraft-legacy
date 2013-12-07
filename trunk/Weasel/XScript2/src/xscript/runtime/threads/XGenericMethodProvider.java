package xscript.runtime.threads;

import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.method.XMethod;

public interface XGenericMethodProvider {

	public XMethod getMethod();

	public XGenericClass getGeneric(int genericID);

}
