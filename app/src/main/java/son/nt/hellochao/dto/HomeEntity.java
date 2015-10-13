package son.nt.hellochao.dto;

import son.nt.hellochao.base.AObject;

/**
 * Created by Sonnt on 10/12/15.
 */
public class HomeEntity extends AObject {

    private String homeGroup;
    private String homeTitle;
    private String homeHref;
    private String homeQuizLink;

    public HomeEntity() {
    }

    public HomeEntity(String homeGroup, String homeTitle, String homeHref, String homeQuizLink) {

        this.homeGroup = homeGroup;
        this.homeTitle = homeTitle;
        this.homeHref = homeHref;
        this.homeQuizLink = homeQuizLink;
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
}
