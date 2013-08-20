package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WeaselClass implements WeaselSaveable {

	public static final int normalModifier = WeaselModifier.PUBLIC|WeaselModifier.FINAL|WeaselModifier.ABSTRACT;
	public static final int childModifier = WeaselModifier.PUBLIC|WeaselModifier.PRIVATE|WeaselModifier.PROTECTED|
			WeaselModifier.FINAL|WeaselModifier.ABSTRACT|WeaselModifier.STATIC;
	public static final int lokalModifier = WeaselModifier.FINAL|WeaselModifier.ABSTRACT;
	
	protected final WeaselInterpreter interpreter;
	protected int modifier;
	protected final String name;
	protected final String fileName;
	protected final Object parent;
	protected WeaselClass superClass;
	protected WeaselClass[] interfaces;
	protected HashMap<WeaselClass, WeaselInterfaceMaps> interfaceMaps;
	protected WeaselField[] fields;
	protected WeaselMethod[] methods;
	protected WeaselClass[] childClasses;
	
	protected int[] staticEasyTypes;
	protected int[] staticObjectRefs;
	protected WeaselMethodBody[] staticMethodBodys;
	protected WeaselMethodBody[] methodBodys;
	
	protected WeaselID ids = new WeaselID();
	
	protected final WeaselClass arrayClass;
	
	protected boolean isInterface;
	
	protected int classObject;
	
	protected WeaselClass(WeaselInterpreter interpreter, Object parent, String name, int modifier, String fileName){
		WeaselChecks.checkName(name);
		if(parent!=null && !(parent instanceof WeaselClass) && !(parent instanceof WeaselMethodBody)){
			throw new WeaselNativeException("Parent has to be null, a WeaselClass or a WeaselMethodBody but not a %s", parent.getClass());
		}
		WeaselChecks.checkModifier(parent instanceof WeaselClass?modifier&~WeaselModifier.STATIC:modifier, 
				parent instanceof WeaselClass?childModifier:parent==null?normalModifier:lokalModifier);
		this.interpreter = interpreter;
		this.name = name;
		this.modifier = modifier;
		this.parent = parent;
		this.fileName = fileName;
		this.arrayClass = null;
	}
	
	protected WeaselClass(WeaselInterpreter interpreter, Object parent, String name, String fileName){
		if(name.startsWith("[")){
			WeaselChecks.checkName(name.substring(1));
			this.arrayClass = (WeaselClass)parent;
			parent = null;
		}else{
			WeaselChecks.checkName(name);
			this.arrayClass = null;
		}
		if(parent!=null && !(parent instanceof WeaselClass) && !(parent instanceof WeaselMethodBody)){
			throw new WeaselNativeException("Parent has to be null, a WeaselClass or a WeaselMethodBody but not a %s", parent.getClass());
		}
		this.interpreter = interpreter;
		this.name = name;
		this.parent = parent;
		this.fileName = fileName;
	}
	
	public String getName(){
		return isArray()?getArrayClass().getName()+"[]":isPrimitive()?name:isInterface()?"interface "+name:isEnum()?"enum "+name: "class "+name;
	}
	
	public int getModifier(){
		return modifier;
	}
	
	public WeaselClass getParentClass() {
		return parent instanceof WeaselClass?(WeaselClass)parent:null;
	}
	
	public WeaselMethodBody getParentMethod() {
		return parent instanceof WeaselMethodBody?(WeaselMethodBody)parent:null;
	}
	
	public Object getParent() {
		return parent;
	}
	
	public WeaselClass getSuperClass() {
		return superClass;
	}
	
	public WeaselClass[] getInterfaces() {
		return interfaces;
	}
	
	public final boolean isPrimitive() {
		return this instanceof WeaselPrimitive;
	}

	public boolean isArray(){
		return arrayClass!=null;
	}
	
	public WeaselInterpreter getInterpreter() {
		return interpreter;
	}

	public WeaselField getField(String name) {
		for(int i=0; i<fields.length; i++){
			if(fields[i].getName().equals(name)){
				return fields[i];
			}
		}
		if(superClass==null)
			throw new WeaselNativeException("Can't find Field %s", name);
		return superClass.getField(name);
	}

	public WeaselMethod getMethod(String name, WeaselClass[] paramClasses){
		for(int i=0; i<methods.length; i++){
			if(methods[i].getName().equals(name)){
				WeaselClass params[] = methods[i].getParamClasses();
				if(params.length == paramClasses.length){
					boolean eq=true;
					for(int j=0; j<params.length; j++){
						if(params[j] != paramClasses[j]){
							eq = false;
							break;
						}
					}
					if(eq)
						return methods[i];
				}
			}
		}
		if(superClass==null)
			throw new WeaselNativeException("Can't find Method %s", name);
		return superClass.getMethod(name, paramClasses);
	}
	
	public WeaselMethod getMethod(String name, String desk) {
		int openBraket = desk.indexOf('(');
		int closeBraket = desk.indexOf(')');
		String paramDesk = desk.substring(openBraket+1, closeBraket);
		List<String> paramDesks = new ArrayList<String>();
		for(int i=0; i<paramDesk.length(); i++){
			String className = ""+paramDesk.charAt(i);
			if(className.equals("L")){
				char c = 0;
				while(c!=';'){
					i++;
					c = paramDesk.charAt(i);
					className += c;
				}
			}
			paramDesks.add(className);
		}
		WeaselClass paramClasses[] = new WeaselClass[paramDesks.size()];
		int i=0;
		for(String param:paramDesks){
			paramClasses[i++] = interpreter.getWeaselClass(param);
		}
		WeaselMethod method = getMethod(name, paramClasses);
		
		return method;
	}
	
	public WeaselClass getChildClass(String name) {
		String[] names = name.split("\\.", 2);
		WeaselClass weaselClass = null;
		for(int i=0; i<childClasses.length; i++){
			if(childClasses[i].getName().equals(names[0])){
				weaselClass = childClasses[i];
				break;
			}
		}
		if(weaselClass==null)
			throw new WeaselNativeException("Class not found %s", getName()+"."+name);
		if(names.length==1)
			return weaselClass;
		return weaselClass.getChildClass(names[1]);
	}
	
	public boolean isInterface(){
		return isInterface;
	}
	
	public boolean isEnum(){
		return superClass==interpreter.baseTypes.getEnumClass();
	}
	
	public boolean canCastTo(WeaselClass weaselClass){
		if(weaselClass==this)
			return true;
		if(isInterface() && !weaselClass.isInterface())
			return false;
		if(superClass != null && superClass.canCastTo(weaselClass))
			return true;
		if(weaselClass.isInterface()){
			for(int i=0; i<interfaces.length; i++){
				if(interfaces[i].canCastTo(weaselClass))
					return true;
			}
		}
		return false;
	}

	public int[] getInterfaceEasyTypeMap(WeaselClass weaselInterface) {
		if(!weaselInterface.isInterface())
			throw new WeaselNativeException("Can only get a fieldMap for interfaces, not for %s", weaselInterface);
		if(isInterface())
			throw new WeaselNativeException("Can only get a fieldMap from classes, not from %s", this);
		WeaselInterfaceMaps maps = interfaceMaps.get(weaselInterface);
		if(maps!=null){
			return maps.easyTypeMapper;
		}
		if(superClass==null)
			throw new WeaselNativeException("Interface not implemented %s", weaselInterface);
		return superClass.getInterfaceEasyTypeMap(weaselInterface);
	}
	
	public int[] getInterfaceObjectRefMap(WeaselClass weaselInterface) {
		if(!weaselInterface.isInterface())
			throw new WeaselNativeException("Can only get a fieldMap for interfaces, not for %s", weaselInterface);
		if(isInterface())
			throw new WeaselNativeException("Can only get a fieldMap from classes, not from %s", this);
		WeaselInterfaceMaps maps = interfaceMaps.get(weaselInterface);
		if(maps!=null){
			return maps.objectRefMapper;
		}
		if(superClass==null)
			throw new WeaselNativeException("Interface not implemented %s", weaselInterface);
		return superClass.getInterfaceObjectRefMap(weaselInterface);
	}
	
	public int[] getInterfaceMethodMap(WeaselClass weaselInterface) {
		if(!weaselInterface.isInterface())
			throw new WeaselNativeException("Can only get a methodMap for interfaces, not for %s", weaselInterface);
		if(isInterface())
			throw new WeaselNativeException("Can only get a methodMap from classes, not from %s", this);
		WeaselInterfaceMaps maps = interfaceMaps.get(weaselInterface);
		if(maps!=null){
			return maps.methodMapper;
		}
		if(superClass==null)
			throw new WeaselNativeException("Interface not implemented %s", weaselInterface);
		return superClass.getInterfaceMethodMap(weaselInterface);
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getByteName() {
		return isArray()?"["+getArrayClass().getRealName():"O"+name+";";
	}
	
	public String getRealName() {
		return isArray()?getArrayClass().getRealName()+"[]":name;
	}

	protected void loadClassFormInputStream(DataInputStream dataInputStream) throws IOException {
		modifier = dataInputStream.readInt();
		WeaselChecks.checkModifier(parent instanceof WeaselClass?modifier&~WeaselModifier.STATIC:modifier, 
				parent instanceof WeaselClass?childModifier:parent==null?normalModifier:lokalModifier);
		superClass = interpreter.getWeaselClass(dataInputStream.readUTF());
		WeaselChecks.checkSuperClass(superClass);
		if(superClass!=null){
			if(isInterface()){
				throw new WeaselNativeException("Interfaces can't have a superClass");
			}
			ids.method = superClass.ids.method;
			ids.easyType = superClass.ids.easyType;
			ids.objectRef = superClass.ids.objectRef;
		}
		int interfaceCount = dataInputStream.readInt();
		interfaces = new WeaselClass[interfaceCount];
		for(int i=0; i<interfaceCount; i++){
			interfaces[i] = interpreter.getWeaselClass(dataInputStream.readUTF());
			WeaselChecks.checkInterface(interfaces[i]);
		}
		int fieldCount = dataInputStream.readInt();
		fields = new WeaselField[fieldCount];
		for(int i=0; i<fieldCount; i++){
			fields[i] = new WeaselField(this, dataInputStream, ids);
		}
		int methodCount = dataInputStream.readInt();
		methods = new WeaselMethod[methodCount];
		for(int i=0; i<methodCount; i++){
			methods[i] = new WeaselMethod(this, dataInputStream, ids);
		}
		int childClassCount = dataInputStream.readInt();
		childClasses = new WeaselClass[methodCount];
		for(int i=0; i<childClassCount; i++){
			childClasses[i] = new WeaselClass(interpreter, this, dataInputStream.readUTF(), fileName);
			childClasses[i].loadClassFormInputStream(dataInputStream);
		}
		staticEasyTypes = new int[ids.staticEasyType];
		staticObjectRefs = new int[ids.staticObjectRef];
		staticMethodBodys = new WeaselMethodBody[ids.staticMethod];
		if(!isInterface())
			methodBodys  = new WeaselMethodBody[ids.method];
		for(int i=0; i<methods.length; i++){
			if(WeaselModifier.isStatic(methods[i].getModifier())){
				staticMethodBodys[methods[i].id] = new WeaselMethodBody(methods[i], this, dataInputStream); 
			}else if(!isInterface()){
				methodBodys[methods[i].id] = new WeaselMethodBody(methods[i], this, dataInputStream); 
			}
		}
		if(!isInterface()){
			interfaceMaps = new HashMap<WeaselClass, WeaselInterfaceMaps>();
			for(int i=0; i<interfaceCount; i++){
				interfaces[i].makeInterfaceMaps(this, interfaceMaps);
			}
		}
	}

	private void makeInterfaceMaps(WeaselClass weaselClass, HashMap<WeaselClass, WeaselInterfaceMaps> maps){
		if(isInterface()){
			if(maps.containsKey(this))
				return;
			WeaselInterfaceMaps map = new WeaselInterfaceMaps();
			map.methodMapper = new int[ids.method];
			for(int i=0; i<methods.length; i++){
				if(!WeaselModifier.isStatic(methods[i].getModifier())){
					map.methodMapper[methods[i].id] = weaselClass.getMethod(methods[i].getName(), methods[i].getParamClasses()).id;
				}
			}
			map.easyTypeMapper = new int[ids.easyType];
			map.objectRefMapper = new int[ids.objectRef];
			for(int i=0; i<fields.length; i++){
				if(!WeaselModifier.isStatic(fields[i].getModifier())){
					if(fields[i].getType().isPrimitive()){
						map.easyTypeMapper[fields[i].id] = weaselClass.getField(fields[i].getName()).id;
					}else{
						map.objectRefMapper[fields[i].id] = weaselClass.getField(fields[i].getName()).id;
					}
				}
			}
			maps.put(this, map);
			for(int i=0; i<interfaces.length; i++){
				interfaces[i].makeInterfaceMaps(weaselClass, maps);
			}
		}
	}
	
	public int getObjectRefCount() {
		return ids.objectRef;
	}

	public int getEasyTypeCount() {
		return ids.easyType;
	}

	protected void loadFromDataStream(DataInputStream dataInputStream) throws IOException {
		for(int i=0; i<staticEasyTypes.length; i++){
			staticEasyTypes[i] = dataInputStream.readInt();
		}
		for(int i=0; i<staticObjectRefs.length; i++){
			staticObjectRefs[i] = dataInputStream.readInt();
		}
	}

	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException {
		for(int i=0; i<staticEasyTypes.length; i++){
			dataOutputStream.writeInt(staticEasyTypes[i]);
		}
		for(int i=0; i<staticObjectRefs.length; i++){
			dataOutputStream.writeInt(staticObjectRefs[i]);
		}
	}
	
	public WeaselClass getArrayClass() {
		return arrayClass;
	}

	@Override
	public String toString() {
		return getName();
	}

	public WeaselID getIDS() {
		return ids;
	}
	
	public void setClassObject(int createClassObject) {
		classObject = createClassObject;
	}
	
	public int getClassObject() {
		return classObject;
	}
	
	private static class WeaselInterfaceMaps{
		
		public int[] easyTypeMapper;
		public int[] objectRefMapper;
		public int[] methodMapper;
		
	}
	
	protected static class WeaselID{
		
		public int objectRef;
		public int staticObjectRef;
		
		public int easyType;
		public int staticEasyType;
		
		public int method;
		public int staticMethod;
		
	}
	
}
