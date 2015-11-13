package son.nt.hellochao.utils;

import android.text.TextUtils;

/**
 * Created by Sonnt on 11/14/15.
 */
public class StringUtils {
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
