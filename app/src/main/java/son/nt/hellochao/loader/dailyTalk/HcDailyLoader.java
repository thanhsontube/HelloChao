package son.nt.hellochao.loader.dailyTalk;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import son.nt.hellochao.loader.ContentLoader;
import son.nt.hellochao.parse_object.HelloChaoDaily;
import son.nt.hellochao.utils.DatetimeUtils;
import son.nt.hellochao.utils.Logger;

/**
 * Created by Sonnt on 9/14/15.
 */
public abstract class HcDailyLoader extends ContentLoader<List<HelloChaoDaily>> {
    public static final String TAG = "DailyTalkLoader";

    public HcDailyLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected List<HelloChaoDaily> handleStream(InputStream in) throws IOException {
        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setAllowHtmlInsideAttributes(true);
            props.setAllowMultiWordAttributes(true);
            props.setRecognizeUnicodeChars(true);
            props.setOmitComments(true);
            Object[] data;

            TagNode tagNode = cleaner.clean(in);
            String xpath = "//ul[@class='raw-menu']";
            data = tagNode.evaluateXPath(xpath);

            List<HelloChaoDaily> list = new ArrayList<>();
            HelloChaoDaily dto;
            if (data != null && data.length > 0) {
                Logger.debug(TAG, ">>>" + "length:" + data
                        .length);
                TagNode nodeA = (TagNode) data[1];
                Logger.debug(TAG, ">>>" + "nodeA:" + nodeA.getChildTagList());
                for (TagNode tag : nodeA.getChildTagList()) {
                    TagNode tagLink = tag.getChildTagList().get(0);
                    String text = tagLink.getText().toString();

                    String translation = tag.getText().toString();
                    translation = translation.substring(text.length(), translation.length());

                    String audio = tagLink.getChildTagList().get(0).getAttributeByName("href");
                    int []arr = DatetimeUtils.getCurrentTime();
                    dto = new HelloChaoDaily(audio, text, translation, arr[0], arr[1], arr[2]);
                    list.add(dto);
                }
            }
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
