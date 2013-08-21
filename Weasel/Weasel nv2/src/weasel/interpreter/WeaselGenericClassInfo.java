package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class WeaselGenericClassInfo implements WeaselSaveable {

	public final WeaselClass genericClass;
	public final int genericID;
	public final WeaselGenericClassInfo[] generics;
	
	public WeaselGenericClassInfo(WeaselClass genericClass, int genericID, WeaselGenericClassInfo[] generics){
		if(genericClass==null)
			throw new NullPointerException();
		this.genericClass = genericClass;
		this.genericID = genericID;
		this.generics = generics;
	}
	
	@Override
	public String toString() {
		return "WeaselGenericInfo [genericClass="+genericClass+", generics=" + Arrays.toString(generics) + "]";
	}

	public WeaselGenericClassInfo(WeaselInterpreter interpreter, DataInputStream dataInputStream) throws IOException{
		genericClass = interpreter.getWeaselClass(dataInputStream.readUTF());
		genericID = dataInputStream.readInt();
		generics = new WeaselGenericClassInfo[dataInputStream.readInt()];
		for(int i=0; i<generics.length; i++){
			generics[i] = new WeaselGenericClassInfo(interpreter, dataInputStream);
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
			name += generics[0].getName(weaselClass);
			for(int i=1; i<generics.length; i++){
				name += ", ";
				name += generics[i].getName(weaselClass);
			}
			name += ">";
		}
		return name;
	}

	public WeaselClass getWeaselClass(WeaselInterpreter weaselInterpreter, WeaselClass[] weaselClass) {
		if(genericID==-1)
			return genericClass;
		String cn = weaselClass[genericID].getByteName();
		WeaselClass wc = genericClass;
		while(wc.isArray()){
			wc = wc.getArrayClass();
			cn = "["+cn;
		}
		return weaselInterpreter.getWeaselClass(cn);
	}
	
}
