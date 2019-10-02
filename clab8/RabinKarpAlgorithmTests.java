import org.junit.Test;
import static org.junit.Assert.*;

public class RabinKarpAlgorithmTests {
    @Test
    public void basic() {
        String input = "hello";
        String pattern = "ell";
        assertEquals(1, RabinKarpAlgorithm.rabinKarp(input, pattern));
    }

    @Test
    public void moreTest() {
        String input = "abracadabra";
        String pattern = "ada";
        assertEquals(5, RabinKarpAlgorithm.rabinKarp(input, pattern));
    }

}