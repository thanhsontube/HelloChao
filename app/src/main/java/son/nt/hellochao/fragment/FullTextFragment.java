package son.nt.hellochao.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import son.nt.hellochao.R;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.HomeEntity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullTextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullTextFragment extends AFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    HomeEntity homeEntity;
    TextView txtFullText;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FullTextFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FullTextFragment newInstance(String param1, String param2) {
        FullTextFragment fragment = new FullTextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static FullTextFragment newInstance (HomeEntity entity) {
        FullTextFragment f = new FullTextFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", entity);
        f.setArguments(bundle);
        return f;
    }

    public FullTextFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            homeEntity = (HomeEntity) getArguments().getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_full_text, container, false);
        txtFullText = (TextView) view.findViewById(R.id.fulltext_txt_full);

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

    }

    @Override
    protected void initListener(View view) {

    }

    @Override
    protected void updateLayout() {
        if (homeEntity != null && !TextUtils.isEmpty(homeEntity.getHomeFullText())) {
            txtFullText.setText(homeEntity.getHomeFullText());
        }

    }
}
