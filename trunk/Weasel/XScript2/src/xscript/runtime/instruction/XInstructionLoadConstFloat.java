package xscript.runtime.instruction;

import java.io.IOException;

import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;

public class XInstructionLoadConstFloat extends XInstruction {

	private float value;
	
	public XInstructionLoadConstFloat(float value){
		this.value = value;
	}
	
	public XInstructionLoadConstFloat(XInputStream inputStream) throws IOException{
		value = inputStream.readFloat();
	}
	
	@Override
	public void run(XVirtualMachine vm, XThread thread, XMethodExecutor methodExecutor) {
		methodExecutor.fPush(value);
	}

	@Override
	protected void save(XOutputStream outputStream) throws IOException {
		outputStream.writeFloat(value);
	}

	@Override
	public String getSource() {
		return "lcf "+value;
	}

}
