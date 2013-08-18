package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WeaselObject implements WeaselSaveable{

	protected final WeaselClass weaselClass;
	protected final int parent;
	protected final int[] easyTypes;
	protected final int[] objectRefs;
	protected boolean isVisible;
	
	protected WeaselObject(WeaselClass weaselClass, int parent, int arrayLength) {
		WeaselChecks.checkCreationClass(weaselClass);
		this.weaselClass = weaselClass;
		this.parent = parent;
		if(weaselClass.isArray()){
			if(weaselClass.getArrayClass().isPrimitive()){
				if(WeaselPrimitive.getPrimitiveID(weaselClass.getArrayClass())==WeaselPrimitive.LONG||WeaselPrimitive.getPrimitiveID(weaselClass.getArrayClass())==WeaselPrimitive.DOUBLE){
					easyTypes = new int[arrayLength*2+1];
				}else{
					easyTypes = new int[arrayLength+1];
				}
				objectRefs = new int[0];
			}else{
				easyTypes = new int[1];
				objectRefs = new int[arrayLength];
			}
			easyTypes[0] = arrayLength;
		}else{
			easyTypes = new int[weaselClass.getEasyTypeCount()];
			objectRefs = new int[weaselClass.getObjectRefCount()];
		}
	}

	protected WeaselObject(WeaselInterpreter weaselInterpreter, DataInputStream dataInputStream) throws IOException {
		weaselClass = weaselInterpreter.getWeaselClass(dataInputStream.readUTF());
		WeaselChecks.checkCreationClass(weaselClass);
		parent = dataInputStream.readInt();
		easyTypes = new int[weaselClass.getEasyTypeCount()];
		for(int i=0; i<easyTypes.length; i++){
			easyTypes[i] = dataInputStream.readInt();
		}
		objectRefs = new int[weaselClass.getObjectRefCount()];
		for(int i=0; i<objectRefs.length; i++){
			objectRefs[i] = dataInputStream.readInt();
		}
	}

	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException{
		dataOutputStream.writeUTF(weaselClass.getByteName());
		dataOutputStream.writeInt(parent);
		for(int i=0; i<easyTypes.length; i++){
			dataOutputStream.writeInt(easyTypes[i]);
		}
		for(int i=0; i<objectRefs.length; i++){
			dataOutputStream.writeInt(objectRefs[i]);
		}
	}
	
	protected void setInvisible(){
		isVisible = false;
	}
	
	public void markVisible(){
		if(!isVisible){
			isVisible = true;
			WeaselInterpreter interpreter = getInterpreter();
			interpreter.getObject(parent).markVisible();
			for(int i=0; i<objectRefs.length; i++){
				interpreter.getObject(objectRefs[i]).markVisible();
			}
		}
	}
	
	public boolean isVisible(){
		return isVisible;
	}

	public WeaselInterpreter getInterpreter() {
		return weaselClass.getInterpreter();
	}

	public WeaselClass getWeaselClass() {
		return weaselClass;
	}
	
}
