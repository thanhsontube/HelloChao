package son.nt.hellochao.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import butterknife.ButterKnife;
import son.nt.hellochao.R;
import son.nt.hellochao.base.AFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayingMidFragment extends AFragment implements View.OnTouchListener, SpringListener{
    private static double TENSION = 40;
    private static double DAMPER = 3; //friction

    ImageView mImageToAnimate;
    private SpringSystem mSpringSystem;
    private Spring mSpring;


    public PlayingMidFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_playing_mid, container, false);
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
}
