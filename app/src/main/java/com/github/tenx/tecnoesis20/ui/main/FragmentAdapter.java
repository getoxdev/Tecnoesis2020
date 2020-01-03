package com.github.tenx.tecnoesis20.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.github.tenx.tecnoesis20.ui.main.teams.TeamsFragment;
import com.github.tenx.tecnoesis20.ui.main.events.EventsFragment;
import com.github.tenx.tecnoesis20.ui.main.home.HomeFragment;
import com.github.tenx.tecnoesis20.ui.main.notifications.NotificationsFragment;
import com.github.tenx.tecnoesis20.ui.main.schedule.ScheduleFragment;

import java.util.HashMap;

public class FragmentAdapter extends FragmentPagerAdapter {

    public static final int INDEX_HOME = 0;
    public static final int INDEX_EVENTS = 1;
    public static final int INDEX_LOCATION = 2;
    public static final int INDEX_TEAMS = 3;
    public static final int INDEX_NOTIFICATIONS = 4;


    private HashMap<Integer, Fragment> fragMap;

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
       fragMap = new HashMap<>();
       fragMap.put(INDEX_HOME , new HomeFragment());
       fragMap.put(INDEX_EVENTS , new EventsFragment());
       fragMap.put(INDEX_LOCATION , new ScheduleFragment());
       fragMap.put(INDEX_TEAMS, new TeamsFragment());
       fragMap.put(INDEX_NOTIFICATIONS , new NotificationsFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragMap.get(position);
    }

    @Override
    public int getCount() {
        return fragMap.size();
    }
}
