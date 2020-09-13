package lock;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestSpinLock {

    @Test
    public void singleThreadLockUnlockTest() throws InterruptedException {
        SpinLock spinLock = new SpinLock();
        Assertions.assertFalse(spinLock.isLocked(), "initially lock object should not be locked");

        spinLock.lock();
        Assertions.assertTrue(spinLock.isLocked(), "after calling lock() method it has to be locked");

        spinLock.unlock();
        Assertions.assertFalse(spinLock.isLocked(), "after unlock state has to be false");
    }

    @Test
    public void multiThreadedTestLock() throws InterruptedException {
        SpinLock spinLock = new SpinLock();
        Thread t1 = new Thread(() -> {
            try {
                spinLock.lock();
                Assertions.assertTrue(spinLock.isLocked(), "after calling lock() method it has to be locked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                Assertions.assertTrue(spinLock.isLocked(), "after calling lock() method it has to be locked");
                spinLock.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // thread t1,t2 try to acquire lock, t1 first next t2
        // after t1 acquires lock t2 cannot acquire lock and its in waiting state
        t1.start();
        Thread.sleep(1000);
        Assertions.assertTrue(spinLock.isLocked(), "after calling lock() method it has to be locked");
        t2.start();
        Thread.sleep(1000);
        Assertions.assertEquals(Thread.State.WAITING, t2.getState());

    }

    @Test
    public void multiThreadedTestUnLock() throws InterruptedException{
        SpinLock spinLock = new SpinLock();
        Thread t1 = new Thread(() -> {
            try {
                spinLock.lock();
                Assertions.assertTrue(spinLock.isLocked(), "after calling lock() method it has to be locked");
                spinLock.unlock();
                Assertions.assertFalse(spinLock.isLocked(), "after unlock state has to be false");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                spinLock.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // thread t1,t2 try to acquire lock, t1 first next t2
        // after t1 acquires lock t2 cannot acquire lock and its in waiting state
        t1.start();
        Thread.sleep(1000);
        Assertions.assertFalse(spinLock.isLocked(), "after unlock state has to be false");
        t2.start();
        Thread.sleep(1000);

        Assertions.assertEquals(t2.getState(), Thread.State.TERMINATED);

    }


    @Test
    public void testSpuriousWakeup() throws InterruptedException {
        SpinLock spinLock = new SpinLock();
        Thread t1 = new Thread(() -> {
            try {
                spinLock.lock();
                Assertions.assertTrue(spinLock.isLocked(), "after calling lock() method it has to be locked");
                Thread.sleep(1000);
                spinLock.notifyAllThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                spinLock.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t3 = new Thread(() -> {
            try {
                spinLock.notifyAllThread();
                spinLock.lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(3000);
        Assertions.assertTrue(spinLock.isLocked(), "after calling lock() method it has to be locked");
        Assertions.assertEquals(Thread.State.WAITING, t3.getState());
        Assertions.assertEquals(Thread.State.WAITING, t2.getState());
    }


}
