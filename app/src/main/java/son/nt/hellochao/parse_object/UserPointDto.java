package son.nt.hellochao.parse_object;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Sonnt on 1/13/16.
 */
@ParseClassName("UserPointDto")
public class UserPointDto extends ParseObject {
    public ParseUser getParseUser() {
        return getParseUser("user");
    }

    public void setParseUser(ParseUser parseUser) {
        put("user", parseUser);
    }

    public void setType (int type) {
        put("type", type);
    }
    public int getType () {
        return getInt("type");
    }

    public void setPoint (PointDto dto) {
        put("point", dto);
    }
    public PointDto getPoint () {
        return (PointDto) getParseObject("point");
    }

    public void setHelloChaoDaily(HelloChaoDaily helloChaoDaily) {
        put("hcDaily", helloChaoDaily);
    }

    public ParseObject getHelloChaoDaily(HelloChaoDaily helloChaoDaily) {
        return getParseObject("hcDaily");
    }
    public void setAdditionPoint (PointDto dto) {
        put("addition", dto);
    }
    public PointDto getAdditionPoint () {
        return (PointDto) getParseObject("addition");
    }


}
