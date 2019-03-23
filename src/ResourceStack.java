import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ResourceStack {

    private final LinkedList<Resource> stack; 
    private int resourceCount = 0;  // To generate each resource's ID.

    private final Lock resourceStackChangeLock;
    private final Condition sufficientResourcesCondition;
    
    
    public ResourceStack(int size) {
        stack = new LinkedList<>();
        for(int i=0; i<size; i++) {
            stack.push(new Resource(resourceCount));
            resourceCount++;
        }

        resourceStackChangeLock = new ReentrantLock();
        sufficientResourcesCondition = resourceStackChangeLock.newCondition();
    }
    
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
