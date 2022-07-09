package deque;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import java.util.Comparator;

public class MaxArrayDequeTest {
    class Cmp implements Comparator<Integer>
    {
        @Override
        public int compare(Integer o1, Integer o2) {
            return Integer.compare(o1, o2);
        }
    }
    @Test
    public void maxTest(){
        MaxArrayDeque<Integer> x = new MaxArrayDeque<>();
        Cmp cmp = new Cmp();
        for(int i = 0; i < 10; i++)
            x.addFirst(i);
        x.addLast(100);
        assertEquals(100, (int) x.max(cmp));
    }
}
