public class Thread implements Runnable{
	
	public final int DEFAULTSTACKSIZE = 100;
	
	private final String name;
	private final int stackSize;
	
	public Thread(String name, int stackSize){
		this.name = name;
		this.stackSize = stackSize;
	}
	
	public Thread(String name){
		this(name, DEFAULTSTACKSIZE);
	}
	
	public Thread(int stackSize){
		this(getDefaultName(), stackSize);
	}
	
	public Thread(){
		this(getDefaultName(), DEFAULTSTACKSIZE);
	}
	
	public void start(){
		run();
	}
	
	public void run(){
		
	}
	
	private native static String getDefaultName();
	
	public native static void sleep(long ms);
	
}