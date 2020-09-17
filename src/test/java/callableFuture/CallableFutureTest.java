package callableFuture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CallableFutureTest {

    @Test
    public void testGetValue() throws InterruptedException {
        int value = 100;
        CallableTest callableTest = new CallableTest(value);
        Future<Integer> callableInt = callableTest.execute();

        Assertions.assertEquals(value, callableInt.getValue());
    }
}

class CallableTest extends Callable<Integer> {
    private int value;
    public CallableTest(int value){
        this.value = value;
    }

    @Override
    protected Integer call() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value;
    }
}