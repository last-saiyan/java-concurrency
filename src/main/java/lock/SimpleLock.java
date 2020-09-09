package lock;

public class SimpleLock {

    private boolean isLocked = false;

    synchronized public void  lock() throws InterruptedException {
        while (isLocked){
            wait();
        }
        isLocked = true;
    }

    public synchronized void unlock(){
        isLocked = false;
        notifyAll();
    }

    public boolean isLocked(){
        return isLocked;
    }
}
