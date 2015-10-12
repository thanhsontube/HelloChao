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

    public String getESLRoot () {
        return "http://www.esl-lab.com/";
    }

    public String getHelloChao () {
        return "http://hellochao.vn/thu-thach-trong-ngay/";
    }

    public String getESL () {
        return "http://www.esl-lab.com/index.htm";
    }

    public String getESLDetails (String path) {
        return "http://www.esl-lab.com/" + path;
    }

    public String getESLDetailsTest1 () {
        return "dating/datingrd1.htm";
    }

}
