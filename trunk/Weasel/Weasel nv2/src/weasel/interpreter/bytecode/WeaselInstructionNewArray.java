package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselObject;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionNewArray extends WeaselInstruction {

	private final String className;
	private final int sizes;
	private WeaselClass weaselClass;
	
	public WeaselInstructionNewArray(String className, int sizes){
		this.className = className;
		this.sizes = sizes;
	}
	
	public WeaselInstructionNewArray(DataInputStream dataInputStream) throws IOException{
		className = dataInputStream.readUTF();
		sizes = dataInputStream.readInt();
	}

	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		resolve(interpreter);
		int[] realSizes = new int[sizes];
		for(int i=0; i<sizes; i++){
			realSizes[sizes-i-1] = (Integer)thread.popValue();
		}
		int obj = interpreter.baseTypes.createArrayObject(realSizes[0], weaselClass);
		fill(interpreter, interpreter.getObject(obj), 1, realSizes);
		thread.pushObject(obj);
	}
	
	private void fill(WeaselInterpreter interpreter, WeaselObject array, int depth, int[] realSizes){
		WeaselClass weaselArrayClass = array.getWeaselClass();
		for(int i=0; i<realSizes[depth-1]; i++){
			int obj = interpreter.baseTypes.createArrayObject(realSizes[depth], weaselArrayClass);
			if(depth+1<realSizes.length)
				fill(interpreter, interpreter.getObject(obj), depth+1, realSizes);
			interpreter.baseTypes.setArrayObject(array, i, obj);
		}
	}
	
	public void resolve(WeaselInterpreter interpreter){
		if(weaselClass==null){
			weaselClass = interpreter.getWeaselClass(className);
		}
	}
	
	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(className);
		dataOutputStream.writeInt(sizes);
	}

	@Override
	public String toString() {
		return "new "+sizes+" "+className;
	}
	
}
