package son.nt.hellochao.loader.dailyTalk;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import son.nt.hellochao.loader.ContentLoader;
import son.nt.hellochao.parse_object.HelloChaoDaily;

/**
 * Created by Sonnt on 9/14/15.
 */
public abstract class HcDailyLoader extends ContentLoader<ArrayList<HelloChaoDaily>> {
    public static final String TAG = "HcDailyLoader";

    public HcDailyLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected ArrayList<HelloChaoDaily> handleStream(InputStream in) throws IOException {
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

            ArrayList<HelloChaoDaily> list = new ArrayList<>();
            HelloChaoDaily dto;
            if (data != null && data.length > 0) {
                TagNode nodeA = (TagNode) data[1];
                for (TagNode tag : nodeA.getChildTagList()) {
                    TagNode tagLink = tag.getChildTagList().get(0);
                    String text = tagLink.getText().toString();

                    String translation = tag.getText().toString();
                    translation = translation.substring(text.length(), translation.length());

                    String audio = tagLink.getChildTagList().get(0).getAttributeByName("href");
                    dto = new HelloChaoDaily(audio, text, translation, 5, 1);
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
