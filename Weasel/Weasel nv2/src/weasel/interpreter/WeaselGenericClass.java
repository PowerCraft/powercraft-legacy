package weasel.interpreter;

import java.util.ArrayList;
import java.util.List;


public class WeaselGenericClass {
	
	private final WeaselClass baseClass;
	
	private final WeaselGenericClass[] generics;
	
	private WeaselGenericClass genericSuperClass;
	
	private WeaselGenericClass[] genericInterfaces;
	
	private WeaselGenericField[] genericFields;
	
	private WeaselGenericMethod[] genericMethods;
	
	private WeaselGenericClass(int dummy, WeaselClass baseClass) {
		this.baseClass = baseClass;
		if(baseClass.genericInformation==null){
			generics = new WeaselGenericClass[0];
		}else{
			generics = new WeaselGenericClass[baseClass.genericInformation.length];
		}
	}
	
	public WeaselGenericClass(WeaselClass baseClass) {
		this.baseClass = baseClass;
		if(baseClass.genericInformation==null){
			generics = new WeaselGenericClass[0];
		}else{
			generics = new WeaselGenericClass[baseClass.genericInformation.length];
		}
		for(int i=0; i<generics.length; i++){
			generics[i] = new WeaselGenericClass(0, baseClass.genericInformation[i].genericInfo.getWeaselClass(this));
		}
		for(int i=0; i<generics.length; i++){
			for(int j=0; j<generics[i].generics.length; j++){
				generics[i].generics[j] = baseClass.genericInformation[i].genericInfo.generics[j].getGenericClass(this);
			}
		}
		for(int i=0; i<generics.length; i++){
			generics[i].resolve();
		}
		resolve();
	}
	
	public WeaselGenericClass(WeaselClass baseClass, WeaselGenericClass[] generics){
		this.baseClass = baseClass;
		this.generics = generics;
		resolve();
	}

	public void resolve(){
		if(baseClass.genericSuperClass!=null){
			genericSuperClass = baseClass.genericSuperClass.getGenericClass(this);
		}
		if(baseClass.genericInterfaces!=null){
			genericInterfaces = new WeaselGenericClass[baseClass.genericInterfaces.length];
			for(int i=0; i<genericInterfaces.length; i++){
				genericInterfaces[i] = baseClass.genericInterfaces[i].getGenericClass(this);
			}
		}
		if(baseClass.fields!=null){
			genericFields = new WeaselGenericField[baseClass.fields.length];
			for(int i=0; i<genericFields.length; i++){
				genericFields[i] = new WeaselGenericField(this, baseClass.fields[i]);
			}
		}
		if(baseClass.methods!=null){
			genericMethods = new WeaselGenericMethod[baseClass.methods.length];
			for(int i=0; i<genericMethods.length; i++){
				genericMethods[i] = new WeaselGenericMethod(this, baseClass.methods[i]);
			}
		}
	}
	
	public WeaselClass getBaseClass(){
		return baseClass;
	}

	public int getGenericSize() {
		return generics.length;
	}
	
	public WeaselGenericClass getGeneric(int index) {
		return generics[index];
	}
	
	public WeaselGenericClass[] getGenerics() {
		return generics;
	}
	
	public WeaselGenericClass getGenericSuperClass(){
		return genericSuperClass;
	}
	
	public WeaselGenericClass[] getGenericInterfaces(){
		return genericInterfaces;
	}
	
	public WeaselGenericField[] getGenericFields(){
		return genericFields;
	}
	
	public WeaselGenericField getGenericField(String name){
		for(int i=0; i<genericFields.length; i++){
			if(genericFields[i].getField().getName().equals(name)){
				return genericFields[i];
			}
		}
		if(genericSuperClass==null)
			return null;
		return genericSuperClass.getGenericField(name);
	}
	
	public WeaselGenericMethod[] getGenericMethod(){
		return genericMethods;
	}

	public WeaselGenericMethod2 getGenericMethod(String name, WeaselGenericClass[] genericClasses) {
		for(int i=0; i<genericMethods.length; i++){
			if(genericMethods[i].getMethod().getNameAndDesk().equals(name)){
				return genericMethods[i].getMethod(genericClasses);
			}
		}
		if(genericSuperClass==null)
			return null;
		return genericSuperClass.getGenericMethod(name, genericClasses);
	}
	
	public static WeaselGenericClass getSmallestSame(WeaselGenericClass wc, WeaselGenericClass wc2) {
		if(wc.generics.length==0 && wc2.generics.length==0 && wc.baseClass==wc2.baseClass){
			return wc;
		}
		while(wc2.canCastTo(wc)){
			wc = wc.getGenericSuperClass();
		}
		return wc;
	}

	public boolean canCastTo(WeaselGenericClass wc) {
		WeaselGenericClass wc2 = this;
		while(!wc2.equals(wc)){
			wc2 = wc2.getGenericSuperClass();
			if(wc2 == null)
				return false;
		}
		return true;
	}
	
	@Override
	public boolean equals(Object object){
		if(object instanceof WeaselGenericClass){
			WeaselGenericClass gc = (WeaselGenericClass)object;
			if(baseClass!=gc.baseClass)
				return false;
			for(int i=0; i<generics.length; i++){
				if(!generics[i].equals(gc.generics[i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString(){
		return getName();
	}
	
	public String getName() {
		String s = baseClass.getName();
		int b = s.indexOf('[');
		String a = "";
		if(b!=-1){
			a = s.substring(b);
			s = s.substring(0, b);
		}
		if(generics.length>0){
			s += "<";
			s += generics[0].getRealName();
			for(int i=1; i<generics.length; i++){
				s += ", "+generics[i].getRealName();
			}
			s += ">";
		}
		return s+a;
	}
	
	public String getRealName() {
		String s = baseClass.getRealName();
		int b = s.indexOf('[');
		String a = "";
		if(b!=-1){
			a = s.substring(b);
			s = s.substring(0, b);
		}
		if(generics.length>0){
			s += "<";
			s += generics[0].getRealName();
			for(int i=1; i<generics.length; i++){
				s += ", "+generics[i].getRealName();
			}
			s += ">";
		}
		return s+a;
	}

	public WeaselGenericClass getGenericClass(String className) {
		for(int i=0; i<baseClass.genericInformation.length; i++){
			if(baseClass.genericInformation[i].genericName.equals(className)){
				return generics[i];
			}
		}
		return null;
	}

	public List<WeaselGenericMethod2> getGenericMethods(String name, boolean notOnlyStatic) {
		List<WeaselGenericMethod2> list = new ArrayList<WeaselGenericMethod2>();
		addGenericMethods(name, notOnlyStatic, list);
		return list;
	}
	
	private void addGenericMethods(String name, boolean notOnlyStatic, List<WeaselGenericMethod2> list) {
		for(int i=0; i<genericMethods.length; i++){
			if(genericMethods[i].getMethod().getName().equals(name)){
				if(notOnlyStatic || WeaselModifier.isStatic(genericMethods[i].getMethod().getModifier())){
					WeaselGenericClass[] generics = new WeaselGenericClass[genericMethods[i].getMethod().genericInfo.length];
					for(int j=0; j<generics.length; j++){
						generics[j] = new WeaselGenericClass(genericMethods[i].getMethod().genericInfo[j].genericInfo.genericClass);
					}
					list.add(genericMethods[i].getMethod(generics));
				}
			}
		}
		if(genericSuperClass!=null){
			genericSuperClass.addGenericMethods(name, notOnlyStatic, list);
		}
	}
	
}
