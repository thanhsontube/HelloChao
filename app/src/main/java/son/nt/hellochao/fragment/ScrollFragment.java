package son.nt.hellochao.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import son.nt.hellochao.DataManager;
import son.nt.hellochao.R;
import son.nt.hellochao.activity.LoginActivity;
import son.nt.hellochao.activity.ProfileActivity;
import son.nt.hellochao.adapter.AdapterHot;
import son.nt.hellochao.adapter.AdapterListEsl;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.HomeEntity;
import son.nt.hellochao.dto.HotEntity;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.loader.LoaderManager;
import son.nt.hellochao.utils.IParse;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.TsParse;
import son.nt.hellochao.widget.MovieDetailCardLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScrollFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ScrollFragment extends AFragment implements IParse.Callback, View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    public final String TAG = getClass().getSimpleName();

    ViewPager pagerHot;
    AdapterHot adapterHot;
    List<HotEntity> listHot = new ArrayList<>();

    LoaderManager manager;

    TsParse tsParse;

    @Bind(R.id.home_pic)
    ImageView homePic;

    @Bind(R.id.daily_top)
    MovieDetailCardLayout dailyTop;

    RecyclerView rcv;

    private List<HomeEntity> list = new ArrayList<>();
    private AdapterListEsl adapter;

    TextView txtDaily;




    public ScrollFragment() {
        // Required empty public constructor
        manager = new LoaderManager();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scroll, container, false);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    protected void initData() {


        listHot.clear();
        AppAPI.getInstance().hcDaiLy();

    }

    @Override
    protected void initLayout(View view) {
        pagerHot = (ViewPager) view.findViewById(R.id.scroll_pager);
        adapterHot = new AdapterHot(getFragmentManager(), getContext());
        pagerHot.setHorizontalScrollBarEnabled(true);
        pagerHot.setAdapter(adapterHot);
        pagerHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "qwertyuio", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new AdapterListEsl(getAActivity(), list);

        View pin1 = LayoutInflater.from(getContext()).inflate(R.layout.pin_1, null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv = (RecyclerView) pin1.findViewById(R.id.rcv_part_daily);
        txtDaily = (TextView) pin1.findViewById(R.id.txt_part_daily);
        rcv.setLayoutManager(linearLayoutManager);
        rcv.hasFixedSize();
        rcv.setAdapter(adapter);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dailyTop.addView(pin1, 1, layoutParams);
        dailyTop.setSeeMoreOnClickListener(this);
    }

    @Override
    protected void initListener(View view) {
        tsParse = new TsParse();
        tsParse.settsParseCallback(this);
        tsParse.getHomeEntities();
        homePic.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLayout();
    }

    @Override
    protected void updateLayout() {


        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser != null && !TextUtils.isEmpty(parseUser.getString("avatar"))) {
            Glide.with(this).load(parseUser.getString("avatar")).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(homePic);
        } else {
            Glide.with(this).load(R.drawable.ic_no_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(homePic);
        }
    }

    @Override
    public void onDoneGetHomeEntities(List<HomeEntity> listData) {
        if (listData == null || listData.size() == 0) {
            return;
        }
        Logger.debug(TAG, ">>>" + "onDoneGetHomeEntities:" + listData.get(0).getHomeGroup() + ";href:" + listData.get(0).getHomeTitle());
        DataManager.getInstance().setHomeEntities(listData);
        adapterHot.notifyDataSetChanged();

        list.clear();
        for (int i = 0; i < 3; i ++) {
            list.add(listData.get(i));
        }
//        list.addAll(listData);
        adapter.notifyDataSetChanged();
        Logger.debug(TAG, ">>>" + "list:" + list.size());
        txtDaily.setText(getString(R.string.large_text));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_pic:
                ParseUser parseUser = ParseUser.getCurrentUser();
                if (parseUser == null) {
                    startActivity(new Intent(getAActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getAActivity(), ProfileActivity.class));
                }
                break;
            case R.id.daily_top:
                Toast.makeText(getActivity(), "OK SEE MORE DAILY", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
