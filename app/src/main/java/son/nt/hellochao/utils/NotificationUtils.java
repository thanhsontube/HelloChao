package son.nt.hellochao.utils;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import son.nt.hellochao.R;
import son.nt.hellochao.dto.MusicItem;

/**
 * Created by Sonnt on 12/10/15.
 */
public class NotificationUtils {
    public static void show(Context context, MusicItem musicItem, int ID) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentText("content Text").setSmallIcon(R.drawable.com_facebook_button_icon);
        builder.setContentTitle("content title");

        notificationManagerCompat.notify(ID, builder.build());

    }
}
