package son.nt.hellochao.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apptimize.Apptimize;
import com.apptimize.ApptimizeTest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import son.nt.hellochao.R;
import son.nt.hellochao.ResourceManager;
import son.nt.hellochao.activity.LoginActivity;
import son.nt.hellochao.activity.ProfileActivity;
import son.nt.hellochao.adapter.AdapterHot;
import son.nt.hellochao.adapter.AdapterListEsl;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.dto.DailyTopDto;
import son.nt.hellochao.dto.HomeEntity;
import son.nt.hellochao.dto.HotEntity;
import son.nt.hellochao.dto.MusicItem;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.interface_app.IHelloChao;
import son.nt.hellochao.parse_object.HelloChaoDaily;
import son.nt.hellochao.service.MusicPlayback;
import son.nt.hellochao.service.MusicService;
import son.nt.hellochao.utils.CommonUtils;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;
import son.nt.hellochao.widget.MovieDetailCardLayout;
import son.nt.hellochao.widget.ViewRowHcDaily;
import son.nt.hellochao.widget.ViewRowTopDaily;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScrollFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ScrollFragment extends AFragment implements View.OnClickListener {

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

    @Bind(R.id.smooth_bar)
    SmoothProgressBar smoothProgressBar;


    private List<HomeEntity> list = new ArrayList<>();
    private AdapterListEsl adapter;

    AppAPI appAPI;

    MusicService musicService;

    ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalBinder localBinder = (MusicService.LocalBinder) iBinder;
            musicService = localBinder.getService();
            musicService.setMusicPlayBackCallback(musicPlayBackCallback);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


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
        Apptimize.runTest("Avatar click", new ApptimizeTest() {
            @Override
            public void baseline() {
// Variant: original
            }

            @SuppressWarnings("unused")
            public void variation1() {
// Variant: new Arvatar
            }
        });

        getAActivity().bindService(MusicService.getService(getContext()), musicServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getAActivity().unbindService(musicServiceConnection);
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

        void onPracticeFull ();
    }


    @Override
    protected void initData() {
        appAPI = new AppAPI(getContext());
        appAPI.setHcCallback(hcCallback);
        appAPI.getHcDaily();
        appAPI.getHcUserDailyTop();
        listHot.clear();

    }

    @Override
    protected void initLayout(View view) {
        pagerHot = (ViewPager) view.findViewById(R.id.scroll_pager);
        adapterHot = new AdapterHot(getFragmentManager(), getContext());
        pagerHot.setHorizontalScrollBarEnabled(true);
        pagerHot.setAdapter(adapterHot);
        adapter = new AdapterListEsl(getAActivity(), list);
    }

    @Override
    protected void initListener(View view) {
        homePic.setOnClickListener(this);
        dailyTop.setSeeMoreOnClickListener(null);
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

        smoothProgressBar.setVisibility(View.GONE);


        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser != null && !TextUtils.isEmpty(parseUser.getString("avatar"))) {
            Glide.with(this).load(parseUser.getString("avatar")).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(homePic);
        } else {
            Glide.with(this).load(R.drawable.ic_no_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(homePic);
        }
    }

    View.OnClickListener moreDailyPractice = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Logger.debug(TAG, ">>>" + "daily_practice click");
            if (mListener != null) {
                mListener.onPracticeFull();
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_pic:
                Apptimize.runTest("Avatar click", new ApptimizeTest() {
                    @Override
                    public void baseline() {
                        Logger.debug(TAG, ">>>" + "CLICK BASE");
                        Apptimize.track("Avatar Click", 1.0);
// Variant: original
                    }

                    @SuppressWarnings("unused")
                    public void variation1() {
                        Logger.debug(TAG, ">>>" + "CLICK VARIANT1");
                        Apptimize.track("Avatar Click", 1.0);
// Variant: new Arvatar
                    }
                });
                ParseUser parseUser = ParseUser.getCurrentUser();
                if (parseUser == null) {
                    startActivity(new Intent(getAActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getAActivity(), ProfileActivity.class));
                }
                break;
        }
    }


    private void processData(ArrayList<HelloChaoDaily> listDaiLy) {
        Logger.debug(TAG, ">>>" + "processData:" + listDaiLy.size());
        int[] arr = CommonUtils.getRandom(3, listDaiLy.size());
        ViewRowHcDaily viewRowHcDaily;
        for (int i = 0; i < arr.length; i++) {
            viewRowHcDaily = new ViewRowHcDaily(getContext());
            viewRowHcDaily.setData(listDaiLy.get(arr[i]));
            viewRowHcDaily.setOnViewRowHcDailyClickCallback(onViewRowHcDailyClick);
            dailyPractice.addView(viewRowHcDaily);
        }

    }

    private void processTopData(ArrayList<DailyTopDto> list) {
        if (list.isEmpty()) {
            return;
        }
        Logger.debug(TAG, ">>>" + "processTopData:" + list.size());
        ViewRowTopDaily view;
        int max = list.size() >= 3 ? 3 : list.size();
        for (int i = 0; i < max; i++) {
            view = new ViewRowTopDaily(getContext());
            view.setData(list.get(i));
            dailyTop.addView(view);
        }

    }

    ViewRowHcDaily.OnViewRowHcDailyClick onViewRowHcDailyClick = new ViewRowHcDaily.OnViewRowHcDailyClick() {
        @Override
        public void onClick(HelloChaoDaily data) {
            if (musicService == null) {
                return;
            }
            musicService.processAddRequest(data);

        }
    };

    IHelloChao.HcCallback hcCallback = new IHelloChao.HcCallback() {
        @Override
        public void throwUserTop(ArrayList<DailyTopDto> listTop) {
            Logger.debug(TAG, ">>>" + "throwUserTop:" + listTop.size());
            if(!listTop.isEmpty()) {
                ResourceManager.getInstance().setListTops(listTop);
            }
            processTopData (listTop);

        }

        @Override
        public void throwDailySentences(ArrayList<HelloChaoDaily> listDaiLy) {
            if (!listDaiLy.isEmpty()) {
                ResourceManager.getInstance().setListHelloChaoDaily(listDaiLy);
            }
            processData(listDaiLy);


        }

        @Override
        public void throwAllSentences(ArrayList<DailySpeakDto> listDaiLy) {

        }

        @Override
        public void throwSubmitDaily(boolean isUpdate, String error) {

        }
    };

    MusicPlayback.Callback musicPlayBackCallback = new MusicPlayback.Callback() {
        @Override
        public void onPreparing(MusicItem musicItem) {
            if (!isSafe() || smoothProgressBar == null) {
                return;
            }
            smoothProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPlaying(MusicItem musicItem) {
            if (!isSafe() || smoothProgressBar == null) {
                return;
            }
            smoothProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onCompletion() {

        }

        @Override
        public void onPlaybackStatusChanged(int state) {

        }

        @Override
        public void onError(MusicItem musicItem, String error) {

        }

        @Override
        public void onCurrentPosition(long currentTime) {

        }

        @Override
        public void onDuration(long duration) {

        }
    };


}
