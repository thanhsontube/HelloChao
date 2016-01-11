package son.nt.hellochao.interface_app;

import java.util.ArrayList;
import java.util.List;

import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.dto.parse.DailyTopDto;
import son.nt.hellochao.parse_object.HelloChaoDaily;

/**
 * Created by Sonnt on 11/9/15.
 */
public interface IHelloChao {

    void helloChaoGetDailyQuestions();

    void getHcDaily();

    void getHcUserDailyTop();

    void hcSubmitTestResult(DailyTopDto helloChaoSubmitDto);

    void setHcCallback(HcCallback callback);

    void pushDailyQuestionsFromWebToParse (List <HelloChaoDaily> list);


    interface HcCallback {
        void throwUserTop(ArrayList<DailyTopDto> listTop);
        void throwDailySentences(ArrayList<HelloChaoDaily> listDaiLy);
        void throwAllSentences(ArrayList<DailySpeakDto> listDaiLy);
        void throwSubmitDaily (boolean isUpdate, String error);
    }
}
