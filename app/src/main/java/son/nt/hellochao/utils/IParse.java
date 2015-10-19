package son.nt.hellochao.utils;

import java.util.List;

import son.nt.hellochao.dto.HomeEntity;

/**
 * Created by Sonnt on 10/16/15.
 */
public interface  IParse {
    interface Callback {
        void onDoneGetHomeEntities (List<HomeEntity> listData);
    }

    void settsParseCallback (Callback callBack);
}
