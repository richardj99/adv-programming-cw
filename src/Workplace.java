public class Workplace {

    private final int JOB_COUNT = 100;
    private final int RESOURCE_COUNT = 30;  // Must be equal or greater than the largest requirement from a single job.
    private final int WORKER_COUNT = 20;
    
    private final JobStack jobs;
    private final ResourceStack resources;
    private final Workforce workers;
    
    /* Constructor.
     * creates a JobStack using job count
     * creates a resource stack using resource count
     * creates a work force using worker count, the job stack and the resource stack
     */
    public Workplace() {
        jobs = new JobStack(JOB_COUNT);
        resources = new ResourceStack(RESOURCE_COUNT);
        workers = new Workforce(WORKER_COUNT, jobs, resources);  
    }
    
    /*main method
     * instantiates itself
     * prints number of current jobs in jobstack against the original amount
     * prints the same for resources
     * starts off workforce resource management (task a)
     * sleeps thread until all workers are finished
     * prints all of the job records and then shows remaining jobs and resources. 
     */
    public static void main(String[] args) throws InterruptedException {
        
        Workplace workplace = new Workplace();

        System.out.println("Jobs in job stack: " + workplace.jobs.getSize() + " out of " + workplace.JOB_COUNT + ".");
        System.out.println("Resources in resources stack: " + workplace.resources.getSize() + " out of " + workplace.RESOURCE_COUNT + ".\n");

        workplace.workers.start();
        
        while(!workplace.workers.allWorkersFinished()) {
            Thread.sleep(100);  // To allow other threads to carry on.
        }
        
        workplace.workers.printJobRecords();
        
        System.out.println("\nJobs in job stack: " + workplace.jobs.getSize() + " out of " + workplace.JOB_COUNT + ".");
        System.out.println("Resources in resources stack: " + workplace.resources.getSize() + " out of " + workplace.RESOURCE_COUNT + ".");
        
    }    
}
