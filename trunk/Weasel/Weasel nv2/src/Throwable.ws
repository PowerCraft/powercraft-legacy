public class Throwable{

	private final String message;
	private final Throwable cause;
	private StackTraceElement[] stackTrace;
	
	public Throwable(String message, Throwable cause){
		this.message = message;
		this.cause = cause;
	}

	public Throwable(String message){
		this(message, null);
	}

	public Throwable(Throwable cause){
		this(null, cause);
	}

	public String getMessage(){
		return message;
	}

	public Throwable getCause(){
		return cause;
	}

	public StackTraceElement[] getStackTrace(){
		return stackTrace;
	}

}