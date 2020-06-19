package com.github.tenx.tecnoesis20.ui.about.home;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.ui.about.AboutActivity;
import com.github.tenx.tecnoesis20.ui.about.policy.PolicyFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutHomeFragment extends Fragment {


    @BindView(R.id.btn_contact_dev)
    MaterialButton btnContactDev;
    @BindView(R.id.btn_privacy_policy)
    MaterialButton btnPrivacyPolicy;
    @BindView(R.id.animationView)
    LottieAnimationView animationView;

    public AboutHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, v);
        initAnimation();

        return v;

    }


    @OnClick(R.id.btn_contact_dev)
    void onBtnContact(View v){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","tenx.devs@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Tecnoesis2020 Error Report");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    @OnClick(R.id.btn_privacy_policy)
    void onBtnPolicy(View v){
        ((AboutActivity) getActivity()).initFragment(new PolicyFragment());
    }

    private void initAnimation(){

        FirebaseDatabase.getInstance().getReference().child("anim").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            animationView.cancelAnimation();
                            String url = dataSnapshot.getValue(String.class);
                            animationView.setAnimationFromUrl(url);
                            animationView.playAnimation();
                        }catch (Error e){
                            Timber.e(e.getMessage());
                        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }








}
