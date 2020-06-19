package com.github.tenx.tecnoesis20.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.ui.main.events.EventsFragment;
import com.github.tenx.tecnoesis20.ui.main.home.HomeFragment;
import com.github.tenx.tecnoesis20.ui.main.notifications.NotificationsFragment;
import com.github.tenx.tecnoesis20.ui.main.schedule.ScheduleFragment;
import com.github.tenx.tecnoesis20.ui.main.teams.TeamsFragment;
import com.github.tenx.tecnoesis20.ui.module.ModuleActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static final String LOAD_NOTIFICATIONS = "notif";

    @BindView(R.id.act_main_bnv)
    BottomNavigationView botNav;



    //    frags
    private MainViewModel viewModel;

    private Fragment fragHome, fragEvents,fragLocation,fragNotificaitons, fragTeams;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        this is necessary bind call for BindView decorators
        ButterKnife.bind(this);
//        set callback as implemented interface

        initFragments();
        botNav.setOnNavigationItemSelectedListener(this);

//        get view model
        viewModel = ViewModelProviders.of(MainActivity.this).get(MainViewModel.class);


//    get all base data from database
        loadAppData();

        //        initialize home fragment in main activity


//        if coming back from module activity load HomeEventBody fragment first
        if (getIntent().getBooleanExtra(ModuleActivity.START_EVENTS, false)) {
            loadFragment(fragEvents);
            botNav.setSelectedItemId(R.id.nav_events);
        } else if (getIntent().getBooleanExtra(MainActivity.LOAD_NOTIFICATIONS, false)) {
           loadFragment(fragNotificaitons);
            botNav.setSelectedItemId(R.id.nav_notifications);
        }else {
//            initialize home
            loadFragment(fragHome);
        }


//        user logger like this
        Timber.d("Welcome to my main application");
    }

    @Override
    protected void onStart() {
        super.onStart();

    }



    //    handle bottom navigation clicks
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        int colorID;
        int index;
        Fragment fragment;

        switch (id) {
            case R.id.nav_home:
                colorID = R.color.nav_home;
               fragment = fragHome;
                break;
            case R.id.nav_events:
                fragment = fragEvents;
                colorID = R.color.nav_events;
                break;
            case R.id.nav_location:
                fragment = fragLocation;
                colorID = R.color.nav_schedule;
                break;
            case R.id.nav_teams:
                fragment = fragTeams;
                colorID = R.color.nav_teams;
                break;
            case R.id.nav_notifications:
                fragment = fragNotificaitons;
                colorID = R.color.nav_notifications;
                break;
            default:
                return false;
        }
        botNav.setBackgroundColor(getResources().getColor(colorID));
        loadFragment(fragment);
        return true;
    }





    private void loadAppData() {
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


    private void loadFragment(Fragment frag){
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_frame_container, frag).commit();
    }


    private void initFragments(){
        fragHome = new HomeFragment();
        fragTeams = new TeamsFragment();
        fragEvents = new EventsFragment();
        fragLocation = new ScheduleFragment();
        fragNotificaitons = new NotificationsFragment();
    }





}
