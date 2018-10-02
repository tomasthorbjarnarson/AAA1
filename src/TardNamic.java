import java.util.Arrays;
import java.util.HashMap;
import java.lang.Math;

public class TardNamic {
  private int numJobs;
  private int[][] jobs;
  private HashMap<String, Integer> tardinessMap;
  private int hashGer;


  public TardNamic(ProblemInstance instance) {
    numJobs = instance.getNumJobs();
    jobs = instance.getJobs();
    tardinessMap = new HashMap<String, Integer>();
    hashGer = 0;
  }

  private static int getLongestJobIndex(int[][] jobs) {
    int longestJobIndex = 0;
    int longestTime = Integer.MIN_VALUE;
    for(int i = 0; i < jobs.length; i++) {
      if(jobs[i][0] > longestTime) {
        longestJobIndex = i;
        longestTime = jobs[i][0];
      }
    }
    return longestJobIndex;
  }

  private static int getJobTimeElapsed(int[][] someJobs) {
    int timeElapsed = 0;
    for(int i = 0; i < someJobs.length; i++) {
      timeElapsed += someJobs[i][0];
    }
    return timeElapsed;
  }

  private static String createJobKey(int[][] someJobs, int timeElapsed) {
    String key = "";
    for(int i = 0; i < someJobs.length; i++) {
      key += someJobs[i][0] + "-" + someJobs[i][1];
      key += "|";
    }
    return key + timeElapsed;
  }

  public int getTardiness() {
    return getTardiness(jobs, 0, 0);
  }

  public int getHashGer() {
    return hashGer;
  }

  public int getHashSize() {
    return tardinessMap.size();
  }

  public static String jobsToString(int numJobs, int[][] jobs) {
    String text = "Number of jobs: " + numJobs + ".\n";
    for(int i = 0; i < numJobs; i++) {
      text += "Job " + i + ": Time = " + jobs[i][0] + ". Due = " + jobs[i][1] + ".\n";
    }
    return text;
  }

  private int getTardiness(int[][] jobs, int timeElapsed, int depth) {
    if(jobs.length == 0) {
      return 0;
    } else if(jobs.length == 1) {
      return Math.max(timeElapsed + jobs[0][0] - jobs[0][1], 0);
    }
    String jobsKey = createJobKey(jobs, timeElapsed);
    if (tardinessMap.containsKey(jobsKey)) {
      hashGer += 1;
      return tardinessMap.get(jobsKey);
    }
    int longestJobIndex = getLongestJobIndex(jobs);
    int minTardiness = Integer.MAX_VALUE;
    for(int i = 0; i < jobs.length - longestJobIndex; i++) {
      int tmpTardiness = 0;
      int numLeftJobs = longestJobIndex+i;
      int numRightJobs = jobs.length - longestJobIndex - i - 1;

      int[][] leftJobs = new int[numLeftJobs][2];
      int[][] rightJobs = new int[numRightJobs][2];

      for(int j = 0; j < numLeftJobs; j++) {
        int tmpJ = j;
        if(j >= longestJobIndex) {
          tmpJ += 1;
        }
        leftJobs[j] = jobs[tmpJ];
      }
      for(int j = 0; j < numRightJobs; j++) {
        rightJobs[j] = jobs[longestJobIndex + j + i + 1];
      }
      int leftJobsTimeElapsed = timeElapsed + getJobTimeElapsed(leftJobs) + jobs[longestJobIndex][0];

      if(rightJobs.length == 0 || leftJobsTimeElapsed < rightJobs[0][1]){
        tmpTardiness += Math.max(leftJobsTimeElapsed - jobs[longestJobIndex][1], 0);
        tmpTardiness += getTardiness(leftJobs, timeElapsed, depth + 1);
        tmpTardiness += getTardiness(rightJobs, leftJobsTimeElapsed, depth + 1);
        if(tmpTardiness < minTardiness) {
          minTardiness = tmpTardiness;
        }
      }


    }
    tardinessMap.put(jobsKey, minTardiness);
    return minTardiness;
  }

}