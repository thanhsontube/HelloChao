package son.nt.hellochao.loader;

import android.content.Context;

import org.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.List;

import son.nt.hellochao.dto.RankDto;
import son.nt.hellochao.utils.Logger;

/**
 * Created by Sonnt on 10/9/15.
 */
public abstract class GunBoundLevelLoader extends HtmlCleanerBaseLoader<List<RankDto>> {
    public static final String TAG = "GunBoundLevelLoader";

    public GunBoundLevelLoader(Context context, String link) {
        super(context, link);
    }

    @Override
    protected List<RankDto> handleTagNode(TagNode tagNode, String link) {
        try {
            Logger.debug(TAG, ">>>" + "handleTagNode:" + link);
            List<RankDto> list = new ArrayList<>();
//            String xpath = "//td[@class='level_table_line']";
            String xpath = "//table[@class='level_table']/tbody/tr";
            Object[] objects = tagNode.evaluateXPath(xpath);

            if (objects == null || objects.length == 0) {
                Logger.error(TAG, ">>>" + "objects == null || objects.length == 0");
                return null;
            }
            Logger.debug(TAG, ">>>" + "objects:" + objects.length);
//            for (int i = 1; i < objects.length; i++) {
//                TagNode tagA = (TagNode) objects[i];
//                Logger.debug(TAG, ">>>" + "TAGA:" + tagA.getText());
//            }

            RankDto dto;

            List<String> listName = new ArrayList<>();
            List<String> listImage = new ArrayList<>();
            List<String> listValue = new ArrayList<>();

            for (int i = 1; i < objects.length; i++) {
                TagNode tagA = (TagNode) objects[i];
//                Logger.debug(TAG, ">>>" + "TAGA:" + tagA.getChildTagList().size());
                for (int j = 0; j < tagA.getChildTagList().size(); j++) {
                    TagNode tag = tagA.getChildTagList().get(j);

                    String image = null;
                    String name = null;
                    String value = "";
                    Logger.debug(TAG, ">>>" + "---------i:" + i);
                    if (tag.getChildTagList().size() > 0) {
                        TagNode tagChild = tag.getChildTagList().get(0);
                        image = tagChild.getAttributeByName("src");
                        name = tag.getText().toString().trim();
                        listImage.add(image);
                        listName.add(name);


                    } else {

                        value = tag.getText().toString().trim();
                        if (!value.contains("&nbsp")) {

                            listValue.add(value);
                        }
                    }
                }
            }

            for (int i = 0; i < listImage.size(); i++) {
                if (listName.get(i).contains("A Little Chick")) {
                    listValue.set(i, "0");
                }
                int no = listImage.size() - i;
                list.add(new RankDto(no, listName.get(i), listImage.get(i), Integer.valueOf(listValue.get(i).replace(",", "").replace("\"", ""))));
            }


            list.addAll(getSpecialLevel(tagNode));

            for (RankDto d : list) {
                Logger.debug(TAG, ">>>" + d.getRankNo() + ";" + d.getRankName() + ":" + d.getRankScoreStandard() + ";link:" + d.getRankIcon());
            }


            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<RankDto> getSpecialLevel(TagNode tagNode) {
        List<RankDto> list = new ArrayList<>();
        try {
            String xpath = "//div[@id='gameinfo_level']/ul/li";
            Object[] objects = tagNode.evaluateXPath(xpath);

            if (objects == null || objects.length == 0) {
                Logger.error(TAG, ">>>" + "objects == null || objects.length == 0");
            }
            Logger.debug(TAG, ">>>" + "objects:" + objects.length);
            String image = "";
            String name = "";
            RankDto dto;


            for (int i = 0; i < objects.length; i++) {
                TagNode tag = (TagNode) objects[i];
                if (tag.getChildTagList().size() > 0) {
                    TagNode tagChild = tag.getChildTagList().get(0);
                    image = tagChild.getAttributeByName("src");
                    name = tag.getText().toString().trim();
                    dto = new RankDto();
                    dto.setRankIcon(image);
                    dto.setRankName(name);
                    dto.setRankScoreStandard(-1);
                    if (name.contains("Administrator")) {
                        dto.setRankNo(0);
                    } else {
                        String s = image.substring(image.lastIndexOf("rank_") + 5, image.indexOf(".gif"));
                        Logger.debug(TAG, ">>>" + "s:" + s);
                        dto.setRankNo(Integer.valueOf(s));
                    }


                    list.add(dto);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
