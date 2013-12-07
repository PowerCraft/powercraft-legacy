package xscript.runtime.instruction;

import java.io.IOException;

import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;

public class XInstructionLoadConstString extends XInstruction {

	private String value;
	
	public XInstructionLoadConstString(String value){
		this.value = value;
	}
	
	public XInstructionLoadConstString(XInputStream inputStream) throws IOException{
		value = inputStream.readUTF();
	}
	
	@Override
	public void run(XVirtualMachine vm, XThread thread, XMethodExecutor methodExecutor) {
		long pointer = vm.getObjectProvider().createString(value);
		methodExecutor.oPush(pointer);
	}

	@Override
	protected void save(XOutputStream outputStream) throws IOException {
		outputStream.writeUTF(value);
	}

	@Override
	public String getSource() {
		return "lcs \""+value+"\"";
	}

}
