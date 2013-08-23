public enum Boolean{

	TRUE, FALSE;

	private static final String TRUENAME="true";
	private static final String FALSENAME="false";

	public String toString(){
		return this==TRUE?TRUENAME:FALSENAME;
	}

	public static Boolean valueOf(boolean b){
		return b?TRUE:FALSE;
	}

	public static Boolean valueOf(String s){
		return TRUENAME.equalsIgnoreCase(s)?TRUE:FALSE;
	}

}