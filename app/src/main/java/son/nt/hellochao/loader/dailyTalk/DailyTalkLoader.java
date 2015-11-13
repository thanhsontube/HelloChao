package son.nt.hellochao.loader.dailyTalk;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.loader.ContentLoader;
import son.nt.hellochao.utils.DatetimeUtils;
import son.nt.hellochao.utils.Logger;

/**
 * Created by Sonnt on 9/14/15.
 */
public abstract class DailyTalkLoader extends ContentLoader<List<DailySpeakDto>> {
    public static final String TAG = "DailyTalkLoader";

    public DailyTalkLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected List<DailySpeakDto> handleStream(InputStream in) throws IOException {
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
                    int []arr = DatetimeUtils.getCurrentTime();
                    dto.setDay(arr[0]);
                    dto.setMonth(arr[1]);
                    dto.setYear(arr[2]);
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
