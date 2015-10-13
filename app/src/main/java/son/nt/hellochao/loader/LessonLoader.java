package son.nt.hellochao.loader;

import android.content.Context;

import org.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.List;

import son.nt.hellochao.dto.IdiomEntity;
import son.nt.hellochao.dto.LessonEntity;
import son.nt.hellochao.utils.Logger;


/**
 * Created by Sonnt on 10/12/15.
 */
public abstract class LessonLoader extends BaseLoader<LessonEntity> {
    public final String TAG = LessonLoader.class.getSimpleName();
    public final String HOST = "http://www.esl-lab.com/";
    public static final int MAX = 3;

    public LessonLoader(Context context, String link) {
        super(context, link);
    }

    @Override
    protected LessonEntity handleTagNode(TagNode tagNode) {
        LessonEntity lessonEntity = new LessonEntity();
        try {
            TagNode tagDescription = tagNode.findElementByAttValue("name", "description", true, true);
            Logger.debug(TAG, ">>>" + "tagDescription:" + tagDescription.getAttributeByName("content"));

//            getIdioms(tagNode);
//            getAudioLink(tagNode);
//            getHelpTip(tagNode);
//            lessonEntity.setImage(getImage(tagNode));

            getLessonDescription(tagNode);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return lessonEntity;
    }

    public String cleanString (String in) {
        return in.trim().replace("\n","").replace("\r","");
    }



    private String getHelpTip (TagNode tagNode) {
        Logger.debug(TAG, ">>>" + "-----getHelpTip");
        try {
            String xPath = "//font[@size='2']";
            Object []data = tagNode.evaluateXPath(xPath);
            for (int i = 0; i < data.length; i ++) {
                String text = ((TagNode) data[i]).getText().toString().trim().replace("\n","").replace("\r","");
                if (text.contains("HELPFUL TIP")) {

                    Logger.debug(TAG, ">>>" + "data:"+ i +":" +text);
                    return text;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getImage (TagNode tagNode) {
        Logger.debug(TAG, ">>>" + "-----getImage");
        try {
            TagNode tagA = tagNode.findElementByAttValue("height", "126", true, true);
            String image = tagA.getAttributeByName("src").toString();
            String alt = tagA.getAttributeByName("alt");
            Logger.debug(TAG, ">>>" + "Img:" + image + "    ;alt:" + alt);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //not work
    private String getLessonDescription (TagNode tagNode) {
        Logger.debug(TAG, ">>>" + "-----getLessonDescription");
        try {
            String path = "//div[@class='addthis_toolbox addthis_default_style addthis_16x16_style']/p";
//            String path = "//td[@width='550']/p";
            Object[] object = tagNode.evaluateXPath(path);
            Logger.debug(TAG, ">>>" + "object:" + object.length);

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0 ; i < object.length ; i ++) {
                TagNode node = (TagNode) object[i];
                String text = cleanString(node.getText().toString());
                stringBuilder.append(text);
                stringBuilder.append("\n\r");
                if (text.contains("HELPFUL TIP")) {
                    Logger.debug(TAG, ">>>" + "Builder:" + stringBuilder.toString());
                    return stringBuilder.toString();
//                    Logger.debug(TAG, ">>>" + "Node:" + i + ":" + text);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getIdioms (TagNode tagNode) {
        List <IdiomEntity> list = new ArrayList<>();
        IdiomEntity idiomEntity;
        String key = null;
        String value = null;
        String sample = null;
        Logger.debug(TAG, ">>>" + "getIdioms");
        try {
            String idiomPath = "//td[@width='160'][@height='150'][@align='left']";

            Object[] objectIdiom = tagNode.evaluateXPath(idiomPath);
            TagNode tagA = (TagNode) objectIdiom[0];
//            Logger.debug(TAG, ">>>" + "***String notes:" + tagA.getText());

            if (tagA.getChildTagList().size() == 0) {

                key = tagA.getText().toString();
            } else {


                key = tagA.getText().toString();

                tagA = tagA.getChildTagList().get(0);
                value = tagA.getText().toString();
            }

            Logger.debug(TAG, ">>>" + "KEY:" + key + "\r\n;VALUE:" + value);


        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAudioLink (TagNode tagNode) {
        Logger.debug(TAG, ">>>" + "-----getAudioLink");
        try {
            String path = "//center/font[@size = '2']/a[@href]";
            Object[] object = tagNode.evaluateXPath(path);
            Logger.debug(TAG, ">>>" + "object:" + object.length);

            for (int i = 0 ; i < object.length ; i ++) {
                TagNode node = (TagNode) object[i];
                Logger.debug(TAG, ">>>" + "Node:" + i + ":" + node.getAttributeByName("href"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
