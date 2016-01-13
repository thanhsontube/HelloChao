package son.nt.hellochao.interface_app;

import java.util.List;

import son.nt.hellochao.dto.RankDto;

/**
 * Created by Sonnt on 11/14/15.
 */
public interface IRank {

    void getRankSystem();

    void setRankCallback (IRank.Callback callback);

    interface Callback {

        void onRankSystem(List<RankDto> list);

    }

}
