import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.lang.Math;

public class ApproximationNation {

  private Job[] jobs;
  private JobUtils jobUtils;
  private double epsilon;

  public ApproximationNation(Job[] jobs, double epsilon) {
    this.jobs = jobs;
    this.jobUtils = new JobUtils(jobs);
    System.out.println(jobUtils.getString("All jobs"));
    this.epsilon = epsilon;
  }

  public double getTardiness() {
    int maxTardiness = jobUtils.getMaxTardiness();
    if (maxTardiness == 0) return 0;

    int n = jobs.length;
    Double K = 2.0*epsilon*maxTardiness/(n*(n+1));
    Job[] scaledJobs = new Job[jobs.length];
    for (int i = 0; i < jobs.length; i++) {
      scaledJobs[i] = new Job(i, (int) Math.floor(jobs[i].getProcesingTime()/K), 1.0 * jobs[i].getDueTime()/K);
    }

    TardNamic tard = new TardNamic(scaledJobs);
    double scaledTardiness = tard.getTardiness();

    return scaledTardiness;
  }
}
