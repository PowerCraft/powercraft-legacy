package weasel.compiler;

public class WeaselCompilerMessage {

	private final MessageType type;
	private final int line;
	private final String sourceFile;
	private final String message;
	
	public WeaselCompilerMessage(MessageType type, int line, String sourceFile, String message) {
		this.type = type;
		this.line = line;
		this.sourceFile = sourceFile;
		this.message = message;
	}

	public MessageType getMessageType(){
		return type;
	}
	
	public int getLine(){
		return line;
	}
	
	public String getSourceFile(){
		return sourceFile;
	}
	
	public String getMessage(){
		return message;
	}
	
	@Override
	public String toString() {
		if(line==0){
			return String.format("%s in %s: %s", type, sourceFile, message);
		}
		return String.format("%s at line %s in %s: %s", type, line, sourceFile, message);
	}
	
	public static enum MessageType{
		ERROR,
		WARNING,
		INFO
	}
	
}
