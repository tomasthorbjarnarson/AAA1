
public class Greedy {
	private int numJobs;
	private int[][] jobs;
	
	public Greedy(ProblemInstance instance) {
		numJobs = instance.getNumJobs();
		jobs = instance.getJobs();
	}
	
	// returns the earliest deadline first schedule
	// sorting is a little quicker, but then it is more tricky
	// to use this as a subroutine for a search method
	public Schedule getSchedule() {
		int jobID = -1;
		int jobLength = -1;
		int jobDueTime = -1;
		
		for(int i = 0; i < numJobs; ++i){
			if(jobDueTime == -1 || jobDueTime > jobs[i][1]){
				jobID = i;
				jobLength = jobs[i][0];
				jobDueTime = jobs[i][1];
			}
		}
		return getSchedule(new Schedule(null, jobID, jobLength, jobDueTime));
	}
	
	// adds the next earliest deadline first job to the schedule
	private Schedule getSchedule(Schedule s){
		if(s.getDepth() >= numJobs) return s;
		
		int jobID = -1;
		int jobLength = -1;
		int jobDueTime = -1;
		
		for(int i = 0; i < numJobs; ++i){
			if(s.containsJob(i) == false && (jobDueTime == -1 || jobDueTime > jobs[i][1])){
				jobID = i;
				jobLength = jobs[i][0];
				jobDueTime = jobs[i][1];
			}
		}

		return getSchedule(new Schedule(s, jobID, jobLength, jobDueTime));
	}
}
