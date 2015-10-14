package son.nt.hellochao.dto;

import son.nt.hellochao.base.AObject;

/**
 * Created by Sonnt on 10/13/15.
 */
public class HotEntity extends AObject {

    private HomeEntity homeEntity;

    public HotEntity() {
    }

    public HotEntity(HomeEntity homeEntity) {
        this.homeEntity = homeEntity;
    }

    public HomeEntity getHomeEntity() {
        return homeEntity;
    }

    public void setHomeEntity(HomeEntity homeEntity) {
        this.homeEntity = homeEntity;
    }
}
