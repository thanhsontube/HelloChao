package son.nt.hellochao;

import android.app.Application;

import com.apptimize.Apptimize;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import son.nt.hellochao.dto.DailyTopDto;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.loader.HTTPParseUtils;
import son.nt.hellochao.utils.Logger;

/**
 * Created by Sonnt on 9/14/15.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.start(this, BuildConfig.DEBUG);
        ResourceManager.createInstance(getApplicationContext());
        DataManager.createInstance(getApplicationContext());
        HTTPParseUtils.createInstance(getApplicationContext());

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "iGLwcZLU6J6t5IAbpB70RvpwxmM5zgBGh9iSlmyo", "9c9qi1jbDWMkwT8WFwzo2uURHAD8OaOoDsUwMSEu");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseFacebookUtils.initialize(getApplicationContext());

        parseRegisterSubClass();

        AppAPI.createInstance(getApplicationContext());

        /*apptimize*/
        Apptimize.setup(this, "D6uPHsHdkicGWh3ceWzAbaeuW2vK48d");

    }

    private void parseRegisterSubClass () {
        ParseObject.registerSubclass(DailyTopDto.class);
    }
}
