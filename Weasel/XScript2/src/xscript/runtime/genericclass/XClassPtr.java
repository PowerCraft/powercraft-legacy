package xscript.runtime.genericclass;

import java.io.IOException;

import xscript.runtime.XRuntimeException;
import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XClass;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.threads.XGenericMethodProvider;

public abstract class XClassPtr {
	
	public abstract XClass getXClass(XVirtualMachine virtualMachine);
	
	public XClass getXClassNonNull(XVirtualMachine virtualMachine){
		XClass xClass = getXClass(virtualMachine);
		if(xClass==null)
			throw new XRuntimeException("Can't get class because it's a generic argument");
		return xClass;
	}
	
	public abstract XGenericClass getXClass(XVirtualMachine virtualMachine, XGenericClass genericClass, XGenericMethodProvider methodExecutor);
	
	public abstract boolean isStatic();

	public abstract void save(XOutputStream outputStream) throws IOException;
	
	@Override
	public abstract String toString();
	
	public static XClassPtr load(XInputStream inputStream) throws IOException {
		return load(inputStream.readUnsignedByte(), inputStream);
	}
	
	public static XClassPtr load(int i, XInputStream inputStream) throws IOException {
		String className;
		if(i=='N'){
			className = inputStream.readUTF();
			return new XClassPtrClass(className);
		}else if(i=='G'){
			className = inputStream.readUTF();
			XClassPtr[] generics = new XClassPtr[inputStream.readUnsignedByte()];
			for(int j=0; j<generics.length; j++){
				generics[i] = load(inputStream);
			}
			return new XClassPtrGeneric(className, generics);
		}else if(i=='M'){
			className = inputStream.readUTF();
			String methodName = inputStream.readUTF();
			String[] paramNames = new String[inputStream.readUnsignedByte()];
			for(int j=0; j<paramNames.length; j++){
				paramNames[j] = inputStream.readUTF();
			}
			String retName = inputStream.readUTF();
			String genericName = inputStream.readUTF();
			return new XClassPtrMethodGeneric(className, methodName, paramNames, retName, genericName);
		}else if(i=='C'){
			className = inputStream.readUTF();
			String genericName = inputStream.readUTF();
			return new XClassPtrClassGeneric(className, genericName);
		}else if(i=='z'){
			return new XClassPtrClass("boolean");
		}else if(i=='b'){
			return new XClassPtrClass("byte");
		}else if(i=='s'){
			return new XClassPtrClass("short");
		}else if(i=='c'){
			return new XClassPtrClass("char");
		}else if(i=='i'){
			return new XClassPtrClass("int");
		}else if(i=='l'){
			return new XClassPtrClass("long");
		}else if(i=='f'){
			return new XClassPtrClass("float");
		}else if(i=='d'){
			return new XClassPtrClass("double");
		}else if(i=='v'){
			return new XClassPtrClass("void");
		}else if(i=='['){
			int ni = inputStream.readUnsignedByte();
			if(ni=='z'){
				return new XClassPtrClass("xscript.lang.ArrayBoolean");
			}else if(ni=='b'){
				return new XClassPtrClass("xscript.lang.ArrayByte");
			}else if(ni=='s'){
				return new XClassPtrClass("xscript.lang.ArrayShort");
			}else if(ni=='c'){
				return new XClassPtrClass("xscript.lang.ArrayChar");
			}else if(ni=='i'){
				return new XClassPtrClass("xscript.lang.ArrayInt");
			}else if(ni=='l'){
				return new XClassPtrClass("xscript.lang.ArrayLong");
			}else if(ni=='f'){
				return new XClassPtrClass("xscript.lang.ArrayFloat");
			}else if(ni=='d'){
				return new XClassPtrClass("xscript.lang.ArrayDouble");
			}
			return new XClassPtrGeneric("xscript.lang.Array", new XClassPtr[]{load(ni, inputStream)});
		}
		return null;
	}
	
}
