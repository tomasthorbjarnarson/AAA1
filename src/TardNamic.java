import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.lang.Math;

public class TardNamic {
  private Double[][] jobs;
  private HashMap<String, Double> tardinessMap;
  private HashMap<String, Integer> optimalPosMap;
  private int cacheHits;


//  public TardNamic(ProblemInstance instance) {
//    jobs = instance.getJobs();
//    Arrays.sort(jobs, Comparator.comparingInt(a -> a[1]));
////    System.out.println(jobsToString(jobs, "All jobs"));
//    tardinessMap = new HashMap<String, Double>();
//    optimalPosMap = new HashMap<String, Double>();
//    cacheHits = 0;
//  }

  public TardNamic(Double[][] jobs) {
    this.jobs = jobs;
    Arrays.sort(jobs, Comparator.comparingDouble(a -> a[1]));
//    System.out.println(jobsToString(jobs, "All jobs"));
    tardinessMap = new HashMap<String, Double>();
    optimalPosMap = new HashMap<String, Integer>();
    cacheHits = 0;
  }

  private static int getLongestJobIndex(Double[][] jobs) {
    int longestJobIndex = 0;
    int longestTime = Integer.MIN_VALUE;
    for (int i = 0; i < jobs.length; i++) {
      if (jobs[i][0] > longestTime) {
        longestJobIndex = i;
        longestTime = jobs[i][0].intValue();
      }
    }
    return longestJobIndex;
  }

  private static int getJobTimeElapsed(Double[][] someJobs) {
    int timeElapsed = 0;
    for (Double[] someJob : someJobs) {
      timeElapsed += someJob[0];
    }
    return timeElapsed;
  }

  private static String createJobKey(Double[][] someJobs, int timeElapsed) {
    StringBuilder keyBuilder = new StringBuilder();
    for (Double[] someJob : someJobs) {
      keyBuilder.append(someJob[0]).append("-").append(someJob[1]).append("|");
    }
    return keyBuilder.append(timeElapsed).toString();
  }

  public Double getTardiness() {
    Double tardiness = getTardiness(jobs, 0, 0);
    Double[][] optJobs = createSchedule(jobs, 0);
    System.out.println(jobsToString(optJobs, "optJobs"));
    int scheduleTardiness = calculateTardinessFromSchedule(optJobs);
    System.out.println("Opt schedule tardiness: " + scheduleTardiness);
    return tardiness;
  }

  private Double[][] getPartOfJobs(Double[][] jobs, int start, int length, int leaveOut) {
    Double[][] part = new Double[length][2];
    int offset = start;
    for (int i = 0; i < length; i++) {
      if (i == leaveOut) {
        offset += 1;
      }
      part[i] = jobs[i + offset];
    }
    return part;
  }

  private Double[][] createSchedule(Double[][] jobs, int timeElapsed) {
    if (jobs.length <= 1) {
      return jobs;
    }
    Double[][] optJobs = new Double[jobs.length][2];
    int longestJobIndex = getLongestJobIndex(jobs);
    int optLongestJobIndex = 0;
    if (optimalPosMap.containsKey(createJobKey(jobs, timeElapsed))) {
      optLongestJobIndex = optimalPosMap.get(createJobKey(jobs, timeElapsed));
    }
    optJobs[optLongestJobIndex] = jobs[longestJobIndex];
    int numLeftJobs = optLongestJobIndex;
    int numRightJobs = jobs.length - optLongestJobIndex - 1;

    Double[][] leftJobs = getPartOfJobs(jobs, 0, numLeftJobs, longestJobIndex);
    Double[][] rightJobs = getPartOfJobs(jobs, optLongestJobIndex + 1, numRightJobs, -1);

    Double[][] optLeftJobs = createSchedule(leftJobs, timeElapsed);
    int leftTimeElapsed = (int) (timeElapsed + jobs[longestJobIndex][0] + getJobTimeElapsed(leftJobs));
    Double[][] optRightJobs = createSchedule(rightJobs, leftTimeElapsed);

    for (int i = 0; i < numLeftJobs; i++) {
      optJobs[i] = optLeftJobs[i];
    }
    for (int j = 0; j < numRightJobs; j++) {
      optJobs[j + optLongestJobIndex + 1] = optRightJobs[j];
    }

    return optJobs;
  }

  private int calculateTardinessFromSchedule(Double[][] jobs) {
    int tardiness = 0;
    int timeElapsed = 0;
    for (int i = 0; i < jobs.length; i++) {
      timeElapsed += jobs[i][0];
      tardiness += Math.max(timeElapsed - jobs[i][1], 0);
    }
    return tardiness;
  }

  public int getCacheHits() {
    return cacheHits;
  }

  public int getCacheSize() {
    return tardinessMap.size();
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

  private String jobsToString(Double[][] jobs, String label) {
    String s = "";
    if (label != "") {
      s += label + ": " + "\n";
    }
    for (int i = 0; i < jobs.length; i++) {
      s += jobs[i][0] + " " + jobs[i][1] + "\n";
    }
    return s;
  }

  private double getTardiness(Double[][] jobs, int timeElapsed, int depth) {
    if (jobs.length == 0) {
      return 0;
    } else if (jobs.length == 1) {
      return Math.max(timeElapsed + jobs[0][0] - jobs[0][1], 0);
    }
    String jobsKey = createJobKey(jobs, timeElapsed);
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

      Double[][] leftJobs = new Double[numLeftJobs][2];
      Double[][] rightJobs = new Double[numRightJobs][2];

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
      int leftJobsTimeElapsed = (int) (timeElapsed + getJobTimeElapsed(leftJobs) + jobs[longestJobIndex][0]);

      if (rightJobs.length == 0 || leftJobsTimeElapsed < rightJobs[0][1]) {
        tmpTardiness += Math.max(leftJobsTimeElapsed - jobs[longestJobIndex][1], 0);
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