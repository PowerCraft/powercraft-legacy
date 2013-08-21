package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WeaselGenericInfo implements WeaselSaveable {

	public Object[] generics;
	
	public WeaselGenericInfo(Object[] generics){
		this.generics = generics;
	}

	public WeaselGenericInfo(WeaselInterpreter interpreter, DataInputStream dataInputStream) throws IOException{
		generics = new Object[dataInputStream.readInt()];
		for(int i=0; i<generics.length; i++){
			if(dataInputStream.readBoolean()){
				generics[i] = interpreter.getWeaselClass(dataInputStream.readUTF());
			}else{
				generics[i] = dataInputStream.readInt();
			}
		}
	}
	
	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(generics.length);
		for(Object o:generics){
			if(o instanceof WeaselClass){
				dataOutputStream.writeBoolean(true);
				dataOutputStream.writeUTF(((WeaselClass)o).getByteName());
			}else{
				dataOutputStream.writeBoolean(false);
				dataOutputStream.writeInt((Integer)o);
			}
		}
	}
	
}
