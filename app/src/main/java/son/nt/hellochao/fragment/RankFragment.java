package son.nt.hellochao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import son.nt.hellochao.R;
import son.nt.hellochao.adapter.RankAdapter;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.RankDto;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.interface_app.IRank;
import son.nt.hellochao.utils.Logger;

/**
 * Created by Sonnt on 1/13/16.
 */
public class RankFragment extends AFragment {

    public static final String TAG = "RankFragment";

    @Bind(R.id.rank_rcv)
    RecyclerView recyclerView;
    RankAdapter adapter;
    List<RankDto> list = new ArrayList<>();

    AppAPI appAPI;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.debug(TAG, ">>>" + "onCreate");
        super.onCreate(savedInstanceState);
        appAPI = new AppAPI(getContext());
        appAPI.setRankCallback(new IRank.Callback() {
            @Override
            public void onRankSystem(List<RankDto> l) {
                Logger.debug(TAG, ">>>" + "onRankSystem:" + list.size());
                list.clear();
                list.addAll(l);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initLayout(View view) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new RankAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initListener(View view) {

    }

    @Override
    protected void updateLayout() {
        appAPI.getRankSystem();

    }
}
