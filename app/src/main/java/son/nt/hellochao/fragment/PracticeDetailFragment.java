package son.nt.hellochao.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import son.nt.hellochao.R;
import son.nt.hellochao.ResourceManager;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.MusicItem;
import son.nt.hellochao.otto.GoFullItemClick;
import son.nt.hellochao.parse_object.HelloChaoDaily;
import son.nt.hellochao.service.MusicPlayback;
import son.nt.hellochao.service.MusicService;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PracticeDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PracticeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PracticeDetailFragment extends AFragment {
    public static final String TAG = "FullPracticeFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    enum StatePlay {
        None,
        Playing,
        Paused,
    }

    StatePlay statePlay = StatePlay.None;

    @Bind(R.id.full_rcv)
    RecyclerView recyclerView;
    AdapterFull adapterFull;
    List<HelloChaoDaily> list = new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    @Bind(R.id.smooth_bar)
    SmoothProgressBar smoothProgressBar;

    @Bind(R.id.full_toolbar)
    Toolbar toolbar;

    @Bind(R.id.detail_practice_CollapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.full_play)
    FloatingActionButton playButton;

    MusicService musicService;

    ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalBinder localBinder = (MusicService.LocalBinder) iBinder;
            musicService = localBinder.getService();
            musicService.setMusicPlayBackCallback(musicPlayBackCallback);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public PracticeDetailFragment() {
        // Required empty public constructor
    }


    public static PracticeDetailFragment newInstance(String param1, String param2) {
        PracticeDetailFragment fragment = new PracticeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        MusicService.bindToMe(getContext(), musicServiceConnection);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (musicService != null) {
            getAActivity().unbindService(musicServiceConnection);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        OttoBus.register(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        OttoBus.unRegister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_full_practice, container, false);
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
        void onMore (HelloChaoDaily helloChaoDaily);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initLayout(View view) {

        getAActivity().setSupportActionBar(toolbar);
        getAActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getAActivity().getSupportActionBar().setTitle("Practice");
        collapsingToolbarLayout.setTitle("My Practice");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        list.clear();
        list.addAll(ResourceManager.getInstance().getListHelloChaoDaily());
        Logger.debug(TAG, ">>>" + "size:" + list.size());
        adapterFull = new AdapterFull(getContext(), list);
        recyclerView.setAdapter(adapterFull);

    }

    @Override
    protected void initListener(View view) {
        playButton.setOnClickListener(onClickListener);

    }

    @Override
    protected void updateLayout() {

    }

    @Subscribe
    public void GoFullItemClick(GoFullItemClick goFullItemClick) {
        if (goFullItemClick.isMore) {
            if (mListener != null) {
                mListener.onMore((HelloChaoDaily) goFullItemClick.musicItem);
            }
        } else {
            statePlay = StatePlay.None;
            if (musicService != null) {
                musicService.processAddRequest(goFullItemClick.musicItem);
            }
        }


    }

    public class AdapterFull extends RecyclerView.Adapter<AdapterFull.ViewHolder> {
        private Context context;
        List<? extends MusicItem> list;

        public AdapterFull(Context context, List<? extends MusicItem> l) {
            this.context = context;
            this.list = l;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtText;
            TextView txtTranslate;
            TextView txtMore;

            public ViewHolder(View itemView) {
                super(itemView);
                txtText = (TextView) itemView.findViewById(R.id.txt_text);
                txtTranslate = (TextView) itemView.findViewById(R.id.txt_translate);
                txtMore = (TextView) itemView.findViewById(R.id.txt_more);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OttoBus.post(new GoFullItemClick(list.get(getAdapterPosition()), false));
                    }
                });

                txtMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OttoBus.post(new GoFullItemClick(list.get(getAdapterPosition()), true));
                    }
                });
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_list_3lines, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            HelloChaoDaily musicItem = (HelloChaoDaily) list.get(position);
            holder.txtText.setText(musicItem.getText());
            holder.txtTranslate.setText(musicItem.getTranslate());


        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    MusicPlayback.Callback musicPlayBackCallback = new MusicPlayback.Callback() {
        @Override
        public void onPreparing(MusicItem musicItem) {
            if (!isSafe() || smoothProgressBar == null) {
                return;
            }
            smoothProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPlaying(MusicItem musicItem) {
            if (!isSafe() || smoothProgressBar == null) {
                return;
            }
            smoothProgressBar.setVisibility(View.GONE);
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



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.full_play:
                    switch (statePlay) {
                        case None:
                            statePlay = StatePlay.Playing;
                            playButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_pause));
                            playAllList(list);
                            break;
                        case Playing:
                            statePlay = StatePlay.Paused;
                            playButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_play));
                            if (musicService != null) {
                                musicService.processTogglePlayback();
                            }
                            break;
                        case Paused:
                            statePlay = StatePlay.Playing;
                            playButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_pause));
                            if (musicService != null) {
                                musicService.processTogglePlayback();
                            }
                            break;
                    }

                    break;
            }
        }
    };
    private void playAllList (List<? extends MusicItem> list) {
        if (musicService == null) {
            return;
        }

        musicService.processAllList(list);
    }



}
