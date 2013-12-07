package xscript.runtime.instruction;

import java.io.IOException;

import xscript.runtime.XChecks;
import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.genericclass.XClassPtr;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;

public class XInstructionNew extends XInstruction {

	private XClassPtr xClass;
	
	public XInstructionNew(XClassPtr xClass){
		this.xClass = xClass;
	}
	
	public XInstructionNew(XInputStream inputStream) throws IOException{
		xClass = XClassPtr.load(inputStream);
	}
	
	@Override
	public void run(XVirtualMachine vm, XThread thread, XMethodExecutor methodExecutor) {
		XGenericClass rClass = xClass.getXClass(vm, methodExecutor.getDeclaringClass(), methodExecutor);
		XChecks.checkAccess(methodExecutor.getDeclaringClass().getXClass(), rClass.getXClass());
		long pointer = vm.getObjectProvider().createObject(rClass);
		methodExecutor.oPush(pointer);
	}

	@Override
	protected void save(XOutputStream outputStream) throws IOException {
		xClass.save(outputStream);
	}

	@Override
	public String getSource() {
		return "new "+xClass;
	}

}
