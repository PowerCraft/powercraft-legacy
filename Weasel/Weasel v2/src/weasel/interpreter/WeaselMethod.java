package weasel.interpreter;

import java.io.DataInputStream;
import java.io.IOException;

import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselMethod implements WeaselModifier, WeaselNameable {

	private WeaselClassBuffer interpreter;
	private WeaselClass parentClass;
	private int modifier;
	private String methodName;
	private WeaselClass returnWeaselClass;
	private WeaselClass[] paramWeaselClasses;
	private WeaselNativeMethod nativeMethod;
	
	public WeaselMethod(WeaselClassBuffer interpreter, WeaselClass parentClass){
		this.interpreter = interpreter;
		this.parentClass = parentClass;
	}
	
	public WeaselMethod(WeaselClassBuffer interpreter, WeaselClass parentClass, DataInputStream dataInputStream) throws IOException {
		this.interpreter = interpreter;
		this.parentClass = parentClass;
		modifier = dataInputStream.readInt();
		methodName = dataInputStream.readUTF();
		returnWeaselClass = interpreter.getClassByName(dataInputStream.readUTF());
		int paramWeaselCount = dataInputStream.readInt();
		paramWeaselClasses = new WeaselClass[paramWeaselCount];
		for(int i=0; i<paramWeaselCount; i++){
			paramWeaselClasses[i] = interpreter.getClassByName(dataInputStream.readUTF());
		}
	}

	@Override
	public int getModifier() {
		return modifier;
	}

	@Override
	public String getName() {
		return methodName;
	}

	public String getDesk(){
		String desk="(";
		for(int i=0; i<paramWeaselClasses.length; i++){
			if(i!=0)
				desk += ",";
			desk += paramWeaselClasses[i].getName();
		}
		desk += ")";
		desk += returnWeaselClass.getName();
		return desk;
	}
	
	public WeaselClass[] getParamWeaselClasses(){
		return paramWeaselClasses; 
	}
	
	public String getNameAndDesk(){
		return parentClass.getName()+"."+methodName+getDesk();
	}
	
	public void invoke(WeaselThread thread){
		if((modifier & WeaselModifier.NATIVE)!=0){
			if(nativeMethod==null){
				nativeMethod = interpreter.getNativeMethod(getNameAndDesk());
			}
			nativeMethod.invoke(interpreter, methodName, null, null);
		}else{
			
		}
	}

	public WeaselInstruction getInstruction(int i) {
		return null;
	}

	public WeaselClass getParentClass() {
		return parentClass;
	}
	
}
