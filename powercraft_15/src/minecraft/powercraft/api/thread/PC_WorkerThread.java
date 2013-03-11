package powercraft.api.thread;

public class PC_WorkerThread extends Thread {
	
	public PC_WorkerThread(){
		setDaemon(true);
	}
	
	@Override
	public void run() {
		while(true){
			PC_ThreadJob job = PC_ThreadManager.getNextJob();
			if(job==null){
				synchronized (this){
					try {
						wait();
					} catch (InterruptedException e) {}
				}
			}else{
				job.doJob();
			}
		}
	}
	
}
