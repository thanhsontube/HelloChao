package son.nt.hellochao;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

import son.nt.hellochao.loader.HTTPParseUtils;

/**
 * Created by Sonnt on 9/14/15.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ResourceManager.createInstance(getApplicationContext());
        HTTPParseUtils.createInstance(getApplicationContext());

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "iGLwcZLU6J6t5IAbpB70RvpwxmM5zgBGh9iSlmyo", "9c9qi1jbDWMkwT8WFwzo2uURHAD8OaOoDsUwMSEu");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
