package son.nt.hellochao.otto;

import java.util.List;

import son.nt.hellochao.base.AObject;
import son.nt.hellochao.dto.DailySpeakDto;

/**
 * Created by Sonnt on 9/14/15.
 */
public class GoOnList extends AObject {
    public List<DailySpeakDto> list;

    public GoOnList(List<DailySpeakDto> list) {
        this.list = list;
    }
}
