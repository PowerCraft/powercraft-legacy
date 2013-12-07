package xscript.runtime.instruction;

import java.io.IOException;

import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;

public class XInstructionLoadConstDouble extends XInstruction {

	private double value;
	
	public XInstructionLoadConstDouble(double value){
		this.value = value;
	}
	
	public XInstructionLoadConstDouble(XInputStream inputStream) throws IOException{
		value = inputStream.readDouble();
	}
	
	@Override
	public void run(XVirtualMachine vm, XThread thread, XMethodExecutor methodExecutor) {
		methodExecutor.dPush(value);
	}

	@Override
	protected void save(XOutputStream outputStream) throws IOException {
		outputStream.writeDouble(value);
	}

	@Override
	public String getSource() {
		return "lcd "+value;
	}

}
