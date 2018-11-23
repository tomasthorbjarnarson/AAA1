public class JobUtils {
  private Job[] jobs;

  public JobUtils(Job[] jobs) {
    this.jobs = jobs;
  }

  public String getString(String label) {
    String s = "";
    if (label != "") {
      s += label + ": " + "\n";
    }
    for (int i = 0; i < jobs.length; i++) {
      s += "p: " + jobs[i].getProcesingTime() + " d: " + jobs[i].getDueTime() + " i: " + jobs[i].getIndex() + "\n";
    }
    return s;
  }

  public double getMaxTardiness() {
    int timeElapsed = 0;
    double maxTardiness = 0;
    double tmpTardiness = 0;
    for (Job job : this.jobs) {
      timeElapsed += job.getProcesingTime();
      tmpTardiness = Math.max(timeElapsed - job.getDueTime(), 0);
      if (tmpTardiness > maxTardiness) {
        maxTardiness = tmpTardiness;
      }
    }
    return maxTardiness;
  }

  public double getTotalTardiness() {
    int timeElapsed = 0;
    double maxTardiness = 0;
    for (Job job : this.jobs) {
      timeElapsed += job.getProcesingTime();
      maxTardiness += Math.max(timeElapsed - job.getDueTime(), 0);
    }
    return maxTardiness;
  }
}