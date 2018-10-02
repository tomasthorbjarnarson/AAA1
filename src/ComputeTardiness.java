import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ComputeTardiness {	
	public static ProblemInstance readInstance(String filename){
		ProblemInstance instance = null;
		
		try {
			int numJobs = 0;
			int[][] jobs = null;
			
			Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
			if(sc.hasNextInt()){
				numJobs = sc.nextInt();
				jobs = new int[numJobs][2];
				int nextJobID = 0;
			
				while (sc.hasNextInt() && nextJobID < numJobs) {
					jobs[nextJobID][0] = sc.nextInt();
					jobs[nextJobID][1] = sc.nextInt();
					nextJobID++;
				}
			}
			sc.close();
			
			instance = new ProblemInstance(numJobs, jobs);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return instance;
	}

	public static String jobsToString(int numJobs, int[][] jobs) {
		String text = "Number of jobs: " + numJobs + ".\n";
		for(int i = 0; i < numJobs; i++) {
			text += "Job i: Time = " + jobs[i][0] + ". Due = " + jobs[i][1] + ".\n";
		}
		return text;
	}

	// reads a problem, and outputs the result of both greedy and best-first
    public static void main (String args[]) {
		ProblemInstance instance = readInstance(args[0]);
		instance.sortJobs();

		TardNamic tard = new TardNamic(instance);
		//Schedule tardSchedule = tard.getSchedule();
		System.out.println(jobsToString(instance.getNumJobs(), instance.getJobs()));
		int tardiness = tard.getTardiness();
		System.out.println("Tardiness: " + tardiness);

		// Greedy greedy = new Greedy(instance);
		// Schedule greedySchedule = greedy.getSchedule();
		// System.out.println(greedySchedule.getTardiness());
		// 
		// BestFirst bestFirst = new BestFirst(instance);
		// Schedule bestFirstSchedule = bestFirst.getSchedule();
		// System.out.println(bestFirstSchedule.getTardiness());
	}
}
