
public class ProblemInstance {
	private int numJobs;
	private int[][] jobs; //size = [num_jobs][2], for every job [0] is the length, [1] is the due time
	
	public ProblemInstance(int numJobs, int[][] jobs) {
		this.numJobs = numJobs;
		this.jobs = jobs;
	}
	
	public int getNumJobs() {
		return numJobs;
	}
	
	public int[][] getJobs() {
		return jobs;
	}
}
