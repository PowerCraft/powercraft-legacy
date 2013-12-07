package xscript.runtime.instruction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import xscript.runtime.XRuntimeException;
import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;


public abstract class XInstruction {
	
	@SuppressWarnings("unchecked")
	public static Class<? extends XInstruction>[] instructions = new Class[256];
	
	static{
		
	}
	
	public abstract void run(XVirtualMachine vm, XThread thread, XMethodExecutor methodExecutor);

	protected abstract void save(XOutputStream outputStream) throws IOException;

	public abstract String getSource();

	public static XInstruction load(XInputStream inputStream) throws IOException {
		int instruction = inputStream.readUnsignedByte();
		Class<? extends XInstruction> c = instructions[instruction];
		if(c==null){
			throw new XRuntimeException("Unknow instruction %s", instruction);
		}
		try {
			return c.getConstructor(XInputStream.class).newInstance(inputStream);
		} catch (InvocationTargetException e) {
			Throwable e1 = e.getTargetException();
			if(e1 instanceof IOException){
				throw (IOException)e1;
			}else if(e1 instanceof RuntimeException){
				throw (RuntimeException)e1;
			}
			throw new XRuntimeException(e1, "Error while creating instruction %s", instruction);
		} catch (Exception e) {
			e.printStackTrace();
			throw new XRuntimeException(e, "Error while creating instruction %s", instruction);
		} 
	}
	
	public static void save(XOutputStream outputStream, XInstruction instruction) throws IOException{
		Class<? extends XInstruction> c = instruction.getClass();
		for(int i=0; i<instructions.length; i++){
			if(instructions[i] == c){
				outputStream.writeByte(i);
				instruction.save(outputStream);
				return;
			}
		}
		throw new XRuntimeException("Unknown instruction type %s", instruction.getClass());
	}
	
}
