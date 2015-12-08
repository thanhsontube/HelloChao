package son.nt.hellochao.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import son.nt.hellochao.utils.Logger;

/**
 * Created by Sonnt on 11/10/15.
 */
public class AutoGetDailyTestReceiver extends BroadcastReceiver {
    public static final String TAG = "AutoGetDailyTestReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.debug(TAG, ">>>" + "onReceive>>>>>>>>>>>>");

        //get data from daily hello chao
//        AppAPI.getInstance().getHcDaily();
    }
}
