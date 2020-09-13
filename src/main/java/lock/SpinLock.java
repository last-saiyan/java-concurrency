package lock;

public class SpinLock {

    private boolean isLocked = false;
    private Thread lockedBy = null;

    synchronized public void  lock() throws InterruptedException {
        while (isLocked){
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
