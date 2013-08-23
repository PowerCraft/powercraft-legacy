public enum Boolean{

	TRUE, FALSE;

	public String toString(){
		return super.toString().toLowerCase();
	}

	public static Boolean valueOf(boolean b){
		return b?TRUE:FALSE;
	}

	public static Boolean valueOf(String s){
		return "true".equalsIgnoreCase(s)?TRUE:FALSE;
	}

}