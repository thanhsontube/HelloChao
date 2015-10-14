package son.nt.hellochao.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import son.nt.hellochao.R;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.HotEntity;
import son.nt.hellochao.otto.GoHot;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotFragment extends AFragment {
    public final String TAG = this.getClass().getSimpleName();
    HotEntity hotEntity;
    @Bind(R.id.hot_txt_title)
    TextView txtTitle;



    public static HotFragment newInstance (HotEntity entity) {
        HotFragment f = new HotFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", entity);
        f.setArguments(bundle);
        return f;
    }


    public HotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hotEntity = (HotEntity) getArguments().getSerializable("data");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.view_recommend, container, false);
        ButterKnife.bind(this, view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.debug(TAG, ">>>" + "onClick");
                OttoBus.post(new GoHot(hotEntity));
            }
        });
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
        txtTitle.setText(hotEntity.getHomeEntity().getHomeTitle() + ":" + hotEntity.getHomeEntity().getHomeGroup());

    }
}
