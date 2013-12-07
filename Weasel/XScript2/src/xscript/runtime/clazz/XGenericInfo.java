package xscript.runtime.clazz;

import java.io.IOException;

import xscript.runtime.XVirtualMachine;
import xscript.runtime.genericclass.XClassPtr;

public class XGenericInfo {

	private String genericName;
	private XClassPtr[] needExtends;
	
	public XGenericInfo(String genericName, XClassPtr needExtends[]){
		this.genericName = genericName;
		this.needExtends = needExtends;
	}
	
	public XGenericInfo(XVirtualMachine virtualMachine, XInputStream inputStream) throws IOException {
		genericName = inputStream.readUTF();
		needExtends = new XClassPtr[inputStream.readUnsignedByte()];
		for(int i=0; i<needExtends.length; i++){
			(needExtends[i] = XClassPtr.load(inputStream)).getXClass(virtualMachine);
		}
	}

	public String getName() {
		return genericName;
	}

	public void save(XOutputStream outputStream) throws IOException {
		outputStream.writeUTF(genericName);
		outputStream.writeByte(needExtends.length);
		for(int i=0; i<needExtends.length; i++){
			needExtends[i].save(outputStream);
		}
	}
	
}
