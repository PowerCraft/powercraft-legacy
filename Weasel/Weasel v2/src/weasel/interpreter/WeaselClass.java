package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WeaselClass implements WeaselModifier, WeaselNameable, WeaselSaveable {

	private WeaselInterpreter interpreter;
	private WeaselClass parentClass;
	private int modifier;
	private String className;
	private WeaselClass superClass;
	private WeaselClass interfaces[];
	private WeaselMethod methods[];
	private WeaselClass childClasses[];
	private int staticDataBuffer[];
	private WeaselMethod staticMethods[];
	private WeaselClass staticChildClasses[];
	private int dataBufferSize;
	private int initialized;
	
	public WeaselClass(WeaselInterpreter interpreter, WeaselClass parentClass){
		this.interpreter = interpreter;
		this.parentClass = parentClass;
	}
	
	public WeaselClass(WeaselInterpreter interpreter, WeaselClass parentClass, String className)  {
		this.interpreter = interpreter;
		this.parentClass = parentClass;
		this.className = className;
	}

	public int getDataBufferSize() {
		return dataBufferSize;
	}
	
	public int[] getStaticDataBuffer(){
		return staticDataBuffer;
	}
	
	public boolean canBeCastTo(WeaselClass weaselClass){
		if(weaselClass==this)
			return true;
		if(superClass!=null){
			if(superClass.canBeCastTo(weaselClass)){
				return true;
			}
		}
		if(interfaces!=null){
			for(int i=0; i<interfaces.length; i++){
				if(interfaces[i].canBeCastTo(weaselClass)){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getModifier() {
		return modifier;
	}

	@Override
	public String getName() {
		if(parentClass==null)
			return className;
		return parentClass.getName()+"."+className;
	}

	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		for(int i=0; i<staticDataBuffer.length; i++){
			dataOutputStream.writeInt(staticDataBuffer[i]);
		}
	}

	public void loadFromDataStream(DataInputStream dataInputStream) throws IOException {
		for(int i=0; i<staticDataBuffer.length; i++){
			staticDataBuffer[i] = dataInputStream.readInt();
		}
	}

	public void loadClassFormInputStream(DataInputStream dataInputStream) throws IOException {
		if(initialized == 0){
			initialized = 1;
			modifier = dataInputStream.readInt();
			dataBufferSize = dataInputStream.readInt();
			int staticDataBufferSize = dataInputStream.readInt();
			staticDataBuffer = new int[staticDataBufferSize];
			superClass = interpreter.getClassByName(dataInputStream.readUTF());
			int interfaceCount = dataInputStream.readInt();
			interfaces = new WeaselClass[interfaceCount];
			for(int i=0; i<interfaceCount; i++){
				interfaces[i] = interpreter.getClassByName(dataInputStream.readUTF());
			}
			int methodCount = dataInputStream.readInt();
			methods = new WeaselMethod[methodCount];
			for(int i=0; i<methodCount; i++){
				methods[i] = new WeaselMethod(interpreter, this, dataInputStream);
			}
			int childClassCount = dataInputStream.readInt();
			childClasses = new WeaselClass[methodCount];
			for(int i=0; i<childClassCount; i++){
				childClasses[i] = new WeaselClass(interpreter, this);
				childClasses[i].loadClassFormInputStream(dataInputStream);
			}
			int staticMethodCount = dataInputStream.readInt();
			staticMethods = new WeaselMethod[methodCount];
			for(int i=0; i<staticMethodCount; i++){
				staticMethods[i] = new WeaselMethod(interpreter, this, dataInputStream);
			}
			int staticChildClassCount = dataInputStream.readInt();
			staticChildClasses = new WeaselClass[methodCount];
			for(int i=0; i<staticChildClassCount; i++){
				staticChildClasses[i] = new WeaselClass(interpreter, this);
				staticChildClasses[i].loadClassFormInputStream(dataInputStream);
			}
			initialized = 2;
		}
	}

	public WeaselClass getChildClass(String className) {
		String classes[] = className.split("\\.", 2);
		WeaselClass wClass = null;
		for(int i=0; i<childClasses.length; i++){
			if(childClasses[i].getName().equals(classes[0])){
				wClass = childClasses[i];
				break;
			}
		}
		if(wClass==null){
			for(int i=0; i<staticChildClasses.length; i++){
				if(staticChildClasses[i].getName().equals(classes[0])){
					wClass = staticChildClasses[i];
					break;
				}
			}
		}
		if(classes.length==1)
			return wClass;
		return wClass.getChildClass(classes[1]);
	}

	public WeaselMethod getMethod(String name, String desk) {
		int openBraket = desk.indexOf('(');
		int closeBraket = desk.indexOf(')');
		String paramDesk = desk.substring(openBraket+1, closeBraket);
		String paramDesks[] = paramDesk.split(",");
		WeaselClass paramClasses[] = new WeaselClass[paramDesks.length];
		for(int i=0; i<paramDesks.length; i++){
			paramClasses[i] = interpreter.getClassByName(paramDesks[i].trim());
		}
		WeaselMethod method = getMethod(name, paramClasses);
		
		return method;
	}
	
	private static boolean isMethodGood(WeaselMethod method, String name, WeaselClass[] paramClasses){
		if(!method.getName().equals(name))
			return false;
		WeaselClass weaselClasses[] = method.getParamWeaselClasses();
		if(paramClasses.length!=weaselClasses.length)
			return false;
		for(int j=0; j<paramClasses.length; j++){
			if(paramClasses[j]!=weaselClasses[j]){
				return false;
			}
		}
		return true;
	}
	
	public WeaselMethod getMethod(String name, WeaselClass...paramClasses) {
		for(int i=0; i<methods.length; i++){
			if(isMethodGood(methods[i], name, paramClasses)){
				return  methods[i];
			}
		}
		for(int i=0; i<staticMethods.length; i++){
			if(isMethodGood(staticMethods[i], name, paramClasses)){
				return  staticMethods[i];
			}
		}
		return null;
		
	}
	
}
