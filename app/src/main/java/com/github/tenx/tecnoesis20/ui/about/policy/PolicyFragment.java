package com.github.tenx.tecnoesis20.ui.about.policy;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import com.github.tenx.tecnoesis20.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PolicyFragment extends Fragment {


    @BindView(R.id.web_policy)
    WebView webPolicy;

    public PolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_policy, container, false);
        ButterKnife.bind(this , v);

        webPolicy.loadUrl(getResources().getString(R.string.privacy_policy_link));

        return v ;
    }

}
