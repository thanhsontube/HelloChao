package son.nt.hellochao.dto;

import java.util.ArrayList;
import java.util.List;

import son.nt.hellochao.base.AObject;

/**
 * Created by Sonnt on 10/12/15.
 */
public class HomeObject extends AObject {
    private List<HomeEntity> listHomeEntity = new ArrayList<>();

    public HomeObject() {
    }

    public HomeObject(List<HomeEntity> listHomeEntity) {

        this.listHomeEntity = listHomeEntity;
    }

    public List<HomeEntity> getListHomeEntity() {
        return listHomeEntity;
    }

    public void setListHomeEntity(List<HomeEntity> listHomeEntity) {
        this.listHomeEntity = listHomeEntity;
    }
}
