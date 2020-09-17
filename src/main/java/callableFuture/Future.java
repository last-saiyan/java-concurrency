package callableFuture;

public class Future<V> {
    private boolean completed;
    private V value;

    public synchronized V getValue() throws InterruptedException {
        while (!completed){
            wait();
        }
        return value;
    }

    protected synchronized void completed(V value){
        completed = true;
        this.value = value;
        notifyAll();
    }
}
