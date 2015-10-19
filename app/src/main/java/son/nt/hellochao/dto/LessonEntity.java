package son.nt.hellochao.dto;

import son.nt.hellochao.base.AObject;

/**
 * Created by Sonnt on 10/12/15.
 */
public class LessonEntity extends AObject {
    private String hrefLink;
    private String title;
    private String image;
    private String mp3Link;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHrefLink() {
        return hrefLink;
    }

    public void setHrefLink(String hrefLink) {
        this.hrefLink = hrefLink;
    }
}
