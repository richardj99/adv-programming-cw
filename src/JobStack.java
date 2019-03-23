import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JobStack {

    private final int MAX_TIME_REQUIREMENT_PER_JOB = 500;  // Milliseconds
    private final int MAX_RESOURCE_COUNT_PER_JOB = 10;
    
    private final LinkedList<Job> stack; 
    private int jobCount = 0;  // To generate each job's ID.

    private final Lock jobStackChangeLock;
    
    public JobStack(int size) {
        stack = new LinkedList<>();
        Random rn = new Random(12345);  // Created with a seed so that random numbers generated are the same in each run.
        for(int i=0; i<size; i++) {
            stack.push(new Job(jobCount, (rn.nextInt(MAX_TIME_REQUIREMENT_PER_JOB)+1), (rn.nextInt(MAX_RESOURCE_COUNT_PER_JOB)+1)));
            jobCount++;
        }
        
        jobStackChangeLock = new ReentrantLock();
    }
    
    // Will return the next job on the stack or null to signal that there are no more jobs left on the stack.
    public Job pop() {
        Job returnValue = null;
        jobStackChangeLock.lock();
        try {
            if(!stack.isEmpty()) {
                returnValue = stack.pop();
            }
        } finally {
            jobStackChangeLock.unlock();
        }
        return returnValue;
    }
    
    public int getSize() {
        int returnValue = 0;
        jobStackChangeLock.lock();
        try {
            returnValue = stack.size();
        } finally {
            jobStackChangeLock.unlock();
        }
        return returnValue;
    }
    
}
