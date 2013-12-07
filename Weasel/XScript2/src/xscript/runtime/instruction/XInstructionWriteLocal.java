package xscript.runtime.instruction;

import java.io.IOException;

import xscript.runtime.XChecks;
import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.clazz.XPrimitive;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;

public class XInstructionWriteLocal extends XInstruction {

	private final int local;
	
	public XInstructionWriteLocal(int local){
		this.local = local;
	}
	
	public XInstructionWriteLocal(XInputStream inputStream) throws IOException{
		local = inputStream.readUnsignedShort();
	}
	
	@Override
	public void run(XVirtualMachine vm, XThread thread, XMethodExecutor methodExecutor) {
		long value = methodExecutor.pop(XPrimitive.getPrimitiveID(methodExecutor.getLocalType(local).getXClass()));
		if(XPrimitive.getPrimitiveID(methodExecutor.getLocalType(local).getXClass())==XPrimitive.OBJECT){
			XChecks.checkCast(vm.getObjectProvider().getObject(value).getXClass(), methodExecutor.getLocalType(local));
		}
		methodExecutor.setLocal(local, value);
	}

	@Override
	protected void save(XOutputStream outputStream) throws IOException {
		outputStream.writeShort(local);
	}

	@Override
	public String getSource() {
		return "wl "+local;
	}

}
