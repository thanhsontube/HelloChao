package son.nt.hellochao.otto;

import son.nt.hellochao.base.AObject;
import son.nt.hellochao.dto.HotEntity;

/**
 * Created by Sonnt on 10/14/15.
 */
public class GoHot extends AObject {
    HotEntity hotEntity;

    public GoHot(HotEntity hotEntity) {
        this.hotEntity = hotEntity;
    }

    public HotEntity getHotEntity() {
        return hotEntity;
    }

    public void setHotEntity(HotEntity hotEntity) {
        this.hotEntity = hotEntity;
    }
}
