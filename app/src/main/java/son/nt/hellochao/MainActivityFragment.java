package son.nt.hellochao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.dto.TopDto;
import son.nt.hellochao.otto.GoOnList;
import son.nt.hellochao.service.DownloadIntentService;
import son.nt.hellochao.utils.DatetimeUtils;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends AFragment {
    public static final String TAG = "MainActivityFragment";
    ProgressDialog progressDialog;
    TextView txtWelcome;
    TextView txtUserName;
    TextView txtSentence;
    TextView txtTimes;
    TextView txtTop;

    View viewLogin;
    View viewLogout;
    List<TopDto> listTops = new ArrayList<>();

    @Override
    protected void initLayout(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();

        txtWelcome = (TextView) view.findViewById(R.id.main_hello);
        txtUserName = (TextView) view.findViewById(R.id.txtUsername);
        txtSentence = (TextView) view.findViewById(R.id.main_txt_sentences);
        txtTimes = (TextView) view.findViewById(R.id.main_txt_time);
        txtTop = (TextView) view.findViewById(R.id.main_txt_top);
        viewLogin = view.findViewById(R.id.main_login);
        viewLogout = view.findViewById(R.id.main_logout);
        txtUserName.setText(R.string.no_name);


    }

    @Override
    protected void initListener(View view) {


        view.findViewById(R.id.main_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGoSignUp();
            }
        });
        viewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkOnline()) {
                    Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                mListener.onGoLogin();
            }
        });
        viewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (!isNetworkOnline()) {
                            Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        updateStatus(false);

                    }
                });
            }
        });

        view.findViewById(R.id.main_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkOnline()) {
                    Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                mListener.onGoTop();
//                downloadAFile();
//                downloadAllLink();
            }
        });
        view.findViewById(R.id.main_txt_practice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkOnline()) {
                    Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                mListener.onGoPractice();
            }
        });
        view.findViewById(R.id.main_txt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkOnline()) {
                    Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ParseUser.getCurrentUser() == null) {
                    Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_SHORT).show();
                } else {
                    mListener.onGoTest();
                }
            }
        });


        view.findViewById(R.id.test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTest1();
            }
        });

        view.findViewById(R.id.test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTest1();
            }
        });

    }


    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    protected void initData() {


    }

    public interface IInteraction {
        void onGotoOral();

        void onGoSignUp();

        void onGoLogin();

        void onGoTop();

        void onGoPractice();

        void onGoTest();

        void onTest1();
    }

    IInteraction mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IInteraction) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    protected void updateLayout() {

//        ParseUser parseUser = ParseUser.getCurrentUser();
//        if (parseUser == null) {
//            txtWelcome.setText("");
//        } else {
//            txtWelcome.setText(getString(R.string.welcome) + " " + parseUser.getUsername());
//            txtUserName.setText(parseUser.getUsername());
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int month = calendar.get(Calendar.MONTH);
//        int year = calendar.get(Calendar.YEAR);
//        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(DailySpeakDto.class.getSimpleName());
//        query.whereEqualTo("day", day);
//        query.whereEqualTo("month", month);
//        query.whereEqualTo("year", year);
//
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> list, ParseException e) {
//                if (e != null || list == null || list.size() != 10) {
//                    HTTPParseUtils.getInstance().withHcDaily();
//                    return;
//                }
//
//                Logger.debug(TAG, ">>>" + "size:" + list.size());
//                List<DailySpeakDto> dailyList = new ArrayList<DailySpeakDto>();
//                DailySpeakDto d;
//                for (ParseObject p : list) {
//                    d = new DailySpeakDto();
//                    d.setMonth(p.getInt("month"));
//                    d.setYear(p.getInt("year"));
//                    d.setDay(p.getInt("day"));
//                    d.setLinkMp3(p.getString("linkMp3"));
//                    d.setSentenceEng(p.getString("sentenceEng"));
//                    d.setSentenceVi(p.getString("sentenceVi"));
//                    dailyList.add(d);
//                }
//                ResourceManager.getInstance().setDailySpeakDtoList(dailyList);
//                progressDialog.dismiss();
//
//            }
//        });
//
//        getTotalSubmitInDay();

    }

    private void getTotalSubmitInDay() {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("TopScore");
            query.addDescendingOrder("score");
            query.addDescendingOrder("total");

            int[]arr = DatetimeUtils.getCurrentTime();
            //check first
            query.whereEqualTo("day", arr[0]);
            query.whereEqualTo("month", arr[1]);
            query.whereEqualTo("year", arr[2]);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> l, ParseException e) {
                    listTops.clear();
                    TopDto dto;
                    int i = 1;
                    for (ParseObject p : l) {
                        dto = new TopDto();
                        dto.setNo(i);
                        dto.setName(p.getString("username"));
                        dto.setScore(p.getInt("score"));
                        dto.setTotalTime(p.getInt("total"));
//                        dto.setSubmitTime(p.getLong("submitTime"));
                        i++;
                        listTops.add(dto);
                    }
//                    ResourceManager.getInstance().setListTops(listTops);
                    updateStatus(false);
                }
            });

        } catch (Exception e) {

        }
    }

    public void updateStatus(boolean isTryRequest) {
        ParseUser parseUser = ParseUser.getCurrentUser();
        if (ParseUser.getCurrentUser() == null) {
            viewLogin.setVisibility(View.VISIBLE);
            viewLogout.setVisibility(View.GONE);
            txtUserName.setText("No Name");
            txtSentence.setText(getString(R.string.total_sentences) + 0 + "/10");
            txtTimes.setText(getString(R.string.total_duration) + "---");
            txtTop.setText(getString(R.string.my_top) + "---" + "/" + listTops.size());
            return;
        }


        txtUserName.setText(parseUser.getUsername());
        viewLogin.setVisibility(View.GONE);
        viewLogout.setVisibility(View.VISIBLE);

        if (isTryRequest) {
            updateLayout();

        } else {
            TopDto userTop = null;
            for (TopDto d : listTops) {
                if (d.getName().equals(parseUser.getUsername())) {
                    userTop = d;

                    break;
                }
            }
            if (userTop == null) {
                txtSentence.setText(getString(R.string.total_sentences) + 0 + "/10");
                txtTimes.setText(getString(R.string.total_duration) + "---");
                txtTop.setText(getString(R.string.my_top) + "---" + "/" + listTops.size());
            } else {
                txtSentence.setText(getString(R.string.total_sentences) + userTop.getScore() + "/10");
                txtTimes.setText(getString(R.string.total_duration) + userTop.getTotalTime() + getString(R.string.seconds));
                txtTop.setText(getString(R.string.my_top) + userTop.getNo() + "/" + listTops.size());
            }
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OttoBus.unRegister(this);
    }

    @Subscribe
    public void goOnList(GoOnList goOnList) {
        ResourceManager.getInstance().setDailySpeakDtoList(goOnList.list);
        for (DailySpeakDto d : goOnList.list) {
            push(d);
        }
        progressDialog.dismiss();


    }

    public static void push(final DailySpeakDto d) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(DailySpeakDto.class.getSimpleName());
        query.whereEqualTo("sentenceEng", d.getSentenceEng());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    ParseObject p = new ParseObject(DailySpeakDto.class.getSimpleName());
                    p.put("day", d.getDay());
                    p.put("month", d.getMonth());
                    p.put("year", d.getYear());
                    p.put("sentenceEng", d.getSentenceEng());
                    p.put("sentenceVi", d.getSentenceVi());
                    p.put("linkMp3", d.getLinkMp3());
                    p.saveInBackground();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void downloadAllLink() {
        getAActivity().startService(DownloadIntentService.getIntent(getAActivity()));
    }

    private void downloadAFile() {
        Logger.debug(TAG, ">>>" + "downloadAFile");
        try {
            String url = "http://www.hellochao.vn/play_sound.php?sid=c4ca1238a0b923821dcc309a6f7584Ms&type=11";
            Ion.with(this).load(url).progressDialog(progressDialog)
                    .write(new File(ResourceManager.getInstance().folderAlarm, "t_" + System.currentTimeMillis() + ".mp3"))
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File result) {
                            Logger.debug(TAG, ">>>" + "onCompleted :" + e.toString());

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getAActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }
}
