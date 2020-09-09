package lock;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestSimpleLock {

    @Test
    public void singleThreadLockUnlockTest() throws InterruptedException {
        SimpleLock simpleLock = new SimpleLock();
        Assertions.assertFalse(simpleLock.isLocked(), "initially lock object should not be locked");

        simpleLock.lock();
        Assertions.assertTrue(simpleLock.isLocked(), "after calling lock() method it has to be locked");

        simpleLock.unlock();
        Assertions.assertFalse(simpleLock.isLocked(), "after unlock state has to be false");
    }

    @Test
    public void multiThreadedTestLock() throws InterruptedException {
        SimpleLock simpleLock = new SimpleLock();
        Thread t1 = new Thread(() -> {
            try {
                simpleLock.lock();
                Assertions.assertTrue(simpleLock.isLocked(), "after calling lock() method it has to be locked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                Assertions.assertTrue(simpleLock.isLocked(), "after calling lock() method it has to be locked");
                simpleLock.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // thread t1,t2 try to acquire lock, t1 first next t2
        // after t1 acquires lock t2 cannot acquire lock and its in waiting state
        t1.start();
        Thread.sleep(1000);
        Assertions.assertTrue(simpleLock.isLocked(), "after calling lock() method it has to be locked");
        t2.start();
        Thread.sleep(1000);
        Assertions.assertEquals(Thread.State.WAITING, t2.getState());

    }

    @Test
    public void multiThreadedTestUnLock() throws InterruptedException{
        SimpleLock simpleLock = new SimpleLock();
        Thread t1 = new Thread(() -> {
            try {
                simpleLock.lock();
                Assertions.assertTrue(simpleLock.isLocked(), "after calling lock() method it has to be locked");
                simpleLock.unlock();
                Assertions.assertFalse(simpleLock.isLocked(), "after unlock state has to be false");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                simpleLock.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // thread t1,t2 try to acquire lock, t1 first next t2
        // after t1 acquires lock t2 cannot acquire lock and its in waiting state
        t1.start();
        Thread.sleep(1000);
        Assertions.assertFalse(simpleLock.isLocked(), "after unlock state has to be false");
        t2.start();
        Thread.sleep(1000);

        Assertions.assertEquals(t2.getState(), Thread.State.TERMINATED);

    }


}
