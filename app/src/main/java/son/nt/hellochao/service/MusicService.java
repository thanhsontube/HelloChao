package son.nt.hellochao.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import son.nt.hellochao.dto.MusicItem;
import son.nt.hellochao.utils.Logger;

public class MusicService extends Service {
    public static final String TAG = "MusicService";
    public static final String ACTION_TOGGLE_PLAYBACK =
            "com.example.android.musicplayer.action.TOGGLE_PLAYBACK";
    public static final String ACTION_PLAY = "com.example.android.musicplayer.action.PLAY";
    public static final String ACTION_PAUSE = "com.example.android.musicplayer.action.PAUSE";
    public static final String ACTION_STOP = "com.example.android.musicplayer.action.STOP";
    public static final String ACTION_SKIP = "com.example.android.musicplayer.action.SKIP";
    public static final String ACTION_REWIND = "com.example.android.musicplayer.action.REWIND";
    public static final String ACTION_URL = "com.example.android.musicplayer.action.URL";

    public static Intent getService (Context context) {
        return new Intent(context, MusicService.class);
    }

    public class LocalBinder extends Binder {

        public MusicService getService() {
            return MusicService.this;
        }

    }

    public interface MusicFocusable {
        public void onGainedAudioFocus();

        //canDuck if true, audio can continue playing in duck mode (low volume).
        public void onLostAudioFocus(boolean canDuck);
    }

    class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {
        AudioManager audioManager;
        MusicFocusable musicFocusable;

        public AudioFocusHelper(Context context, MusicFocusable musicFocusable) {
            audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            this.musicFocusable = musicFocusable;

        }

        public boolean requestFocus() {
            if (audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                return true;
            }
            return false;
        }

        public boolean abandonFocus() {
            if (audioManager.abandonAudioFocus(this) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                return true;
            }
            return false;
        }


        @Override
        public void onAudioFocusChange(int i) {

            if (musicFocusable == null) {
                return;
            }

            switch (i) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    Logger.debug(TAG, ">>>" + "onAudioFocusChange AUDIOFOCUS_GAIN");
                    musicFocusable.onGainedAudioFocus();
                    break;


                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Logger.debug(TAG, ">>>" + "onAudioFocusChange AUDIOFOCUS_LOSS");
                    musicFocusable.onLostAudioFocus(false);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Logger.debug(TAG, ">>>" + "onAudioFocusChange AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    musicFocusable.onLostAudioFocus(true);
                    break;
                default:
            }

        }
    }

    enum State {
        Fetching,
        Stopped,
        Playing,
        Paused,
        Preparing
    }
    State mState = State.Stopped;

    enum AudioFocus {
        NoFocusNoDuck,
        NoFocusCanDuck,
        Focused

    }

    AudioFocus audioFocus = AudioFocus.NoFocusNoDuck;

    AudioFocusHelper audioFocusHelper;
    LocalBinder localBinder = new LocalBinder();
    MediaPlayer mediaPlayer;

    NotificationManager notificationManager;
    AudioManager audioManager;
    ComponentName componentName;

    boolean isStreaming;
    String songTitle;

    WifiManager.WifiLock wifiLock;

    MusicItem currentItem;

    float currentVolume = 0.7f;

    public MusicService() {
        Logger.debug(TAG, ">>>" + "MusicService");

    }

    private void setupMediaPlayerIfNeeded() {
        Logger.debug(TAG, ">>>" + "setupMediaPlayerIfNeeded");
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            mediaPlayer.setOnPreparedListener(onPreparedListener);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.setOnErrorListener(onErrorListener);


        } else {
            mediaPlayer.reset();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.debug(TAG, ">>>" + "onCreate");
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        audioFocusHelper = new AudioFocusHelper(getApplicationContext(), musicFocusable);
        componentName = new ComponentName(this, MusicIntentReceiver.class);

        wifiLock = ((WifiManager) getSystemService(WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "myLock");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        Logger.debug(TAG, ">>>" + "onStartCommand action:" + action);

        if (ACTION_TOGGLE_PLAYBACK.equals(action)) {

        } else if (ACTION_PAUSE.equals(action)) {

        } else if (ACTION_PLAY.equals(action)) {

        } else if (ACTION_REWIND.equals(action)) {

        } else if (ACTION_SKIP.equals(action)) {

        } else if (ACTION_STOP.equals(action)) {

        } else if (ACTION_URL.equals(action)) {
            //

        }


        return START_NOT_STICKY; // we started the service, but we don't want it restart if it was killed by system,
    }

    public void processTogglePlayback () {

    }

    public void processPlayRequest() {
        Logger.debug(TAG, ">>>" + "processPlayRequest");
        tryToGetAudioFocus();


    }

    public void processAddRequest(MusicItem musicItem) {
        Logger.debug(TAG, ">>>" + "processAddRequest");
        tryToGetAudioFocus();
        playNextSong(musicItem);

    }

    public void playNextSong (MusicItem musicItem) {
        String url = musicItem.audio;
        Logger.debug(TAG, ">>>" + "playNextSong:" + url);
        mState = State.Stopped;

        relaxResource(false);
        try {
            setupMediaPlayerIfNeeded();

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            isStreaming = url.startsWith("http:") || url.startsWith("https:");

            songTitle = url;

            mState = State.Preparing;
            setupAsForeGround(musicItem);

            /**
             * @see onPreparedListener
             */

            mediaPlayer.prepareAsync();

            if (isStreaming) {
                wifiLock.acquire();
            } else  if (wifiLock.isHeld()) {
                wifiLock.release();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }






    }

    private void setupAsForeGround (MusicItem musicItem) {
        //notification setup
    }

    void relaxResource (boolean isReleaseMediaPlayer) {
        stopForeground(true);
        if (isReleaseMediaPlayer && mediaPlayer!= null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (wifiLock.isHeld()) {
            wifiLock.release();
        }
    }

    private void tryToGetAudioFocus() {
        Logger.debug(TAG, ">>>" + "tryToGetAudioFocus");
        if (audioFocus != AudioFocus.Focused && audioFocusHelper != null && audioFocusHelper.requestFocus()) {
            audioFocus = AudioFocus.Focused;
        }

    }

    private void configAndStartMediaPlayer () {
        if (audioFocus == AudioFocus.NoFocusNoDuck) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            return;
        }
        if (audioFocus ==AudioFocus.NoFocusCanDuck) {
            mediaPlayer.setVolume(0.1f, 0.1f);

        } else {
            mediaPlayer.setVolume(currentVolume, currentVolume);
        }

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

    }

    private void updateNotification (MusicItem musicItem) {

    }



    @Override
    public IBinder onBind(Intent intent) {
        Logger.debug(TAG, ">>>" + "onBind");
        return localBinder;
    }

    MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            Logger.debug(TAG, ">>>" + "onPrepared");
            mState = State.Playing;
            updateNotification(currentItem);
            configAndStartMediaPlayer();

        }
    };

    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

        }
    };
    MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            return false;
        }
    };

    MusicFocusable musicFocusable = new MusicFocusable() {
        @Override
        public void onGainedAudioFocus() {

        }

        @Override
        public void onLostAudioFocus(boolean canDuck) {

        }
    };
}
