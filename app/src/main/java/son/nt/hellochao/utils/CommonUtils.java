package son.nt.hellochao.utils;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
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

    public static void startVoiceRecognitionActivity(Fragment activity, String text, int REQUEST_CODE) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, text);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }
}
