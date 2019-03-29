import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ResourceStack {
	
	/*
	 * contains a linked list of resources --> the stack
	 * sets resource count to zero --> incremented to create the resource's id.
	 * Lock instance --> resource stack change lock
	 */

    private final LinkedList<Resource> stack; 
    private int resourceCount = 0;  // To generate each resource's ID.

    private final Lock resourceStackChangeLock;
    private final Condition sufficientResourcesCondition;
    
    /* Constructor Definition:
     * initialises stack and fills it with new resources
     * previous lock set as a ReentrantLock
     * condition added to lock
     */
    public ResourceStack(int size) {
        stack = new LinkedList<>();
        for(int i=0; i<size; i++) {
            stack.push(new Resource(resourceCount));
            resourceCount++;
        }

        resourceStackChangeLock = new ReentrantLock();
        sufficientResourcesCondition = resourceStackChangeLock.newCondition();
    }
    
    /* Push Method Definition
     * Lock activated
     * resources (from array) are then passed into stack
     * Condition signalled
     * Lock is deactivated
     */
    public void push(Resource[] resources) {
        resourceStackChangeLock.lock();
        try {
            for (Resource r : resources) {
                stack.push(r);
            }
            sufficientResourcesCondition.signalAll();
        } finally {
            resourceStackChangeLock.unlock();
        }
    } 
    
    /* Pop Method Definition:
     * number of resources needed passed into the method
     * resource stack is locked
     * array initialised with length of number of required resources.
     * 
     */
    public Resource[] pop(int numberOfRequiredResources) {
        Resource[] requiredResources = new Resource[numberOfRequiredResources];
        resourceStackChangeLock.lock();
        try {
            while(stack.size() < numberOfRequiredResources) {
                sufficientResourcesCondition.await();
                //Thread.sleep(100);
            }
            for(int i=0; i<numberOfRequiredResources; i++) {
                requiredResources[i] = stack.pop();
            }
        } catch(InterruptedException e) {
            System.out.println(e.toString());
        } finally {
            resourceStackChangeLock.unlock();
        }
        return requiredResources;
    }

    public int getSize() {
        int returnValue = 0;
        resourceStackChangeLock.lock();
        try {
            returnValue = stack.size();
        } finally {
            resourceStackChangeLock.unlock();
        }
        return returnValue;
    }
    
}
