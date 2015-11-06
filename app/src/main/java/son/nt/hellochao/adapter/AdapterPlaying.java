package son.nt.hellochao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import son.nt.hellochao.base.BasePagerAdapter;
import son.nt.hellochao.dto.HomeEntity;
import son.nt.hellochao.fragment.FullTextFragment;
import son.nt.hellochao.fragment.PlayingMidFragment;
import son.nt.hellochao.fragment.esl.EslListFragment;

/**
 * Created by Sonnt on 10/13/15.
 */
public class AdapterPlaying extends BasePagerAdapter {


    HomeEntity homeEntity;

    public AdapterPlaying(FragmentManager fragmentManager, HomeEntity homeEntity) {
        super(fragmentManager);
        this.homeEntity = homeEntity;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FullTextFragment.newInstance(homeEntity);
            case 1:
                return PlayingMidFragment.newInstance(homeEntity);
            case 2:
                return EslListFragment.newInstance("", "");
        }
        return PlayingMidFragment.newInstance(homeEntity);
    }

    @Override
    public boolean isFragmentReusable(Fragment f, int position) {

        return true;
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void updateData(HomeEntity homeEntity) {
        this.homeEntity = homeEntity;
    }
}
