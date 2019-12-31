package com.github.tenx.tecnoesis20.ui.event.eventPage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.github.tenx.tecnoesis20.R;
import com.github.tenx.tecnoesis20.data.models.EventBody;
import com.github.tenx.tecnoesis20.ui.MyApplication;
import com.github.tenx.tecnoesis20.ui.event.EventActivity;
import com.google.android.material.button.MaterialButton;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventPageFragment extends Fragment {


    @BindView(R.id.btn_event_website)
    MaterialButton btnEventWebsite;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    private int currentModuleIndex;
    private int currentEventIndex;
    private EventBody currentEvent;

    private MyApplication application;


    @BindView(R.id.iv_event_image)
    ImageView ivEventImage;
    @BindView(R.id.tv_event_title)
    TextView tvEventTitle;
    @BindView(R.id.tv_event_date)
    TextView tvEventDate;
    @BindView(R.id.btn_event_register)
    MaterialButton btnEventRegister;
    @BindView(R.id.tv_event_desc)
    TextView tvEventDesc;
    @BindView(R.id.tv_event_rules)
    TextView tvEventRules;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_page, container, false);

        ButterKnife.bind(this, v);


//        get indexes
        try {

            currentEventIndex = getArguments().getInt(EventActivity.EVENT_INDEX_KEY);
            currentModuleIndex = getArguments().getInt(EventActivity.MODULE_INDEX_KEY);

        } catch (NullPointerException e) {
            currentEventIndex = 0;
            currentModuleIndex = 0;
        }

        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        application = (MyApplication) getActivity().getApplication();
    }

    @Override
    public void onStart() {
        super.onStart();
        currentEvent = application.getModuleList().get(currentModuleIndex).getEvents().get(currentEventIndex);
        initEvents(currentEvent, getActivity());
    }

    private void initEvents(EventBody data, Context context) {
        tvEventDate.setText(data.getDate());
        tvEventDesc.setText(data.getDescription());
        tvEventTitle.setText(data.getName());
        Glide.with(context).load(data.getImage()).into(ivEventImage);

        ivEventImage.setOnClickListener(v -> {
            List<String> images = new ArrayList<>();
            images.add(data.getImage());

            GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                    .setFailureImage(R.drawable.placeholder_image)
                    .setProgressBarImage(R.drawable.placeholder_image)
                    .setPlaceholderImage(R.drawable.placeholder_image);
            new ImageViewer.Builder(context, images)
                    .setStartPosition(0).setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                    .show();
        });
        if (data.getRegisterLink() != null && !data.getRegisterLink().equals("")) {
            btnEventRegister.setOnClickListener(v -> {
                openUrl(data.getRegisterLink(), context);
            });
        }

        btnEventWebsite.setOnClickListener(v-> {
            if(data.getWebsite() == null)
                openUrl("http://tecnoesis.org" , context);
            else
                openUrl(data.getWebsite() , context);
        });
        tvEventRules.setText("");
        String temp = "";
        for (int i = 0; i < data.getRules().size(); i++) {
            String text = data.getRules().get(i);
            temp += i + 1 + " : " + text + "\n\n";

        }
        tvEventRules.setText(temp);


    }

    private void openUrl(String url, Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }


}
