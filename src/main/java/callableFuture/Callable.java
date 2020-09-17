
package callableFuture;

public abstract class Callable<V> extends Thread{
    Future future;
    private V value;

    @Override
    public void run(){
        try {
            value = call();
            completed(value);
        } catch (Exception e) {
            System.out.println("Exception in call method");
            e.printStackTrace();
        }
    }

    protected abstract V call();


    public Future<V> execute(){
        this.start();
        this.future = new Future();
        return future;
    }

    private void completed(V value){
        future.completed(value);
    }
}
