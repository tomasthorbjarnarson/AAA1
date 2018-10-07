import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.lang.Math;

public class TardNamic {
  private int[][] jobs;
  private HashMap<String, Integer> tardinessMap;
  private int cacheHits;


  public TardNamic(ProblemInstance instance) {
    jobs = instance.getJobs();
    Arrays.sort(jobs, Comparator.comparingInt(a -> a[1]));
    tardinessMap = new HashMap<String, Integer>();
    cacheHits = 0;
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
    for (int[] someJob : someJobs) {
      timeElapsed += someJob[0];
    }
    return timeElapsed;
  }

  private static String createJobKey(int[][] someJobs, int timeElapsed) {
    StringBuilder keyBuilder = new StringBuilder();
    for (int[] someJob : someJobs) {
      keyBuilder.append(someJob[0]).append("-").append(someJob[1]).append("|");
    }
    return keyBuilder.append(timeElapsed).toString();
  }

  public int getTardiness() {
    return getTardiness(jobs, 0);
  }

  public int getTardinessRaw() {
    return getTardinessRaw(jobs, 0);
  }

  public int getCacheHits() {
    return cacheHits;
  }

  public int getCacheSize() {
    return tardinessMap.size();
  }

  private int getTardiness(int[][] jobs, int timeElapsed) {
    if(jobs.length == 0) {
      return 0;
    } else if(jobs.length == 1) {
      return Math.max(timeElapsed + jobs[0][0] - jobs[0][1], 0);
    }
    String jobsKey = createJobKey(jobs, timeElapsed);
    if (tardinessMap.containsKey(jobsKey)) {
      cacheHits += 1;
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
        tmpTardiness += getTardiness(leftJobs, timeElapsed);
        tmpTardiness += getTardiness(rightJobs, leftJobsTimeElapsed);
        if(tmpTardiness < minTardiness) {
          minTardiness = tmpTardiness;
        }
        if(minTardiness == 0) {
          break;
        }
      }
    }
    tardinessMap.put(jobsKey, minTardiness);
    return minTardiness;
  }

  //      All Functions below this one are purely for testing purposes.




  public int getTardinessWithDeltaRestriction() {
    return getTardinessWithDeltaRestriction(jobs, 0);
  }

  public int getTardinessWithCache() {
    return getTardinessWithCache(jobs, 0);
  }

  public int getTardinessWithMinOptimisation() {
    return getTardinessWithMinOptimisation(jobs, 0);
  }

  private int getTardinessWithDeltaRestriction(int[][] jobs, int timeElapsed) {
    if(jobs.length == 0) {
      return 0;
    } else if(jobs.length == 1) {
      return Math.max(timeElapsed + jobs[0][0] - jobs[0][1], 0);
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
        tmpTardiness += getTardinessWithDeltaRestriction(leftJobs, timeElapsed);
        tmpTardiness += getTardinessWithDeltaRestriction(rightJobs, leftJobsTimeElapsed);
        if(tmpTardiness < minTardiness) {
          minTardiness = tmpTardiness;
        }
      }
    }
    return minTardiness;
  }

  private int getTardinessWithCache(int[][] jobs, int timeElapsed) {
    if(jobs.length == 0) {
      return 0;
    } else if(jobs.length == 1) {
      return Math.max(timeElapsed + jobs[0][0] - jobs[0][1], 0);
    }
    String jobsKey = createJobKey(jobs, timeElapsed);
    if (tardinessMap.containsKey(jobsKey)) {
      cacheHits += 1;
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

      tmpTardiness += Math.max(leftJobsTimeElapsed - jobs[longestJobIndex][1], 0);
      tmpTardiness += getTardinessWithCache(leftJobs, timeElapsed);
      tmpTardiness += getTardinessWithCache(rightJobs, leftJobsTimeElapsed);
      if(tmpTardiness < minTardiness) {
        minTardiness = tmpTardiness;
      }
    }
    tardinessMap.put(jobsKey, minTardiness);
    return minTardiness;
  }

  private int getTardinessWithMinOptimisation(int[][] jobs, int timeElapsed) {
    if(jobs.length == 0) {
      return 0;
    } else if(jobs.length == 1) {
      return Math.max(timeElapsed + jobs[0][0] - jobs[0][1], 0);
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

      tmpTardiness += Math.max(leftJobsTimeElapsed - jobs[longestJobIndex][1], 0);
      tmpTardiness += getTardinessRaw(leftJobs, timeElapsed);
      tmpTardiness += getTardinessRaw(rightJobs, leftJobsTimeElapsed);
      if(tmpTardiness < minTardiness) {
        minTardiness = tmpTardiness;
      }
      if (minTardiness == 0) break;
    }
    return minTardiness;
  }

  private int getTardinessRaw(int[][] jobs, int timeElapsed) {
    if(jobs.length == 0) {
      return 0;
    } else if(jobs.length == 1) {
      return Math.max(timeElapsed + jobs[0][0] - jobs[0][1], 0);
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

      tmpTardiness += Math.max(leftJobsTimeElapsed - jobs[longestJobIndex][1], 0);
      tmpTardiness += getTardinessRaw(leftJobs, timeElapsed);
      tmpTardiness += getTardinessRaw(rightJobs, leftJobsTimeElapsed);
      if(tmpTardiness < minTardiness) {
        minTardiness = tmpTardiness;
      }
    }
    return minTardiness;
  }

}