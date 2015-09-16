package son.nt.hellochao.otto;


import son.nt.hellochao.base.AObject;

/**
 * Created by Sonnt on 8/22/15.
 */
public class GoDownload extends AObject {
    int count;
    String heroID;
    String link;
    String voiceText;

    public GoDownload(int count, String heroID, String link, String text) {
        this.count = count;
        this.heroID = heroID;
        this.link = link;
        this.voiceText = text;
    }

    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getHeroID() {
        return heroID;
    }

    public void setHeroID(String heroID) {
        this.heroID = heroID;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
