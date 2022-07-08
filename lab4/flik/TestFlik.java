package flik;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestFlik {
    @Test
    public void testEqual(){
        int a = 129;
        int b = 129;
        boolean expect = true;

        assertEquals(expect, Flik.isSameNumber(a, b));
    }

}
