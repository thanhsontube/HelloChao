package son.nt.hellochao.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.loader.HTTPParseUtils;
import son.nt.hellochao.utils.DatetimeUtils;
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

        int[]arr = DatetimeUtils.getCurrentTime();
        //check first

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(DailySpeakDto.class.getSimpleName());
        query.whereEqualTo("day", arr[0]);
        query.whereEqualTo("month", arr[1]);
        query.whereEqualTo("year", arr[2]);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                Logger.debug(TAG, ">>>" + "DONE receiver:" + e);
                if (e != null || list == null || list.size() != 10) {
                    HTTPParseUtils.getInstance().withHelloChaoDailyTest();
                }

            }
        });
    }
}
