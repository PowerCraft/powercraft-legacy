package weasel.interpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WeaselObject implements WeaselSaveable, Map<String, Object>{

	protected final WeaselClass weaselClass;
	protected final int parent;
	protected final int[] easyTypes;
	protected final int[] objectRefs;
	protected boolean isVisible;
	protected WeaselGenericClass[] generics;
	
	protected WeaselObject(WeaselClass weaselClass, int parent, int arrayLength) {
		WeaselChecks.checkCreationClass(weaselClass);
		this.weaselClass = weaselClass;
		this.parent = parent;
		if(weaselClass.isArray()){
			if(weaselClass.getArrayClass().isPrimitive()){
				if(WeaselPrimitive.getPrimitiveID(weaselClass.getArrayClass())==WeaselPrimitive.LONG||WeaselPrimitive.getPrimitiveID(weaselClass.getArrayClass())==WeaselPrimitive.DOUBLE){
					easyTypes = new int[arrayLength*2+1];
				}else{
					easyTypes = new int[arrayLength+1];
				}
				objectRefs = new int[0];
			}else{
				easyTypes = new int[1];
				objectRefs = new int[arrayLength];
			}
			easyTypes[0] = arrayLength;
		}else{
			easyTypes = new int[weaselClass.getEasyTypeCount()];
			objectRefs = new int[weaselClass.getObjectRefCount()];
		}
	}

	protected WeaselObject(WeaselInterpreter weaselInterpreter, DataInputStream dataInputStream) throws IOException {
		weaselClass = weaselInterpreter.getWeaselClass(dataInputStream.readUTF());
		WeaselChecks.checkCreationClass(weaselClass);
		parent = dataInputStream.readInt();
		easyTypes = new int[weaselClass.getEasyTypeCount()];
		for(int i=0; i<easyTypes.length; i++){
			easyTypes[i] = dataInputStream.readInt();
		}
		objectRefs = new int[weaselClass.getObjectRefCount()];
		for(int i=0; i<objectRefs.length; i++){
			objectRefs[i] = dataInputStream.readInt();
		}
	}

	@Override
	public void saveToDataStream(DataOutputStream dataOutputStream) throws IOException{
		dataOutputStream.writeUTF(weaselClass.getByteName());
		dataOutputStream.writeInt(parent);
		for(int i=0; i<easyTypes.length; i++){
			dataOutputStream.writeInt(easyTypes[i]);
		}
		for(int i=0; i<objectRefs.length; i++){
			dataOutputStream.writeInt(objectRefs[i]);
		}
	}
	
	protected void setInvisible(){
		isVisible = false;
	}
	
	public void markVisible(){
		if(!isVisible){
			isVisible = true;
			WeaselInterpreter interpreter = getInterpreter();
			interpreter.getObject(parent).markVisible();
			for(int i=0; i<objectRefs.length; i++){
				interpreter.getObject(objectRefs[i]).markVisible();
			}
		}
	}
	
	public boolean isVisible(){
		return isVisible;
	}

	public WeaselInterpreter getInterpreter() {
		return weaselClass.getInterpreter();
	}

	public WeaselClass getWeaselClass() {
		return weaselClass;
	}

	
	
	@Override
	public void clear() {
		throw new WeaselNativeException("Can't clear WeaselObject");
	}

	@Override
	public boolean containsKey(Object key) {
		if(!(key instanceof String))
			return false;
		return getWeaselClass().getField((String) key)!=null;
	}

	@Override
	public boolean containsValue(Object value) {
		return values().contains(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		List<java.util.Map.Entry<String, Object>> list = new ArrayList<java.util.Map.Entry<String, Object>>();
		List<String> fields = getWeaselClass().getAllNonStaticFields();
		for(String field:fields){
			list.add(new WeaselObjectEntry(field));
		}
		return new WeaselReadableSet<java.util.Map.Entry<String, Object>>(list);
	}

	@Override
	public Object get(Object key) {
		if(!(key instanceof String))
			return null;
		WeaselField field = getWeaselClass().getField((String)key);
		if(field==null)
			return null;
		return WeaselConvertHelper.readField(field, this);
	}

	@Override
	public boolean isEmpty() {
		return easyTypes.length==0 && objectRefs.length==0;
	}

	@Override
	public Set<String> keySet() {
		return new WeaselReadableSet<String>(getWeaselClass().getAllNonStaticFields());
	}

	@Override
	public Object put(String key, Object value) {
		WeaselField field = getWeaselClass().getField(key);
		if(field==null)
			return null;
		Object ret = WeaselConvertHelper.readField(field, this);
		WeaselConvertHelper.writeField(field, this, value);
		return ret;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		for(Entry<? extends String, ? extends Object>e:m.entrySet()){
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public Object remove(Object key) {
		throw new WeaselNativeException("Can't remove entry from WeaselObject");
	}

	@Override
	public int size() {
		return getWeaselClass().getAllNonStaticFields().size();
	}

	@Override
	public Collection<Object> values() {
		List<Object> list = new ArrayList<Object>();
		List<String> fields = getWeaselClass().getAllNonStaticFields();
		for(String field:fields){
			list.add(get(field));
		}
		return list;
	}
	
	private class WeaselObjectEntry implements java.util.Map.Entry<String, Object>{

		private final String field;
		
		public WeaselObjectEntry(String field){
			this.field = field;
		}
		
		@Override
		public String getKey() {
			return field;
		}

		@Override
		public Object getValue() {
			return get(field);
		}

		@Override
		public Object setValue(Object value) {
			return put(field, value);
		}
		
	}
	
}
