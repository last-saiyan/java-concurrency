package barrier;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestCountDownLatch {

    @Test
    public void testCountDown() throws InterruptedException {
        int threadCount = 10,  diff = 4;
        CountDownLatch latchSingleThread = new CountDownLatch(threadCount);
        Assertions.assertEquals(threadCount, latchSingleThread.getCount());
        latchSingleThread.countDown();
        latchSingleThread.countDown();
        Assertions.assertEquals(threadCount-2, latchSingleThread.getCount());


        CountDownLatch latch = new CountDownLatch(threadCount);
        java.util.concurrent.CountDownLatch realLatch = new
                java.util.concurrent.CountDownLatch(threadCount - diff);
        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            service.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
                realLatch.countDown();
                System.out.println(latch.getCount() + " - count " + threadCount);
            });
        }
        realLatch.await();
        latch.await();
        Assertions.assertEquals(diff, latch.getCount());
    }


    @Test
    public void testAwait() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
        t3.start();

        latch.await();
        Assertions.assertEquals(0, latch.getCount());
        Assertions.assertEquals(Thread.State.TERMINATED, t1.getState());
        Assertions.assertEquals(Thread.State.TERMINATED, t2.getState());
        Assertions.assertEquals(Thread.State.TERMINATED, t3.getState());

    }


}
