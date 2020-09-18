package barrier;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CountDownLatch {
    ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock rLock = lock.readLock();
    private Lock wLock = lock.writeLock();
    private int count;


    public CountDownLatch(int count){
        this.count = count;
    }


    public synchronized void countDown(){
            System.out.println(count + " -count in " + Thread.currentThread().getName());
            count--;
            this.notifyAll();
    }


    public int getCount(){
        return this.count;
    }


    public void await() throws InterruptedException {
        while (count > 0) {
            wait();
        }
    }

}
