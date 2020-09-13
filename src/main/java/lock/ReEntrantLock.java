package lock;

public class ReEntrantLock {

    private boolean isLocked = false;
    private Thread lockedBy = null;
    private int count = 0;

    synchronized public void  lock() throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        while (isLocked && lockedBy != currentThread){
            wait();
        }
        isLocked = true;
        count++;
        lockedBy = currentThread;
    }

    public synchronized void unlock(){
        if(Thread.currentThread() == lockedBy) {
            count--;
        }
        if(count == 0) {
            isLocked = false;
            notifyAll();
        }
    }

    public boolean isLocked(){
        return isLocked;
    }
}
