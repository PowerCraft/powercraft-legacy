package weasel.interpreter;

import java.util.HashMap;

public class WeaselPackage {

	protected final String name;
	protected WeaselPackage parent;
	protected final HashMap<String, WeaselPackage> childPackages = new HashMap<String, WeaselPackage>();
	
	protected WeaselPackage(String name){
		this.name = name;
	}
	
	public String getName(){
		return parent==null || parent.getParent()==null?name:parent.getName()+"."+name;
	}
	
	public String getSimpleName(){
		return name;
	}
	
	public WeaselPackage getParent() {
		return parent;
	}

	public WeaselPackage getPackage(String name, boolean create){
		String names[] = name.split("\\.");
		WeaselPackage p = childPackages.get(names[0]);
		if(p==null){
			if(create){
				p = new WeaselPackage(names[0]);
				addPackage(p);
			}else{
				throw new WeaselRuntimeException("Package not found %s", getName()+"."+names[0]);
			}
		}
		if(names.length==1)
			return p;
		return p.getPackage(names[1], create);
	}

	public void addPackage(WeaselPackage p) {
		WeaselPackage op = childPackages.get(p.name);
		if(op!=null)
			throw new WeaselRuntimeException("Package already exist %s", op.getName());
		childPackages.put(p.name, p);
		p.parent = this;
	}
	
}
