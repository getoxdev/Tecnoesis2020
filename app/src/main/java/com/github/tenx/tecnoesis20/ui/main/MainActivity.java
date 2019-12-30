package com.github.tenx.tecnoesis20.ui.main;

import android.os.Bundle;

import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.ui.main.about.AboutFragment;
import com.github.tenx.tecnoesis20.ui.main.events.EventsFragment;
import com.github.tenx.tecnoesis20.ui.main.home.HomeFragment;
import com.github.tenx.tecnoesis20.ui.main.notifications.NotificationsFragment;
import com.github.tenx.tecnoesis20.ui.main.schedule.ScheduleFragment;
import com.github.tenx.tecnoesis20.ui.module.ModuleActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    @BindView(R.id.act_main_bnv)
    BottomNavigationView botNav;


//    frags
    private MainViewModel viewModel;

    private HomeFragment fragHome;
    private EventsFragment fragEvents;
    private AboutFragment fragAbout;
    private ScheduleFragment fragSchedule;

//    frag mans
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        this is necessary bind call for BindView decorators
        ButterKnife.bind(this);

//        set callback as implemented interface
        botNav.setOnNavigationItemSelectedListener(this);

//        get view model
        viewModel = ViewModelProviders.of(MainActivity.this).get(MainViewModel.class);


//    get modules data from database
        initModulesData();

        //        initialize home fragment in main activity
        if(fragHome == null){
            fragHome = new HomeFragment();
        }


//        if coming back from module activity load HomeEventBody fragment first
        if(getIntent().getBooleanExtra(ModuleActivity.START_EVENTS, false)){
            initFragment(new EventsFragment());
            botNav.setSelectedItemId(R.id.nav_events);
        }else{
            initFragment(fragHome);
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
        Fragment frag;
        switch (id){
            case R.id.nav_home:
                frag = new HomeFragment();
                colorID = R.color.nav_home;
                break;
            case R.id.nav_events:
                frag = new EventsFragment();
                colorID = R.color.nav_events;
                break;
            case R.id.nav_location:
                frag = new ScheduleFragment();
                colorID = R.color.nav_schedule;
                break;
            case R.id.nav_about:
                frag = new AboutFragment();
                colorID = R.color.nav_about;
                break;
                case R.id.nav_notifications:
                frag = new NotificationsFragment();
                colorID = R.color.nav_notifications;
                break;
                default:
                    return  false;
        }

        botNav.setBackgroundColor(getResources().getColor(colorID));
        initFragment(frag);
        return true;
    }



    private void initFragment(Fragment frag){
            fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.act_main_fl_container, frag).commit();

    }


    private void initModulesData(){
        viewModel.loadModules();
        viewModel.loadLocationDetails();
        viewModel.loadMainEvents();
        viewModel.loadSponsors();
        viewModel.loadPagerImages();
        viewModel.loadFeedsData();
        viewModel.loadTeamsData();
    }

    public MainViewModel getViewModel() {
//        required for child fragments
        return viewModel;
    }
}
