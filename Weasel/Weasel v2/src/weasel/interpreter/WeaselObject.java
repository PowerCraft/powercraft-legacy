package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WeaselObject implements WeaselSaveable {

	private final WeaselInterpreter interpreter;
	private final WeaselClass weaselClass;
	private final int parent;
	private final int[] dataBuffer;
	private boolean visible;
	private Object userData;
	
	public WeaselObject(WeaselInterpreter interpreter, int parent, WeaselClass weaselClass) {
		this.interpreter = interpreter;
		this.weaselClass = weaselClass;
		this.parent = parent;
		dataBuffer = new int[weaselClass.getDataBufferSize()];
		visible = true;
	}

	public WeaselObject(WeaselInterpreter interpreter, DataInputStream dataInputStream) throws IOException {
		this.interpreter = interpreter;
		parent = dataInputStream.readInt();
		weaselClass = interpreter.getClassByName(dataInputStream.readUTF());
		dataBuffer = new int[dataInputStream.readInt()];
		for(int i=0; i<dataBuffer.length; i++){
			dataBuffer[i] = dataInputStream.readInt();
		}
		if(dataInputStream.readBoolean()){
			userData = WeaselUserDataSaver.loadUserDataFromDataStream(interpreter, dataInputStream);
		}
	}
	
	public WeaselClass getWeaselClass(){
		return weaselClass;
	}
	
	public int[] getDataBuffer(){
		return dataBuffer;
	}
	
	public int getWeaselObjectPointer(int index){
		return dataBuffer[index];
	}
	
	public void setWeaselObjectPointer(int index, int value){
		dataBuffer[index] = value;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible(){
		return visible;
	}

	public int getParent(){
		return parent;
	}
	
	public void setThisAndKnownVisible(){
		visible = true;
		
	}
	
	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(weaselClass.getName());
		dataOutputStream.writeInt(dataBuffer.length);
		for(int i=0; i<dataBuffer.length; i++){
			dataOutputStream.writeInt(dataBuffer[i]);
		}
		dataOutputStream.writeBoolean(userData!=null);
		if(userData!=null){
			WeaselUserDataSaver.saveUserDataToDataStream(interpreter, dataOutputStream, userData);
		}
	}
	
	public Object getUserData(){
		return userData;
	}
	
	public void setUserData(Object userData){
		this.userData = userData;
	}
	
}
