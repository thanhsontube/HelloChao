package son.nt.hellochao.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by Sonnt on 12/8/15.
 */
public class CommonUtils {
    public static int[] getRandom (int number, int max) {
        final Random random = new Random();
        final Set<Integer> intSet = new HashSet<>();
        while (intSet.size() < number) {
            intSet.add(random.nextInt(max));
        }
        final int[] ints = new int[intSet.size()];
        final Iterator<Integer> iter = intSet.iterator();
        for (int i = 0; iter.hasNext(); ++i) {
            ints[i] = iter.next();
        }
        return ints;
    }
}
