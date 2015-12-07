package son.nt.hellochao.interface_app;

import java.util.ArrayList;
import java.util.List;

import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.dto.HelloChaoSubmitDto;
import son.nt.hellochao.dto.TopDto;

/**
 * Created by Sonnt on 11/9/15.
 */
public interface IHelloChao {

    void helloChaoGetDailyQuestions();

    void hcDaiLy();

    void helloChaoGetUserTop();

    void helloChaoSubmitTest(HelloChaoSubmitDto helloChaoSubmitDto);

    void setHelloCHaoCallback(HelloChaoCallback callback);

    void pushDailyQuestionsFromWebToParse (List <DailySpeakDto> list);


    interface HelloChaoCallback {
        void helloChaoGetListUserTop(ArrayList<TopDto> listTop);
        void helloChaoGetDailyQuestions(ArrayList<DailySpeakDto> listDaiLy);
        void helloChaoGetAllQuestions(ArrayList<DailySpeakDto> listDaiLy);
    }
}
