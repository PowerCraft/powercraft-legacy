package xscript.runtime.instruction;

import java.io.IOException;

import xscript.runtime.XChecks;
import xscript.runtime.XModifier;
import xscript.runtime.XRuntimeException;
import xscript.runtime.XVirtualMachine;
import xscript.runtime.clazz.XClass;
import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;
import xscript.runtime.clazz.XPrimitive;
import xscript.runtime.genericclass.XClassPtr;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.genericclass.XGenericMethodProviderImp;
import xscript.runtime.method.XMethod;
import xscript.runtime.object.XObject;
import xscript.runtime.threads.XMethodExecutor;
import xscript.runtime.threads.XThread;

public class XInstructionInvokeStatic extends XInstruction {

	private String className;
	private String methodName;
	private String[] methodParams;
	private String methodReturn;
	private XClassPtr[] generics;
	private XMethod method;
	
	public XInstructionInvokeStatic(XMethod method, XClassPtr[] generics){
		className = method.getDeclaringClass().getName();
		methodName = method.getSimpleName();
		methodParams = method.getMethodParamNames();
		methodReturn = method.getMethodReturnName();
		this.generics = generics;
		this.method = method;
		if(generics==null){
			if(method.getGenericParams()!=0)
				throw new XRuntimeException("Can't create a generic method %s without generic params, need %s generic params", method, method.getGenericParams());
		}else if(generics.length!=method.getGenericParams()){
			throw new XRuntimeException("Can't create a generic method %s with %s generic params, need %s generic params", method, generics.length, method.getGenericParams());
		}
		if(!XModifier.isStatic(method.getModifier())){
			throw new XRuntimeException("Method %s isn't static", method);
		}
	}
	
	public XInstructionInvokeStatic(XInputStream inputStream) throws IOException{
		className = inputStream.readUTF();
		methodName = inputStream.readUTF();
		methodParams = new String[inputStream.readUnsignedByte()];
		for(int i=0; i<methodParams.length; i++){
			methodParams[i] = inputStream.readUTF();
		}
		methodReturn = inputStream.readUTF();
		generics = new XClassPtr[inputStream.readUnsignedByte()];
		for(int i=0; i<generics.length; i++){
			generics[i] = XClassPtr.load(inputStream);
		}
	}
	
	@Override
	public void run(XVirtualMachine vm, XThread thread, XMethodExecutor methodExecutor) {
		resolve(vm, methodExecutor);
		final XGenericClass[] solvedGenerics = new XGenericClass[generics.length];
		for(int i=0; i<solvedGenerics.length; i++){
			solvedGenerics[i] = generics[i].getXClass(vm, methodExecutor.getDeclaringClass(), methodExecutor);
		}
		XGenericClass[] paramTypes = method.getParams(null, new XGenericMethodProviderImp(method, solvedGenerics));
		long[] params = new long[paramTypes.length];
		for(int i=params.length-1; i>=0; i++){
			int pID = XPrimitive.getPrimitiveID(paramTypes[i].getXClass());
			params[i] = methodExecutor.pop(pID);
			if(pID==XPrimitive.OBJECT){
				XObject obj = vm.getObjectProvider().getObject(params[i]);
				XChecks.checkCast(obj.getXClass(), paramTypes[i]);
			}
		}
		thread.call(method, solvedGenerics, params);
	}

	private void resolve(XVirtualMachine vm, XMethodExecutor methodExecutor){
		if(method==null){
			XClass xClass = vm.getClassProvider().getXClass(className);
			method = xClass.getMethod(methodName, methodParams, methodReturn);
			XChecks.checkAccess(methodExecutor.getMethod().getDeclaringClass(), method);
			if(generics==null){
				if(method.getGenericParams()!=0)
					throw new XRuntimeException("Can't create a generic method %s without generic params, need %s generic params", method, method.getGenericParams());
			}else if(generics.length!=method.getGenericParams()){
				throw new XRuntimeException("Can't create a generic method %s with %s generic params, need %s generic params", method, generics.length, method.getGenericParams());
			}
			if(!XModifier.isStatic(method.getModifier())){
				throw new XRuntimeException("Method %s isn't static", method);
			}
		}
	}
	
	@Override
	protected void save(XOutputStream outputStream) throws IOException {
		outputStream.writeUTF(className);
		outputStream.writeUTF(methodName);
		outputStream.writeByte(methodParams.length);
		for(int i=0; i<methodParams.length; i++){
			outputStream.writeUTF(methodParams[i]);
		}
		outputStream.writeUTF(methodReturn);
		outputStream.writeByte(generics.length);
		for(int i=0; i<generics.length; i++){
			generics[i].save(outputStream);
		}
	}

	@Override
	public String getSource() {
		String s = "(";
		if(methodParams.length>0){
			s += methodParams[0];
			for(int i=0; i<methodParams.length; i++){
				s += ", "+methodParams[i];
			}
			s += ")";
		}
		s += methodReturn;
		return "invs "+className+"."+methodName+s;
	}

}
