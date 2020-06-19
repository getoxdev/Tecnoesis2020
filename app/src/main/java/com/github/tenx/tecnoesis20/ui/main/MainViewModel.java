package com.github.tenx.tecnoesis20.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.tenx.tecnoesis20.data.models.FeedBody;
import com.github.tenx.tecnoesis20.data.models.HomeEventBody;
import com.github.tenx.tecnoesis20.data.models.LocationDetailBody;
import com.github.tenx.tecnoesis20.data.models.ModuleBody;
import com.github.tenx.tecnoesis20.data.models.SponsorBody;
import com.github.tenx.tecnoesis20.data.models.TeamBody;
import com.github.tenx.tecnoesis20.ui.MyApplication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import timber.log.Timber;

public class MainViewModel  extends AndroidViewModel {



    private MutableLiveData<List<ModuleBody>> ldModulesList;

    private MutableLiveData<List<LocationDetailBody>> ldLocationDetailsList;
    private MutableLiveData<Boolean> isModulesLoaded;
    private MyApplication app;
    private FirebaseDatabase db;
    private MutableLiveData<List<SponsorBody>> ldSponsorImageList;
    private MutableLiveData<List<String>> ldPagerImageList;
    private MutableLiveData<List<HomeEventBody>> ldMainEventList;
    private MutableLiveData<List<TeamBody>> ldTeamsList;
    private MutableLiveData<Boolean> isMainContentLoaded;


    public MainViewModel(@NonNull Application application)
    {
        super(application);
        app  = (MyApplication) application;
        db = FirebaseDatabase.getInstance();
        ldModulesList = new MutableLiveData<>();
        isModulesLoaded = new MutableLiveData<>();
        ldLocationDetailsList = new MutableLiveData<>();
        ldSponsorImageList = new MutableLiveData<>();
        ldPagerImageList = new MutableLiveData<>();
        ldMainEventList = new MutableLiveData<>();
        isMainContentLoaded = new MutableLiveData<>();
        ldTeamsList = new MutableLiveData<>();

    }



    public LiveData<List<ModuleBody>> getModules(){
        return ldModulesList;
    }

    public LiveData<List<LocationDetailBody>> getLdLocationDetailsList() {
        return ldLocationDetailsList;
    }

    public MutableLiveData<List<SponsorBody>> getLdSponsorImageList() {
        return ldSponsorImageList;
    }

    public MutableLiveData<List<String>> getLdPagerImageList() {
        return ldPagerImageList;
    }

    public MutableLiveData<List<HomeEventBody>> getLdMainEventList() {
        return ldMainEventList;
    }

    public MutableLiveData<Boolean> getIsMainContentLoaded() {
        return isMainContentLoaded;
    }

    public MutableLiveData<Boolean> getIsModulesLoaded() {
        return isModulesLoaded;
    }

    public void loadModules(){
        isModulesLoaded.postValue(false);
            db.getReference().child("modules").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    isModulesLoaded.postValue(true);
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    List<ModuleBody> temp = new ArrayList<>();
                    while (iterator.hasNext()){

                        DataSnapshot snap = iterator.next();

                        ModuleBody data = snap.getValue(ModuleBody.class);



                        temp.add(data);

                        if(data.getEvents() != null){

                        }else {
                            Timber.d("Events is empty!");
                        }
                    }

//store data at application level
                    app.setModuleList(temp);

//                    post update to activity
                    ldModulesList.postValue(temp);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                        isModulesLoaded.postValue(true);
                }
            });
    }

    public void loadLocationDetails(){
        db.getReference().child("locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                isModulesLoaded.postValue(true);
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                List<LocationDetailBody> temp = new ArrayList<>();
                while (iterator.hasNext()){

                    DataSnapshot snap = iterator.next();

                    LocationDetailBody data = snap.getValue(LocationDetailBody.class);
                    temp.add(data);
                }

//                    post update to activity
                ldLocationDetailsList.postValue(temp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void loadSponsors(){
        db.getReference().child("sponsors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                List<SponsorBody> temp = new ArrayList<>();
                while (iterator.hasNext()){

                    DataSnapshot snap = iterator.next();

                    SponsorBody data = snap.getValue(SponsorBody.class);
                    temp.add(data);
                }

//                    post update to activity
                ldSponsorImageList.postValue(temp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadPagerImages(){
        db.getReference().child("pager").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                List<String> temp = new ArrayList<>();
                while (iterator.hasNext()){

                    DataSnapshot snap = iterator.next();

                    String data = snap.getValue(String.class);
                    temp.add(data);
                }

//                    post update to activity
                ldPagerImageList.postValue(temp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void loadMainEvents(){
        isMainContentLoaded.postValue(false);
        db.getReference().child("main").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                isMainContentLoaded.postValue(true);
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                List<HomeEventBody> temp = new ArrayList<>();
                while (iterator.hasNext()){

                    DataSnapshot snap = iterator.next();

                    HomeEventBody data = snap.getValue(HomeEventBody.class);
                    temp.add(data);
                }

//                    post update to activity
                ldMainEventList.postValue(temp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                isMainContentLoaded.postValue(true);
            }
        });
    }



    public MutableLiveData<List<TeamBody>> getLdTeamsList() {
        return ldTeamsList;
    }

    public void loadTeamsData(){

        db.getReference().child("teams").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                List<TeamBody> temp = new ArrayList<>();
                while (iterator.hasNext()){

                    DataSnapshot snap = iterator.next();

                    TeamBody data = snap.getValue(TeamBody.class);
                    temp.add(data);
                }

//                    post update to activity
                ldTeamsList.postValue(temp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }










}
