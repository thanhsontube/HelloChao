package son.nt.hellochao.parse_object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import son.nt.hellochao.base.AObject;
import son.nt.hellochao.utils.DatetimeUtils;

/**
 * Created by Sonnt on 12/6/15.
 */
public class HelloChaoDaily extends AObject {
    private String audio;
    private String text;
    private String translate;

//    private List<Integer> day;
//    private List<Integer> month;
//    private List<Integer> year;

    private int day;
    private int month;
    private int year;

    private List<String> tags = new ArrayList<>();
    private String sID;
    private int value = 5;
    private int level = 1;

    public HelloChaoDaily() {
        tags.add("hello chao");
        tags.add("daily");
    }

    public HelloChaoDaily(String audio, String text, String translate, int day, int month, int year) {
        this.audio = audio;
        this.text = text;
        this.translate = translate;
        this.day = day;
        this.month = month;
        this.year = year;

        tags.add("hello chao");
        tags.add("daily");
    }

    public HelloChaoDaily(String audio, String text, String translate, int level, int value) {
        this.audio = audio;
        this.text = text;
        this.translate = translate;
        this.level = level;
        this.value = value;
        int []arr = DatetimeUtils.getCurrentTime();
        day = arr[0];
        month = arr[1];
        year = arr[2];
    }

    public List<String> getDates() {
        StringBuilder dates = new StringBuilder();
        dates.append(day < 10 ? "0" + day : String.valueOf(day));
        dates.append("_");
        dates.append(month < 10 ? "0" + month : String.valueOf(month));
        dates.append("_");
        dates.append(String.valueOf(year));

        return Arrays.asList(dates.toString());
    }



    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getsID() {
        StringBuilder id = new StringBuilder();
        id.append("hcDaily_");
        id.append(day < 10 ? "0" + day : String.valueOf(day));
        id.append("_");
        id.append(month < 10 ? "0" + month : String.valueOf(month));
        id.append("_");
        id.append(String.valueOf(year));

        return id.toString();
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
