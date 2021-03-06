package son.nt.hellochao.utils;

import android.content.Context;
import android.util.Log;

/**
 * Created by Sonnt on 4/26/15.
 */
public final class Logger {
    public static boolean DEBUG = false;

    private Logger() {
        // enforcing singleton
        super();
    }
    public static void start(final Context context, final boolean debug)
    {
        Logger.DEBUG = debug;
    }

    /**
     * Convenience method.
     */
    public static void debug(final String tag, final String msg) {
        if (Logger.DEBUG) {
            Log.d(tag, "" + msg);
        }
    }

    /**
     * Convenience method.
     */
    public static void info(final String tag, final String msg) {
        if (Logger.DEBUG) {
            Log.i(tag, "" + msg);
        }
    }

    /**
     * Convenience method.
     */
    public static void warn(final String tag, final String msg) {
        if (Logger.DEBUG) {
            Log.w(tag, "" + msg);
        }

    }

    /**
     * Convenience method.
     */
    public static void error(final String tag, final String msg) {
        if (Logger.DEBUG) {
            Log.e(tag, "" + msg);
        }

    }

    public static void logException(final Throwable throwable) {
        if (Logger.DEBUG) {
            throwable.printStackTrace();
        }

    }

}
