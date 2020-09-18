package dataStructures;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class HashmapTest {

    @Test
    public void singleThread(){

        Hashmap<Integer, Integer> map = new Hashmap();
        map.insert(1,2);
        map.insert(2,32);
        map.insert(3,5);
        map.insert(7,53);
        Assertions.assertEquals(2, map.get(1));
        Assertions.assertEquals(32, map.get(2));
        Assertions.assertEquals(null, map.get(234));
        map.delete(2);
        Assertions.assertEquals(null, map.get(2));
    }
}