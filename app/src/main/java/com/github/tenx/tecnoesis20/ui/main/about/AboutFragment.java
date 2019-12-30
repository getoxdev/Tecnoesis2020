package com.github.tenx.tecnoesis20.ui.main.about;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.data.models.TeamBody;
import com.github.tenx.tecnoesis20.ui.main.MainActivity;
import com.github.tenx.tecnoesis20.ui.main.MainViewModel;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutFragment extends Fragment {


    @BindView(R.id.recycler_teams)
    RecyclerView recyclerTeams;


    @BindView(R.id.progress_content)
    NewtonCradleLoading progressContent;

    private TeamsAdapter adapter;

    private MainViewModel parentViewModel;


    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, v);


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
        initTeamsAdapter(getActivity());

        parentViewModel = ((MainActivity) getActivity()).getViewModel();
    }

    @Override
    public void onStart() {
        super.onStart();
        parentViewModel.getLdTeamsList().observe(getActivity() , data -> {
            if(data.size() == 0){
                showProgress();
            }else {
                hideProgress();
                adapter.setData(data);
            }
        });
    }

    private void initTeamsAdapter(Context context) {
        adapter = new TeamsAdapter(context);
        recyclerTeams.setLayoutManager(new LinearLayoutManager(context));
        recyclerTeams.setAdapter(adapter);

    }


    private void hideProgress(){
        progressContent.stop();
        progressContent.setVisibility(View.GONE);
        recyclerTeams.setVisibility(View.VISIBLE);
    }

    private  void showProgress(){
        progressContent.start();
        progressContent.setVisibility(View.VISIBLE);
        recyclerTeams.setVisibility(View.GONE);
    }

}
