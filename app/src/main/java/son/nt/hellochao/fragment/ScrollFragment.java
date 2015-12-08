package son.nt.hellochao.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import butterknife.Bind;
import son.nt.hellochao.R;
import son.nt.hellochao.activity.LoginActivity;
import son.nt.hellochao.activity.ProfileActivity;
import son.nt.hellochao.adapter.AdapterHot;
import son.nt.hellochao.adapter.AdapterListEsl;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.dto.HomeEntity;
import son.nt.hellochao.dto.HotEntity;
import son.nt.hellochao.dto.TopDto;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.interface_app.IHelloChao;
import son.nt.hellochao.parse_object.HelloChaoDaily;
import son.nt.hellochao.utils.IParse;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;
import son.nt.hellochao.widget.MovieDetailCardLayout;
import son.nt.hellochao.widget.ViewRowHcDaily;

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

    @Bind(R.id.home_pic)
    ImageView homePic;

    @Bind(R.id.daily_top)
    MovieDetailCardLayout dailyTop;

    @Bind(R.id.daily_practice)
    MovieDetailCardLayout dailyPractice;



    RecyclerView rcv;

    private List<HomeEntity> list = new ArrayList<>();
    private AdapterListEsl adapter;

    TextView txtDaily;
    AppAPI appAPI;


    public ScrollFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scroll, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        appAPI = new AppAPI(getContext());
        appAPI.setHcCallback(hcCallback);
        appAPI.getHcDaily();
//        AppAPI.getInstance().setHcCallback(hcCallback);
//        AppAPI.getInstance().getHcDaily();
        listHot.clear();

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

//        View pin1 = LayoutInflater.from(getContext()).inflate(R.layout.pin_1, null);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        rcv = (RecyclerView) pin1.findViewById(R.id.rcv_part_daily);
//        txtDaily = (TextView) pin1.findViewById(R.id.txt_part_daily);
//        rcv.setLayoutManager(linearLayoutManager);
//        rcv.hasFixedSize();
//        rcv.setAdapter(adapter);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        dailyTop.addView(pin1, 1, layoutParams);

    }

    @Override
    protected void initListener(View view) {
        homePic.setOnClickListener(this);
        dailyTop.setSeeMoreOnClickListener(this);
        dailyPractice.setSeeMoreOnClickListener(moreDailyPractice);
    }

    @Override
    public void onResume() {
        super.onResume();
        OttoBus.register(this);
        updateLayout();
    }

    @Override
    public void onPause() {
        super.onPause();
        OttoBus.unRegister(this);
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
//        Logger.debug(TAG, ">>>" + "onDoneGetHomeEntities:" + listData.get(0).getHomeGroup() + ";href:" + listData.get(0).getHomeTitle());
//        DataManager.getInstance().setHomeEntities(listData);
//        adapterHot.notifyDataSetChanged();
//
//        list.clear();
//        for (int i = 0; i < 3; i ++) {
//            list.add(listData.get(i));
//        }
//        adapter.notifyDataSetChanged();
//        Logger.debug(TAG, ">>>" + "list:" + list.size());
    }

    View.OnClickListener moreDailyPractice = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Logger.debug(TAG, ">>>" + "daily_practice click");
        }
    };

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
        }
    }



    private void processData (ArrayList<HelloChaoDaily> listDaiLy) {
        Logger.debug(TAG, ">>>" + "processData:" + listDaiLy.size());
        int []arr = getRandom(3, listDaiLy.size());
        for (int i = 0; i < arr.length; i ++) {
            ViewRowHcDaily viewRowHcDaily = new ViewRowHcDaily(getContext());
            viewRowHcDaily.setData(listDaiLy.get(arr[i]));
            dailyPractice.addView(viewRowHcDaily);
        }

    }

    private int[] getRandom (int number, int max) {
        final Random random = new Random();
        final Set<Integer> intSet = new HashSet<>();
        while (intSet.size() < number) {
            intSet.add(random.nextInt(max));
        }
        final int[] ints = new int[intSet.size()];
        final Iterator<Integer> iter = intSet.iterator();
        for (int i = 0; iter.hasNext(); ++i) {
            ints[i] = iter.next();
        }
        return ints;
    }

    IHelloChao.HcCallback hcCallback = new IHelloChao.HcCallback() {
        @Override
        public void throwUserTop(ArrayList<TopDto> listTop) {
            Logger.debug(TAG, ">>>" + "throwUserTop");

        }

        @Override
        public void throwDailySentences(ArrayList<HelloChaoDaily> listDaiLy) {
            Logger.debug(TAG, ">>>" + "throwDailySentences");
            processData(listDaiLy);

        }

        @Override
        public void throwAllSentences(ArrayList<DailySpeakDto> listDaiLy) {

        }
    };
}
