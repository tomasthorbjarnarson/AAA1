import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.lang.Math;

public class ApproximationNation {

  private int[][] jobs;
  private double epsilon;

  public ApproximationNation(ProblemInstance instance, double epsilon) {
    jobs = instance.getJobs();
    Arrays.sort(jobs, Comparator.comparingInt(a -> a[1]));
    System.out.println(jobsToString(jobs, "All jobs"));
    this.epsilon = epsilon;
  }

  private String jobsToString(int[][] jobs, String label) {
    StringBuilder s = new StringBuilder();
    if (!label.equals("")) {
      s.append(label).append(": ").append("\n");
    }
    for (int[] job : jobs) {
      s.append(job[0]).append(" ").append(job[1]).append("\n");
    }
    return s.toString();
  }

  private int getMaxTardiness(int[][] jobs) {
    int timeElapsed = 0;
    int maxTardiness = 0;
    for (int[] job : jobs) {
      timeElapsed += job[0];
      maxTardiness = Math.max(timeElapsed - job[1], maxTardiness);
    }
    return maxTardiness;
  }

  public int getTardiness() {
    int maxTardiness = getMaxTardiness(this.jobs);
    if (maxTardiness == 0) return 0;

    int n = jobs.length;
    Double K = 2.0*epsilon*maxTardiness/(n*(n+1));
    Job[] scaledJobs = new Job[jobs.length];
    for (int i = 0; i < jobs.length; i++) {
      scaledJobs[i] = new Job(i, (int) Math.floor(jobs[i][0]/K), 1.0 * jobs[i][1]/K);
    }

    TardNamic tard = new TardNamic(scaledJobs);
    tard.getTardiness();

    return 1;
  }
}
