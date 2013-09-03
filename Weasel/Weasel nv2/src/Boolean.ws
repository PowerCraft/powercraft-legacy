public enum Boolean{

	TRUE, FALSE;

	private static final String TRUENAME="true";
	private static final String FALSENAME="false";

	private static int counter = 0;
	private static final String names[] ={"TRUE", "FALSE"};

	private Boolean(){
		super(names[counter], counter++);
	}

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