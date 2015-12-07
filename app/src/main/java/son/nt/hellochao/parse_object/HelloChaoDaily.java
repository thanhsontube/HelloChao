package son.nt.hellochao.parse_object;

import java.util.List;

import son.nt.hellochao.base.AObject;

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

    private List<String> tags;
    private String sID;
    private int value;
    private int level;

    public HelloChaoDaily() {
    }

    public HelloChaoDaily(String audio, String text, String translate, int day, int month, int year) {
        this.audio = audio;
        this.text = text;
        this.translate = translate;
        this.day = day;
        this.month = month;
        this.year = year;
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
        return sID;
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
