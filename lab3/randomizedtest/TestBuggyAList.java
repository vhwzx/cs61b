package randomizedtest;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> a = new AListNoResizing<Integer>();
        BuggyAList<Integer> b = new BuggyAList<Integer>();
        a.addLast(4);
        b.addLast(4);
        a.addLast(5);
        b.addLast(5);
        a.addLast(6);
        b.addLast(6);
        for(int i = 0; i < 3; i += 1){
            assertEquals(a.size(), b.size());
            assertEquals(a.removeLast(), b.removeLast());
        }
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            assertEquals(L.size(), B.size());
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int sizeb = B.size();
                assertEquals(size, sizeb);
            } else if (operationNumber == 2 && L.size() > 0){
                //getLast
                int last = L.getLast();
                int lastb = B.getLast();
                assertEquals(last, lastb);

            } else if (operationNumber == 3 && L.size() > 0){
                assertEquals(L.removeLast(), B.removeLast());

            }
        }

    }
}
