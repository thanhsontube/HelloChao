package son.nt.hellochao.dto.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Sonnt on 11/9/15.
 */
@ParseClassName("DailyTopDto")
public class DailyTopDto extends ParseObject {

    public Date getSubmitTime() {
        return getDate("submitTime");
    }

    public void setSubmitTime(Date submitTime) {
        put("submitTime", submitTime);
    }


    public int getCorrectSentence() {
        return getInt("correctSentence");
    }

    public void setCorrectSentence(int correctSentence) {
        put("correctSentence", correctSentence);
    }

    public int getTotalSeconds() {
        return getInt("totalSeconds");
    }

    public void setTotalSeconds(int totalSeconds) {
        this.put("totalSeconds", totalSeconds);
    }

    public String getRelativeTime() {
        return getString("relativeTime");
    }

    public void setRelativeTime(String relativeTime) {
        put("relativeTime", relativeTime);
    }

    public ParseUser getParseUser() {
        return getParseUser("user");
    }

    public void setParseUser(ParseUser parseUser) {
        put("user", parseUser);
    }

    public static ParseQuery<DailyTopDto> getQuery () {
        return ParseQuery.getQuery(DailyTopDto.class);
    }
}
