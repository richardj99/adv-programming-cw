public class Job {

    private final int id;
    private final int timeToComplete;  // Time required to complete job (milliseconds).
    private final int resourceRequirement;  // Number of resources required to complete the job.
            
    public Job(int theId, int theTimeToComplete, int theResourceRequirement) {
        id = theId;
        resourceRequirement = theResourceRequirement;
        timeToComplete = theTimeToComplete;
    }

    public int getId() {
        return id;
    }

    public int getTimeToComplete() {
        return timeToComplete;
    }
    
    public int getResourceRequirement() {
        return resourceRequirement;
    }
    
}
