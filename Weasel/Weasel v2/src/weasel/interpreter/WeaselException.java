package weasel.interpreter;

public class WeaselException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9100177212927408302L;

	public WeaselException(){
		
	}

	public WeaselException(String error, Object...o){
		super(String.format(error, o));
	}
	
	public WeaselException(String error, Throwable e){
		super(error, e);
	}
	
	public WeaselException(Throwable e){
		super(e);
	}
	
}
