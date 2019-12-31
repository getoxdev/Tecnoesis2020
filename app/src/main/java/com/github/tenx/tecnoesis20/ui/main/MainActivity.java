package com.github.tenx.tecnoesis20.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.ui.main.about.AboutFragment;
import com.github.tenx.tecnoesis20.ui.main.events.EventsFragment;
import com.github.tenx.tecnoesis20.ui.main.home.HomeFragment;
import com.github.tenx.tecnoesis20.ui.main.notifications.NotificationsFragment;
import com.github.tenx.tecnoesis20.ui.main.schedule.ScheduleFragment;
import com.github.tenx.tecnoesis20.ui.module.ModuleActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static final String LOAD_NOTIFICATIONS = "notif";

    @BindView(R.id.act_main_bnv)
    BottomNavigationView botNav;


    @BindView(R.id.vp_main_pager)
    ViewPager vpMainPager;

    private FragmentAdapter fragmentPagerAdapter;

    //    frags
    private MainViewModel viewModel;

    private HomeFragment fragHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        this is necessary bind call for BindView decorators
        ButterKnife.bind(this);
//        set callback as implemented interface

        initFragmentPager();
        botNav.setOnNavigationItemSelectedListener(this);

//        get view model
        viewModel = ViewModelProviders.of(MainActivity.this).get(MainViewModel.class);


//    get modules data from database
        initModulesData();

        //        initialize home fragment in main activity
        if (fragHome == null) {
            fragHome = new HomeFragment();
        }


//        if coming back from module activity load HomeEventBody fragment first
        if (getIntent().getBooleanExtra(ModuleActivity.START_EVENTS, false)) {
            vpMainPager.setCurrentItem(FragmentAdapter.INDEX_EVENTS);
            botNav.setSelectedItemId(R.id.nav_events);
        } else if (getIntent().getBooleanExtra(MainActivity.LOAD_NOTIFICATIONS, false)) {
            vpMainPager.setCurrentItem(FragmentAdapter.INDEX_NOTIFICATIONS);
            botNav.setSelectedItemId(R.id.nav_notifications);
        }


//        user logger like this
        Timber.d("Welcome to my main application");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //    handle bottom navigation clicks
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        int colorID;
        int index = 0;

        switch (id) {
            case R.id.nav_home:
                colorID = R.color.nav_home;
                index = FragmentAdapter.INDEX_HOME;
                break;
            case R.id.nav_events:
                index = FragmentAdapter.INDEX_EVENTS;
                colorID = R.color.nav_events;
                break;
            case R.id.nav_location:
                index = FragmentAdapter.INDEX_LOCATION;
                colorID = R.color.nav_schedule;
                break;
            case R.id.nav_about:
                index = FragmentAdapter.INDEX_ABOUT;
                colorID = R.color.nav_about;
                break;
            case R.id.nav_notifications:
                index = FragmentAdapter.INDEX_NOTIFICATIONS;
                colorID = R.color.nav_notifications;
                break;
            default:
                return false;
        }
        botNav.setBackgroundColor(getResources().getColor(colorID));
        vpMainPager.setCurrentItem(index, true);

        return true;
    }





    private void initModulesData() {
        viewModel.loadModules();
        viewModel.loadLocationDetails();
        viewModel.loadMainEvents();
        viewModel.loadSponsors();
        viewModel.loadPagerImages();
        viewModel.loadTeamsData();
    }

    public MainViewModel getViewModel() {
//        required for child fragments
        return viewModel;
    }

    private void initFragmentPager(){
        fragmentPagerAdapter = new FragmentAdapter(getSupportFragmentManager() , FragmentPagerAdapter.POSITION_NONE);
        vpMainPager.setAdapter(fragmentPagerAdapter);
        vpMainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int menu_id;

                if(position == FragmentAdapter.INDEX_EVENTS)
                    menu_id = R.id.nav_events;
                else if(position == FragmentAdapter.INDEX_LOCATION)
                    menu_id = R.id.nav_location;
                else if(position == FragmentAdapter.INDEX_ABOUT)
                    menu_id = R.id.nav_about;
                else if(position == FragmentAdapter.INDEX_NOTIFICATIONS)
                    menu_id = R.id.nav_notifications;
                else
                    menu_id = R.id.nav_home;

                botNav.setSelectedItemId(menu_id);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


}
