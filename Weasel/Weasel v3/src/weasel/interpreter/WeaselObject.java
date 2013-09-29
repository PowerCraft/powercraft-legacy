package weasel.interpreter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class WeaselObject implements Map<String, Object>{

	private WeaselClass weaselClass;
	private long parent;
	private long[] datas;
	private boolean visible;
	
	public WeaselObject(WeaselClass weaselClass, long parent, long[] datas){
		this.weaselClass = weaselClass;
		this.parent = parent;
		this.datas = datas;
	}
	
	public WeaselObject getWeaselObject(){
		return this;
	}
	
	public WeaselClass getWeaselClass(){
		return weaselClass;
	}
	
	public long getParent(){
		return parent;
	}
	
	public long[] getDatas(){
		return datas;
	}
	
	public long castTo(WeaselClass wc){
		weaselClass.getWeaselClassIndex(wc);
	}
	
	public void resetVisible(){
		visible = false;
	}
	
	public void makeVisible(){
		visible = true;
	}
	
	public boolean isVisible(){
		return visible;
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsKey(Object key) {
		
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		
		return null;
	}

	@Override
	public Object get(Object key) {
		if(key instanceof String){
			WeaselField field = weaselClass.getClassBuildPlan().getField((String)key);
			return field.get(this);
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Set<String> keySet() {
		return null;
	}

	@Override
	public Object put(String key, Object value) {
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		for(Entry<? extends String, ? extends Object> e:m.entrySet()){
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return datas.length;
	}

	@Override
	public Collection<Object> values() {
		return null;
	}
	
}
