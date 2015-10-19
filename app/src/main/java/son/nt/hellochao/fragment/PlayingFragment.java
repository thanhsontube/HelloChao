package son.nt.hellochao.fragment;


import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import son.nt.hellochao.R;
import son.nt.hellochao.ResourceManager;
import son.nt.hellochao.adapter.AdapterPlaying;
import son.nt.hellochao.base.AFragment;
import son.nt.hellochao.dto.HomeEntity;
import son.nt.hellochao.dto.HotEntity;
import son.nt.hellochao.service.Playback;
import son.nt.hellochao.service.PlaybackState;
import son.nt.hellochao.service.ServiceMedia;
import son.nt.hellochao.utils.DatetimeUtils;
import son.nt.hellochao.utils.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayingFragment extends AFragment implements Playback.Callback {

    public final String TAG = getClass().getSimpleName();

    ViewPager pager;
    AdapterPlaying adapter;
    List<HotEntity> list = new ArrayList<>();
    ServiceMedia serviceMedia;
    HomeEntity homeEntity;
    View viewPrev;

    MediaPlayer mediaPlayer;
    TextView txtPlayingText;
    TextView txtDuration;
    TextView txtCurrent;

    SeekBar seekBar;

    ImageView imgPlay;


    public PlayingFragment() {
        // Required empty public constructor
    }

    public static PlayingFragment createInstance(HomeEntity homeEntity) {
        PlayingFragment p = new PlayingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", homeEntity);
        p.setArguments(bundle);
        return p;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeEntity = (HomeEntity) getArguments().getSerializable("data");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playing, container, false);
        getAActivity().bindService(ServiceMedia.getIntentService(getContext()), serviceConnection, Service.BIND_AUTO_CREATE);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getAActivity().unbindService(serviceConnection);
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
        list.add(new HotEntity(homeEntity));
        list.add(new HotEntity(homeEntity));
        list.add(new HotEntity(homeEntity));

    }

    @Override
    protected void initLayout(View view) {

        imgPlay = (ImageView) view.findViewById(R.id.media_play);
        txtDuration = (TextView) view.findViewById(R.id.media_txt_max_duration);
        txtCurrent = (TextView) view.findViewById(R.id.media_txt_current_duration);
        seekBar = (SeekBar) view.findViewById(R.id.media_seek_bar);


        txtPlayingText = (TextView) view.findViewById(R.id.playing_text);
        if (homeEntity != null) {
            txtPlayingText.setText(homeEntity.getHomeTitle());
        }
        viewPrev = view.findViewById(R.id.media_prev);
        viewPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        pager = (ViewPager) view.findViewById(R.id.playing_pager);
        adapter = new AdapterPlaying(getFragmentManager(), list);
        pager.setHorizontalScrollBarEnabled(true);
        pager.setAdapter(adapter);

    }

    @Override
    protected void initListener(View view) {
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceMedia == null) {
                    return;
                }

                if (serviceMedia.isPlaying()) {
                    serviceMedia.pause();
                } else {
                    serviceMedia.play(homeEntity.getHomeMp3());
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    serviceMedia.seekTo((int)(i * duration /100));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    protected void updateLayout() {
        pager.setCurrentItem(1);
    }

    private void anotherPlayer() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //mediaPlayer.setDataSource("http://hydra-media.cursecdn.com/dota2.gamepedia.com/4/4e/Erth_rival_05.mp3");
    private void setupMedia() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource("http://www.esl-lab.com/ramfiles/firstdate.ram");
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ServiceMedia.LocalBinder localBinder = (ServiceMedia.LocalBinder) iBinder;
            serviceMedia = localBinder.getService();
            serviceMedia.setCallback(PlayingFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceMedia = null;

        }
    };

    private void play() {
        if (serviceMedia != null) {
//            serviceMedia.playStream("http://www.esl-lab.com/ramfiles/firstdate.wax");
            serviceMedia.playStream("https://www.hellochao.vn/play_sound.php?sid=c4ca1238a0b923821dcc209a6f7584Ms&type=11#1444811193331599845");
        }
    }

    private class DownLoadA extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Logger.debug(TAG, ">>>" + "doInBackground");

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://www.esl-lab.com/audio/mp3/firstdate.mp3")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Logger.error(TAG, ">>>" + "Error:" + e.toString());
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Logger.debug(TAG, ">>>" + "onResponse");
                    try {
                        InputStream input = response.body().byteStream(); // Read the data from the stream
                        File file = new File(ResourceManager.getInstance().folderAudio, "firstday1.mp3");
                        OutputStream output = new FileOutputStream(file);
                        byte[] buffer = new byte[4 * 1024]; // or other buffer size
                        int read;

                        while ((read = input.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                        output.close();
                        input.close();

                        Logger.debug(TAG, ">>>" + "OKKKK");
                    } catch (Exception e) {
                        Logger.error(TAG, ">>>" + "Error down:" + e.toString());

                    }

                }
            });
            return null;
        }
    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onPlaybackStatusChanged(int state) {
        switch (state) {
            case PlaybackState.STATE_BUFFERING:
                break;
            case PlaybackState.STATE_PLAYING:
                imgPlay.setImageDrawable(ContextCompat.getDrawable(getAActivity(), R.mipmap.ic_pause));

                break;
            case PlaybackState.STATE_STOPPED:
                imgPlay.setImageDrawable(ContextCompat.getDrawable(getAActivity(), R.mipmap.ic_play));
                break;
            case PlaybackState.STATE_PAUSED:
                imgPlay.setImageDrawable(ContextCompat.getDrawable(getAActivity(), R.mipmap.ic_play));
                break;
        }

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onMetadataChanged(String mediaId) {

    }

    @Override
    public void onCurrentPosition(long currentTime) {
        txtCurrent.setText(DatetimeUtils.covertMillisToMediaTime(currentTime));
        if (duration <= 0L) {
            return;
        }
        seekBar.setProgress((int)(currentTime * 100 / this.duration));


    }

    @Override
    public void onDuration(long duration) {
        txtDuration.setText(DatetimeUtils.covertMillisToMediaTime(duration));
        this.duration = duration;


    }

    long duration;


}
