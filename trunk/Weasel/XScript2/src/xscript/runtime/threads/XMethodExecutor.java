package xscript.runtime.threads;

import xscript.runtime.XModifier;
import xscript.runtime.XRuntimeException;
import xscript.runtime.clazz.XPrimitive;
import xscript.runtime.genericclass.XGenericClass;
import xscript.runtime.instruction.XInstruction;
import xscript.runtime.method.XMethod;
import xscript.runtime.object.XObject;

public class XMethodExecutor implements XGenericMethodProvider {

	private XMethodExecutor parent;
	private XGenericClass declaringClass;
	private XMethod method;
	private XGenericClass[] generics;
	private int stackPointer;
	private long[] stack;
	private byte[] stackType;
	private long[] local;
	private long ret;
	private int programPointer;
	
	public XMethodExecutor(XMethodExecutor parent, XMethod method, XGenericClass[] generics, long[] params) {
		this.parent = parent;
		this.method = method;
		if(XModifier.isNative(method.getModifier()))
			throw new XRuntimeException("Can't run native method %s", method);
		this.generics = generics;
		if(generics==null){
			if(method.getGenericParams()!=0)
				throw new XRuntimeException("Can't create a generic method %s without generic params, need %s generic params", method, method.getGenericParams());
		}else if(generics.length!=method.getGenericParams()){
			throw new XRuntimeException("Can't create a generic method %s with %s generic params, need %s generic params", method, generics.length, method.getGenericParams());
		}
		int pl = params.length;
		if(!XModifier.isStatic(method.getModifier())){
			XObject _this = method.getDeclaringClass().getVirtualMachine().getObjectProvider().getObject(params[0]);
			declaringClass = _this.getXClass();
			pl--;
		}
		if(pl!=method.getParamCount()){
			throw new XRuntimeException("Wrong number of arguments got %s but need %s", method.getParamCount(), pl);
		}
		stack = new long[method.getMaxStackSize()];
		stackType = new byte[method.getMaxStackSize()];
		local = new long[method.getMaxLocalSize()];
	}

	@Override
	public XGenericClass getGeneric(int genericID) {
		return generics[genericID];
	}

	public XGenericClass getDeclaringClass(){
		return declaringClass;
	}
	
	public long[] getReturn(){
		long[] l = new long[2];
		l[0] = ret;
		l[1] = method.getReturnTypePrimitive();
		return l;
	}
	
	public long[] pop(){
		if(stackPointer==0)
			throw new XRuntimeException("Stack underflow");
		long[] l = new long[2];
		l[0] = stack[--stackPointer];
		l[1] = stackType[stackPointer];
		return l;
	}
	
	public long pop(int primitiveID) {
		long[] l = pop();
		if(l[1]!=primitiveID)
			throw new XRuntimeException("Can't cast %s to %s", XPrimitive.getName((int) l[1]), XPrimitive.getName(primitiveID));
		return l[0];
	}
	
	public long oPop(){
		 return pop(XPrimitive.OBJECT);
	}
	
	
	public long lPop(){
		 return pop(XPrimitive.LONG);
	}
	
	public int iPop() {
		 return (int) pop(XPrimitive.INT);
	}
	
	public short sPop() {
		 return (short) pop(XPrimitive.SHORT);
	}
	
	public byte bPop() {
		 return (byte) pop(XPrimitive.BYTE);
	}
	
	public boolean zPop() {
		 return pop(XPrimitive.BOOL)!=0;
	}
	
	public char cPop() {
		 return (char) pop(XPrimitive.CHAR);
	}
	
	public float fPop() {
		return Float.intBitsToFloat((int)pop(XPrimitive.FLOAT));
	}
	
	public double dPop() {
		return Double.longBitsToDouble(pop(XPrimitive.DOUBLE));
	}

	public void push(long value, int type) {
		if(stackPointer==stack.length)
			throw new XRuntimeException("Stack overflow");
		stackType[stackPointer] = (byte) type;
		stack[stackPointer++] = value;
	}
	
	public void oPush(long value) {
		push(value, XPrimitive.OBJECT);
	}
	
	public void lPush(long value) {
		push(value, XPrimitive.LONG);
	}
	
	public void iPush(int value) {
		push(value, XPrimitive.INT);
	}
	
	public void sPush(short value) {
		push(value, XPrimitive.SHORT);
	}
	
	public void bPush(byte value) {
		push(value, XPrimitive.BYTE);
	}
	
	public void zPush(boolean value) {
		push(value?-1:0, XPrimitive.BOOL);
	}
	
	public void cPush(char value) {
		push(value, XPrimitive.CHAR);
	}
	
	public void fPush(float value) {
		push(Float.floatToIntBits(value), XPrimitive.FLOAT);
	}
	
	public void dPush(double value) {
		push(Double.doubleToLongBits(value), XPrimitive.DOUBLE);
	}

	public long getLocal(int local) {
		if(local<0 || local>=this.local.length)
			throw new XRuntimeException("Local out of bounds %s", local);
		return this.local[local];
	}

	public void setLocal(int local, long value) {
		if(local<0 || local>=this.local.length)
			throw new XRuntimeException("Local out of bounds %s", local);
		this.local[local] = value;
	}

	public void setReturn(long value){
		ret = value;
	}
	
	public void setProgramPointer(int programPointer){
		this.programPointer = programPointer;
	}
	
	public XInstruction getNextInstruction(){
		return method.getInstruction(programPointer++);
	}
	
	public void markVisible(){
		if(parent!=null){
			parent.markVisible();
		}
		if(method.getReturnTypePrimitive()==XPrimitive.OBJECT){
			XObject obj = method.getDeclaringClass().getVirtualMachine().getObjectProvider().getObject(ret);
			if(obj!=null)
				obj.markVisible();
		}
		for(int i=0; i<stackPointer; i++){
			if(stackType[i] == XPrimitive.OBJECT){
				XObject obj = method.getDeclaringClass().getVirtualMachine().getObjectProvider().getObject(stack[i]);
				if(obj!=null)
					obj.markVisible();
			}
		}
	}

	public XMethodExecutor getParent() {
		return parent;
	}

	@Override
	public XMethod getMethod() {
		return method;
	}

	public boolean jumpToExceptionHandlePoint(XGenericClass xClass, long exception) {
		programPointer = method.getExceptionHandlePoint(programPointer, xClass, declaringClass, this);
		if(programPointer==-1){
			return false;
		}
		stack[0] = exception;
		stackType[0] = XPrimitive.OBJECT;
		stackPointer = 1;
		return true;
	}

	public XGenericClass getLocalType(int local) {
		return null;
	}
	
	public void ret(long value) {
		ret = value;
		programPointer = Integer.MAX_VALUE;
	}
	
}
