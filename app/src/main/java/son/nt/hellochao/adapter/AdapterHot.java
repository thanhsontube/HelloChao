package son.nt.hellochao.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import son.nt.hellochao.DataManager;
import son.nt.hellochao.base.BasePagerAdapter;
import son.nt.hellochao.dto.HomeEntity;
import son.nt.hellochao.dto.HotEntity;
import son.nt.hellochao.fragment.HotFragment;
import son.nt.hellochao.fragment.intro.IntroTopDayFragment;

/**
 * Created by Sonnt on 10/13/15.
 */
public class AdapterHot extends BasePagerAdapter {

    public static final int INDEX_DAILY_TEST = 0;
    public static final int INDEX_PAGE_EASY = 1;
    public static final int INDEX_PAGE_MEDIUM = 2;
    public static final int INDEX_PAGE_HARD = 3;
    public static final int INDEX_OTHER = 4;
    public static final int COUNT = 4;

    Context context;
    private Fragment mPrimaryFragment;


    public AdapterHot(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        this.mPrimaryFragment = (Fragment) object;
    }

    public Fragment getCurrentFragment () {
        return mPrimaryFragment;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f;

        switch (position) {
            case INDEX_DAILY_TEST:
                f = IntroTopDayFragment.newInstance("", "");
                return f;
            case INDEX_PAGE_EASY:
                f = HotFragment.newInstance(new HotEntity(getRandom(DataManager.getInstance().getHomeEntities(), 0)));
            case INDEX_PAGE_MEDIUM:
                f = HotFragment.newInstance(new HotEntity(getRandom(DataManager.getInstance().getHomeEntities(), 1)));
            case INDEX_PAGE_HARD:
                f = HotFragment.newInstance(new HotEntity(getRandom(DataManager.getInstance().getHomeEntities(), 2)));
                return f;

        }
        return null;
    }

    @Override
    public boolean isFragmentReusable(Fragment f, int position) {
        return true;
    }

    @Override
    public int getCount() {
        return 4;
    }

    private HomeEntity getRandom (List<HomeEntity> list, int type) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<HomeEntity> list1 = new ArrayList<>();
        int pos;
        switch (type) {

            case 0:
                list1.clear();
                for (HomeEntity dto : list) {
                    if (dto.getHomeGroup().equalsIgnoreCase("Easy")) {
                        list1.add(dto);
                    }
                }
                pos = new Random().nextInt(list1.size() -1);
                return list1.get(pos);
            case 1:
                list1.clear();
                for (HomeEntity dto : list) {
                    if (dto.getHomeGroup().equalsIgnoreCase("Medium")) {
                        list1.add(dto);
                    }


                }
                pos = new Random().nextInt(list1.size() - 1);
                return list1.get(pos);
            case 2:
                list1.clear();
                for (HomeEntity dto : list) {
                    if (dto.getHomeGroup().equalsIgnoreCase("Difficult")) {
                        list1.add(dto);
                    }
                }
                pos = new Random().nextInt(list1.size() - 1);
                return list1.get(pos);
        }
        return null;
    }
}
