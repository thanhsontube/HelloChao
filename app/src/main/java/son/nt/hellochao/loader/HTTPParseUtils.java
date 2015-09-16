package son.nt.hellochao.loader;

import android.content.Context;

import org.apache.http.client.methods.HttpGet;

import java.util.List;

import son.nt.hellochao.ResourceManager;
import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.loader.dailyTalk.DailyTalkLoader;
import son.nt.hellochao.otto.GoOnList;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;


/**
 * Created by Sonnt on 7/13/15.
 */
public class HTTPParseUtils {
    public static final String TAG = "HTTPParseUtils";

    static HTTPParseUtils INSTANCE = null;
    public Context context;
    int max = 0;
    int start = 0;

    public static void createInstance(Context context) {
        INSTANCE = new HTTPParseUtils(context);

    }

    public HTTPParseUtils(Context context) {
        this.context = context;
    }

    public static HTTPParseUtils getInstance() {
        return INSTANCE;
    }

    public void withHelloChao () {
        HttpGet httpGet = new HttpGet(ResourceManager.getInstance().getMyPath().getHelloChao());
        ResourceManager.getInstance().getContentManager().load(new DailyTalkLoader(httpGet, false) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "onContentLoaderStart");

            }

            @Override
            public void onContentLoaderSucceed(List<DailySpeakDto> entity) {
                Logger.debug(TAG, ">>>" + "onContentLoaderSucceed:" + entity.size());
                OttoBus.post(new GoOnList(entity));


            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + "onContentLoaderFailed:" + e.toString());

            }
        });

    }

//    public void withRoles() {
//        HttpGet httpGet = new HttpGet(RolesLoader.PATH_ROLES);
//        ResourceManager.getInstance().getContentManager().load(new RolesLoader(httpGet, false) {
//            @Override
//            public void onContentLoaderStart() {
//                Logger.debug(TAG, ">>>" + "onContentLoaderStart");
//            }
//
//            @Override
//            public void onContentLoaderSucceed(List<RoleDto> entity) {
//                Logger.debug(TAG, ">>>" + "onContentLoaderSucceed");
//            }
//
//            @Override
//            public void onContentLoaderFailed(Throwable e) {
//                Logger.error(TAG, ">>>" + "onContentLoaderFailed:" + e.toString());
//            }
//        });
//    }





}
