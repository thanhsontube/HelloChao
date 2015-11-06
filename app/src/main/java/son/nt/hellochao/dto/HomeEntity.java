package son.nt.hellochao.dto;

import java.util.List;

import son.nt.hellochao.base.AObject;

/**
 * Created by Sonnt on 10/12/15.
 */
public class HomeEntity extends AObject {

    private String homeGroup;
    private String homeTitle;
    private String homeHref;
    private String homeQuizLink;
    private String homeMp3;
    private String homeDescription;
    private String homeImage;
    private List<String> listChats;

    public HomeEntity() {
    }

    public HomeEntity(String homeGroup, String homeTitle, String homeHref, String homeQuizLink) {

        this.homeGroup = homeGroup;
        this.homeTitle = homeTitle;
        this.homeHref = homeHref;
        this.homeQuizLink = homeQuizLink;
    }

    public String getHomeImage() {
        return homeImage;
    }

    public void setHomeImage(String homeImage) {
        this.homeImage = homeImage;
    }

    public String getHomeGroup() {
        return homeGroup;
    }

    public void setHomeGroup(String homeGroup) {
        this.homeGroup = homeGroup;
    }

    public String getHomeTitle() {
        return homeTitle;
    }

    public void setHomeTitle(String homeTitle) {
        this.homeTitle = homeTitle;
    }

    public String getHomeHref() {
        return homeHref;
    }

    public void setHomeHref(String homeHref) {
        this.homeHref = homeHref;
    }

    public String getHomeQuizLink() {
        return homeQuizLink;
    }

    public void setHomeQuizLink(String homeQuizLink) {
        this.homeQuizLink = homeQuizLink;
    }

    public String getHomeMp3() {
        return homeMp3;
    }

    public void setHomeMp3(String homeMp3) {
        this.homeMp3 = homeMp3;
    }

    public String getHomeDescription() {
        return homeDescription;
    }

    public void setHomeDescription(String homeDescription) {
        this.homeDescription = homeDescription;
    }

    public List<String> getListChats() {
        return listChats;
    }

    public void setListChats(List<String> listChats) {
        this.listChats = listChats;
    }
}
