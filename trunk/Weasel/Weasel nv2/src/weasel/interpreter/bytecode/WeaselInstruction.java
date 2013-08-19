package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public abstract class WeaselInstruction {
	
	@SuppressWarnings("unchecked")
	private static final Class<? extends WeaselInstruction>[] instructions = new Class[]{
		WeaselInstructionCatch.class,
		WeaselInstructionJump.class,
		WeaselInstructionLine.class,
		WeaselInstructionSync.class,
		WeaselInstructionSyncBreak.class,
		WeaselInstructionSyncEnd.class,
		WeaselInstructionTry.class,
		WeaselInstructionTryBreak.class,
		WeaselInstructionTryEnd.class
	};
	
	public abstract void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method);

	protected abstract void saveToDataStream(DataOutputStream dataOutputStream) throws IOException;
	
	public static int getInstructionID(WeaselInstruction instruction){
		return getInstructionID(instruction.getClass());
	}
	
	public static int getInstructionID(Class<? extends WeaselInstruction> instructionClass){
		for(int i=0; i<instructions.length; i++){
			if(instructions[i] == instructionClass){
				return i;
			}
		}
		return -1;
	}
	
	public static void saveInstructionToDataStream(WeaselInstruction instruction, DataOutputStream dataOutputStream) throws IOException{
		dataOutputStream.writeInt(getInstructionID(instruction));
		instruction.saveToDataStream(dataOutputStream);
	}
	
	public static WeaselInstruction loadInstructionFromDataStream(DataInputStream dataInputStream) throws IOException{
		int instructionID = dataInputStream.readInt();
		Class<? extends WeaselInstruction> instructionClass = instructions[instructionID];
		try {
			return instructionClass.getConstructor(DataInputStream.class).newInstance(dataInputStream);
		} catch (InvocationTargetException e) {
			Throwable throwable = e.getCause();
			if(throwable instanceof IOException){
				throw (IOException)throwable;
			}
			throwable.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	
}
