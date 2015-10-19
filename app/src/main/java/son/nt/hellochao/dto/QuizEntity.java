package son.nt.hellochao.dto;

import son.nt.hellochao.base.AObject;

/**
 * Created by Sonnt on 10/12/15.
 */
public class QuizEntity extends AObject {
    private String fullText;
    private String quizLink;

    public String getQuizLink() {
        return quizLink;
    }

    public void setQuizLink(String quizLink) {
        this.quizLink = quizLink;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }
}
