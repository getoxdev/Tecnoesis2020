package com.github.tenx.tecnoesis20.ui.main.schedule;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.data.models.LocationDetailBody;
import com.github.tenx.tecnoesis20.ui.main.MainActivity;
import com.github.tenx.tecnoesis20.ui.main.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import timber.log.Timber;

public class ScheduleFragment extends Fragment implements OnMapReadyCallback {



    private Toolbar toolbarBotSheetMap;

    private RecyclerView recyclerBotSheetMap;
    private ImageView ivBottomSheetImage;

    private ScheduleViewModel mViewModel;
    private MainViewModel parentViewModel;



    private BottomSheetRecyclerAdapter adapter;

    private HashMap<String , Integer> markerRecord;
    private HashMap<String , Marker> markerIdRecord;

    private GoogleMap map;
    private BottomSheetDialog dialog;

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_schedule, container, false);

        ButterKnife.bind(this, v);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        // TODO: Use the ViewModel
        parentViewModel = ((MainActivity) getActivity()).getViewModel();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        markerRecord = new HashMap<>();
        markerIdRecord = new HashMap<>();
        initMap(googleMap);
        initBottomSheet(getActivity());
        parentViewModel.getLdLocationDetailsList().observe(getActivity(), data -> {
            if(map != null){
                initMapData(data, getActivity());
            }
        });
    }

    private void initMap(GoogleMap map) {

        try {
            this.map = map;
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.style_json));

            if (!success) {
                Timber.e("Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Timber.e("Can't find style. Error:");
        }
        LatLng nitCords = new LatLng(24.7577, 92.7923);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(nitCords, 16.0f));
    }

    private void initMapData(List<LocationDetailBody> locationData, Context context){
        map.clear();
        markerIdRecord.clear();
        markerRecord.clear();
        for (int i=0 ; i< locationData.size() ; i++) {
            LocationDetailBody data = locationData.get(i);
            LatLng cords = new LatLng(data.getLat(), data.getLng());

            MarkerOptions opts = new MarkerOptions().position(cords).title(data.getName());
            opts.icon(getBitMapFromDrawable(R.drawable.ic_marker_purple));
            Marker tempMarker = map.addMarker(opts);
            markerRecord.put(tempMarker.getId(), i);
            markerIdRecord.put(tempMarker.getId(), tempMarker);

            Timber.d("image url : "+data.getMarker());
            Glide.with(context).asBitmap().load(data.getMarker()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    tempMarker.setIcon(getBitmapDescriptorFromBitmap(resource));
                }
            });

        }

        map.setOnMarkerClickListener(marker1 -> {
            if (marker1.isInfoWindowShown()) {
                marker1.hideInfoWindow();
            } else {
                marker1.showInfoWindow();
            }

            try {
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       initBottomSheetData(locationData.get(markerRecord.get(marker1.getId())) , getActivity());
                   }
               },1000);
                return true;
            }catch (NullPointerException e){
                Timber.e("Null error occured");
            }

            return true;
        });
    }


    private BitmapDescriptor getBitMapFromDrawable(int id) {
        int height = 150;
        int width = 150;
        Bitmap b = BitmapFactory.decodeResource(getResources(), id);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

        return smallMarkerIcon;
    }

    private BitmapDescriptor getBitmapDescriptorFromBitmap(Bitmap bitmap) {
        int height = 150;
        int width = 150;
        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

        return smallMarkerIcon;
    }


    private void initBottomSheet(Context context) {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_location_details, null);
        toolbarBotSheetMap = dialogView.findViewById(R.id.toolbar_bot_sheet_map);
        recyclerBotSheetMap = dialogView.findViewById(R.id.recycler_bot_sheet_map);
        ivBottomSheetImage = dialogView.findViewById(R.id.iv_bottom_sheet_image_loc);
        toolbarBotSheetMap.setTitleTextColor(getResources().getColor(R.color.md_white_1000));
        toolbarBotSheetMap.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close));
        toolbarBotSheetMap.setNavigationOnClickListener(v -> dialog.dismiss());
        dialog = new BottomSheetDialog(context);
        dialog.setContentView(dialogView);
        adapter = new BottomSheetRecyclerAdapter();
    }

    private void initBottomSheetData(LocationDetailBody data, Context context) {
        toolbarBotSheetMap.setTitle(data.getName());
        adapter.setEventList(data.getEvents());
        if(data.getImage().equals("") || data.getImage() == null)
             Glide.with(context).load("https://via.placeholder.com/400x300?text=Location+Image").placeholder(R.drawable.placeholder_image).into(ivBottomSheetImage);
        else
            Glide.with(context).load(data.getImage()).placeholder(R.drawable.placeholder_image).into(ivBottomSheetImage);

        recyclerBotSheetMap.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerBotSheetMap.setAdapter(adapter);
        dialog.show();


    }


}
