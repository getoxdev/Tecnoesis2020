package com.github.tenx.tecnoesis20.ui.main.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.data.models.FeedBody;
import com.github.tenx.tecnoesis20.ui.main.MainActivity;
import com.github.tenx.tecnoesis20.ui.main.MainViewModel;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.victor.loading.newton.NewtonCradleLoading;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    @BindView(R.id.progress_loading)
    NewtonCradleLoading progressLoading;
    @BindView(R.id.ll_main_content)
    LinearLayout llMainContent;
    @BindView(R.id.recycler_home_feeds)
    RecyclerView recyclerHomeFeeds;
    @BindView(R.id.ll_home_sponsors_container)
    LinearLayout llHomeSponsorsContainer;

//    google fragment lifecycle or https://developer.android.com/guide/components/fragments if you are unsure about how to use fragment lifecycle

    private HomeViewModel mViewModel;
    private MainViewModel parentViewModel;

    @BindView(R.id.recycler_home_events)
    RecyclerView mainEventsRecycler;

    @BindView((R.id.slider_home_events))
    SliderView sliderView;

    @BindView(R.id.recycler_sponsors_home_events)
    RecyclerView sponsorsRecycler;


    private HomeAdapter mainEventAdapter;
    private SliderAdapter homeSliderAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SponsorsAdapter sponsorsAdapter;
    private FeedAdapter feedAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, parent);
        initModuleRecycler();
        initSlider();
        initSponsorsRecycler();
        initFeedsRecycler(getActivity());
        return parent;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        parentViewModel = ((MainActivity) getActivity()).getViewModel();
        // TODO: Use the ViewModel this is for demo delete this later
        mViewModel.loadEvents();


    }


    @Override
    public void onStop() {
        super.onStop();
        feedAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();

//        @TODO how to call view model demo
        feedAdapter.startListening();

        parentViewModel.getLdPagerImageList().observe(getActivity(), data -> {
            homeSliderAdapter.setImageUrls(data);
        });

        parentViewModel.getLdSponsorImageList().observe(getActivity(), data -> {
                if(!data.isEmpty()){
                    llHomeSponsorsContainer.setVisibility(View.VISIBLE);
                    sponsorsAdapter.setHlist(data);
                }else {
                    llHomeSponsorsContainer.setVisibility(View.GONE);
                }
        });


        parentViewModel.getLdMainEventList().observe(getActivity(), data -> {
            mainEventAdapter.setHlist(data);
        });

        parentViewModel.getIsMainContentLoaded().observe(getActivity(), isLoaded -> {
            if (isLoaded)
                hideProress();
            else
                showProgress();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    private void initModuleRecycler() {
        layoutManager = new LinearLayoutManager(getActivity());
        mainEventsRecycler.setHasFixedSize(true);
        mainEventsRecycler.setLayoutManager(layoutManager);
        mainEventAdapter = new HomeAdapter(getActivity());
        mainEventsRecycler.setAdapter(mainEventAdapter);
    }

    private void initSlider() {
        homeSliderAdapter = new SliderAdapter(getActivity());
        sliderView.setSliderAdapter(homeSliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.startAutoCycle();
    }


    private void initSponsorsRecycler() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        sponsorsRecycler.setHasFixedSize(true);
        sponsorsRecycler.setLayoutManager(layoutManager);
        sponsorsAdapter = new SponsorsAdapter(getActivity());
        sponsorsRecycler.setAdapter(sponsorsAdapter);
    }

    private void initFeedsRecycler(Context context) {

        Query baseQuery = FirebaseDatabase.getInstance().getReference().child("feeds");

// This configuration comes from the Paging Support Library
// https://developer.android.com/reference/android/arch/paging/PagedList.Config.html

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();

// The options for the adapter combine the paging configuration with query information
// and application-specific options for lifecycle, etc.
        DatabasePagingOptions<FeedBody> options = new DatabasePagingOptions.Builder<FeedBody>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, FeedBody.class)
                .build();

        feedAdapter = new FeedAdapter(options, context);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setReverseLayout(true);
        recyclerHomeFeeds.setLayoutManager(lm);
        recyclerHomeFeeds.setAdapter(feedAdapter);
    }


    private void hideProress() {
        progressLoading.stop();
        progressLoading.setVisibility(View.GONE);
        llMainContent.setVisibility(View.VISIBLE);
    }

    private void showProgress() {
        progressLoading.start();
        progressLoading.setVisibility(View.VISIBLE);
        llMainContent.setVisibility(View.GONE);
    }
}
