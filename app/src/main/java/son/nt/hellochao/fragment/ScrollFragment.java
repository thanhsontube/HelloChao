package son.nt.hellochao.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import son.nt.hellochao.R;
import son.nt.hellochao.adapter.AdapterHot;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.HomeEntity;
import son.nt.hellochao.dto.HomeObject;
import son.nt.hellochao.dto.HotEntity;
import son.nt.hellochao.loader.HomeLoader;
import son.nt.hellochao.loader.LoaderManager;
import son.nt.hellochao.utils.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScrollFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ScrollFragment extends AFragment {

    private OnFragmentInteractionListener mListener;

    ViewPager pagerHot;
    AdapterHot adapterHot;
    List<HotEntity> listHot = new ArrayList<>();

    LoaderManager manager;


    public ScrollFragment() {
        // Required empty public constructor
        manager = new LoaderManager();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scroll, container, false);
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


        listHot.clear();

    }

    @Override
    protected void initLayout(View view) {
        pagerHot = (ViewPager) view.findViewById(R.id.scroll_pager);
        adapterHot = new AdapterHot(getFragmentManager(), listHot);
        pagerHot.setHorizontalScrollBarEnabled(true);
        pagerHot.setAdapter(adapterHot);
    }

    @Override
    protected void initListener(View view) {

    }

    @Override
    protected void updateLayout() {
        manager.execute(new HomeLoader(getAActivity(), "http://www.esl-lab.com/index.htm") {
            @Override
            public void onLoaderStart() {
                Logger.debug(TAG, ">>>" + "onLoaderStart");

            }

            @Override
            public void onLoaderSuccess(HomeObject entity) {
                Logger.debug(TAG, ">>>" + "onLoaderSuccess:" + entity.getListHomeEntity().size());
                listHot.clear();
                listHot.add(new HotEntity(getRandom(entity.getListHomeEntity(), 0)));
                listHot.add(new HotEntity(getRandom(entity.getListHomeEntity(), 1)));
                listHot.add(new HotEntity(getRandom(entity.getListHomeEntity(), 2)));
                adapterHot.notifyDataSetChanged();

//                ResourceManager.getInstance().homeObject = entity;

            }

            @Override
            public void onLoaderFail(Throwable e) {
                Logger.error(TAG, ">>>" + "onLoaderFail:" + e.toString());

            }
        });

    }

    private HomeEntity getRandom (List<HomeEntity> list, int type) {
        List<HomeEntity> list1 = new ArrayList<>();
        int pos;
        switch (type) {

            case 0:
                list1.clear();
                for (HomeEntity dto : list) {
                    if (dto.getHomeGroup().equalsIgnoreCase("Easy")) {
                        list1.add(dto);
                    }
                }
                pos = new Random().nextInt(list1.size() -1);
                return list1.get(pos);
            case 1:
                list1.clear();
                for (HomeEntity dto : list) {
                    if (dto.getHomeGroup().equalsIgnoreCase("Medium")) {
                        list1.add(dto);
                    }


                }
                pos = new Random().nextInt(list1.size() - 1);
                return list1.get(pos);
            case 2:
                list1.clear();
                for (HomeEntity dto : list) {
                    if (dto.getHomeGroup().equalsIgnoreCase("Difficult")) {
                        list1.add(dto);
                    }
                }
                pos = new Random().nextInt(list1.size() - 1);
                return list1.get(pos);
        }
        return null;
    }
}
