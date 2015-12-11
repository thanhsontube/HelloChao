package son.nt.hellochao.base;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import son.nt.hellochao.service.MusicPlayback;
import son.nt.hellochao.service.MusicService;

/**
 * Created by Sonnt on 12/11/15.
 */
public abstract class AMusicServiceFragment extends AFragment {
    protected MusicService musicService;

    ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalBinder localBinder = (MusicService.LocalBinder) iBinder;
            musicService = localBinder.getService();
            musicService.setMusicPlayBackCallback(getMusicPlayBackCallback());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicService.bindToMe(getContext(), musicServiceConnection);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (musicService != null) {
            getAActivity().unbindService(musicServiceConnection);
        }
    }

    protected abstract MusicPlayback.Callback getMusicPlayBackCallback ();
}
