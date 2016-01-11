package son.nt.hellochao.otto;

import java.util.ArrayList;

import son.nt.hellochao.base.AObject;
import son.nt.hellochao.parse_object.HelloChaoDaily;

/**
 * Created by Sonnt on 12/22/15.
 */
public class GoUpdateDaily extends AObject {
    public ArrayList<HelloChaoDaily> list;

    public GoUpdateDaily(ArrayList<HelloChaoDaily> list) {
        this.list = list;
    }
}
