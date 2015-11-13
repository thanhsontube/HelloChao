package son.nt.hellochao.retrofit.esl;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.utils.DatetimeUtils;
import son.nt.hellochao.utils.Logger;

/**
 * Created by Sonnt on 10/8/15.
 */
public class ESLApi {
    public final String TAG = getClass().getSimpleName();
    Retrofit retrofit;
    IESLInterfaceAPI ieslInterfaceAPI;

    public Retrofit getRetrofit() {
//        return new Retrofit.Builder().baseUrl(ResourceManager.getInstance().getMyPath().getESLRoot())
        return new Retrofit.Builder().baseUrl("http://www.esl-lab.com/")
                .addConverterFactory(GsonConverterFactory.create())

                .build();
    }

    public ESLApi() {
        retrofit = getRetrofit();
        ieslInterfaceAPI = retrofit.create(IESLInterfaceAPI.class);
    }

    public void getESLList() {
        Logger.debug(TAG, ">>>" + "-----getESLList");

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://www.esl-lab.com/index.html").build();
        okHttpClient.newCall(request).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.error(TAG, ">>>" + "getESLList FAIL:" + e.toString());
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                Logger.debug(TAG, ">>>" + "getESLList OK:" + response);

                try {
                    HtmlCleaner cleaner = new HtmlCleaner();
                    CleanerProperties props = cleaner.getProperties();
                    props.setAllowHtmlInsideAttributes(true);
                    props.setAllowMultiWordAttributes(true);
                    props.setRecognizeUnicodeChars(true);
                    props.setOmitComments(true);
                    Object[] data;

                    TagNode tagNode = cleaner.clean(response.body().byteStream());
                    String xpath = "//table[@border='1']";
                    data = tagNode.evaluateXPath(xpath);
                    Logger.debug(TAG, ">>>" + "data:" + data.length);

                    List<DailySpeakDto> list = new ArrayList<>();
                    DailySpeakDto dto;
                    if (data != null && data.length > 0) {
                        Logger.debug(TAG, ">>>" + "length:" + data
                                .length);
                        TagNode nodeA = (TagNode) data[1];
                        Logger.debug(TAG, ">>>" + "nodeA:" + nodeA.getChildTagList());
                        for (TagNode tag : nodeA.getChildTagList()) {
                            TagNode tagLink = tag.getChildTagList().get(0);
                            String sentenceEng = tagLink.getText().toString();
                            Logger.debug(TAG, ">>>" + "sentenceEng:" + sentenceEng);

                            String sentenceVi = tag.getText().toString();
                            sentenceVi = sentenceVi.substring(sentenceEng.length(), sentenceVi.length());

                            String link = tagLink.getChildTagList().get(0).getAttributeByName("href");
                            Logger.debug(TAG, ">>>" + "sentenceVi:" + sentenceVi);
                            Logger.debug(TAG, ">>>" + "link:" + link);

                            dto = new DailySpeakDto(link, sentenceEng, sentenceVi);

                            int[]arr = DatetimeUtils.getCurrentTime();
                            dto.setDay(arr[0]);
                            dto.setMonth(arr[1]);
                            dto.setYear(arr[2]);
                            list.add(dto);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
