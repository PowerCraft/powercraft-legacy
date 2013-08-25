package weasel.interpreter;

public class WeaselGenericField {

	private final WeaselGenericClass genericParentClass;
	
	private final WeaselField field;
	
	private final WeaselGenericClass genericType;
	
	public WeaselGenericField(WeaselGenericClass genericParentClass, WeaselField field){
		this.genericParentClass = genericParentClass;
		this.field = field;
		genericType = field.genericType.getGenericClass(genericParentClass);
	}
	
	public WeaselGenericClass getGenericParentClass(){
		return genericParentClass;
	}
	
	public WeaselField getField(){
		return field;
	}
	
	public WeaselGenericClass getGenericType(){
		return genericType;
	}
	
	@Override
	public String toString() {
		return WeaselModifier.toString2(field.modifier)+genericType.getRealName()+" "+field.name;
	}
	
}
