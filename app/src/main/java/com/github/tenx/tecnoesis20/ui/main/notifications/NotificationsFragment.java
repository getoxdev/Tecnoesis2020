package com.github.tenx.tecnoesis20.ui.main.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.data.models.NotificationBody;
import com.github.tenx.tecnoesis20.ui.about.AboutActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.victor.loading.newton.NewtonCradleLoading;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class NotificationsFragment extends Fragment {

    @BindView(R.id.recycler_notifications_list)
    RecyclerView recyclerNotificationsList;
    @BindView(R.id.progress)
    NewtonCradleLoading progress;
    @BindView(R.id.toolbar_notifications)

    Toolbar toolbarNotifications;

    private NotificationsViewModel mViewModel;


    private FirebaseNotificationAdapter adapter;


    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this, v);
        initToolbar(getActivity());
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        // TODO: Use the ViewModel
    }

    private void initAdapter(Context ctx) {
//        as latest notification is at last
        LinearLayoutManager llman = new LinearLayoutManager(ctx);
        llman.setReverseLayout(true);
        recyclerNotificationsList.setLayoutManager(llman);


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("notifications")
                .limitToLast(50);
        FirebaseRecyclerOptions<NotificationBody> options =
                new FirebaseRecyclerOptions.Builder<NotificationBody>()
                        .setQuery(query, NotificationBody.class)
                        .build();
        adapter = new FirebaseNotificationAdapter(options);
        adapter.getSnapshots().addChangeEventListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {
                hideProgress();
                Timber.d("Changed now");
            }

            @Override
            public void onDataChanged() {

            }

            @Override
            public void onError(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerNotificationsList.setAdapter(adapter);


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notifications, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId() ==  R.id.action_about){
           Intent i = new Intent(getActivity() , AboutActivity.class);
           startActivity(i);
           return true;
       }
       return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        initAdapter(getActivity());
        showProgress();
        adapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    private void initToolbar(Context context){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarNotifications);

    }

    private void showProgress() {
        progress.start();
        progress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progress.stop();
        progress.setVisibility(View.GONE);
    }
}
