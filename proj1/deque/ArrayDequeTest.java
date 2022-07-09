package deque;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {

    @Test
    public void addTest(){
        ArrayDeque<String> a = new ArrayDeque<>();
        assertEquals(true, a.isEmpyt());
        a.addFirst("a");
        a.addLast("b");
        a.addLast("c");
        assertEquals(3, a.size());
        a.printDeque();

    }

    @Test
    public void resizeTest(){
        ArrayDeque<Integer> x = new ArrayDeque<>();
        for(int i = 1; i < 200; i++){
            x.addLast(i);
        }
        for(int i = 0; i < 200; i++)
            x.addFirst(-i);
        x.printDeque();
        for(int i = -199; i < 200; i++)
            assertEquals(i, (int)x.removeFirst());
    }

    @Test
    public void foreachTest(){
        ArrayDeque<Integer> x = new ArrayDeque<>();
        for(int i = 1; i < 200; i++){
            x.addLast(i);
        }
        for(int i = 0; i < 200; i++)
            x.addFirst(-i);
        x.printDeque();
        int i = -199;
        for(int t : x){
            assertEquals(t, i);
            i += 1;
        }
    }

    @Test
    public void equalTest(){
        ArrayDeque<Integer> x = new ArrayDeque<>();
        ArrayDeque<Integer> y = new ArrayDeque<>();
        ArrayDeque<Integer> z = null;
        ArrayDeque<String> a = new ArrayDeque<>();
        ArrayDeque<String> b = new ArrayDeque<>();
        a.addLast("x");

        for(int i = 1; i < 200; i++){
            x.addLast(i);
            y.addLast(i);
        }
        assertEquals(true, x.equals(x));
        assertEquals(true, x.equals(y));
        assertEquals(true, y.equals(x));
        assertEquals(false, x.equals(z));
        assertEquals(false, a.equals(z));
        assertEquals(false, y.equals(a));
        assertEquals(false, a.equals(y));
        assertEquals(false, b.equals(x));
        assertEquals(false, x.equals(b));

    }
}
