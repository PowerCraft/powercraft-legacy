package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselThread;

public class WeaselInstructionReservate extends WeaselInstruction {

	private final int reservate;
	
	public WeaselInstructionReservate(int reservate){
		this.reservate = reservate;
	}
	
	public WeaselInstructionReservate(DataInputStream dataInputStream) throws IOException{
		reservate = dataInputStream.readInt();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		for(int i=0; i<reservate; i++){
			thread.push(null);
		}
	}

	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(reservate);
	}

	@Override
	public String toString() {
		return "reservate "+reservate;
	}

}
