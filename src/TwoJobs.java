public class TwoJobs {
  private int[][] jobs;
  private int[][] leftJobs;
  private int[][] rightJobs;
  private int numJobs;
  private int longestJobTime;
  private int tardiness;

  public TwoJobs(int[][] jobs, int splitIndex) {
    jobs = jobs;
    numJobs = jobs.length;
    longestJobTime = jobs[splitIndex][0];
    leftJobs = new int[splitIndex][2];
    rightJobs = new int[numJobs-splitIndex][2];
    
    for(int i = 0; i < numJobs; i++) {
      if(i < splitIndex) {
        leftJobs[i] = jobs[i];
      } else {
        rightJobs[i-splitIndex] = jobs[i];

      }
    }
  }

  public int getLongestJobTime() {
    return longestJobTime;
  }

  public int getNumJobs() {
    return numJobs;
  }

  public int getNumLeftJobs() {
    return leftJobs.length;
  }

  public int getNumRightJobs() {
    return rightJobs.length;
  }

  public int[][] getLeftJobs() {
    return leftJobs;
  }

  public int[][] getRightJobs() {
    return rightJobs;
  }

  public String toString() {
    String text = "Number of total: " + numJobs + ".\n";
    text += "Left Jobs: \n";
    for(int i = 0; i < leftJobs.length; i++) {
      text += "Job i: Time = " + leftJobs[i][0] + ". Due = " + leftJobs[i][1] + ".\n";
    }
    text += "Right Jobs: \n";
    for(int i = 0; i < rightJobs.length; i++) {
      text += "Job i: Time = " + rightJobs[i][0] + ". Due = " + rightJobs[i][1] + ".\n";
    }
    return text;
  }
}