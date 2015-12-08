package son.nt.hellochao.fragment.intro;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import son.nt.hellochao.R;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.dto.TopDto;
import son.nt.hellochao.interface_app.AppAPI;
import son.nt.hellochao.interface_app.IHelloChao;
import son.nt.hellochao.otto.GoDaiLyTest;
import son.nt.hellochao.parse_object.HelloChaoDaily;
import son.nt.hellochao.utils.DatetimeUtils;
import son.nt.hellochao.utils.OttoBus;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IntroTopDayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IntroTopDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroTopDayFragment extends AFragment implements IHelloChao.HcCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.day_time)
    TextView txtDateTime;

    @Bind(R.id.top_1)
    TextView txtTop1;
    @Bind(R.id.top_2)
    TextView txtTop2;
    @Bind(R.id.top_3)
    TextView txtTop3;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopDayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntroTopDayFragment newInstance(String param1, String param2) {
        IntroTopDayFragment fragment = new IntroTopDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public IntroTopDayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_day, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OttoBus.post(new GoDaiLyTest(null));
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    protected void initData() {
        AppAPI.getInstance().setHcCallback(this);

    }

    @Override
    protected void initLayout(View view) {
        txtDateTime.setText(DatetimeUtils.getTimeFromLongTime(DatetimeUtils.DATE_FORMAT, Calendar.getInstance().getTimeInMillis()));
        txtTop1.setText("---");
        txtTop2.setText("---");
        txtTop3.setText("---");
    }

    @Override
    protected void initListener(View view) {

    }

    @Override
    protected void updateLayout() {
        AppAPI.getInstance().getHcUserTop();

    }

    @Override
    public void throwUserTop(ArrayList<TopDto> listTop) {
        if (listTop == null || listTop.size() == 0) {
            return;
        }

        if (listTop.size() == 1) {
            txtTop1.setText(listTop.get(0).getName());
        }
        if (listTop.size() == 2) {
            txtTop1.setText(listTop.get(1).getName());
        }
        if (listTop.size() == 3) {
            txtTop1.setText(listTop.get(2).getName());
        }
    }

    @Override
    public void throwDailySentences(ArrayList<HelloChaoDaily> listDaiLy) {

    }

    @Override
    public void throwAllSentences(ArrayList<DailySpeakDto> listDaiLy) {

    }
}
