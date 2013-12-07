package xscript.runtime;

import java.io.IOException;

import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.genericclass.XClassPtr;


public class XAnnotationEntry {
	
	public String name;
	public int type;
	public Object[] value;
	
	public XAnnotationEntry(XInputStream inputStream) throws IOException {
		name = inputStream.readUTF();
		type = inputStream.readUnsignedByte();
		if((type&1)==0){
			value = new Object[1];
		}else{
			value = new Object[inputStream.readUnsignedShort()];
		}
		switch(type>>1){
		case 0:
			for(int i=0; i<value.length; i++){
				value[i] = inputStream.readBoolean();
			}
			break;
		case 1:
			for(int i=0; i<value.length; i++){
				value[i] = inputStream.readByte();
			}
			break;
		case 2:
			for(int i=0; i<value.length; i++){
				value[i] = inputStream.readShort();
			}
			break;
		case 3:
			for(int i=0; i<value.length; i++){
				value[i] = inputStream.readChar();
			}
			break;
		case 4:
			for(int i=0; i<value.length; i++){
				value[i] = inputStream.readInt();
			}
			break;
		case 5:
			for(int i=0; i<value.length; i++){
				value[i] = inputStream.readLong();
			}
			break;
		case 6:
			for(int i=0; i<value.length; i++){
				value[i] = inputStream.readFloat();
			}
			break;
		case 7:
			for(int i=0; i<value.length; i++){
				value[i] = inputStream.readDouble();
			}
			break;
		case 8:
			for(int i=0; i<value.length; i++){
				value[i] = inputStream.readUTF();
			}
			break;
		case 9:
			for(int i=0; i<value.length; i++){
				Object[] obj = new Object[2];
				obj[0] = XClassPtr.load(inputStream);
				obj[1] = inputStream.readUTF();
				value[i] = obj;
			}
			break;
		case 10:
			for(int i=0; i<value.length; i++){
				value[i] = XClassPtr.load(inputStream);
			}
			break;
		}
	}

	public XAnnotationEntry(String name, int type, Object[] value){
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
	public void save(XOutputStream outputStream) throws IOException {
		outputStream.writeUTF(name);
		outputStream.writeByte(type);
		if((type&1)!=0){
			outputStream.writeShort(value.length);
		}
		switch(type>>1){
		case 0:
			for(int i=0; i<value.length; i++){
				outputStream.writeBoolean((Boolean)value[i]);
			}
			break;
		case 1:
			for(int i=0; i<value.length; i++){
				outputStream.writeByte((Byte)value[i]);
			}
			break;
		case 2:
			for(int i=0; i<value.length; i++){
				outputStream.writeShort((Short)value[i]);
			}
			break;
		case 3:
			for(int i=0; i<value.length; i++){
				outputStream.writeChar((Character)value[i]);
			}
			break;
		case 4:
			for(int i=0; i<value.length; i++){
				outputStream.writeInt((Integer)value[i]);
			}
			break;
		case 5:
			for(int i=0; i<value.length; i++){
				outputStream.writeLong((Long)value[i]);
			}
			break;
		case 6:
			for(int i=0; i<value.length; i++){
				outputStream.writeFloat((Float)value[i]);
			}
			break;
		case 7:
			for(int i=0; i<value.length; i++){
				outputStream.writeDouble((Double)value[i]);
			}
			break;
		case 8:
			for(int i=0; i<value.length; i++){
				outputStream.writeUTF((String)value[i]);
			}
			break;
		case 9:
			for(int i=0; i<value.length; i++){
				Object[] obj = (Object[])value[i];
				((XClassPtr)obj[0]).save(outputStream);
				outputStream.writeUTF((String)obj[1]);
			}
			break;
		case 10:
			for(int i=0; i<value.length; i++){
				((XClassPtr)value[i]).save(outputStream);
			}
			break;
		}
	}
	
}
