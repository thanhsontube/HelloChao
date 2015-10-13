package son.nt.hellochao.loader;

import android.content.Context;
import android.text.TextUtils;

import org.htmlcleaner.TagNode;

import java.util.List;

import son.nt.hellochao.dto.HomeEntity;
import son.nt.hellochao.dto.HomeObject;
import son.nt.hellochao.utils.Logger;


/**
 * Created by Sonnt on 10/12/15.
 */
public abstract class HomeLoader extends BaseLoader<HomeObject> {
    public final String TAG = HomeLoader.class.getSimpleName();
    public final String HOST = "http://www.esl-lab.com/";
    public static final int MAX = 3;

    public HomeLoader(Context context, String link) {
        super(context, link);
    }

    @Override
    protected HomeObject handleTagNode(TagNode tagNode) {
        HomeObject homeObject = new HomeObject();
        try {
            String xPath = "//tr[@valign='Top']/td[@width='185'][@bgcolor='#FFFFCC']";
            Object[] data = tagNode.evaluateXPath(xPath);
            Logger.debug(TAG, ">>>" + "data:" + data.length);
            if (data == null || data.length == 0) {
                return null;
            }

            HomeEntity homeEntity;

            for (int i = 0; i < MAX; i++) {
                TagNode nodeB = (TagNode) data[i];
                //different have P tag before, remove this tag
                if (i == 2) {
                    nodeB = nodeB.getChildTagList().get(0);
                }
                String group = nodeB.getChildTagList().get(0).getText().toString();
                List<TagNode> listNodeC = nodeB.getChildTagList().get(1).getChildTagList();
                for (int j = 0; j < listNodeC.size(); j++) {
                    TagNode tag = listNodeC.get(j);
                    String title = tag.getText().toString();
                    String href = tag.getAttributeByName("href");
                    if (!TextUtils.isEmpty(title.trim()) && !TextUtils.isEmpty(href.trim())) {
                        href = HOST + href;
                        String quiz = href.replace("rd1.htm", "sc1.htm");
                        homeEntity = new HomeEntity(group, title, href, quiz);
                        homeObject.getListHomeEntity().add(homeEntity);

                        Logger.debug(TAG, ">>>" + "group:" + group + ";title:" + title + ";href:" + href);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return homeObject;
    }
}
