public class Workforce {

    private final Worker[] pool;  // The worker population.
    private int workerCount = 0;  // Used to generate each worker's ID and to keep a record of the number of workers in the workforce.

    Thread[] workerThreads;
            
    private final JobStack jobStack;  // Reference to the job stack.
    private final ResourceStack resourceStack;  // Reference to the resource stack.
    
    // Constructor.
    public Workforce(int size, JobStack theJobStack, ResourceStack theResourceStack) {
        jobStack = theJobStack;
        resourceStack = theResourceStack;

        pool = new Worker[size];
        for(int i=0; i<pool.length; i++) {
            pool[i] = new Worker(workerCount, jobStack, resourceStack);
            workerCount++;
        }

        workerThreads = new Thread[pool.length];
        for(int i=0; i<workerThreads.length; i++) {
            workerThreads[i] = new Thread(pool[i]);
        }
    }
    
    
    /// UNDER CONSTRUCTION /////////////////////////////////////////////////////
    
    
    // Starts all the worker threads.
    public void start() {
    	for(int i=0; i<workerThreads.length; i++) {
            workerThreads[i].start();
        }
    }
    
    // Checks whether all workers have finished.
    public boolean allWorkersFinished() {
    	for(Worker w: pool) {
    		if(w.getBusyState()) {
    			return false;
    		}
    	}
    	return true;
    }

    // Prints the job record of all workers.
    public void printJobRecords() {
        for(Worker w: pool) {
        	System.out.println("Worker " + w.getId() + ":");
        	for(Integer i: w.getJobsCompletedKeys()) {
        		System.out.print("\tJob " + i + ", Resources: ");
        		for(Integer j: w.getJobsCompleted().get(i)) {
        			System.out.print(" " + j);
        		}
        		System.out.print("\n");
        	}
        }
    }
}
