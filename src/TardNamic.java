import java.util.HashMap;
import java.lang.Math;

public class TardNamic {
  private Job[] jobs;
  private HashMap<SubproblemKey, Double> tardinessMap;
  private HashMap<SubproblemKey, Integer> optimalPosMap;
  private int cacheHits;

  public TardNamic(Job[] jobs) {
    this.jobs = jobs;
    tardinessMap = new HashMap<SubproblemKey, Double>();
    optimalPosMap = new HashMap<SubproblemKey, Integer>();
    cacheHits = 0;
  }

  private static int getLongestJobIndex(Job[] jobs) {
    int longestJobIndex = 0;
    int longestTime = Integer.MIN_VALUE;
    for (int i = 0; i < jobs.length; i++) {
      if (jobs[i].getProcesingTime() > longestTime) {
        longestJobIndex = i;
        longestTime = jobs[i].getProcesingTime();
      }
    }
    return longestJobIndex;
  }

  private static int getJobTimeElapsed(Job[] someJobs) {
    int timeElapsed = 0;
    for (Job someJob : someJobs) {
      timeElapsed += someJob.getProcesingTime();
    }
    return timeElapsed;
  }

  private static SubproblemKey createJobKey(Job[] someJobs, int timeElapsed) {
    return new SubproblemKey(someJobs[0].getIndex(), someJobs[someJobs.length-1].getIndex(), timeElapsed, someJobs.length);
  }

  public Double getTardiness() {
    Double tardiness = getTardiness(jobs, 0, 0);
    Job[] optJobs = createSchedule(jobs, 0);
    JobUtils optJobUtils = new JobUtils(optJobs);
    System.out.println(optJobUtils.getString("OptJobs"));
    int scheduleTardiness = optJobUtils.getMaxTardiness();
    System.out.println("Opt schedule tardiness: " + scheduleTardiness);
    return tardiness;
  }

  private Job[] getPartOfJobs(Job[] jobs, int start, int length, int leaveOut) {
    Job[] part = new Job[length];
    int offset = start;
    for (int i = 0; i < length; i++) {
      if (i == leaveOut) {
        offset += 1;
      }
      part[i] = jobs[i + offset];
    }
    return part;
  }

  private Job[] createSchedule(Job[] jobs, int timeElapsed) {
    if (jobs.length <= 1) {
      return jobs;
    }
    Job[] optJobs = new Job[jobs.length];
    int longestJobIndex = getLongestJobIndex(jobs);
    int optLongestJobIndex = 0;
    if (optimalPosMap.containsKey(createJobKey(jobs, timeElapsed))) {
      optLongestJobIndex = optimalPosMap.get(createJobKey(jobs, timeElapsed));
    }
    optJobs[optLongestJobIndex] = jobs[longestJobIndex];
    int numLeftJobs = optLongestJobIndex;
    int numRightJobs = jobs.length - optLongestJobIndex - 1;

    Job[] leftJobs = getPartOfJobs(jobs, 0, numLeftJobs, longestJobIndex);
    Job[] rightJobs = getPartOfJobs(jobs, optLongestJobIndex + 1, numRightJobs, -1);

    Job[] optLeftJobs = createSchedule(leftJobs, timeElapsed);
    int leftTimeElapsed = timeElapsed + jobs[longestJobIndex].getProcesingTime() + getJobTimeElapsed(leftJobs);
    Job[] optRightJobs = createSchedule(rightJobs, leftTimeElapsed);

    for (int i = 0; i < numLeftJobs; i++) {
      optJobs[i] = optLeftJobs[i];
    }
    for (int j = 0; j < numRightJobs; j++) {
      optJobs[j + optLongestJobIndex + 1] = optRightJobs[j];
    }

    return optJobs;
  }

  public int getCacheHits() {
    return cacheHits;
  }

  public int getCacheSize() {
    return tardinessMap.size();
  }

  private double getTardiness(Job[] jobs, int timeElapsed, int depth) {
    if (jobs.length == 0) {
      return 0;
    } else if (jobs.length == 1) {
      return Math.max(timeElapsed + jobs[0].getProcesingTime() - jobs[0].getDueTime(), 0);
    }
    SubproblemKey jobsKey = createJobKey(jobs, timeElapsed);
    if (tardinessMap.containsKey(jobsKey)) {
      cacheHits += 1;
      return tardinessMap.get(jobsKey);
    }
    int longestJobIndex = getLongestJobIndex(jobs);
    double minTardiness = Integer.MAX_VALUE;
    int someIndex = 0;
    for (int i = 0; i < jobs.length - longestJobIndex; i++) {
      double tmpTardiness = 0;
      int numLeftJobs = longestJobIndex + i;
      int numRightJobs = jobs.length - longestJobIndex - i - 1;

      Job[] leftJobs = new Job[numLeftJobs];
      Job[] rightJobs = new Job[numRightJobs];

      for (int j = 0; j < numLeftJobs; j++) {
        int tmpJ = j;
        if (j >= longestJobIndex) {
          tmpJ += 1;
        }
        leftJobs[j] = jobs[tmpJ];
      }
      for (int j = 0; j < numRightJobs; j++) {
        rightJobs[j] = jobs[longestJobIndex + j + i + 1];
      }
      int leftJobsTimeElapsed = timeElapsed + getJobTimeElapsed(leftJobs) + jobs[longestJobIndex].getProcesingTime();

      if (rightJobs.length == 0 || leftJobsTimeElapsed < rightJobs[0].getDueTime()) {
        tmpTardiness += Math.max(leftJobsTimeElapsed - jobs[longestJobIndex].getDueTime(), 0);
        tmpTardiness += getTardiness(leftJobs, timeElapsed, depth + 1);
        tmpTardiness += getTardiness(rightJobs, leftJobsTimeElapsed, depth + 1);
        if (tmpTardiness < minTardiness) {
          minTardiness = tmpTardiness;
          someIndex = numLeftJobs;
        }
        if (minTardiness == 0) {
          break;
        }
      }
    }
    tardinessMap.put(jobsKey, minTardiness);
    optimalPosMap.put(jobsKey, someIndex);
    return minTardiness;
  }
}