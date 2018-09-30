import java.util.Arrays;

public class TardNamic {
  private int numJobs;
  private int[][] jobs;

  public TardNamic(ProblemInstance instance) {
    numJobs = instance.getNumJobs();
    jobs = instance.getJobs();
  }

  public int[][] getSchedule() {
    int longestJob = 0;
    int longestTime = Integer.MIN_VALUE;
    for(int i = 0; i < numJobs; i++) {
      if(jobs[i][0] > longestTime) {
        longestJob = i;
        longestTime = jobs[i][0];
      }
    }
    TwoJobs twoJobs = new TwoJobs(jobs, longestJob);
    System.out.println("Longest job is: " + longestJob);
    System.out.println("Longest job time is: " + twoJobs.getLongestJobTime());
    System.out.println(twoJobs.toString());

    return twoJobs.getRightJobs();

  }

  // private Schedule getSchedule(Schedule s) {
// 
  // }

}