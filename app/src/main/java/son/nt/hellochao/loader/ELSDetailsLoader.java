package son.nt.hellochao.loader;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;

import son.nt.hellochao.dto.ESLDetailsDto;
import son.nt.hellochao.utils.Logger;

/**
 * Created by Sonnt on 10/9/15.
 */
public abstract class ELSDetailsLoader extends ContentLoader<ESLDetailsDto> {
    public static final String TAG = "ELSHomeLoader";

    public ELSDetailsLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected ESLDetailsDto handleStream(InputStream in) throws IOException {
        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setAllowHtmlInsideAttributes(true);
            props.setAllowMultiWordAttributes(true);
            props.setRecognizeUnicodeChars(true);
            props.setOmitComments(true);
            Object[] data;

            TagNode tagNode = cleaner.clean(in);
            String xpath = "//div[@class='addthis_toolbox addthis_default_style addthis_16x16_style']";
            data = tagNode.evaluateXPath(xpath);
            Logger.debug(TAG, ">>>" + "length:" + data
                    .length);

            TagNode nodeA = (TagNode) data[0];
            Logger.debug(TAG, ">>>" + "nodeA:" + nodeA.getChildTagList());

            String question = getQuestion(nodeA);
            Logger.debug(TAG, ">>>" + "question:" + question);

            for (int i =0; i < nodeA.getChildTagList().size() ; i ++) {
                TagNode tag = nodeA.getChildTagList().get(i);
                Logger.debug(TAG, ">>>" + "i:" + i + ";tag:" + tag.getText());
            }


            return null;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getQuestion (TagNode tag) {
        Logger.debug(TAG, ">>>" + "getQuestion");

        return tag.getChildTagList().get(7).getText().toString();
    }

}
