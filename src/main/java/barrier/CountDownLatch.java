package barrier;

public class CountDownLatch {
    private int count;
    public CountDownLatch(int count){
        this.count = count;
    }
    public synchronized void countDown(){
        count--;
        notifyAll();
    }

    public synchronized int getCount(){return this.count;}

    public synchronized void await() throws InterruptedException {
        while (count > 0) {
            wait();
        }
    }

}
