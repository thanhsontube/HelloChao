package son.nt.hellochao.dto;

import son.nt.hellochao.base.AObject;

/**
 * Created by Sonnt on 10/12/15.
 */
public class LessonEntity extends AObject {
    private String image;
    private String mp3Link;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMp3Link() {
        return mp3Link;
    }

    public void setMp3Link(String mp3Link) {
        this.mp3Link = mp3Link;
    }
}
