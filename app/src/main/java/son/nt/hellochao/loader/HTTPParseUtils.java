package son.nt.hellochao.loader;

import android.content.Context;

import org.apache.http.client.methods.HttpGet;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;

import java.net.URL;
import java.util.ArrayList;

import son.nt.hellochao.ResourceManager;
import son.nt.hellochao.dto.ESLDetailsDto;
import son.nt.hellochao.dto.ESLMenuDto;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.loader.dailyTalk.HcDailyLoader;
import son.nt.hellochao.parse_object.HelloChaoDaily;
import son.nt.hellochao.utils.Logger;


/**
 * Created by Sonnt on 7/13/15.
 */
public class HTTPParseUtils {
    public static final String TAG = "HTTPParseUtils";

    static HTTPParseUtils INSTANCE = null;
    public Context context;
    int max = 0;
    int start = 0;

    public static void createInstance(Context context) {
        INSTANCE = new HTTPParseUtils(context);

    }

    public HTTPParseUtils(Context context) {
        this.context = context;
    }

    public static HTTPParseUtils getInstance() {
        return INSTANCE;
    }

    public void withHcDaily() {
        String path = ResourceManager.getInstance().getMyPath().getHelloChao();
        Logger.debug(TAG, ">>>" + "withHcDaily path:" + path);
        HttpGet httpGet = new HttpGet(path);
        ResourceManager.getInstance().getContentManager().load(new HcDailyLoader(httpGet, false) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "withHcDaily onContentLoaderStart");

            }

            @Override
            public void onContentLoaderSucceed(ArrayList<HelloChaoDaily> entity) {
                Logger.debug(TAG, ">>>" + "withHcDaily onContentLoaderSucceed:" + entity.size());
                //push to server
                AppAPI.getInstance().pushDailyQuestionsFromWebToParse(entity);
//                OttoBus.post(new GoDaiLyTest(entity));

            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + " withHcDaily onContentLoaderFailed:" + e.toString());

            }
        });


    }



    public void withESL () {
        Logger.debug(TAG, ">>>" + "------------------withESL");
        HttpGet httpGet = new HttpGet(ResourceManager.getInstance().getMyPath().getESL());
        ResourceManager.getInstance().getContentManager().load(new ELSHomeLoader(httpGet, false) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "onContentLoaderStart");

            }

            @Override
            public void onContentLoaderSucceed(ESLMenuDto entity) {
                Logger.debug(TAG, ">>>" + "onContentLoaderSucceed:");


            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + "onContentLoaderFailed:" + e.toString());

            }
        });

    }


    public void withESLDetails (String path) {
        Logger.debug(TAG, ">>>" + "-------------------------withESLDetails:" + path);
        HttpGet httpGet = new HttpGet(ResourceManager.getInstance().getMyPath().getESLDetails(path));
        ResourceManager.getInstance().getContentManager().load(new ELSDetailsLoader(httpGet, false) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "onContentLoaderStart");

            }

            @Override
            public void onContentLoaderSucceed(ESLDetailsDto entity) {
                Logger.debug(TAG, ">>>" + "onContentLoaderSucceed:");


            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + "onContentLoaderFailed:" + e.toString());

            }
        });

    }

    public void withESLDetails2 () {
        try {
            CleanerProperties props = new CleanerProperties();

// set some properties to non-default values
            props.setTranslateSpecialEntities(true);
            props.setTransResCharsToNCR(true);
            props.setOmitComments(true);

// do parsing
            TagNode tagNode = new HtmlCleaner(props).clean(
                    new URL("http://esl-lab.com/dating/datingrd1.htm")
            );

//            TagNode tagDescription = tagNode.findElementByAttValue("name", "description", true, true);
//            Logger.debug(TAG, ">>>" + "tagDescription:" + tagDescription.getAttributeByName("content"));

            String idiomPath = "//td[@width='160'][@height='150'][@align='left']";

            Object[] objectIdiom = tagNode.evaluateXPath(idiomPath);

            for (int i = 0 ; i< objectIdiom.length; i++) {
                TagNode tagA = ((TagNode) objectIdiom[i]);
                Logger.debug(TAG, ">>>" + "TAG A:" + i + ":" + tagA.getText());

            }


            //find helptip

            TagNode nodeHelp = tagNode.findElementByAttValue("class","addthis_toolbox addthis_default_style addthis_16x16_style",true,true);
            Logger.debug(TAG, ">>>" + "nodeHelp:" + nodeHelp.getText());

            for (TagNode tag : nodeHelp.getChildTagList()) {
                Logger.debug(TAG, ">>>" + "-------");
                Logger.debug(TAG, ">>>" + "tag:" + tag.getText());
            }

            //table tip
            String pathTip = "//table[@width='468'][@border='1']/tbody/tr/td";
            Object []objectsTip = tagNode.evaluateXPath(pathTip);
            Logger.debug(TAG, ">>>" + "objectsTip:" + objectsTip.length);

            TagNode tag1 = (TagNode) objectsTip[0];
            TagNode tag2 = (TagNode) objectsTip[1];

            String tips = tag1.getText().toString();
            String src  = tag2.getChildTagList().get(0).getAttributeByName("src");
            Logger.debug(TAG, ">>>" + "tips:" + tips + ";src:" + src);


            getAudioLink(tagNode);





//            String pathHelp = "//div[@class='addthis_toolbox addthis_default_style addthis_16x16_style']/p";
//            Object []objectHelp = tagNode.evaluateXPath(pathHelp);
//            for (int i = 0 ; i< objectHelp.length; i++) {
//                TagNode tagA = ((TagNode) objectHelp[i]);
//                Logger.debug(TAG, ">>>" + "TAG HELP:" + i + ":" + tagA.getText());
//
//            }


//            TagNode tagA = ((TagNode) objectIdiom[0]).getChildTagList().get(0);
//            Logger.debug(TAG, ">>>" + "nodeIdiom:" + tagA.getText());
//
//            TagNode tagB = tagA.getChildTagList().get(0);
//            Logger.debug(TAG, ">>>" + "nodeIdiomB:" + tagB.getText());


// serialize to xml file
//            new PrettyXmlSerializer(props).writeToFile(
//                    tagNode, ResourceManager.getInstance().folderAudio + File.separator + "dating.xml", "utf-8"
//            );
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

//    public void withRoles() {
//        HttpGet httpGet = new HttpGet(RolesLoader.PATH_ROLES);
//        ResourceManager.getInstance().getContentManager().load(new RolesLoader(httpGet, false) {
//            @Override
//            public void onContentLoaderStart() {
//                Logger.debug(TAG, ">>>" + "onContentLoaderStart");
//            }
//
//            @Override
//            public void onContentLoaderSucceed(List<RoleDto> entity) {
//                Logger.debug(TAG, ">>>" + "onContentLoaderSucceed");
//            }
//
//            @Override
//            public void onContentLoaderFailed(Throwable e) {
//                Logger.error(TAG, ">>>" + "onContentLoaderFailed:" + e.toString());
//            }
//        });
//    }

    public void withSavePages (String page, String path) {
        try {
            CleanerProperties props = new CleanerProperties();

// set some properties to non-default values
            props.setTranslateSpecialEntities(true);
            props.setTransResCharsToNCR(true);
            props.setOmitComments(true);

// do parsing
            TagNode tagNode = new HtmlCleaner(props).clean(
                    new URL(page)
            );

            // serialize to xml file
            new PrettyXmlSerializer(props).writeToFile(
                    tagNode, path, "utf-8"
            );


        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    //get level by gunbound
    public void withGunBoundLevel () {


    }





}
