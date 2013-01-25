package powercraft.management;

public class PC_WorkerThread extends Thread {
	
	@Override
	public void run() {
		while(true){
			PC_ThreadJob job = PC_ThreadManager.getNextJob();
			if(job==null){
				try {
					wait();
				} catch (InterruptedException e) {}
			}else{
				job.doJob();
			}
		}
	}
	
}
