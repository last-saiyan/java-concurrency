package lock;

public class SimpleLock {

    private boolean isLocked = false;
    private Thread lockedBy = null;

    synchronized public void  lock() throws InterruptedException {
//        this is not a spin lock want ot demonstrate that
//        if statement will not work
//        check testSpuriousWakeup in TestSimpleLock class
        if (isLocked) {
            wait();
        }
        isLocked = true;
        lockedBy = Thread.currentThread();
    }

    public synchronized void unlock(){
        if(Thread.currentThread() == lockedBy) {
            isLocked = false;
            notifyAll();
        }
    }

    public synchronized void notifyAllThread(){
        notifyAll();
    }

    public boolean isLocked(){
        return isLocked;
    }

}
