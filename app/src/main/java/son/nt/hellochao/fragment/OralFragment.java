package son.nt.hellochao.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import son.nt.hellochao.MsConst;
import son.nt.hellochao.R;
import son.nt.hellochao.ResourceManager;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.otto.GoDownload;
import son.nt.hellochao.service.ServiceMedia;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;
import son.nt.hellochao.utils.PreferenceUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OralFragment extends AFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "OralFragment";
    private static final int REQUEST_CODE = 12;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<DailySpeakDto> list = new ArrayList<>();

    TextView txtEngSentence;
    TextView txtYourVoice;
    TextView txtViSentence;
    Button btnNext;

    int currentPoint = 0;

    ServiceMedia serviceMedia;
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


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OralFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OralFragment newInstance(String param1, String param2) {
        OralFragment fragment = new OralFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getActivity().bindService(new Intent(getActivity(), ServiceMedia.class), serviceConnectionMedia, Service.BIND_AUTO_CREATE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_oral, container, false);
    }

    @Override
    protected void initData() {
//        HTTPParseUtils.getInstance().withHelloChao();
        list.clear();
        list.addAll(ResourceManager.getInstance().getDailySpeakDtoList());
        initPlayer();
    }

    @Override
    protected void initLayout(View view) {
        btnNext = (Button) view.findViewById(R.id.oral_btn_next);
        txtEngSentence = (TextView) view.findViewById(R.id.oral_eng_sentence);
        txtYourVoice = (TextView) view.findViewById(R.id.oral_your_voice);
        txtEngSentence.setText("Please wait...");

        chbAutoNext = (CheckBox) view.findViewById(R.id.oral_chb_auto_next);

        chbText = (CheckBox) view.findViewById(R.id.oral_chb_hide_text);
        chbTranslation = (CheckBox) view.findViewById(R.id.oral_chb_translate);
        chbVoice = (CheckBox) view.findViewById(R.id.oral_chb_hide_voice);

        txtViSentence = (TextView) view.findViewById(R.id.oral_vi_sentence);

        reListen = (ImageView) view.findViewById(R.id.oral_re_listen);

        viewChecking = view.findViewById(R.id.oral_checking_ll);
        viewChecking.setVisibility(View.INVISIBLE);


        chbText.setChecked(PreferenceUtil.getPreference(getActivity(), MsConst.KEY_SHOW_ENG, true));
        chbTranslation.setChecked(PreferenceUtil.getPreference(getActivity(), MsConst.KEY_SHOW_VI, true));
        chbVoice.setChecked(PreferenceUtil.getPreference(getActivity(), MsConst.KEY_VOICE, true));
        chbAutoNext.setChecked(PreferenceUtil.getPreference(getActivity(), MsConst.KEY_AUTO_NEXT, true));


        isAutoNext = chbAutoNext.isChecked();


    }

    @Override
    protected void initListener(View view) {
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

                if (chbVoice.isChecked() && currentPoint >= 10) {
                    //show top
                    showTop();
                } else {
                    if (currentPoint >= 10) {
                        currentPoint = 0;
                    }
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

                if (chbVoice.isChecked()) {

                    startVoiceRecognitionActivity();
                } else {
                    if (isAutoNext) {
                        currentPoint++;
                        if (currentPoint == 10) {
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

        updateSentence();
        playSentence();
    }



    private void playSentence() {
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

    private void updateSentence() {
        txtEngSentence.setText(currentPoint + 1 + "." + list.get(currentPoint).getSentenceEng());
        txtViSentence.setText(list.get(currentPoint).getSentenceVi());
        txtEngSentence.setVisibility(chbText.isChecked() ? View.VISIBLE : View.INVISIBLE);
        txtViSentence.setVisibility(chbTranslation.isChecked() ? View.VISIBLE : View.INVISIBLE);

        txtYourVoice.setText("");
        txtYourVoice.setBackgroundColor(getResources().getColor(R.color.transparent));


    }

    //MEDIA MUSIC service
    private ServiceMedia mediaService;
    ServiceConnection serviceConnectionMedia = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServiceMedia.LocalBinder binder = (ServiceMedia.LocalBinder) service;
            mediaService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                mediaService = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy() {
        OttoBus.unRegister(this);
        super.onDestroy();
        if (mediaService != null) {
            getActivity().unbindService(serviceConnectionMedia);
        }
    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Your sentence ...");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : matches) {
                stringBuilder.append(s);
                stringBuilder.append("-");
            }

            final String yourVoice = matches.get(0);

            txtYourVoice.setText(matches.get(0));
            viewChecking.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int spaceCorrect = list.get(currentPoint).getSentenceEng().split(" ").length;
                    int spaceYours = yourVoice.split(" ").length;
                    viewChecking.setVisibility(View.INVISIBLE);
                    if (list.get(currentPoint).getSentenceEng().toLowerCase().contains(yourVoice.toLowerCase()) && spaceCorrect == spaceYours) {
                        txtYourVoice.setBackgroundColor(getResources().getColor(R.color.md_green_500));
                        score++;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isAutoNext) {

                                    currentPoint++;

                                    if (chbVoice.isChecked() && currentPoint >= 10) {
                                        //show top
                                        showTop();
                                    } else {
                                        if (currentPoint >= 10) {
                                            currentPoint = 0;
                                        }
                                        updateSentence();
                                        playSentence();
                                    }


                                }
                            }
                        }, 1500);


                    } else {

                        txtYourVoice.setBackgroundColor(getResources().getColor(R.color.md_red_500));
                    }

                }
            }, 1000);


        }
    }



    private void showTop () {
        Logger.debug(TAG, ">>>" + "showTop currentPoint:" + currentPoint);
        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser == null) {

        } else {
            upResult();
        }
    }
    private void upResult () {
        final String userName = ParseUser.getCurrentUser().getUsername();
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        //check first
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TopScore");
        query.whereEqualTo("day", day);
        query.whereEqualTo("month", month);
        query.whereEqualTo("year", year);
        query.whereEqualTo("username", userName);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
//
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
                                        mListener.onTop();
                                        if (e != null) {
                                            Toast.makeText(getActivity(), "updated your score", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        });
                    }


                } else {
                    ParseObject p = new ParseObject("TopScore");
                    p.put("score", score);
                    p.put("username", ParseUser.getCurrentUser().getUsername());
                    p.put("day", day);
                    p.put("month", month);
                    p.put("year", year);
                    p.put("total", totalTimes);
                    p.put("submitTime", System.currentTimeMillis());
                    p.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {


                            btnNext.setEnabled(false);
                            mListener.onTop();
                            if (e != null) {
                                Toast.makeText(getActivity(), "Show top", Toast.LENGTH_SHORT).show();
                            }

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
}
