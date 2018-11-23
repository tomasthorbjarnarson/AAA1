public class Job {
    private final int index, procesingTime;
    private final double dueTime;

    public Job(int index, int processingTime, double dueTime) {
        this.index = index;
        this.dueTime = dueTime;
        this.procesingTime = processingTime;
    }

    public int getIndex() {
        return index;
    }

    public double getDueTime() {
        return dueTime;
    }

    public int getProcesingTime() {
        return procesingTime;
    }
}
