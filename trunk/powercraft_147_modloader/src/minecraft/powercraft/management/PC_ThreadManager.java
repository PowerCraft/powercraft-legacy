package powercraft.management;

import java.util.ArrayList;
import java.util.List;

import powercraft.management.registry.PC_MSGRegistry;

public class PC_ThreadManager implements PC_IMSG {

	private static boolean hasInit = false;
	private static PC_WorkerThread[] threads;
	private static List<PC_ThreadJob> jobs = new ArrayList<PC_ThreadJob>();
	private static List<PC_ThreadJob> jobsFirst = new ArrayList<PC_ThreadJob>();
	
	private PC_ThreadManager(){
		
	}
	
	public static void init(){
		if(hasInit)
			return;
		hasInit = true;
		int numThreads = PC_GlobalVariables.config.getInt("threads.cound", 3, "Number of thread of PowerCraft");
		PC_MSGRegistry.registerMSGObject(new PC_ThreadManager());
		threads = new PC_WorkerThread[numThreads];
		for(int i=0; i<numThreads; i++){
			threads[i] = new PC_WorkerThread();
			threads[i].start();
		}
	}

	private static void tick(){
		while(true){
			PC_ThreadJob job = PC_ThreadManager.getNextJob();
			if(job==null){
				break;
			}else{
				job.doJob();
			}
		}
	}
	
	public static synchronized PC_ThreadJob getNextJob(){
		PC_ThreadJob nextJob = null;
		if(jobsFirst.size()>0){
			nextJob = jobsFirst.get(0);
			jobsFirst.remove(0);
		}else if(jobs.size()>0){
			nextJob = jobs.get(0);
			jobs.remove(0);
		}
		return nextJob;
	}
	
	public static synchronized void addJob(PC_ThreadJob job){
		jobs.add(job);
		startSleepingThread();
	}
	
	public static synchronized void addJobFirst(PC_ThreadJob job) {
		jobsFirst.add(job);
		startSleepingThread();
	}
	
	private static void startSleepingThread(){
		for(PC_WorkerThread thread:threads){
			switch(thread.getState()){
			case BLOCKED:
			case RUNNABLE:
			case NEW:
			case TERMINATED:
				break;
			case TIMED_WAITING:
			case WAITING:
				synchronized (thread){
					thread.notify();
				}
				return;
			}
		}
	}
	
	@Override
	public Object msg(int msg, Object... obj) {
		if(msg==PC_MSGRegistry.MSG_TICK_EVENT){
			tick();
		}
		return null;
	}
	
}
