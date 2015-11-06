package son.nt.hellochao;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import son.nt.hellochao.base.AActivity;
import son.nt.hellochao.dto.HomeEntity;
import son.nt.hellochao.dto.HomeObject;
import son.nt.hellochao.dto.LessonEntity;
import son.nt.hellochao.dto.QuizEntity;
import son.nt.hellochao.fragment.PlayingFragment;
import son.nt.hellochao.fragment.ScrollFragment;
import son.nt.hellochao.loader.HomeLoader;
import son.nt.hellochao.loader.LessonLoader;
import son.nt.hellochao.loader.LoaderManager;
import son.nt.hellochao.loader.QuizLoader;
import son.nt.hellochao.otto.GoHot;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;
import son.nt.hellochao.utils.TsParse;

public class ScrollingActivity extends AActivity {

    public final String TAG = this.getClass().getSimpleName();

    LoaderManager manager;
    DataManager d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        OttoBus.register(this);
        tsParse = new TsParse();
        manager = new LoaderManager();
        d = DataManager.getInstance();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.app_name, R.string.app_name);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_camara) {
                    // Handle the camera action
                    manager.execute(new HomeLoader(getApplicationContext(), ResourceManager.getInstance().getMyPath().getESL()) {
                        @Override
                        public void onLoaderStart() {

                        }

                        @Override
                        public void onLoaderSuccess(HomeObject entity) {
                            Logger.debug(TAG, ">>>" + "onLoaderSuccess:" + entity.getListHomeEntity().size());
                            d.setHomeEntities(entity.getListHomeEntity());

                        }

                        @Override
                        public void onLoaderFail(Throwable e) {
                            Logger.error(TAG, ">>>" + "onLoaderFail:" + e.toString());

                        }
                    });


                } else if (id == R.id.nav_gallery) {

                    HomeEntity d = DataManager.getInstance().getNULLTitle();
                    if (d != null) {
                        Logger.debug(TAG, ">>>" + "D NULL GROUP:" + d.getHomeGroup() + ";href:" + d.getHomeHref());
                    }


                    for (HomeEntity a : DataManager.getInstance().getNULLMp3()) {
                        Logger.debug(TAG, ">>>" + "NULL mp3:" + a.getHomeTitle() + ";link:" + a.getHomeHref());
                    }

//                   final int ran=  new Random().nextInt(d.getHomeEntities().size());
//                    String link = d.getHomeEntities().get(ran).getHomeHref();
//                    Logger.debug(TAG, ">>>" + "link:" + link);
//                    manager.execute(new LessonLoader(getApplicationContext(), link) {
//                        @Override
//                        public void onLoaderStart() {
//
//                        }
//
//                        @Override
//                        public void onLoaderSuccess(LessonEntity entity) {
//                            Logger.debug(TAG, ">>>" + "onLoaderSuccess:" + entity);
//                            HomeEntity homeEntity = d.getHomeEntities().get(ran);
//                            homeEntity.setHomeMp3(entity.getMp3Link());
//                            showFragment(PlayingFragment.createInstance(homeEntity),true);
//
//                        }
//
//                        @Override
//                        public void onLoaderFail(Throwable e) {
//
//                        }
//                    });


                } else if (id == R.id.nav_slideshow) {
                    for (HomeEntity d : DataManager.getInstance().getHomeEntities()) {
                        if (d.getHomeGroup().startsWith("D")) {
                            TsParse.upData(d);
                        }


                    }

                } else if (id == R.id.nav_manage) {

                    for (HomeEntity d : DataManager.getInstance().getHomeEntities()) {
                        if (d.getHomeTitle().equalsIgnoreCase("Security Systems")) {
                            Logger.debug(TAG, ">>>" + "title:" + d.getHomeTitle() + ";group:" + d.getHomeGroup()
                                    + ";href:" + d.getHomeHref() + ";quiz:" + d.getHomeQuizLink());
                            TsParse.upData(d);
                            break;
                        }

                    }

                } else if (id == R.id.nav_share) {
//                    new UpdateLesson().execute();

                } else if (id == R.id.nav_send) {
                    new UpdateFulltext().execute();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;

            }
        });

    }

    TsParse tsParse;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OttoBus.unRegister(this);
    }

    @Override
    protected Fragment fromFragment(Bundle savedInstanceState) {
        return new ScrollFragment();
    }

    @Override
    protected int fromContentId() {
        return R.id.scroll_ll_main;
    }

    @Subscribe
    public void onFromTopFragment(GoHot goHot) {
        showFragment(PlayingFragment.createInstance(goHot.getHotEntity().getHomeEntity()), true);
    }

    private class UpdateFulltext extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            for (HomeEntity d : DataManager.getInstance().getHomeEntities()) {


//                d.setHomeQuizLink("http://www.esl-lab.com/like1/lkscrt1.htm");
//                d.setHomeQuizLink("http://www.esl-lab.com/base/basesc1.htm");
//                d.setHomeQuizLink("http://www.esl-lab.com/cm1/cmsc1.htm");
//                d.setHomeQuizLink("http://www.esl-lab.com/adsense/adsensesc1.htm");
//                d.setHomeQuizLink("http://www.esl-lab.com/bank/banksc1.htm");
//                d.setHomeQuizLink("http://www.esl-lab.com/intro2/intrsc2.htm");
//                d.setHomeQuizLink("http://www.esl-lab.com/bath1/bathsc1.htm");
//                d.setHomeQuizLink("http://www.esl-lab.com/dating/datingsc1.htm");
                Logger.debug(TAG, ">>>" + "------------------------");

//                if (d.getHomeGroup().startsWith("M")  ) {

                if (d.getHomeGroup().startsWith("E") || d.getHomeGroup().startsWith("M")
                        || d.getHomeGroup().startsWith("D")) {

                    manager.execute(new QuizLoader(getApplicationContext(), d.getHomeQuizLink()) {
                        @Override
                        public void onLoaderStart() {

                        }

                        @Override
                        public void onLoaderSuccess(QuizEntity entity) {
                            updateFullText(entity);

                        }

                        @Override
                        public void onLoaderFail(Throwable e) {

                        }
                    });

                }

//                break;

            }
            return null;
        }
    }
    private void updateFullText(QuizEntity entity) {
        Logger.debug(TAG, ">>>" + "updateFullText:" + entity.getFullText() + ";link:" + entity.getQuizLink());
        if (entity == null || entity.getFullText().size() == 0) {
            Logger.error(TAG, ">>>" + "updateFullText ERROR:" + entity.getQuizLink());
            return;
        }
        HomeEntity d = DataManager.getInstance().getDataByQuiz(entity.getQuizLink());
        if (d == null) {
            Logger.error(TAG, ">>>" + "ERROR Mp3 HomeEntity NULL:" + entity.getQuizLink());
            return;
        }

        for (String s : entity.getFullText()) {
            Logger.debug(TAG, ">>>" + "s:" + s);
        }
        d.setListChats(entity.getFullText());
        tsParse.upFullText(d);


    }

    private class UpdateLesson extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            for (HomeEntity d : DataManager.getInstance().getHomeEntities()) {
                Logger.debug(TAG, ">>>" + "------------------------");

//                if (d.getHomeGroup().startsWith("M") || d.getHomeGroup().startsWith("D")   ) {

                if (d.getHomeGroup().startsWith("E") || d.getHomeGroup().startsWith("M")
                        || d.getHomeGroup().startsWith("D")) {

                    manager.execute(new LessonLoader(getApplicationContext(), d.getHomeHref()) {
                        @Override
                        public void onLoaderStart() {

                        }

                        @Override
                        public void onLoaderSuccess(LessonEntity entity) {

                            //mp3 link
//                            updateMp3(entity);


                            //description and image
//                            updateImageAndDescription(entity);




                        }

                        @Override
                        public void onLoaderFail(Throwable e) {

                        }
                    });

                }


            }
            return null;
        }
    }



    private void updateMp3(LessonEntity entity) {
        Logger.debug(TAG, ">>>" + "TITLE:" + entity.getTitle() + ";AND-LINK: " + entity.getMp3Link());
        if (TextUtils.isEmpty(entity.getMp3Link())) {
            Logger.error(TAG, ">>>" + "ERROR Mp3 href:" + entity.getHrefLink() + ";mp3:" + entity.getMp3Link());
            return;
        }
        HomeEntity d = DataManager.getInstance().getDataByHref(entity.getHrefLink());
        if (d == null) {
            Logger.error(TAG, ">>>" + "ERROR Mp3 HomeEntity NULL:" + entity.getHrefLink());
            return;
        }
        d.setHomeMp3(entity.getMp3Link());
        tsParse.updateMp3(d);
    }

    private void updateImageAndDescription(LessonEntity entity) {
        if (TextUtils.isEmpty(entity.getDescription())) {
            Logger.error(TAG, ">>>" + "ERROR getDescription href:" + entity.getHrefLink());
        }

        if (TextUtils.isEmpty(entity.getImage())) {
            Logger.error(TAG, ">>>" + "ERROR getImage href:" + entity.getHrefLink());
        } else {
            String[] arr = entity.getHrefLink().replace("http://", "").split("/");
            //http://www.esl-lab.com/music/musicrd1.htm
            String image = "http://" + arr[0] + "/" + arr[1] + "/" + entity.getImage();
            Logger.debug(TAG, ">>>" + "image:" + image);

            HomeEntity d = DataManager.getInstance().getDataByHref(entity.getHrefLink());
            if (d == null) {
                Logger.error(TAG, ">>>" + "ERROR Mp3 HomeEntity NULL:" + entity.getHrefLink());
                return;
            }
            d.setHomeMp3(entity.getMp3Link());
            d.setHomeImage(image);
            d.setHomeDescription(entity.getDescription());
            tsParse.updateImageDescription(d);

        }
    }
}
