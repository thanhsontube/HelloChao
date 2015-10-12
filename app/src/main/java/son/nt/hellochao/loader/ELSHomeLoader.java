package son.nt.hellochao.loader;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import son.nt.hellochao.dto.ESLMenuDto;
import son.nt.hellochao.utils.Logger;

/**
 * Created by Sonnt on 10/9/15.
 */
public abstract class ELSHomeLoader extends ContentLoader<ESLMenuDto> {
    public static final String TAG = "ELSHomeLoader";

    public ELSHomeLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected ESLMenuDto handleStream(InputStream in) throws IOException {
        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setAllowHtmlInsideAttributes(true);
            props.setAllowMultiWordAttributes(true);
            props.setRecognizeUnicodeChars(true);
            props.setOmitComments(true);
            Object[] data;

            TagNode tagNode = cleaner.clean(in);
//            String xpath = "//table[@border='1'][@cellspacing='0'][@cellpadding='3'][@bordercolor='#8000ff']/tbody";
//            String xpath = "//tr[@valign='Top']/td[@width='185']/font[@size='2']";
            String xpath = "//tr[@valign='Top']/td[@width='185']";
//            String xpath = "//tr[@valign='Top']";
            data = tagNode.evaluateXPath(xpath);
            Logger.debug(TAG, ">>>" + "length:" + data
                    .length);

            for (int i = 0; i < data.length; i++) {
                TagNode tagA = (TagNode) data[i];
                List<TagNode> listA = tagA.getChildTagList();
                Logger.debug(TAG, ">>>" + "----------------i:" + i + ";tagA:" + listA);
                if (listA.size() == 2) {
                    TagNode nodeGroup = listA.get(0);
                    TagNode nodeList = listA.get(1);

                    List<TagNode> listTitle = nodeList.getChildTagList();

                    Logger.debug(TAG, ">>>" + "nodeGroup:" + nodeGroup.getText() + ";nodeList:" + nodeList.getChildTagList());

                    for (TagNode tag : listTitle) {
                        String title = tag.getText().toString();
                        String href = tag.getAttributeByName("href");
                        Logger.debug(TAG, ">>>" + "title:" + title + ";href:" + href);

                    }
                }


                if (listA.size() == 1) {
                    TagNode nodeGroup = listA.get(0).getChildTagList().get(0);
                    TagNode nodeList = listA.get(0).getChildTagList().get(1);
                    List<TagNode> listTitle = nodeList.getChildTagList();

                    Logger.debug(TAG, ">>>" + "nodeGroup:" + nodeGroup.getText() + ";nodeList:" + nodeList.getChildTagList());

                    for (TagNode tag : listTitle) {
                        String title = tag.getText().toString();
                        String href = tag.getAttributeByName("href");
                        Logger.debug(TAG, ">>>" + "title:" + title + ";href:" + href);
                    }
                }
            }

            return null;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
