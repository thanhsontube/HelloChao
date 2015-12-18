package son.nt.hellochao.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import son.nt.hellochao.MsConst;
import son.nt.hellochao.R;
import son.nt.hellochao.ResourceManager;
import son.nt.hellochao.base.AMusicServiceFragment;
import son.nt.hellochao.dto.MusicItem;
import son.nt.hellochao.parse_object.HelloChaoDaily;
import son.nt.hellochao.service.MusicPlayback;
import son.nt.hellochao.utils.CommonUtils;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.widget.ViewRowHcDaily;


public class SentencePracticeFragment extends AMusicServiceFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SentencePracticeFragment";

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

    /**
     * Record layout 1
     */

    @Bind(R.id.txt_record1)
    TextView txtRecord1;

    @Bind(R.id.record_ll_play1)
    View llRecordPlay1;

    @Bind(R.id.record_play_1)
    ImageView playRecord1;

    @Bind(R.id.record_delete_1)
    ImageView delete1;

    /**
     * record 2
     */
    @Bind(R.id.txt_record2)
    TextView txtRecord2;

    @Bind(R.id.record_ll_play2)
    View llRecordPlay2;

    @Bind(R.id.record_play_2)
    ImageView playRecord2;

    @Bind(R.id.record_delete_2)
    ImageView delete2;

    //write TExt
    @Bind(R.id.sentence_write_text)
    AppCompatEditText txtWriteText;
    @Bind(R.id.sentence_sample) TextView txtSample;
    @Bind(R.id.sentence_send) View viewSend;
    @Bind(R.id.sentence_write_result) TextView txtWriteResult;



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

    @Bind(R.id.sentence_google_check)
    View sentenceGoogleCheck;

    @Bind(R.id.sentence_google_check_2)
    View sentenceGoogleCheck2;

    @Bind(R.id.sentence_txt_result)
    TextView txtResult;

    long timeBase;
    MediaRecorder mMediaRecorder;
    int recordID = 1;


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
        recordAction.setOnClickListener(onClickListener);
        recordCancel.setOnClickListener(onClickListener);
        recordDone.setOnClickListener(onClickListener);
        sentenceGoogleCheck.setOnClickListener(onClickListener);
        sentenceGoogleCheck2.setOnClickListener(onClickListener);

        viewSend.setOnClickListener(onClickListener);

        txtRecord1.setOnClickListener(onClickListener);
        playRecord1.setOnClickListener(onClickListener);
        delete1.setOnClickListener(onClickListener);

        txtRecord2.setOnClickListener(onClickListener);
        playRecord2.setOnClickListener(onClickListener);
        delete2.setOnClickListener(onClickListener);

        txtWriteText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND) {
                    //do here your stuff f
                    processResult(txtWriteText.getText().toString(), txtWriteResult, helloChaoDaily);
                    txtWriteText.setText("");
                    txtWriteText.clearFocus();
                    return true;
                }
                return false;
            }
        });

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

        viewRowHcDaily.setOnViewRowHcDailyClickCallback(new ViewRowHcDaily.OnViewRowHcDailyClick() {
            @Override
            public void onClick(HelloChaoDaily data) {
                if (musicService != null) {
                    musicService.processAddRequest(helloChaoDaily);
                }
            }
        });

    }

    @Override
    protected void updateLayout() {
        if (helloChaoDaily != null) {
            collapsingToolbarLayout.setTitle(helloChaoDaily.getText());
            viewRowHcDaily.setData(helloChaoDaily);
            txtSample.setText(helloChaoDaily.getText());
        }

        addRecordToView(1);
        addRecordToView(2);

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
                    recordID = 1;
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    break;
                case R.id.txt_record2:
                    recordID = 2;
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
                    String output;
                    if (recordID == 1) {
                        output = ResourceManager.getInstance().getRecordPath1(helloChaoDaily.getAudio());
                    } else {
                        output = ResourceManager.getInstance().getRecordPath2(helloChaoDaily.getAudio());
                    }
                    setupMediaRecorder(output);
                    break;
                case R.id.record_cancel:
                case R.id.record_done:
                    chronometer.stop();
                    chronometer.setText("00:00");
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    mMediaRecorder.stop();
                    resetResource();
                    addRecordToView(recordID);

                    break;

                case R.id.record_play_1:
                    if (musicService != null) {
                        MusicItem item = new MusicItem();
                        item.audio = ResourceManager.getInstance().getRecordPath1(helloChaoDaily.getAudio());
                        item.text = "Record 1";
                        musicService.processAddRequest(item);
                    }
                    break;

                case R.id.record_play_2:
                    if (musicService != null) {
                        MusicItem item = new MusicItem();
                        item.audio = ResourceManager.getInstance().getRecordPath2(helloChaoDaily.getAudio());
                        item.text = "Record 2";
                        musicService.processAddRequest(item);
                    }
                    break;

                case R.id.record_delete_1:
                    delete(1);
                    break;
                case R.id.record_delete_2:
                    delete(2);
                    break;

                case R.id.sentence_google_check:
                case R.id.sentence_google_check_2:
                    Logger.debug(TAG, ">>>" + "sentence_google_check");
                    CommonUtils.startVoiceRecognitionActivity(SentencePracticeFragment.this, helloChaoDaily.getText(), MsConst.REQUEST_CODE_SENTENCE);
                    break;

                case R.id.sentence_send:
                    processResult(txtWriteText.getText().toString(), txtWriteResult, helloChaoDaily);
                    txtWriteText.setText("");
                    txtWriteText.clearFocus();
                    break;
            }

        }
    };

    void processResult (String text, TextView txtWriteResult, HelloChaoDaily helloChaoDaily) {
        text = text.trim();
        txtWriteResult.setText(text);
        int spaceYours = text.split(" ").length;

        String fromOther = helloChaoDaily.getText().toLowerCase().trim();

        fromOther = fromOther.replace(" - ", " ").replace(". ", " ").replace(", ", " ").replace("? ", " ");
        int spaceCorrect = fromOther.split(" ").length;
        if (fromOther.contains(text.toLowerCase()) && spaceCorrect == spaceYours) {
            txtWriteResult.setBackgroundResource(R.drawable.box_bg_green_with_corner_white);

        } else {
            txtWriteResult.setBackgroundResource(R.drawable.box_bg_red_with_corner_white);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MsConst.REQUEST_CODE_SENTENCE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            final String yourVoice = matches.get(0);
            txtResult.setText(yourVoice);


            int spaceYours = yourVoice.split(" ").length;

            String fromOther = helloChaoDaily.getText().toLowerCase().trim();

            fromOther = fromOther.replace(" - ", " ").replace(". ", " ").replace(", ", " ").replace("? ", " ");
            int spaceCorrect = fromOther.split(" ").length;
            if (fromOther.contains(yourVoice.toLowerCase()) && spaceCorrect == spaceYours) {
                txtResult.setBackgroundResource(R.drawable.box_blue_with_corner);
                txtResult.setTextColor(ContextCompat.getColor(getContext(), R.color.md_blue_500));

            } else {
                txtResult.setBackgroundResource(R.drawable.box_black_with_corner);
                txtResult.setTextColor(ContextCompat.getColor(getContext(), R.color.md_black_1000));
            }
        }
    }

    private void delete(int record) {
        String output;
        if (record == 1) {
            output = ResourceManager.getInstance().getRecordPath1(helloChaoDaily.getAudio());
        } else {
            output = ResourceManager.getInstance().getRecordPath2(helloChaoDaily.getAudio());
        }

        try {
            File fRecord = new File(output);
            if (!fRecord.exists()) {
                return;
            }
            fRecord.delete();

            if (record == 1) {
                txtRecord1.setVisibility(View.VISIBLE);
                llRecordPlay1.setVisibility(View.GONE);
            } else {
                txtRecord2.setVisibility(View.VISIBLE);
                llRecordPlay2.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRecordToView(int record) {
        //check visible
        String output;
        if (record == 1) {
            output = ResourceManager.getInstance().getRecordPath1(helloChaoDaily.getAudio());
        } else {
            output = ResourceManager.getInstance().getRecordPath2(helloChaoDaily.getAudio());
        }

        try {
            File fRecord = new File(output);
            if (!fRecord.exists()) {
                return;
            }

            if (record == 1) {
                txtRecord1.setVisibility(View.GONE);
                llRecordPlay1.setVisibility(View.VISIBLE);
            } else {
                txtRecord2.setVisibility(View.GONE);
                llRecordPlay2.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private void setupMediaRecorder(String output) {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setOutputFile(output);
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            resetResource();
        }

    }

    private void resetResource() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }
}
