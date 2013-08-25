package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselBaseTypes;
import weasel.interpreter.WeaselChecks;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselNativeException;
import weasel.interpreter.WeaselObject;
import weasel.interpreter.WeaselThread;
import weasel.interpreter.WeaselThread.StackElement;

public class WeaselInstructionCast extends WeaselInstruction {

	private final String className;
	private WeaselClass weaselClass;
	
	public WeaselInstructionCast(String className){
		this.className = className;
	}
	
	public WeaselInstructionCast(DataInputStream dataInputStream) throws IOException{
		className = dataInputStream.readUTF();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		resolve(interpreter);
		StackElement se = thread.pop();
		if(se.value==null){
			WeaselObject object = interpreter.getObject(se.object);
			if(object!=null){
				WeaselChecks.checkCast(object.getWeaselClass(), weaselClass);
			}
			thread.push(se);
		}else{
			String name = weaselClass.getByteName();
			Object object = se.value;
			Class<?> c = object.getClass();
			if(name==WeaselBaseTypes.weaselBooleanClassName){
				if(c!=Boolean.class){
					throw new WeaselNativeException("Can't cast %s to %s", c, weaselClass);
				}
				thread.pushObject(interpreter.baseTypes.createBooleanObject((Boolean)object));
			}else if(name==WeaselBaseTypes.weaselCharClassName){
				if(c!=Character.class){
					throw new WeaselNativeException("Can't cast %s to %s", c, weaselClass);
				}
				thread.pushObject(interpreter.baseTypes.createCharObject((Character)object));
			}else if(name==WeaselBaseTypes.weaselByteClassName){
				if(c!=Byte.class){
					throw new WeaselNativeException("Can't cast %s to %s", c, weaselClass);
				}
				thread.pushObject(interpreter.baseTypes.createByteObject((Byte)object));
			}else if(name==WeaselBaseTypes.weaselShortClassName){
				if(c!=Short.class){
					throw new WeaselNativeException("Can't cast %s to %s", c, weaselClass);
				}
				thread.pushObject(interpreter.baseTypes.createShortObject((Short)object));
			}else if(name==WeaselBaseTypes.weaselIntClassName){
				if(c!=Integer.class){
					throw new WeaselNativeException("Can't cast %s to %s", c, weaselClass);
				}
				thread.pushObject(interpreter.baseTypes.createIntObject((Integer)object));
			}else if(name==WeaselBaseTypes.weaselLongClassName){
				if(c!=Long.class){
					throw new WeaselNativeException("Can't cast %s to %s", c, weaselClass);
				}
				thread.pushObject(interpreter.baseTypes.createLongObject((Long)object));
			}else if(name==WeaselBaseTypes.weaselFloatClassName){
				if(c!=Float.class){
					throw new WeaselNativeException("Can't cast %s to %s", c, weaselClass);
				}
				thread.pushObject(interpreter.baseTypes.createFloatObject((Float)object));
			}else if(name==WeaselBaseTypes.weaselDoubleClassName){
				if(c!=Double.class){
					throw new WeaselNativeException("Can't cast %s to %s", c, weaselClass);
				}
				thread.pushObject(interpreter.baseTypes.createDoubleObject((Double)object));
			}else{
				throw new WeaselNativeException("Can't cast %s to char", c);
			}
		}
	}

	public void resolve(WeaselInterpreter interpreter){
		if(weaselClass==null){
			weaselClass = interpreter.getWeaselClass(className);
		}
	}
	
	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(className);
	}

	@Override
	public String toString() {
		return "cast "+className;
	}
	
}
