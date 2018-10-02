import java.util.Arrays;
import java.lang.Math;

public class TardNamic {
  private int numJobs;
  private int[][] jobs;
  private int tardiness;


  public TardNamic(ProblemInstance instance) {
    numJobs = instance.getNumJobs();
    jobs = instance.getJobs();
    tardiness = 0;
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

  public int getTardiness() {
    return getTardiness(jobs, 0, 0);

  }

  public static String jobsToString(int numJobs, int[][] jobs) {
    String text = "Number of jobs: " + numJobs + ".\n";
    for(int i = 0; i < numJobs; i++) {
      text += "Job " + i + ": Time = " + jobs[i][0] + ". Due = " + jobs[i][1] + ".\n";
    }
    return text;
  }

  private int getTardiness(int[][] jobs, int timeElapsed, int depth) {
    System.out.println("At depth: " + depth);
    System.out.println("Total jobs: " + jobsToString(jobs.length, jobs));
    if(jobs.length == 0) {
      return 0;
    } else if(jobs.length == 1) {
      return Math.max(timeElapsed + jobs[0][0] - jobs[0][1], 0);
    }
    int longestJobIndex = getLongestJobIndex(jobs);
    System.out.println("Longest job index is: " + longestJobIndex);
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
      int leftJobsTimeElapsed = getJobTimeElapsed(leftJobs) + jobs[longestJobIndex][0];
      System.out.println("Left Job Time Elapsed: " + leftJobsTimeElapsed);
      tmpTardiness += Math.max(leftJobsTimeElapsed - jobs[longestJobIndex][1], 0);
      System.out.println("Current tardiness: " + tmpTardiness);

      System.out.println("Left jobs: " + jobsToString(leftJobs.length, leftJobs));
      System.out.println("Right jobs: " + jobsToString(rightJobs.length, rightJobs));

      tmpTardiness += getTardiness(leftJobs, 0, depth + 1);
      tmpTardiness += getTardiness(rightJobs, leftJobsTimeElapsed, depth + 1);

      if(tmpTardiness < minTardiness) {
        minTardiness = tmpTardiness;
      }
    }
    return minTardiness;
  }

}