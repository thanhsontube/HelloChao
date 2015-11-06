package son.nt.hellochao.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

import son.nt.hellochao.utils.Logger;


public class ServiceMedia extends Service implements Playback {

    private static final String TAG = "ServiceMedia";
    private MediaPlayer mMediaPlayer;
    private LocalBinder mBinder = new LocalBinder();
    int mState;
    Playback.Callback mCallback;
    int mCurrentPosition;
    private  String mCurrentMediaId;
    private String mCurrentSource;
    private File mCurrentFile;

    Handler mHandler = new Handler();

    public static Intent getIntentService(Context context) {
        return new Intent(context, ServiceMedia.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createMediaPlayerIfNeeded();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        public ServiceMedia getService() {
            return ServiceMedia.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void createMediaPlayerIfNeeded() {
        Logger.debug(TAG, ">>>" + "createMediaPlayerIfNeeded, Needed? :" + (mMediaPlayer == null));
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            // Make sure the media mMediaPlayer will acquire a wake-lock while
            // playing. If we don't do that, the CPU might go to sleep while the
            // song is playing, causing playback to stop.
            mMediaPlayer.setWakeMode(getApplicationContext(),
                    PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mCallback != null) {
                        mCallback.onCompletion();
                    }

                }
            });

            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {

                    if (mCallback != null) {
                        mCallback.onError("MediaPlayer error " + what + " (" + extra + ")");
                    }
                    return true; // true indicates we handled the error
                }
            });

            mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mediaPlayer) {
                    mCurrentPosition = mediaPlayer.getCurrentPosition();
                    if (mState == PlaybackState.STATE_BUFFERING) {
                        mHandler.removeCallbacks(runnableCurrentPosition);
                        mHandler.post(runnableCurrentPosition);

                        mMediaPlayer.start();
                        mState = PlaybackState.STATE_PLAYING;
                    }
                    notifyState();
                }
            });

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    if (mCallback != null) {
                        mCallback.onDuration(mediaPlayer.getDuration());
                    }
                    configMediaPlayerState();
                }
            });
        } else {
            mMediaPlayer.reset();
        }
    }

    private void relaxResources(boolean releaseMediaPlayer) {

        this.stopForeground(true);

        // stop and release the Media Player, if it's available
        if (releaseMediaPlayer && mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

    }



    public void playStream(String link) {
        try {
            Logger.debug(TAG, ">>>" + "play path:" + link);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(link);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configMediaPlayerState () {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            if (mCurrentPosition == mMediaPlayer.getCurrentPosition()) {
                mHandler.removeCallbacks(runnableCurrentPosition);
                mHandler.post(runnableCurrentPosition);
                mMediaPlayer.start();
                mState = PlaybackState.STATE_PLAYING;
            } else {
                mMediaPlayer.seekTo(mCurrentPosition);
                mState = PlaybackState.STATE_BUFFERING;
            }
            notifyState();
        }

    }

    private void notifyState () {
        if (mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop(boolean notifyListeners) {
        mState = PlaybackState.STATE_STOPPED;
        if (notifyListeners && mCallback!= null) {
            mCallback.onPlaybackStatusChanged(mState);
        }

        mCurrentPosition = getCurrentStreamPosition();
        relaxResources(true);


    }

    @Override
    public void setState(int state) {
        this.mState = state;

    }

    @Override
    public int getState() {
        return this.mState;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public boolean isPlaying() {
        return (mMediaPlayer != null && mMediaPlayer.isPlaying());
    }

    @Override
    public int getCurrentStreamPosition() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : mCurrentPosition;
    }

    @Override
    public void setCurrentStreamPosition(int pos) {


    }

    @Override
    public void play(MediaSession.QueueItem item) {

    }

    @Override
    public void play(File file) {

        if (mCurrentFile == null || file == null) {
            return;
        }
        mCurrentSource = null;
        boolean mediaHasChanged = !TextUtils.equals(file.getPath(), mCurrentFile.getPath());
        if (mediaHasChanged) {
            mCurrentPosition = 0;
            mCurrentFile = file;
        }
        mState = PlaybackState.STATE_STOPPED;
        relaxResources(false);
        try {
            createMediaPlayerIfNeeded();
            mState = PlaybackState.STATE_BUFFERING;
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(file.getPath());
            mMediaPlayer.prepareAsync();

            notifyState();

        } catch (Exception ex) {
            ex.printStackTrace();
            if (mCallback != null) {
                mCallback.onError(ex.getMessage());
            }

        }


    }

    @Override
    public void play(String link) {
        Logger.debug(TAG, ">>>" + "play link:" + link);
        mCurrentFile = null;
        boolean mediaHasChanged = !TextUtils.equals(mCurrentSource, link);
        Logger.debug(TAG, ">>>" + "mediaHasChanged:" + mediaHasChanged);
        if (mediaHasChanged) {
            mCurrentPosition = 0;
            mCurrentSource = link;
        }

        if (mState == android.media.session.PlaybackState.STATE_PAUSED && !mediaHasChanged
                && mMediaPlayer != null) {
            configMediaPlayerState();
        } else {
            mState = PlaybackState.STATE_STOPPED;
            relaxResources(false);
            try {
                createMediaPlayerIfNeeded();
                mState = PlaybackState.STATE_BUFFERING;
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(link);
                mMediaPlayer.prepareAsync();
                notifyState();

            } catch (Exception ex) {
                ex.printStackTrace();
                if (mCallback != null) {
                    mCallback.onError(ex.getMessage());
                }

            }
        }

    }
    Runnable runnableCurrentPosition = new Runnable() {
        @Override
        public void run() {
            if (mCallback != null && mMediaPlayer != null) {
                mCallback.onCurrentPosition(mMediaPlayer.getCurrentPosition());
            }
            mHandler.postDelayed(runnableCurrentPosition, 1000);
        }
    };



    @Override
    public void pause() {
        if (mState == PlaybackState.STATE_PLAYING) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentPosition = mMediaPlayer.getCurrentPosition();
            }

            relaxResources(false);
            mState = PlaybackState.STATE_PAUSED;

            notifyState();
        }

    }

    @Override
    public void seekTo(int position) {
        if (mMediaPlayer == null) {
            mCurrentPosition = position;
        } else {
            if (mMediaPlayer.isPlaying()) {
                mState = PlaybackState.STATE_BUFFERING;
            }
            mMediaPlayer.seekTo(position);
            notifyState();

        }

    }

    @Override
    public void setCurrentMediaId(String mediaId) {

    }

    @Override
    public String getCurrentMediaId() {
        return null;
    }

    @Override
    public void setCallback(Callback callback) {
        this.mCallback = callback;

    }

    public void metaData () {
        MediaMetadata mediaMetadata = null;
        MediaBrowser mediaBrowser = null;

    }


    private String link;
    public void setResource (String link) {
        this.link = link;

    }

}
