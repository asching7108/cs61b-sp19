public class HorribleSteve {
    public static void main(String [] args) throws Exception {
        int i = 0;
        for (int j = 0; i < 500; ++i, ++j) {
            if (!Flik.isSameNumber(i, j)) {
                throw new Exception(
                        String.format("i:%d not same as j:%d ??", i, j));
            }
        }
        System.out.println("i is " + i);
    }
}

/* Find the bug:
 * the isSameNumber method in Flik uses parameters by reference,
 * thus causes an error when the addresses of arguments are different.
 * Simply change the parameters type from Integer to int.
 */
