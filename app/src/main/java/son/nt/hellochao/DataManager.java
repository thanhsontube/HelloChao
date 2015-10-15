package son.nt.hellochao;

import android.content.Context;

import java.util.List;

import son.nt.hellochao.dto.HomeEntity;

/**
 * Created by Sonnt on 10/15/15.
 */
public class DataManager {
    static DataManager INSTANCE = null;
    private Context context;

    private List<HomeEntity> homeEntities;

    public static void createInstance (Context context) {
        INSTANCE = new DataManager(context);

    }

    public static DataManager getInstance() {
        return INSTANCE;
    }

    public DataManager (Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<HomeEntity> getHomeEntities() {
        return homeEntities;
    }

    public void setHomeEntities(List<HomeEntity> homeEntities) {
        this.homeEntities = homeEntities;
    }
}
