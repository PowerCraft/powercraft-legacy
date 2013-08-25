package weasel.interpreter;

public class WeaselGenericMethod2 {
	
	private final WeaselGenericMethod method;
	
	private final WeaselGenericClass genericReturn;
	
	private final WeaselGenericClass[] genericParams;
	
	public WeaselGenericMethod2(WeaselGenericMethod method, WeaselGenericClass[] generics) {
		this.method = method;
		genericReturn = method.getMethod().genericReturn.getGenericClass(method.getGenericParentClass(), generics);
		genericParams = new WeaselGenericClass[method.getMethod().genericParams.length];
		for(int i=0; i<genericParams.length; i++){
			genericParams[i] = method.getMethod().genericParams[i].getGenericClass(method.getGenericParentClass(), generics);
		}
	}
	
	public WeaselGenericMethod getMethod(){
		return method;
	}
	
	public WeaselGenericClass getGenericReturn(){
		return genericReturn;
	}
	
	public WeaselGenericClass[] getGenericParams(){
		return genericParams;
	}
	
	@Override
	public String toString() {
		String s = WeaselModifier.toString2(method.getMethod().modifier)+genericReturn.getRealName()+" "+method.getMethod().name+"(";
		if(genericParams.length>0){
			s += genericParams[0].getRealName();
			for(int i=1; i<genericParams.length; i++){
				s += ", "+genericParams[i].getRealName();
			}
		}
		return s+")";
	}
	
}
