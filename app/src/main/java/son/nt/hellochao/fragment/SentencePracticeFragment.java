package son.nt.hellochao.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import son.nt.hellochao.R;
import son.nt.hellochao.base.AMusicServiceFragment;
import son.nt.hellochao.dto.MusicItem;
import son.nt.hellochao.parse_object.HelloChaoDaily;
import son.nt.hellochao.service.MusicPlayback;
import son.nt.hellochao.widget.ViewRowHcDaily;


public class SentencePracticeFragment extends AMusicServiceFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private HelloChaoDaily helloChaoDaily;

    private OnFragmentInteractionListener mListener;

    public SentencePracticeFragment() {
    }

    @Bind(R.id.sentence_collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.sentence_ViewRowHcDaily)
    ViewRowHcDaily viewRowHcDaily;

    @Bind(R.id.sentence_play)
    FloatingActionButton playBtn;

    @Bind(R.id.sentence_sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;

    @Bind(R.id.txt_record1)
    TextView txtRecord1;

    @Bind(R.id.record_action)
    View recordAction;

    @Bind(R.id.record_cancel)
    View recordCancel;

    @Bind(R.id.record_done)
    View recordDone;

    @Bind(R.id.record_layout)
    View recordLayout;

    @Bind(R.id.record_ll_close)
    View recordCloseLayout;

    @Bind(R.id.record_timer)
    Chronometer chronometer;

    long timeBase;


    public static SentencePracticeFragment newInstance(String param1, HelloChaoDaily helloChaoDaily) {
        SentencePracticeFragment fragment = new SentencePracticeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, helloChaoDaily);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            helloChaoDaily = (HelloChaoDaily) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sentence_practice, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initLayout(View view) {
        slidingUpPanelLayout.setTouchEnabled(true);

    }

    @Override
    protected void initListener(View view) {
        playBtn.setOnClickListener(onClickListener);
        txtRecord1.setOnClickListener(onClickListener);
        recordAction.setOnClickListener(onClickListener);
        recordCancel.setOnClickListener(onClickListener);
        recordDone.setOnClickListener(onClickListener);

        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelCollapsed(View panel) {
                recordLayout.setVisibility(View.VISIBLE);
                recordCloseLayout.setVisibility(View.GONE);

            }

            @Override
            public void onPanelExpanded(View panel) {
                recordLayout.setVisibility(View.VISIBLE);
                recordCloseLayout.setVisibility(View.GONE);

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });

    }

    @Override
    protected void updateLayout() {
        if (helloChaoDaily != null) {
            collapsingToolbarLayout.setTitle(helloChaoDaily.getText());
            viewRowHcDaily.setData(helloChaoDaily);
        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.sentence_play:
                    if (musicService != null) {
                        musicService.processAddRequest(helloChaoDaily);
                    }
                    break;
                case R.id.txt_record1:
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    break;
                case R.id.record_action:
                    recordLayout.setVisibility(View.GONE);
                    recordCloseLayout.setVisibility(View.VISIBLE);
                    chronometer.setVisibility(View.VISIBLE);
                    timeBase = SystemClock.elapsedRealtime();
                    chronometer.setBase(timeBase);
                    chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                        @Override
                        public void onChronometerTick(Chronometer chronometer) {

                        }
                    });
                    chronometer.start();
                    break;
                case R.id.record_cancel:
                    chronometer.stop();
                    chronometer.setText("00:00");
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    break;
                case R.id.record_done:
                    chronometer.stop();
                    chronometer.setText("00:00");
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    break;
            }

        }
    };

    @Override
    protected MusicPlayback.Callback getMusicPlayBackCallback() {
        return musicCallback;
    }

    MusicPlayback.Callback musicCallback = new MusicPlayback.Callback() {
        @Override
        public void onPreparing(MusicItem musicItem) {

        }

        @Override
        public void onPlaying(MusicItem musicItem) {

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
