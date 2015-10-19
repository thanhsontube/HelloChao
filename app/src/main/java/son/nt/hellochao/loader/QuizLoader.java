package son.nt.hellochao.loader;

import android.content.Context;
import android.text.TextUtils;

import org.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.List;

import son.nt.hellochao.dto.QuizEntity;
import son.nt.hellochao.utils.Logger;


/**
 * Created by Sonnt on 10/12/15.
 */
public abstract class QuizLoader extends BaseLoader<QuizEntity> {
    public final String TAG = QuizLoader.class.getSimpleName();
    public final String HOST = "http://www.esl-lab.com/";
    public static final int MAX = 3;

    public QuizLoader(Context context, String link) {
        super(context, link);
    }

    @Override
    protected QuizEntity handleTagNode(TagNode tagNode, String link) {
        QuizEntity quizEntity = new QuizEntity();
        try {
            quizEntity.setQuizLink(link);
            quizEntity.setFullText(getCompleteText(tagNode));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return quizEntity;
    }

    private String getCompleteText (TagNode tagNode) {
//        Logger.debug(TAG, ">>>" + "getCompleteText");
        List <String> list = new ArrayList<>();
        try {
            String xPath = "//table[@cellspacing='2'][@cellpadding='6']/tbody/tr/td";
            Object []data = tagNode.evaluateXPath(xPath);
            if (data.length == 0) {
                xPath = "//table[@cellspacing='2'][@cellpadding='1']/tbody/tr/td";
                data = tagNode.evaluateXPath(xPath);
            }
            Logger.debug(TAG, ">>>" + "data:" + data.length);
            TagNode tagA = (TagNode) data[0];
            String fullText = cleanString(tagA.getText().toString());
//            Logger.debug(TAG, ">>>" + "Tag A text:" + fullText +";size:" + tagA.getChildTagList().size());
            if (tagA.getChildTagList().size() == 0) {
                return fullText;
            }
            String second = null;
            for (TagNode tag : tagA.getChildTagList()) {
                String text = cleanString(tag.getText().toString());
                if (!TextUtils.isEmpty(text) && text.contains(":")) {
                    if (second == null) {
                        second = text;
                    }
                    list.add(text);
//                    Logger.debug(TAG, ">>>" + "tag:" + text);
                } else if (!TextUtils.isEmpty(text)) {
                    return fullText;
                }


            }
            //get 1st
            if (!TextUtils.isEmpty(second)) {
                String first = fullText.substring(0, fullText.indexOf(second));
//                Logger.debug(TAG, ">>>" + "FIRST:" + first);

                list.add(0, first);
            }


            StringBuilder sb = new StringBuilder();
            for (String s : list) {
                sb.append(s).append("\n\r");
            }
            return sb.toString();




        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }



    public String cleanString (String in) {
        return in.trim().replace("\n","").replace("\r","");
    }




}
