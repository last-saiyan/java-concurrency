package barrier;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestFakeCountDownLatch {

    @Test
    public void singleThreadTest(){
        int threadCount = 10;
        FakeCountDownLatch latchSingleThread = new FakeCountDownLatch(threadCount);
        Assertions.assertEquals(threadCount, latchSingleThread.getCount());
        latchSingleThread.countDown();
        latchSingleThread.countDown();
        Assertions.assertEquals(threadCount-2, latchSingleThread.getCount());
    }

    @Test
    public void testCountDown() throws InterruptedException {
        int threadCount = 10,  diff = 4;

        FakeCountDownLatch latch = new FakeCountDownLatch(threadCount);
        CountDownLatch realLatch = new CountDownLatch(threadCount);

        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            service.submit(() -> {
                try {
                    int rand = new Random().nextInt();
                    if(rand < 0){
                        rand = rand*-1;
                    }
                    rand = rand%1000;
                    Thread.sleep(rand);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
                realLatch.countDown();
            });
        }
        realLatch.await();
        Assertions.assertEquals(0, latch.getCount());
    }


    @Test
    public void testAwait() throws InterruptedException {
        FakeCountDownLatch latch = new FakeCountDownLatch(3);
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
