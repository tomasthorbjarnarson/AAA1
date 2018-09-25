import java.util.PriorityQueue;

public class BestFirst {
	private int numJobs;
	private int[][] jobs;
	
	public BestFirst(ProblemInstance instance) {
		numJobs = instance.getNumJobs();
		jobs = instance.getJobs();
	}
	
	// returns the best-first (or breadth-first) search schedule
	// It uses a PriorityQueue to store schedules, in every iteration
	// it gets the next best schedule, tries to append all possible jobs
	// and stores the resulting schedules on the queue
	public Schedule getSchedule() {
		PriorityQueue<Schedule> Q = new PriorityQueue<Schedule>();
		
		for(int i = 0; i < numJobs; ++i){
			Q.offer(new Schedule(null, i, jobs[i][0], jobs[i][1]));
		}
		
		Schedule bestSchedule = null;
		
		while(Q.peek() != null){
			Schedule s = Q.poll();

			if(s.getDepth() >= numJobs){
				if(bestSchedule == null || bestSchedule.getTardiness() > s.getTardiness()){
					bestSchedule = s;
				}
				continue;
			}
			
			if(bestSchedule != null && bestSchedule.getTardiness() < s.getTardiness()){
				continue;
			}
			
			for(int i = 0; i < numJobs; ++i){
				if(!s.containsJob(i)){
					Q.offer(new Schedule(s, i, jobs[i][0], jobs[i][1]));
				}
			}
		}
		
		return bestSchedule;
	}
}
