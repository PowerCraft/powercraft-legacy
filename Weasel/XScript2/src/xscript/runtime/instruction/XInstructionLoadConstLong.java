package xscript.runtime.instruction;

import java.io.IOException;

import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;

public class XInstructionLoadConstLong extends XInstruction {

	private long value;
	
	public XInstructionLoadConstLong(long value){
		this.value = value;
	}
	
	public XInstructionLoadConstLong(XInputStream inputStream) throws IOException{
		value = inputStream.readLong();
	}
	
	@Override
	public void run(XVirtualMachine vm, XThread thread, XMethodExecutor methodExecutor) {
		methodExecutor.lPush(value);
	}

	@Override
	protected void save(XOutputStream outputStream) throws IOException {
		outputStream.writeLong(value);
	}

	@Override
	public String getSource() {
		return "lcl "+value;
	}

}
