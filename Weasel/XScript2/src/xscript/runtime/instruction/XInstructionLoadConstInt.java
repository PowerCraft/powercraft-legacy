package xscript.runtime.instruction;

import java.io.IOException;

import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;

public class XInstructionLoadConstInt extends XInstruction {

	private int value;
	
	public XInstructionLoadConstInt(int value){
		this.value = value;
	}
	
	public XInstructionLoadConstInt(XInputStream inputStream) throws IOException{
		value = inputStream.readInt();
	}
	
	@Override
	public void run(XVirtualMachine vm, XThread thread, XMethodExecutor methodExecutor) {
		methodExecutor.iPush(value);
	}

	@Override
	protected void save(XOutputStream outputStream) throws IOException {
		outputStream.writeInt(value);
	}

	@Override
	public String getSource() {
		return "lci "+value;
	}

}
