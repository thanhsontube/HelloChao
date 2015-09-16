package son.nt.hellochao.loader;

import android.content.Context;

/**
 * Created by Sonnt on 12/25/2014.
 */
public class MyPath {
    private Context context;
    private String baseUrl;

    public MyPath(Context context) {
        this.context = context;
    }

    public String getHelloChao () {
        return "http://hellochao.vn/thu-thach-trong-ngay/";
    }

}
