package son.nt.hellochao;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import java.util.Random;

import son.nt.hellochao.base.AActivity;
import son.nt.hellochao.dto.HomeEntity;
import son.nt.hellochao.dto.HomeObject;
import son.nt.hellochao.dto.LessonEntity;
import son.nt.hellochao.fragment.PlayingFragment;
import son.nt.hellochao.fragment.ScrollFragment;
import son.nt.hellochao.loader.HomeLoader;
import son.nt.hellochao.loader.LessonLoader;
import son.nt.hellochao.loader.LoaderManager;
import son.nt.hellochao.otto.GoHot;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;

public class ScrollingActivity extends AActivity {

    public  final String TAG = this.getClass().getSimpleName();

    LoaderManager manager;
    DataManager d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        OttoBus.register(this);
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

                   final int ran=  new Random().nextInt(d.getHomeEntities().size());
                    String link = d.getHomeEntities().get(ran).getHomeHref();
                    Logger.debug(TAG, ">>>" + "link:" + link);
                    manager.execute(new LessonLoader(getApplicationContext(), link) {
                        @Override
                        public void onLoaderStart() {

                        }

                        @Override
                        public void onLoaderSuccess(LessonEntity entity) {
                            Logger.debug(TAG, ">>>" + "onLoaderSuccess:" + entity);
                            HomeEntity homeEntity = d.getHomeEntities().get(ran);
                            homeEntity.setHomeMp3(entity.getMp3Link());
                            showFragment(PlayingFragment.createInstance(homeEntity),true);

                        }

                        @Override
                        public void onLoaderFail(Throwable e) {

                        }
                    });




                } else if (id == R.id.nav_slideshow) {

                } else if (id == R.id.nav_manage) {

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;

            }
        });

    }

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
        showFragment(new PlayingFragment(), true);
    }
}
