package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class WeaselGenericClassInfo implements WeaselSaveable {

	public final WeaselClass genericClass;
	public final int genericID;
	public final Object[] generics;
	
	public WeaselGenericClassInfo(WeaselClass genericClass, int genericID, Object[] generics){
		this.genericClass = genericClass;
		this.genericID = genericID;
		this.generics = generics;
	}
	
	@Override
	public String toString() {
		return "WeaselGenericInfo [generics=" + Arrays.toString(generics) + "]";
	}

	public WeaselGenericClassInfo(WeaselInterpreter interpreter, DataInputStream dataInputStream) throws IOException{
		genericClass = interpreter.getWeaselClass(dataInputStream.readUTF());
		genericID = dataInputStream.readInt();
		generics = new Object[dataInputStream.readInt()];
		for(int i=0; i<generics.length; i++){
			if(dataInputStream.readBoolean()){
				generics[i] = new WeaselGenericClassInfo(interpreter, dataInputStream);
			}else{
				generics[i] = dataInputStream.readInt();
			}
		}
	}
	
	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(genericClass.getByteName());
		dataOutputStream.writeInt(genericID);
		dataOutputStream.writeInt(generics.length);
		for(Object o:generics){
			if(o instanceof WeaselGenericClassInfo){
				dataOutputStream.writeBoolean(true);
				((WeaselGenericClassInfo)o).saveToDataStream(dataOutputStream);
			}else{
				dataOutputStream.writeBoolean(false);
				dataOutputStream.writeInt((Integer)o);
			}
		}
	}

	public String getName(WeaselClass weaselClass) {
		String name;
		if(genericID!=-1){
			name = weaselClass.genericInformation[genericID].genericName;
			WeaselClass wc = genericClass;
			while(wc.isArray()){
				wc = wc.getArrayClass();
				name += "[]";
			}
		}else{
			name = genericClass.getRealName();
		}
		if(generics.length>0){
			name += "<";
			if(generics[0] instanceof WeaselGenericClassInfo){
				name += ((WeaselGenericClassInfo)generics[0]).getName(weaselClass);
			}else{
				name += weaselClass.genericInformation[(Integer)generics[0]].genericName;
			}
			for(int i=1; i<generics.length; i++){
				name += ", ";
				if(generics[i] instanceof WeaselGenericClassInfo){
					name += ((WeaselGenericClassInfo)generics[i]).getName(weaselClass);
				}else{
					name += weaselClass.genericInformation[(Integer)generics[i]].genericName;
				}
			}
			name += ">";
		}
		return name;
	}
	
}
