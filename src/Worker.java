import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Worker implements Runnable {

    private final int id;  // Unique worker ID.
    
    private final JobStack jobStack;  // Reference to the job stack.
    private final ResourceStack resourceStack;  // Reference to the resource stack.
    
    private Job job;  // Job being processed.
    private Resource[] resources;  // Resources being used for job being processed.
    
    private boolean busy;  // Indicates the status of the worker. True when they are working (executing jobs) and false when there are no more jobs left to execute.
    
    private final Map<Integer, ArrayList<Integer>> jobsCompleted;  // The job record of the worker. Stores each job's ID and the IDs of the resources used for each job.
    // Constructor.
    public Worker(int theId, JobStack theJobStack, ResourceStack theResourceStack) {
        id = theId;
        jobStack = theJobStack;
        resourceStack = theResourceStack;
        job = null;
        busy = true;
        jobsCompleted = new TreeMap<>();
    }

    private void log(String msg) {
        System.out.println("Worker "+id+": "+msg);
    }
    
    /// UNDER CONSTRUCTION /////////////////////////////////////////////////////


    private void report(int jobID) throws MalformedURLException,IOException {
        String url = "http://www.scm.keele.ac.uk/staff/stan/app_notify.php?job=";
        ;
        
    }
    
    public void run() {
    	while(jobStack.getSize() > 0) {
    		job = jobStack.pop();
    		Integer[] resourcesUsed = new Integer[job.getResourceRequirement()];
    		while(resources == null) {
    			try{
    				resources = resourceStack.pop(job.getResourceRequirement());
    			} catch(NullPointerException e) {
    				;
    			}
    		}
    		try {
    			Thread.sleep(job.getTimeToComplete());
    		} catch(InterruptedException e) {
    			;
    		}
    		// Job completed
    		for(int i = 0; i<resources.length; i++) {
    			resourcesUsed[i] = resources[i].getId();
    		}
    		jobsCompleted.put(job.getId(), new ArrayList<Integer>(Arrays.asList(resourcesUsed)));
    		this.log("Completed job " + job.getId());
    		job = null;
    		resourceStack.push(resources);
    		resources = null;
    		resourcesUsed = null;
    	}
    	busy = false;
    }
    
    public boolean getBusyState() {
    	return busy;
    }
    
    public Set<Integer> getJobsCompletedKeys(){
    	Set<Integer> keySet = jobsCompleted.keySet();
    	return keySet;
    }
    
    public Map<Integer, ArrayList<Integer>> getJobsCompleted(){
    	return jobsCompleted;
    }
    
    public Integer getId() {
    	return id;
    }
    
}
