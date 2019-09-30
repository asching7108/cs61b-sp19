import java.util.Random;

/**
 * Created by hug.
 * @author Hsingyi Lin (filled the methods)
 * date    09/29/2019
 */
public class ExperimentHelper {

    /** Returns the internal path length for an optimum binary search tree of
     *  size N. Examples:
     *  N = 1, OIPL: 0
     *  N = 2, OIPL: 1
     *  N = 3, OIPL: 2
     *  N = 4, OIPL: 4
     *  N = 5, OIPL: 6
     *  N = 6, OIPL: 8
     *  N = 7, OIPL: 10
     *  N = 8, OIPL: 13
     */
    public static int optimalIPL(int N) {
        int IPL = 0;
        for (int i = 1; i <= N; i++) {
            IPL += (int) (Math.log(i) / Math.log(2));
        }
        return IPL;
    }

    /** Returns the average depth for nodes in an optimal BST of
     *  size N.
     *  Examples:
     *  N = 1, OAD: 0
     *  N = 5, OAD: 1.2
     *  N = 8, OAD: 1.625
     * @return
     */
    public static double optimalAverageDepth(int N) {
        return optimalIPL(N) / (double) N;
    }

    /**
     * Inserts a random key into the BST.
     */
    public static BST addRandomKey(BST bst,int range) {
        Random seed = new Random();
        int key;
        do {
            key = seed.nextInt(range);
        } while (bst.contains(key));
        bst.add(key);
        return bst;
    }

    /* test
    public static void main (String arg[]) {
        System.out.println(optimalIPL(8));
        System.out.println(optimalAverageDepth(8));
    } */

}
