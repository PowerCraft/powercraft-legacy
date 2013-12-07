package xscript.runtime.instruction;

import java.io.IOException;

import xscript.runtime.XChecks;
import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.clazz.XPrimitive;
import xscript.runtime.genericclass.XClassPtr;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;

public class XInstructionCheckCast extends XInstruction {

	private XClassPtr xClass;
	
	public XInstructionCheckCast(XClassPtr xClass){
		this.xClass = xClass;
	}
	
	public XInstructionCheckCast(XInputStream inputStream) throws IOException{
		xClass = XClassPtr.load(inputStream);
	}
	
	@Override
	public void run(XVirtualMachine vm, XThread thread, XMethodExecutor methodExecutor) {
		long[] value = methodExecutor.pop();
		methodExecutor.push(value[0], (int) value[1]);
		XGenericClass genericClass = XPrimitive.getXClass(vm, value[0], (int) value[1]);
		if(genericClass!=null)
			XChecks.checkCast(genericClass, xClass.getXClass(vm, methodExecutor.getDeclaringClass(), methodExecutor));
	}

	@Override
	protected void save(XOutputStream outputStream) throws IOException {
		xClass.save(outputStream);
	}

	@Override
	public String getSource() {
		return "cc "+xClass;
	}

}
