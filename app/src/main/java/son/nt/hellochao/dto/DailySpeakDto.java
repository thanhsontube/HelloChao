package son.nt.hellochao.dto;

import son.nt.hellochao.base.AObject;

/**
 * Created by Sonnt on 9/14/15.
 */
public class DailySpeakDto extends AObject {
    private String linkMp3;
    private String sentenceEng;
    protected String sentenceVi;

    private int day;
    private int month;
    private int year;

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

    public DailySpeakDto() {
    }

    public DailySpeakDto(String linkMp3, String sentenceEng, String sentenceVi) {
        this.linkMp3 = linkMp3;
        this.sentenceEng = sentenceEng;
        this.sentenceVi = sentenceVi;
    }

    public String getLinkMp3() {
        return linkMp3;
    }

    public void setLinkMp3(String linkMp3) {
        this.linkMp3 = linkMp3;
    }

    public String getSentenceEng() {
        return sentenceEng;
    }

    public void setSentenceEng(String sentenceEng) {
        this.sentenceEng = sentenceEng;
    }

    public String getSentenceVi() {
        return sentenceVi;
    }

    public void setSentenceVi(String sentenceVi) {
        this.sentenceVi = sentenceVi;
    }
}
