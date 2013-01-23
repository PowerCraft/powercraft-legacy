package powercraft.management;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PC_ClassInfo {
	
	public static final int MN = 0xCAFEBABE;
	
    public static final int JV1_1 = 0x2d;
    public static final int JV1_2 = 0x2e;
    public static final int JV1_3 = 0x2f;
    public static final int JV1_4 = 0x30;
    public static final int JV1_5 = 0x31;
    public static final int JV1_6 = 0x32;
    public static final int JV1_7 = 0x33;
	
    public static final byte CONSTANTPOOLTAG_UTF8 = 1;
    public static final byte CONSTANTPOOLTAG_INTEGER = 3;
    public static final byte CONSTANTPOOLTAG_FLOAT = 4;
    public static final byte CONSTANTPOOLTAG_LONG = 5;
    public static final byte CONSTANTPOOLTAG_DOUBLE = 6;
    public static final byte CONSTANTPOOLTAG_CLASSREF = 7;
    public static final byte CONSTANTPOOLTAG_STRINGREF = 8;
    public static final byte CONSTANTPOOLTAG_FIELDREF = 9;
    public static final byte CONSTANTPOOLTAG_METHODREF = 10;
    public static final byte CONSTANTPOOLTAG_INTERFACEMETHODREF = 11;
    public static final byte CONSTANTPOOLTAG_NAMEANDTYPEDESC = 12;
    
    public static final byte CONSTANTPOOLTAG_HANDLE = 15;
    public static final byte CONSTANTPOOLTAG_MTYPE = 16;
    public static final byte CONSTANTPOOLTAG_INDY = 18;
	
	private short minorVersion;
	private short mayorVersion;
	
	private int access;
	
	private String className;
	
	private String superClassName;
	
	private String interfaceNames[];
	
	public PC_ClassInfo(byte[] code){
		
		try {
			DataInputStream is = new DataInputStream(new ByteArrayInputStream(code));
			
			if(is.readInt() != MN){
				throw new IllegalArgumentException("Not a Java Class file");
			}
			
			minorVersion = is.readShort();
			mayorVersion = is.readShort();
			if(mayorVersion>JV1_7){
				throw new IllegalArgumentException("To hight Java version");
			}
			
			Object[] constantPool = new Object[is.readUnsignedShort()];
			
			for(int i=1; i<constantPool.length; i++){
		        Object o = null;
		        byte b = is.readByte();
		        switch (b) {
		        case CONSTANTPOOLTAG_UTF8:
		        	o = is.readUTF();
		            break;
		        case CONSTANTPOOLTAG_INTEGER:
		        	o = is.readInt();
		        	break;
		        case CONSTANTPOOLTAG_FLOAT:
		        	o = is.readFloat();
		        	break;
		        case CONSTANTPOOLTAG_LONG:
		        	o = is.readLong();
		        	i++;
		        	break;
		        case CONSTANTPOOLTAG_DOUBLE:
		        	o = is.readDouble();
		        	i++;
		        	break;
		        case CONSTANTPOOLTAG_CLASSREF:
		        case CONSTANTPOOLTAG_STRINGREF:
		        case CONSTANTPOOLTAG_MTYPE:
		        	o = is.readUnsignedShort();
		        	break;
		        case CONSTANTPOOLTAG_FIELDREF:
		        case CONSTANTPOOLTAG_METHODREF:
		        case CONSTANTPOOLTAG_INTERFACEMETHODREF:
		        case CONSTANTPOOLTAG_NAMEANDTYPEDESC:
		        	o = is.readInt();
		        	break;
		        case CONSTANTPOOLTAG_INDY:
		        	o = is.readInt();
		        	break;
		        case CONSTANTPOOLTAG_HANDLE:
		        	o = new int[]{is.readShort(), is.readByte()};
		        	break;
		        default:
		        	return;
		        }
		        constantPool[i] = o;
			}
			
			access = is.readUnsignedShort();
			
			className = ((String)constantPool[(Integer)constantPool[is.readUnsignedShort()]]).replace('/', '.');
			
			superClassName = ((String)constantPool[(Integer)constantPool[is.readUnsignedShort()]]).replace('/', '.');
			
			interfaceNames = new String[is.readUnsignedShort()];
			
			for(int i=0; i<interfaceNames.length; i++){
				interfaceNames[i] = ((String)constantPool[(Integer)constantPool[is.readUnsignedShort()]]).replace('/', '.');
			}
			
			is.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getClassVersion(){
		return mayorVersion;
	}
	
	public int getClassVersionMinor(){
		return minorVersion;
	}
	
	public int getClassAccess(){
		return access;
	}
	
	public String getClassName(){
		return className;
	}
	
	public String getSuperClassName(){
		return superClassName;
	}
	
	public String[] getInterfaceNames(){
		if(interfaceNames==null)
			return null;
		return interfaceNames.clone();
	}
	
}