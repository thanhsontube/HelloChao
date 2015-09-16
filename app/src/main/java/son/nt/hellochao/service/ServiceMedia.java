package son.nt.hellochao.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.File;
import java.io.IOException;

import son.nt.hellochao.ResourceManager;
import son.nt.hellochao.utils.Logger;


public class ServiceMedia extends Service {

    private static final String TAG = "ServiceMedia";
    private MediaPlayer player;
    private LocalBinder mBinder = new LocalBinder();


    public static Intent getIntentService(Context context) {
        return new Intent(context, ServiceMedia.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initController();
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

    private void initController() {
        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                return false;
            }
        });
    }


    public MediaPlayer getPlayer() {
        return player;
    }




    public void play (String link) {
        try {
            File file = new File(ResourceManager.getInstance().getPathAudio(link));
            if (file.exists()) {
                Logger.debug(TAG, ">>>" + "play path:" + file.getPath());
                player.reset();
                player.setDataSource(file.getPath());
                player.prepare();
                player.start();
            }

//                Logger.debug(TAG, ">>>" + "play path:" + link);
//                player.reset();
//                player.setDataSource(link);
//                player.prepare();
//                player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
