package son.nt.hellochao.parse_object;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Sonnt on 1/13/16.
 */
@ParseClassName("PointDto")
public class PointDto extends ParseObject {
    public void setPointName (String pointName){
        put ("name",pointName);
    }
    public void setPointValue (int value) {
        put ("value", value);
    }

    public String getPointName () {
        return getString("name");
    }

    public int getValue () {
        return getInt("value");
    }

    public String getId () {
        return getString("objectId");
    }
}
