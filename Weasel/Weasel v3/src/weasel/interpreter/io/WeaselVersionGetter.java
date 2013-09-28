package weasel.interpreter.io;

public interface WeaselVersionGetter {

	public WeaselVersionClassFileLoader getVersionClassFileLoader(int version);
	
	public WeaselVersionClassFileSaver getVersionClassFileSaver(int version);
	
}
