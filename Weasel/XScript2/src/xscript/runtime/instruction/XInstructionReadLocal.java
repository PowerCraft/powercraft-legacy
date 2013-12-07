package xscript.runtime.instruction;

import java.io.IOException;

import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.clazz.XPrimitive;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;

public class XInstructionReadLocal extends XInstruction {

	private final int local;
	
	public XInstructionReadLocal(int local){
		this.local = local;
	}
	
	public XInstructionReadLocal(XInputStream inputStream) throws IOException{
		local = inputStream.readUnsignedShort();
	}
	
	@Override
	public void run(XVirtualMachine vm, XThread thread, XMethodExecutor methodExecutor) {
		methodExecutor.push(methodExecutor.getLocal(local), XPrimitive.getPrimitiveID(methodExecutor.getLocalType(local).getXClass()));
	}

	@Override
	protected void save(XOutputStream outputStream) throws IOException {
		outputStream.writeShort(local);
	}

	@Override
	public String getSource() {
		return "rl "+local;
	}

}
