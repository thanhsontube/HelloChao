package son.nt.hellochao;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
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
    public HomeEntity getNULLTitle () {
        for (HomeEntity d : homeEntities) {
            if (TextUtils.isEmpty(d.getHomeTitle())) {
                return d;
            }
        }
        return null;
    }

    public List<HomeEntity> getNULLMp3() {
        List<HomeEntity> list = new ArrayList<>();
        for (HomeEntity d : homeEntities) {
            if (TextUtils.isEmpty(d.getHomeMp3())) {
                list.add(d);
            }
        }
        return list;
    }


    public HomeEntity getDataByHref(String href) {
        for (HomeEntity d : homeEntities) {
            if (d == null || d.getHomeTitle() == null || href == null) {
                Log.v("ui", ">>>" + "getDataByHref NULL:" + d.getHomeTitle());
            }
            if (d.getHomeHref().equals(href)) {
                return d;
            }
        }
        return null;
    }

    public HomeEntity getDataByQuiz(String quiz) {
        Log.v("ui", ">>>" + "getDataByQuiz:" + quiz);
        for (HomeEntity d : homeEntities) {
            if (d == null || d.getHomeTitle() == null || quiz == null) {
                Log.v("ui", ">>>" + "getDataByHref NULL:" + d.getHomeTitle());
            }
            if (quiz.equals(d.getHomeQuizLink())) {
                Log.v("UI", ">>>" + "ddd:" + d.getHomeTitle());
                return d;
            }
        }
        return null;
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
