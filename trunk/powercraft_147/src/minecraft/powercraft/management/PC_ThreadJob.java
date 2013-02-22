package powercraft.management;


public class PC_ThreadJob {

	private PC_IThreadJob job;
	private PC_ThreadJob[] jobsAfter;
	
	public PC_ThreadJob(PC_IThreadJob job, PC_ThreadJob...jobsAfter){
		this.job = job;
		this.jobsAfter = jobsAfter;
	}
	
	public void doJob(){
		job.doJob();
		for(int i=0; i<jobsAfter.length; i++){
			PC_ThreadManager.addJobFirst(jobsAfter[i]);
		}
	}
	
}
