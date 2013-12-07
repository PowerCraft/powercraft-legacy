package xscript.runtime.clazz;

import java.util.HashMap;

public class XPackage {

	protected final String name;
	protected XPackage parent;
	protected HashMap<String, XPackage> childs = new HashMap<String, XPackage>();
	
	public XPackage(String name){
		this.name = name;
	}
	
	public XPackage getChild(String name){
		String names[] = name.split("\\.", 2);
		XPackage child = childs.get(names[0]);
		if(child==null)
			return null;
		if(names.length>1){
			return child.getChild(names[1]);
		}
		return child;
	}
	
	public void addChild(XPackage child){
		childs.put(child.getSimpleName(), child);
		child.parent = this;
	}

	public String getName() {
		if(parent==null){
			return getSimpleName();
		}else{
			String s = parent.getName();
			if(s==null){
				s = getSimpleName();
			}else{
				s += "."+getSimpleName();
			}
			return s;
		}
	}
	
	@Override
	public String toString(){
		return getName();
	}
	
	public String getSimpleName(){
		return name;
	}

	public void markVisible() {
		for(XPackage child:childs.values()){
			child.markVisible();
		}
	}
	
}
