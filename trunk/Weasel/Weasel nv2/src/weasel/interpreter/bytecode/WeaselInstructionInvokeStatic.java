package weasel.interpreter.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.WeaselMethod;
import weasel.interpreter.WeaselMethodBody;
import weasel.interpreter.WeaselMethodExecutor;
import weasel.interpreter.WeaselNativeException;
import weasel.interpreter.WeaselNativeMethod;
import weasel.interpreter.WeaselObject;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.WeaselThread;
import weasel.interpreter.WeaselThread.StackElement;

public class WeaselInstructionInvokeStatic extends WeaselInstruction {

	private final String methodDesk;
	private WeaselMethod method;
	private WeaselNativeMethod nativeMethod;
	
	public WeaselInstructionInvokeStatic(String methodDesk){
		this.methodDesk = methodDesk;
	}
	
	public WeaselInstructionInvokeStatic(DataInputStream dataInputStream) throws IOException{
		methodDesk = dataInputStream.readUTF();
	}
	
	@Override
	public void run(WeaselInterpreter interpreter, WeaselThread thread, WeaselMethodExecutor method) {
		resolve(interpreter);
		WeaselMethodBody methodBody = this.method.getMethodFromClass(null);
		if(methodBody.isNative()){
			if(nativeMethod==null){
				nativeMethod = interpreter.getNativeMethod(methodBody.getNameAndDesk());
			}
			Object[] params = new Object[this.method.getParamClasses().length];
			for(int i=0; i<params.length; i++){
				StackElement se = thread.pop();
				if(se.value==null){
					params[i] = interpreter.getObject(se.object);
				}else{
					params[i] = se.value;
				}
			}
			Object ret = nativeMethod.invoke(interpreter, thread, method, methodBody.getNameAndDesk(), null, params);
			WeaselClass rc = this.method.getReturnClasses();
			switch(WeaselPrimitive.getPrimitiveID(rc)){
			case WeaselPrimitive.BOOLEAN:
				thread.pushValue((Boolean)ret);
				break;
			case WeaselPrimitive.CHAR:
				thread.pushValue((Character)ret);
				break;
			case WeaselPrimitive.BYTE:
				thread.pushValue((Byte)ret);
				break;
			case WeaselPrimitive.SHORT:
				thread.pushValue((Short)ret);
				break;
			case WeaselPrimitive.INT:
				thread.pushValue((Integer)ret);
				break;
			case WeaselPrimitive.LONG:
				thread.pushValue((Long)ret);
				break;
			case WeaselPrimitive.FLOAT:
				thread.pushValue((Float)ret);
				break;
			case WeaselPrimitive.DOUBLE:
				thread.pushValue((Double)ret);
				break;
			case WeaselPrimitive.VOID:
				if(ret!=null){
					throw new WeaselNativeException("Got return parameter %s but expect a void value in %s", ret, methodDesk);
				}
				break;
			default:
				if(ret==null){
					thread.pushObject(0);
				}else{
					int objPtr = (Integer)ret;
					WeaselObject obj = interpreter.getObject(objPtr);
					if(!obj.getWeaselClass().canCastTo(rc))
						throw new WeaselNativeException("Got return parameter %s but expect a %s value in %s", ret, rc, methodDesk);
					thread.pushObject(objPtr);
				}
				break;
			}
		}else{
			thread.call(methodBody);
		}
	}
	
	private void resolve(WeaselInterpreter interpreter){
		if(method==null){
			method = interpreter.getMethodByDesk(methodDesk).getMethod();
		}
	}
	
	@Override
	protected void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeUTF(methodDesk);
	}

	@Override
	public String toString() {
		return "invoke static "+methodDesk;
	}
	
}
