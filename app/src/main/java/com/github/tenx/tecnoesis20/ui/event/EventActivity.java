package com.github.tenx.tecnoesis20.ui.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.data.models.EventBody;
import com.github.tenx.tecnoesis20.ui.MyApplication;
import com.github.tenx.tecnoesis20.ui.event.eventPage.EventPageFragment;
import com.github.tenx.tecnoesis20.ui.module.ModuleActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.event_pager)
    ViewPager eventPager;


    EventPagerAdapter vpEventPagerAdapter;


    @BindView(R.id.event_toolbar)
    Toolbar eventToolbar;
    @BindView(R.id.event_tablayout)
    TabLayout eventTablayout;


    private MyApplication application;
    public static final String EVENT_INDEX_KEY = "event_index";
    public static final String MODULE_INDEX_KEY = "module_index";


    private int moduleIndex;
    private int eventIndex;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
        application = (MyApplication) getApplication();

        moduleIndex = getIntent().getIntExtra(ModuleActivity.CURRENT_MODULE_KEY, 0);
        eventIndex = getIntent().getIntExtra(ModuleActivity.CURRENT_EVENT_KEY, 0);

        initToolbar(application.getModuleList().get(moduleIndex).getName());

        initFragmentPager();
    }


    private void initFragmentPager() {
        List<EventBody> eventList = application.getModuleList().get(moduleIndex).getEvents();
        List<Fragment> frags = new ArrayList<>();



        for (int i = 0; i < eventList.size(); i++) {
            Bundle args = new Bundle();
            args.putInt(EVENT_INDEX_KEY, i);
            args.putInt(MODULE_INDEX_KEY, moduleIndex);
            EventPageFragment frag = new EventPageFragment();
            frag.setArguments(args);
            frags.add(frag);
        }

        vpEventPagerAdapter = new EventPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.POSITION_NONE, frags);
        eventPager.setAdapter(vpEventPagerAdapter);
        eventPager.setCurrentItem(eventIndex);
        eventTablayout.setupWithViewPager(eventPager);
    }


    private void initToolbar(String  moduleName){
        setSupportActionBar(eventToolbar);
        getSupportActionBar().setTitle(moduleName);
        eventToolbar.setNavigationIcon(R.drawable.ic_back);
        eventToolbar.setTitleTextColor(getResources().getColor(R.color.md_white_1000));
        eventToolbar.setNavigationOnClickListener(v -> {
                goToModuleActivity();
        });
    }


    private void goToModuleActivity(){
        Intent i = new Intent(EventActivity.this , ModuleActivity.class);
        startActivity(i);
    }
}
