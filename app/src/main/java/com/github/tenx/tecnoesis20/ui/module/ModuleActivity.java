package com.github.tenx.tecnoesis20.ui.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.tenx.tecnoesis20.Config;
import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.data.models.ModuleBody;
import com.github.tenx.tecnoesis20.ui.MyApplication;
import com.github.tenx.tecnoesis20.ui.event.EventActivity;
import com.github.tenx.tecnoesis20.ui.main.MainActivity;
import com.github.tenx.tecnoesis20.ui.module.modulefrag.EventClickHandler;
import com.github.tenx.tecnoesis20.ui.module.modulefrag.ModuleFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ModuleActivity extends AppCompatActivity implements EventClickHandler {


    public static String PAGE_INDEX_KEY = "index";

    public static final String CURRENT_MODULE_KEY = "module_key_1";
    public static final String CURRENT_EVENT_KEY = "event_key_1";
    public static final String START_EVENTS = "HomeEventBody";


    @BindView(R.id.module_tablayout)
    TabLayout moduleTablayout;

    private List<ModuleBody> modules;


    @BindView(R.id.vp_modules_fragment_pager)
    ViewPager vpModulesFragmentPager;

    @BindView(R.id.event_toolbar)
    Toolbar moduleToolbar;

    private ModuleAdapter adapter;


    @BindView(R.id.module_appbar)
    AppBarLayout appBarLayout;


    private ArrayList<Fragment> listFragments;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        ButterKnife.bind(this);
        initToolbar();
        initFragmentPager(this);

    }


    private void initFragmentPager(Context context) {
//        demo fragments

        modules = ((MyApplication) getApplication()).getModuleList();
        listFragments = new ArrayList<>();

        for (int i = 0; i < modules.size(); i++) {
            ModuleFragment frag = new ModuleFragment();
            Bundle data = new Bundle();
            data.putInt(PAGE_INDEX_KEY, i);
            frag.setArguments(data);
            listFragments.add(frag);
        }


        adapter = new ModuleAdapter(getSupportFragmentManager(), FragmentPagerAdapter.POSITION_NONE, context, listFragments);
        vpModulesFragmentPager.setAdapter(adapter);
        moduleTablayout.setupWithViewPager(vpModulesFragmentPager);



        try {
            Intent data = getIntent();
            int currentPosition = data.getIntExtra(Config.INITIAL_PAGE, 0);
            vpModulesFragmentPager.setCurrentItem(currentPosition);
            getSupportActionBar().setTitle(modules.get(currentPosition).getName());

            vpModulesFragmentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    moduleToolbar.setTitle(modules.get(position).getName());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });



        } catch (NullPointerException e) {
            Timber.d("No initial page was passed to intent");
        }


    }


    private void initToolbar() {
        setSupportActionBar(moduleToolbar);

        moduleToolbar.setNavigationIcon(R.drawable.ic_back);
        moduleToolbar.setTitleTextColor(getResources().getColor(R.color.md_white_1000));
        moduleToolbar.setNavigationOnClickListener(v -> {
//            go back
            goBackMainActivity();

        });

    }


    @Override
    public void onEventClick(int eventIndex) {
//                go to event activity
        loadEventActivity(eventIndex, vpModulesFragmentPager.getCurrentItem());
    }

    private void loadEventActivity(int eventIndex, int moduleIndex) {
        Intent intent = new Intent(ModuleActivity.this, EventActivity.class);
        intent.putExtra(CURRENT_MODULE_KEY, moduleIndex);
        intent.putExtra(CURRENT_EVENT_KEY, eventIndex);
        startActivity(intent);
    }


    private void goBackMainActivity() {
        Intent i = new Intent(ModuleActivity.this, MainActivity.class);
        i.putExtra(START_EVENTS, true);
        startActivity(i);

    }


}
