package son.nt.hellochao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

import son.nt.hellochao.base.BasePagerAdapter;
import son.nt.hellochao.dto.HotEntity;
import son.nt.hellochao.fragment.FullTextFragment;
import son.nt.hellochao.fragment.PlayingMidFragment;

/**
 * Created by Sonnt on 10/13/15.
 */
public class AdapterPlaying extends BasePagerAdapter {

    List<HotEntity> list;

    public AdapterPlaying(FragmentManager fragmentManager, List<HotEntity> list) {
        super(fragmentManager);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FullTextFragment.newInstance(list.get(position).getHomeEntity());
            case 1:
                return new PlayingMidFragment();
            case 2:
                return new PlayingMidFragment();
        }
        return new PlayingMidFragment();
    }

    @Override
    public boolean isFragmentReusable(Fragment f, int position) {

        return false;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
