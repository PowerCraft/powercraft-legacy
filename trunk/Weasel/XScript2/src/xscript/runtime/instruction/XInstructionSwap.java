package xscript.runtime.instruction;

import java.io.IOException;

import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;

public class XInstructionSwap extends XInstruction {

	public XInstructionSwap(){}
	
	public XInstructionSwap(XInputStream inputStream){}
	
	@Override
	public void run(XVirtualMachine vm, XThread thread, XMethodExecutor methodExecutor) {
		long[] v1 = methodExecutor.pop();
		long[] v2 = methodExecutor.pop();
		methodExecutor.push(v1[0], (int) v1[1]);
		methodExecutor.push(v2[0], (int) v2[1]);
	}

	@Override
	protected void save(XOutputStream outputStream) throws IOException {}

	@Override
	public String getSource() {
		return "swap";
	}

}
