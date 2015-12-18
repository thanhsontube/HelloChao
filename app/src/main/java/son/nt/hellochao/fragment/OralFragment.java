package son.nt.hellochao.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import son.nt.hellochao.MsConst;
import son.nt.hellochao.R;
import son.nt.hellochao.ResourceManager;
import son.nt.hellochao.base.AMusicServiceFragment;
import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.dto.DailyTopDto;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.interface_app.IHelloChao;
import son.nt.hellochao.parse_object.HelloChaoDaily;
import son.nt.hellochao.service.MusicPlayback;
import son.nt.hellochao.utils.DatetimeUtils;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;
import son.nt.hellochao.utils.PreferenceUtil;
import son.nt.hellochao.widget.MovieDetailCardLayout;
import son.nt.hellochao.widget.ViewRowHcDaily;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OralFragment extends AMusicServiceFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "OralFragment";
    private static final int REQUEST_CODE = 12;

    private String mParam1;
    private boolean isTest;

    List<HelloChaoDaily> list = new ArrayList<>();

    int currentPoint = 0;

    boolean isAutoNext = false;
    CheckBox chbAutoNext;
    CheckBox chbTranslation;
    CheckBox chbVoice;
    CheckBox chbText;
    int score = 0;
    int totalTimes = 0;
    ProgressDialog progressDialog;

    Chronometer chronometer;

    @Bind(R.id.btn_start)
    TextView btnStart;

    @Bind(R.id.view_help_test)
    View viewHelp;


    @Bind(R.id.oral_txt_score)
    TextView txtScore;

    @Bind(R.id.oral_txt_total)
    TextView txtTotal;


    @Bind(R.id.oral_exam)
    ViewRowHcDaily viewExam;

    @Bind(R.id.oral_next)
    View viewNext;
    @Bind(R.id.oral_previous)
    View viewPrev;
    @Bind(R.id.oral_google_test_1)
    View googleTest1;
    @Bind(R.id.oral_google_test_2)
    View googleTest2;
    @Bind(R.id.oral_txt_result)
    TextView txtResult;
    @Bind(R.id.oral_submit)
    TextView submit;

    @Bind(R.id.oral_volume) View viewVolume;

    @Bind(R.id.view_status)
    MovieDetailCardLayout viewStatus;

    boolean isEnd = false;

    AppAPI appAPI;

    public static OralFragment newInstance(String param1, boolean isTest) {
        OralFragment fragment = new OralFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, isTest);
        fragment.setArguments(args);
        return fragment;
    }

    public OralFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoBus.register(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            isTest = getArguments().getBoolean(ARG_PARAM2);
        }
        appAPI = new AppAPI(getContext());
        appAPI.setHcCallback(hcCallback);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oral, container, false);
    }

    @Override
    protected void initData() {
        list.clear();
        if (!ResourceManager.getInstance().getListHelloChaoDaily().isEmpty()) {
            list.addAll(ResourceManager.getInstance().getListHelloChaoDaily());
        }
    }

    @Override
    protected void initLayout(View view) {

        chbAutoNext = (CheckBox) view.findViewById(R.id.oral_chb_auto_next);
        chbText = (CheckBox) view.findViewById(R.id.oral_chb_hide_text);
        chbTranslation = (CheckBox) view.findViewById(R.id.oral_chb_translate);
        chbVoice = (CheckBox) view.findViewById(R.id.oral_chb_hide_voice);
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
        viewNext.setOnClickListener(this);
        submit.setOnClickListener(this);
        viewPrev.setOnClickListener(this);
        googleTest1.setOnClickListener(this);
        googleTest2.setOnClickListener(this);
        viewVolume.setOnClickListener(this);
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


        viewExam.setOnViewRowHcDailyClickCallback(onViewRowHcDailyClick);


    }

    ViewRowHcDaily.OnViewRowHcDailyClick onViewRowHcDailyClick = new ViewRowHcDaily.OnViewRowHcDailyClick() {
        @Override
        public void onClick(HelloChaoDaily data) {
            if (musicService != null) {
                musicService.processAddRequest(data);
            }
        }
    };


    @Override
    protected void updateLayout() {
        txtScore.setText("0");
        btnStart.setText("Start");
        btnStart.setEnabled(true);
        chbVoice.setEnabled(!isTest);
    }


    //this is the case download questions from hellochap


    private void updateSentence() {
        Logger.debug(TAG, ">>>" + "updateSentence");
        if (currentPoint == 0) {
            viewPrev.setVisibility(View.INVISIBLE);
        } else {
            viewPrev.setVisibility(View.VISIBLE);
        }
        if (list == null || list.isEmpty()) {
            Logger.error(TAG, ">>>" + "updateSentence list NULL OR ENPTY");
            return;
        }
        if (currentPoint >= list.size()) {
            return;
        }

        if (currentPoint >= list.size() - 1) {
            viewNext.setVisibility(View.INVISIBLE);
        } else {
            viewNext.setVisibility(View.VISIBLE);
        }

        //update exam
        HelloChaoDaily dto = list.get(currentPoint);
        viewExam.setData(dto);
        switch (dto.typeCheck) {
            case HelloChaoDaily.TYPE_NEW:
                viewStatus.setBG(R.color.md_white_1000);
                break;
            case HelloChaoDaily.TYPE_CORRECT:
                viewStatus.setBG(R.color.md_green_500);
                break;
            case HelloChaoDaily.TYPE_WRONG:
                viewStatus.setBG(R.color.md_red_500);
                break;
        }


        //update record
        txtResult.setText("------------------");


        //update total and score
        txtScore.setText("" + score);
        txtTotal.setText("" + (currentPoint + 1) + "/" + list.size());
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
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, list.get(currentPoint).getText());
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

            processResult(yourVoice, txtResult, list.get(currentPoint));
        }
    }

    void processResult(String text, TextView txtWriteResult, HelloChaoDaily helloChaoDaily) {
        text = text.trim();
        txtWriteResult.setText(text);
        int spaceYours = text.split(" ").length;

        String fromOther = helloChaoDaily.getText().toLowerCase().trim();

        fromOther = fromOther.replace(" - ", " ").replace(". ", " ").replace(", ", " ").replace("? ", " ");
        int spaceCorrect = fromOther.split(" ").length;
        if (fromOther.contains(text.toLowerCase()) && spaceCorrect == spaceYours) {
            txtWriteResult.setBackgroundResource(R.drawable.box_bg_green_with_corner_white);
            if (list.get(currentPoint).typeCheck != HelloChaoDaily.TYPE_CORRECT) {

                score++;
                list.get(currentPoint).typeCheck = HelloChaoDaily.TYPE_CORRECT;
            }
            txtScore.setText("" + score);

        } else {
            txtWriteResult.setBackgroundResource(R.drawable.box_bg_red_with_corner_white);
            if (list.get(currentPoint).typeCheck != HelloChaoDaily.TYPE_CORRECT) {

                list.get(currentPoint).typeCheck = HelloChaoDaily.TYPE_WRONG;
            }
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


    OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

        void onTop();

        void onVolume ();
    }

    //TODO onclick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_start:
                currentPoint = 0;
                updateSentence();

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
                break;
            case R.id.oral_google_test:
            case R.id.oral_google_test_1:
            case R.id.oral_google_test_2:
                startVoiceRecognitionActivity();
                break;
            case R.id.oral_next:
                currentPoint++;
                updateSentence();
                break;
            case R.id.oral_previous:
                currentPoint--;
                updateSentence();
                break;
            case R.id.oral_submit:
                processSubmit();
break;

            case R.id.oral_volume:
                mListener.onVolume();

                break;
        }
    }

    private void processSubmit() {
        totalTimes = (int) ((SystemClock.elapsedRealtime() - timeBase) / 1000);
        Logger.debug(TAG, ">>>" + "showTop currentPoint:" + currentPoint + ";timeTest:" + totalTimes);
        chronometer.stop();
        if (score == 0) {
            Toast.makeText(getActivity(), "Your score is too low for submitting !!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ParseUser.getCurrentUser() == null) {
            Logger.debug(TAG, ">>>" + "You need to login first ...");
            return;
        }
        showProgressDialog("Submitting....", false);
//        DailyTopDto helloChaoSubmitDto = new DailyTopDto(ParseUser.getCurrentUser().getUsername(), score, totalTimes);
        DailyTopDto dto = new DailyTopDto();
        dto.setCorrectSentence(score);
        dto.setTotalSeconds(totalTimes);
        dto.setParseUser(ParseUser.getCurrentUser());
        dto.setSubmitTime(Calendar.getInstance().getTime());
        dto.setRelativeTime(DatetimeUtils.relativeTime());
        appAPI.hcSubmitTestResult(dto);
    }


    @Override
    protected MusicPlayback.Callback getMusicPlayBackCallback() {
        return null;
    }


    IHelloChao.HcCallback hcCallback = new IHelloChao.HcCallback() {
        @Override
        public void throwUserTop(ArrayList<DailyTopDto> listTop) {

        }

        @Override
        public void throwDailySentences(ArrayList<HelloChaoDaily> listDaiLy) {

        }

        @Override
        public void throwAllSentences(ArrayList<DailySpeakDto> listDaiLy) {

        }

        @Override
        public void throwSubmitDaily(boolean isUpdate, String error) {
            hideProgressDialog();
            score = 0;
            totalTimes = 0;
            timeBase = SystemClock.elapsedRealtime();
            Logger.debug(TAG, ">>>" + "throwSubmitDaily isUpdate:" + isUpdate + ";error:" + error);
            if (!TextUtils.isEmpty(error)) {
                Toast.makeText(getActivity(), "SORRY >>> " + error, Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getActivity(), "Congratulations ! Updated your score !!!", Toast.LENGTH_SHORT).show();
        }
    };

}
