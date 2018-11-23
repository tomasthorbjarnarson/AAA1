import java.util.HashMap;

public class SubproblemKey {
    int startIndex, endIndex, startTime, length;

    SubproblemKey(int startIndex, int endIndex, int startTime, int length) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.startTime = startTime;
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SubproblemKey)) return false;
        SubproblemKey other = (SubproblemKey) o;
        return startIndex == other.startIndex
                && endIndex == other.endIndex
                && startTime == other.startTime
                && length == other.length;
    }

    @Override
    public int hashCode() { // note: some random computation that results in few collisions.
        int result = startIndex;
        result = 31 * result + endIndex;
        result = 31 * result + startTime;
        result = 31 * result + length;
        return result;
    }
}