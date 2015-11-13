package son.nt.hellochao.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import son.nt.hellochao.R;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.HomeEntity;
import son.nt.hellochao.utils.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayingMidFragment extends AFragment implements View.OnTouchListener, SpringListener {
    public final String TAG = getClass().getSimpleName();
    private static double TENSION = 400;
    private static double DAMPER = 30; //friction

    ImageView mImageToAnimate;
    private SpringSystem mSpringSystem;
    private Spring mSpring;
    HomeEntity homeEntity;

    TextView txtTitle;
    TextView txtDes;
    TextView txtLevel;


    public PlayingMidFragment() {
        // Required empty public constructor
    }

    public static PlayingMidFragment newInstance(HomeEntity entity) {
        PlayingMidFragment f = new PlayingMidFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", entity);
        f.setArguments(bundle);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            homeEntity = (HomeEntity) getArguments().getSerializable("data");
        }
        Logger.debug(TAG, ">>>" + "homeEntity:" + homeEntity.getHomeDescription());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playing_mid, container, false);
        txtTitle = (TextView) view.findViewById(R.id.mid_txt_title);
        txtDes = (TextView) view.findViewById(R.id.mid_txt_description);
        txtLevel = (TextView) view.findViewById(R.id.mid_txt_level);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initLayout(View view) {
        mImageToAnimate = (ImageView) view.findViewById(R.id.mid_image);
        mImageToAnimate.setOnTouchListener(this);

        mSpringSystem = SpringSystem.create();

        mSpring = mSpringSystem.createSpring();
        mSpring.addListener(this);

        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        mSpring.setSpringConfig(config);

    }

    @Override
    protected void initListener(View view) {

    }

    @Override
    protected void updateLayout() {
        Logger.debug(TAG, ">>>" + "MID updateLayout:" + homeEntity.getHomeImage());
        Glide.with(this).load(homeEntity.getHomeImage()).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageToAnimate);
        txtDes.setText(homeEntity.getHomeDescription());
        txtTitle.setText(homeEntity.getHomeTitle());
        txtLevel.setText(homeEntity.getHomeGroup());


    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSpring.setEndValue(1f);
                return true;
            case MotionEvent.ACTION_UP:
                mSpring.setEndValue(0f);
                return true;
        }

        return false;
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        float scale = 1f - (value * 0.5f);
        mImageToAnimate.setScaleX(scale);
        mImageToAnimate.setScaleY(scale);
    }

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }

    public void setData(HomeEntity data) {
        this.homeEntity = data;
        updateLayout();
    }
}
