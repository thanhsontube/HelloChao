package son.nt.hellochao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.loader.HTTPParseUtils;
import son.nt.hellochao.otto.GoOnList;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends AFragment {
    public static final String TAG = "MainActivityFragment";
    ProgressDialog progressDialog;
    TextView txtWelcome;

    @Override
    protected void initLayout(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        txtWelcome = (TextView) view.findViewById(R.id.main_hello);


    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.main_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGotoOral();
            }
        });

        view.findViewById(R.id.main_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGoSignUp();
            }
        });
        view.findViewById(R.id.main_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGoLogin();
            }
        });

        view.findViewById(R.id.main_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGoTop();
            }
        });

    }

    public MainActivityFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.debug(TAG, ">>>" + "onCreateView");
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
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

        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser == null) {
            txtWelcome.setText("");
        } else {
            txtWelcome.setText("Welcome " + parseUser.getUsername());
        }

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(DailySpeakDto.class.getSimpleName());
        query.whereEqualTo("day", day);
        query.whereEqualTo("month", month);
        query.whereEqualTo("year", year);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null || list == null || list.size() != 10) {
                    HTTPParseUtils.getInstance().withHelloChao();
                    return;
                }

                Logger.debug(TAG, ">>>" + "size:" + list.size());
                List<DailySpeakDto> dailyList = new ArrayList<DailySpeakDto>();
                DailySpeakDto d;
                for (ParseObject p : list) {
                    d = new DailySpeakDto();
                    d.setMonth(p.getInt("month"));
                    d.setYear(p.getInt("year"));
                    d.setDay(p.getInt("day"));
                    d.setLinkMp3(p.getString("linkMp3"));
                    d.setSentenceEng(p.getString("sentenceEng"));
                    d.setSentenceVi(p.getString("sentenceVi"));
                    dailyList.add(d);
                }
                ResourceManager.getInstance().setDailySpeakDtoList(dailyList);
                progressDialog.dismiss();

            }
        });

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

}
