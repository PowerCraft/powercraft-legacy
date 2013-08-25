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
		String array="";
		if(genericID!=-1){
			name = weaselClass.genericInformation[genericID].genericName;
			WeaselClass wc = genericClass;
			while(wc.isArray()){
				wc = wc.getArrayClass();
				array += "[]";
			}
		}else{
			name = genericClass.getRealName();
			int p = name.indexOf('[');
			if(p!=-1){
				array = name.substring(p);
				name = name.substring(0, p);
			}
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
		return name+array;
	}
	
	public WeaselClass getWeaselClass(WeaselGenericClass parentClass) {
		if(genericID==-1){
			return genericClass;
		}
		String cn = parentClass.getGeneric(genericID).getBaseClass().getByteName();
		WeaselClass wc = genericClass;
		while(wc.isArray()){
			wc = wc.getArrayClass();
			cn = "["+cn;
		}
		return parentClass.getBaseClass().interpreter.getWeaselClass(cn);
	}

	public WeaselGenericClass getGenericClass(WeaselGenericClass parentClass) {
		if(genericID==-1){
			WeaselGenericClass[] gcs = new WeaselGenericClass[generics.length];
			for(int i=0; i<generics.length; i++){
				gcs[i] = generics[i].getGenericClass(parentClass);
			}
			return new WeaselGenericClass(genericClass, gcs);
		}
		String cn = parentClass.getGeneric(genericID).getBaseClass().getByteName();
		WeaselClass wc = genericClass;
		while(wc.isArray()){
			wc = wc.getArrayClass();
			cn = "["+cn;
		}
		return new WeaselGenericClass(parentClass.getBaseClass().interpreter.getWeaselClass(cn), new WeaselGenericClass[0]);
	}

	public WeaselGenericClass getGenericClass(WeaselGenericClass parentClass, WeaselGenericClass[] generics2) {
		if(genericID==-1){
			WeaselGenericClass[] gcs = new WeaselGenericClass[generics.length];
			for(int i=0; i<generics.length; i++){
				gcs[i] = generics[i].getGenericClass(parentClass, generics2);
			}
			return new WeaselGenericClass(genericClass, gcs);
		}
		String cn;
		if(parentClass.getGenericSize()>genericID){
			cn = parentClass.getGeneric(genericID).getBaseClass().getByteName();
		}else{
			cn = generics2[genericID-parentClass.getGenericSize()].getBaseClass().getByteName();
		}
		WeaselClass wc = genericClass;
		while(wc.isArray()){
			wc = wc.getArrayClass();
			cn = "["+cn;
		}
		return new WeaselGenericClass(parentClass.getBaseClass().interpreter.getWeaselClass(cn), new WeaselGenericClass[0]);
	}
	
}
