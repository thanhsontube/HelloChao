package son.nt.hellochao.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import son.nt.hellochao.R;
import son.nt.hellochao.adapter.AdapterPlaying;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.HotEntity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayingFragment extends AFragment {

    ViewPager pager;
    AdapterPlaying adapter;
    List<HotEntity> list = new ArrayList<>();


    public PlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playing, container, false);
        ButterKnife.bind(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
        list.add(new HotEntity());
        list.add(new HotEntity());
        list.add(new HotEntity());

    }

    @Override
    protected void initLayout(View view) {
        pager = (ViewPager) view.findViewById(R.id.playing_pager);
        adapter = new AdapterPlaying(getFragmentManager(), list);
        pager.setHorizontalScrollBarEnabled(true);
        pager.setAdapter(adapter);

    }

    @Override
    protected void initListener(View view) {

    }

    @Override
    protected void updateLayout() {

    }
}
