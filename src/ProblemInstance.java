import java.util.Arrays;

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

	public void sortJobs() {
		Arrays.sort(this.jobs, (a, b) -> Integer.compare(a[1], b[1]));
	}

	public String toString() {
		String text = "Number of jobs: " + numJobs;
		for(int i = 0; i < numJobs; i++) {
			text += "Job i: Time = " + jobs[i][0] + ". Due = " + jobs[i][1] + ".\n";
		}
		return text;
	}
}
