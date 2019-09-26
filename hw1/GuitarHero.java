/** A client that uses the synthesizer package to replicate a plucked guitar string sound
 * @Author Hsingyi Lin
 * data    09/25/2019
 */
import es.datastructur.synthesizer.GuitarString;

public class GuitarHero {
    private static final double CONCERT_A = 440.0;
    private static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);

    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        int len = keyboard.length();
        GuitarString[] strings = new GuitarString[len];
        for (int i = 0; i < len; i++) {
            strings[i] = new GuitarString(440 * Math.pow(2, (i - 24) / 12));
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (keyboard.indexOf(key) != -1) {
                    strings[keyboard.indexOf(key)].pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = 0;
            for (int i = 0; i < len; i++) {
                sample += strings[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < len; i++) {
                strings[i].tic();
            }
        }
    }
}

