import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.lang.Math;

public class ApproximationNation {

  private int[][] jobs;

  public ApproximationNation(ProblemInstance instance) {
    jobs = instance.getJobs();
    Arrays.sort(jobs, Comparator.comparingInt(a -> a[1]));
    System.out.println(jobsToString(jobs, "All jobs"));
  }

  private String jobsToString(int[][] jobs, String label) {
    String s = "";
    if (label != "") {
      s += label + ": " + "\n";
    }
    for (int i = 0; i < jobs.length; i++) {
      s += jobs[i][0] + " " + jobs[i][1] + "\n";
    }
    return s;
  }

  private int getMaxTardiness(int[][] jobs) {
    int elapsedTime = 0;
    int tardiness = 0;
    for(int i = 0; i < jobs.length; i++) {
      elapsedTime += jobs[i][0];
      tardiness += Math.max(timeElapsed - jobs[i][1], 0);
    }
    return tardiness;
  }

}
