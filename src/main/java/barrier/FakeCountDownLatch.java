package barrier;

public class FakeCountDownLatch {
    private int count;

    public FakeCountDownLatch(int count){
        this.count = count;
    }


    public synchronized void countDown(){
        count--;
        if (count == 0) {
            this.notifyAll();
        }
    }


    public int getCount(){
        return this.count;
    }


    public synchronized void await() throws InterruptedException {
        while (count > 0) {
            wait();
        }
    }

}
