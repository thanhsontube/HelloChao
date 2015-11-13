package son.nt.hellochao.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import son.nt.hellochao.MsConst;
import son.nt.hellochao.R;
import son.nt.hellochao.ResourceManager;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.dto.TopDto;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.interface_app.IHelloChao;
import son.nt.hellochao.otto.GoDownload;
import son.nt.hellochao.otto.GoOnList;
import son.nt.hellochao.utils.DatetimeUtils;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;
import son.nt.hellochao.utils.PreferenceUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OralFragment extends AFragment implements View.OnClickListener, IHelloChao.HelloChaoCallback {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "OralFragment";
    private static final int REQUEST_CODE = 12;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private boolean isTest;

    List<DailySpeakDto> list = new ArrayList<>();

    TextView txtEngSentence;
    TextView txtYourVoice;
    TextView txtViSentence;
    View btnNext;

    int currentPoint = 0;

    MediaPlayer player;
    boolean isAutoNext = false;
    CheckBox chbAutoNext;
    CheckBox chbTranslation;
    CheckBox chbVoice;
    CheckBox chbText;
    ImageView reListen;
    View viewChecking;
    Handler handler = new Handler();
    int score = 0;
    int totalTimes = 0;
    ProgressDialog progressDialog;
    MediaRecorder mediaRecorder;

    Chronometer chronometer;

    @Bind(R.id.btn_start)
    TextView btnStart;

    @Bind(R.id.view_help_test)
    View viewHelp;

    @Bind(R.id.oral_ll_try_again)
    View viewTryAgain;

    @Bind(R.id.oral_img_answer_result)
    ImageView imgResult;

    @Bind(R.id.oral_txt_score)
    TextView txtScore;

    boolean isEnd = false;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param isTest Parameter 2.
     * @return A new instance of fragment OralFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OralFragment newInstance(String param1, boolean isTest) {
        OralFragment fragment = new OralFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, isTest);
        fragment.setArguments(args);
        return fragment;
    }

    public OralFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoBus.register(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            isTest = getArguments().getBoolean(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oral, container, false);
    }

    @Override
    protected void initData() {
        list.clear();
        list.addAll(ResourceManager.getInstance().getDailySpeakDtoList());
        initPlayer();
        AppAPI.getInstance().setHelloCHaoCallback(this);
    }

    @Override
    protected void initLayout(View view) {
        btnNext = view.findViewById(R.id.oral_btn_next);
        txtEngSentence = (TextView) view.findViewById(R.id.oral_eng_sentence);
        txtYourVoice = (TextView) view.findViewById(R.id.oral_your_voice);
        txtEngSentence.setText(getString(R.string.waiting));

        chbAutoNext = (CheckBox) view.findViewById(R.id.oral_chb_auto_next);

        chbText = (CheckBox) view.findViewById(R.id.oral_chb_hide_text);
        chbTranslation = (CheckBox) view.findViewById(R.id.oral_chb_translate);
        chbVoice = (CheckBox) view.findViewById(R.id.oral_chb_hide_voice);

        txtViSentence = (TextView) view.findViewById(R.id.oral_vi_sentence);

        reListen = (ImageView) view.findViewById(R.id.oral_re_listen);

        viewChecking = view.findViewById(R.id.oral_checking_ll);
        viewChecking.setVisibility(View.INVISIBLE);

        chronometer = (Chronometer) view.findViewById(R.id.timer);


        chbText.setChecked(PreferenceUtil.getPreference(getActivity(), MsConst.KEY_SHOW_ENG, true));
        chbTranslation.setChecked(PreferenceUtil.getPreference(getActivity(), MsConst.KEY_SHOW_VI, false));
        chbVoice.setChecked(PreferenceUtil.getPreference(getActivity(), MsConst.KEY_VOICE, true));
        chbAutoNext.setChecked(PreferenceUtil.getPreference(getActivity(), MsConst.KEY_AUTO_NEXT, true));


        isAutoNext = chbAutoNext.isChecked();


    }

    @Override
    protected void initListener(View view) {

        btnStart.setOnClickListener(this);
        chbVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.setPreference(getActivity(), MsConst.KEY_VOICE, isChecked);
            }
        });
        chbTranslation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.setPreference(getActivity(), MsConst.KEY_SHOW_VI, isChecked);
                updateSentence();
            }
        });
        chbText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.setPreference(getActivity(), MsConst.KEY_SHOW_ENG, isChecked);
                updateSentence();
            }
        });
        chbAutoNext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.setPreference(getActivity(), MsConst.KEY_AUTO_NEXT, isChecked);
                isAutoNext = isChecked;
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentPoint++;

                if (currentPoint >= list.size()) {

                    currentPoint = 0;
                    if (isTest) {
                        isEnd = true;
                        showTop();
                    } else {
                        updateSentence();
                        playSentence();
                    }
                } else {
                    updateSentence();
                    playSentence();
                }

            }
        });

        reListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSentence();
            }
        });

        view.findViewById(R.id.oral_btn_try).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognitionActivity();
            }
        });


    }

    private void initPlayer() {
        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!isSafe()) {
                    return;
                }

                if (chbVoice.isChecked()) {
                    if (!isEnd) {
                        startVoiceRecognitionActivity();
                    }
                } else {
                    if (isAutoNext) {
                        currentPoint++;
                        if (currentPoint >= list.size()) {
                            currentPoint = 0;
                        }
                        updateSentence();
                        playSentence();

                    }
                }


            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                playSentence();
                return false;
            }
        });
    }


    @Override
    protected void updateLayout() {
        viewTryAgain.setVisibility(View.GONE);
        txtScore.setText("Score:0");
        btnStart.setText("Loading...");
        btnStart.setEnabled(false);
        AppAPI.getInstance().helloChaoGetDailyQuestions();
        chbVoice.setEnabled(!isTest);


        updateSentence();
//        playSentence();
    }


    private void playSentence() {
        resetLayoutWhenOpenNewSentence();
        try {
            player.reset();
            player.setDataSource(list.get(currentPoint).getLinkMp3());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void goDownload(GoDownload download) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(download.getCount()).append("-").append(download.getHeroID());
        txtEngSentence.setText(stringBuilder.toString());
        if (download.getCount() == -1) {
            //speak
//            if (mediaService != null) {
//                mediaService.play(list.get(currentPoint).getLinkMp3());
//            }


        }


    }

    //this is the case download questions from hellochap


    private void updateSentence() {
        Logger.debug(TAG, ">>>" + "updateSentence");

        if (list == null || list.isEmpty()) {
            Logger.error(TAG, ">>>" + "updateSentence list NULL OR ENPTY");
            return;
        }
        Logger.debug(TAG, ">>>" + "List size:" + list.size());
        txtEngSentence.setText(currentPoint + 1 + "." + list.get(currentPoint).getSentenceEng());
        txtViSentence.setText(list.get(currentPoint).getSentenceVi());
        txtEngSentence.setVisibility(chbText.isChecked() ? View.VISIBLE : View.INVISIBLE);
        txtViSentence.setVisibility(chbTranslation.isChecked() ? View.VISIBLE : View.INVISIBLE);

        txtYourVoice.setText("");
        txtYourVoice.setBackgroundColor(getResources().getColor(R.color.transparent));


    }


    @Override
    public void onDestroy() {
        OttoBus.unRegister(this);
        super.onDestroy();
    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, list.get(currentPoint).getSentenceEng());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            final String yourVoice = matches.get(0);
            txtYourVoice.setText(matches.get(0));
            viewChecking.setVisibility(View.VISIBLE);
            viewTryAgain.setVisibility(View.GONE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int spaceYours = yourVoice.split(" ").length;
                    viewChecking.setVisibility(View.INVISIBLE);

                    String fromOther = list.get(currentPoint).getSentenceEng().toLowerCase().trim();

                    fromOther = fromOther.replace(" - ", " ").replace(". ", " ").replace(", ", " ").replace("? ", " ");
                    int spaceCorrect = fromOther.split(" ").length;
                    if (fromOther.contains(yourVoice.toLowerCase()) && spaceCorrect == spaceYours) {
//                        txtYourVoice.setBackgroundColor(getResources().getColor(R.color.md_green_500));
                        imgResult.setVisibility(View.VISIBLE);
                        imgResult.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_right_answer));
                        score++;
                        txtScore.setText("Score:" + score);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isSafe()) {
                                    return;
                                }
                                if (isAutoNext) {
                                    currentPoint++;

                                    if (currentPoint >= list.size()) {
                                        currentPoint = 0;
                                        if (isTest) {
                                            showTop();
                                        } else {
                                            if (isAutoNext) {
                                                updateSentence();
                                                playSentence();
                                            }
                                        }
                                    } else {
                                        if (isAutoNext) {
                                            updateSentence();
                                            playSentence();
                                        }
                                    }
                                }
                            }
                        }, 1500);


                    } else {

//                        txtYourVoice.setBackgroundColor(getResources().getColor(R.color.md_red_500));
                        imgResult.setVisibility(View.VISIBLE);
                        imgResult.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_wrong_answer));
                        viewTryAgain.setVisibility(View.VISIBLE);
                    }

                }
            }, 1000);


        } else {
            viewTryAgain.setVisibility(View.VISIBLE);
        }
    }


    long timeBase;

    private void showTop() {


        totalTimes = (int) ((SystemClock.elapsedRealtime() - timeBase) / 1000);


        Logger.debug(TAG, ">>>" + "showTop currentPoint:" + currentPoint + ";timeTest:" + totalTimes);
        chronometer.stop();
        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser == null) {

        } else {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(getString(R.string.submitting));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            upResult();
        }
    }

    private void upResult() {
        final String userName = ParseUser.getCurrentUser().getUsername();

        final int[] arr = DatetimeUtils.getCurrentTime();
        //check first
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TopScore");
        query.whereEqualTo("day", arr[0]);
        query.whereEqualTo("month", arr[1]);
        query.whereEqualTo("year", arr[2]);
        query.whereEqualTo("username", userName);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject != null) {
                    int beforeScore = parseObject.getInt("score");
                    int total = parseObject.getInt("total");
                    if (beforeScore < score || (beforeScore == score && totalTimes < total)) {

                        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("TopScore");
                        query1.getInBackground(parseObject.getObjectId(), new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject p, ParseException e) {
                                p.put("score", score);
                                p.put("total", totalTimes);
                                p.put("submitTime", System.currentTimeMillis());
                                p.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {


                                        btnNext.setEnabled(false);
                                        progressDialog.dismiss();
                                        if (e != null) {
                                            Toast.makeText(getActivity(), R.string.updated_your_score, Toast.LENGTH_SHORT).show();
                                        }
                                        mListener.onTop();

                                    }
                                });
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        if (e != null) {
                            Toast.makeText(getActivity(), R.string.before_test_better, Toast.LENGTH_SHORT).show();
                        }
                        mListener.onTop();
                    }


                } else {
                    ParseObject p = new ParseObject("TopScore");
                    p.put("score", score);
                    p.put("username", ParseUser.getCurrentUser().getUsername());
                    p.put("day", arr[0]);
                    p.put("month", arr[1]);
                    p.put("year", arr[2]);
                    p.put("total", totalTimes);
                    p.put("submitTime", System.currentTimeMillis());
                    p.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {


                            btnNext.setEnabled(false);
                            progressDialog.dismiss();
                            mListener.onTop();

                        }
                    });
                }
            }
        });

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

        void onTop();
    }

    private void initRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(ResourceManager.getInstance().folderBlur + File.separator + "t1.mp3");
    }

    //TODO onclick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_start:
                if (player == null) {
                    return;
                }
                viewHelp.setVisibility(View.GONE);

                if (isTest) {
                    chronometer.setVisibility(View.VISIBLE);
                    chbVoice.setChecked(true);
                    timeBase = SystemClock.elapsedRealtime();
                    chronometer.setBase(timeBase);
                    chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                        @Override
                        public void onChronometerTick(Chronometer chronometer) {

                        }
                    });
                    chronometer.start();
                } else {
                    chronometer.setVisibility(View.GONE);
                }
                playSentence();
                break;
        }
    }

    @Override
    public void helloChaoGetListUserTop(ArrayList<TopDto> listTop) {

    }

    //this this the case getting successful data from parse
    @Override
    public void helloChaoGetDailyQuestions(ArrayList<DailySpeakDto> listDaiLy) {
        list.clear();
        list.addAll(listDaiLy);
        updateSentence();
        btnStart.setText("Start");
        btnStart.setEnabled(true);
    }

    @Override
    public void helloChaoGetAllQuestions(ArrayList<DailySpeakDto> listDaiLy) {

    }

    @Subscribe
    public void onGettingDailyQuestions(GoOnList goDaiLyTest) {
        list.clear();
        list.addAll(goDaiLyTest.list);
        updateSentence();
        btnStart.setText("Start");
        btnStart.setEnabled(true);
    }

    private void resetLayoutWhenOpenNewSentence() {
        viewTryAgain.setVisibility(View.GONE);
        viewChecking.setVisibility(View.GONE);
        imgResult.setVisibility(View.GONE);

    }
}
